package com.example.kevin_sct.beastchat.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kevin_sct.beastchat.R;
import com.example.kevin_sct.beastchat.Utils.CONSTANT;
import com.example.kevin_sct.beastchat.Utils.MarshMellowPermisson;
import com.example.kevin_sct.beastchat.activities.BaseFragmentActivity;
import com.example.kevin_sct.beastchat.activities.GameActivity;
import com.example.kevin_sct.beastchat.aty.InitAty;
import com.example.kevin_sct.beastchat.services.LiveAccountServices;
import com.example.kevin_sct.beastchat.services.LiveFriendServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.roughike.bottombar.BottomBar;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.socket.client.IO;
import io.socket.client.Socket;

import static android.app.Activity.RESULT_OK;

/**
 * Created by kevin_sct on 4/28/17.
 */

public class ProfileFragment extends BaseFragment {

    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;

    @BindView(R.id.fragment_profile_camera_Picture)
    ImageView mCameraImage;

    @BindView(R.id.fragment_profile_image_Picture)
    ImageView mImageView;

    @BindView(R.id.fragment_profile_userPicture)
    ImageView mUserPicture;

    @BindView(R.id.fragment_profile_userEmail)
    TextView mUserEmail;

    @BindView(R.id.fragment_profile_userName)
    TextView mUserName;

    @BindView(R.id.fragment_profile_signOut)
    Button mSignOutButton;

    @BindView(R.id.enterConnect3)
    Button mEnterConnect3;

    @BindView(R.id.goBang)
    Button mgoBang;

    @BindView(R.id.listview_menu_list)
    ListView listView;


    //FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();

    public static ProfileFragment newInstance(){
        return new ProfileFragment();
    }

    private int mExpandedMenuPos = -1;
    private ListAdapter mListAdapter;

    private Unbinder mUnbinder;

    private LiveFriendServices mLiveFriendServices;
    private DatabaseReference mAllFriendRequestReference;
    private ValueEventListener mAllFriendRequestListener;
    private DatabaseReference mAllGameRequestReference;
    private ValueEventListener mAllGameRequestListener;
    private String mUserEmailString;

    private DatabaseReference mUsersNewMessagesReference;
    private ValueEventListener mUsersNewMessagesListener;


    private MarshMellowPermisson mMarshMellowPermission;

    private final int REQUEST_CODE_CAMERA = 100;
    private final int REQUEST_CODE_PICTURE=101;


    private Uri mTempUri;
    private BaseFragmentActivity mActivity;
    private Socket mSocket;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mSocket = IO.socket(CONSTANT.IP_LOCAL_HOST);
        } catch (URISyntaxException e) {
            Log.i(RegisterFragment.class.getSimpleName(),e.getMessage());
            Toast.makeText(getActivity(),"Can't connect to the server",Toast.LENGTH_SHORT).show();
        }
        mSocket.connect();
        mLiveFriendServices = LiveFriendServices.getInstance();
        mUserEmailString = mSharedPreferences.getString(CONSTANT.USER_EMAIL, "");
        mMarshMellowPermission = new MarshMellowPermisson((BaseFragmentActivity) getActivity());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        mBottomBar.selectTabWithId(R.id.tab_profile);
        setUpBottomBar(mBottomBar, 3);

        Picasso.with(getActivity())
                .load(mSharedPreferences.getString(CONSTANT.USER_PICTURE,""))
                .into(mUserPicture);
        mUserEmail.setText(mUserEmailString);
        mUserName.setText(mSharedPreferences.getString(CONSTANT.USER_NAME,""));


        mAllFriendRequestListener = mLiveFriendServices.getFriendRequestBottom(mBottomBar, R.id.tab_friends);
        mAllFriendRequestReference = FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_FRIEND_REQUEST_RECEIVED)
                .child(CONSTANT.encodeEmail(mUserEmailString));
        mAllFriendRequestReference.addValueEventListener(mAllFriendRequestListener);


        mAllGameRequestListener = mLiveFriendServices.getGameRequestBottom(mBottomBar, R.id.tab_friends);
        mAllGameRequestReference = FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_GAME_REQUEST_RECEIVED)
                .child(CONSTANT.encodeEmail(mUserEmailString));
        mAllGameRequestReference.addValueEventListener(mAllGameRequestListener);

        mUsersNewMessagesReference = FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_USER_NEW_MESSAGES).child(CONSTANT.encodeEmail(mUserEmailString));
        mUsersNewMessagesListener = mLiveFriendServices.getAllNewMessages(mBottomBar,R.id.tab_messages);

        mUsersNewMessagesReference.addValueEventListener(mUsersNewMessagesListener);

        ArrayList<String> data = new ArrayList<String>();

        data.add("Games");
        listView.setAdapter(mListAdapter = new ListAdapter(getContext(), data));
        listView.setOnItemClickListener(new OnListItemClickListenser());

        return rootView;
    }


    @OnClick(R.id.goBang)
    public void enterGoBnag(){
        //Toast.makeText(getActivity(),"The button works",Toast.LENGTH_SHORT).show();\
        startActivity(new Intent(getActivity(), InitAty.class));
    }

    @OnClick(R.id.enterConnect3)
    public void setmEnterConnect3(){

        //Toast.makeText(getActivity(),"The button works",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(), GameActivity.class));
        //fragmentManager.beginTransaction().remove(fragmentManager.findFragmentById(R.id.container_all);
        /*
        mFragmentManager.beginTransaction().replace(R.id.profile_fragment_id, new Connect3Fragment());
        mFragmentManager.beginTransaction().addToBackStack(null);
        mFragmentManager.beginTransaction().commit();
        */
    }

    /*
    @OnClick(R.id.dual)
    public void dual(){
        //Toast.makeText(getActivity(),"The button works",Toast.LENGTH_SHORT).show();

        startActivity(new Intent(getActivity(), RSPActivity.class));
    }
    */

    @OnClick(R.id.fragment_profile_signOut)
    public void setmSignOutButton(){
        mSharedPreferences.edit().putString(CONSTANT.USER_PICTURE,"").apply();
        mSharedPreferences.edit().putString(CONSTANT.USER_NAME,"").apply();
        mSharedPreferences.edit().putString(CONSTANT.USER_EMAIL,"").apply();
        FirebaseAuth.getInstance().signOut();
        getActivity().finish();
    }

    @OnClick(R.id.fragment_profile_camera_Picture)
    public void setmCameraImage(){
        if (!mMarshMellowPermission.checkPermissionForCamera()){
            mMarshMellowPermission.requestPermissionForCamera();
        } else if (!mMarshMellowPermission.checkPermissionForWriteExternalStorage()){
            mMarshMellowPermission.requestPermissionForWriteExternalStorage();
        } else if(!mMarshMellowPermission.checkPermissionForReadExternalStorage()){
            mMarshMellowPermission.requestPermissionForReadExternalStorage();
        } else{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            mTempUri = Uri.fromFile(getOutputFile());

            if(mTempUri == null){
                //Toast.makeText(getActivity(), "Uri is null", Toast.LENGTH_SHORT).show();
                Log.i("uri", "Button mTempUri is null");
            } else {
                Log.i("uri", "mTempUri: " + mTempUri.toString());
            }
            //String targetUri = Uri.fromFile(getOutputFile()).toString();
            //intent.putExtra("uri", targetUri);
            //intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,mTempUri);
            startActivityForResult(intent,REQUEST_CODE_CAMERA);
        }
    }

    private static File getOutputFile(){
        File mesdiaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES),"Playground");

        if (!mesdiaStorageDir.exists()){
            if (!mesdiaStorageDir.mkdir()){
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mesdiaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    @OnClick(R.id.fragment_profile_image_Picture)
    public void setmImageView(){
        if (!mMarshMellowPermission.checkPermissionForWriteExternalStorage()){
            mMarshMellowPermission.requestPermissionForWriteExternalStorage();
        } else if(!mMarshMellowPermission.checkPermissionForReadExternalStorage()){
            mMarshMellowPermission.requestPermissionForReadExternalStorage();
        } else{
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
            startActivityForResult(Intent.createChooser(intent,"Choose Image With"),
                    REQUEST_CODE_PICTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PICTURE){
            Uri selectedImageUri = data.getData();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("usersProfilePics").child(CONSTANT.encodeEmail(mUserEmailString))
                    .child(selectedImageUri.getLastPathSegment());

            mCompositeSubscription.add(LiveAccountServices.getInstance()
                    .changeProfilePhoto(storageReference,selectedImageUri,mActivity,
                            mUserEmailString,mUserPicture,mSharedPreferences,mSocket));
        }

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CAMERA){
            //Uri selectedImageUri = mTempUri;

            Bitmap photo = (Bitmap) data.getExtras().get("data");

            String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), photo, "profile", null);
            Uri selectedImageUri = Uri.parse(path);

            /*
            if(data.getData() == null){
                Log.i("uri", "Data is null");
            } else {
                Log.i("uri", "data uri: " + data.getData().toString());
            }
            */
            if(mTempUri == null){
                //Toast.makeText(getActivity(), "Uri is null", Toast.LENGTH_SHORT).show();
                Log.i("uri", "Result mTempUri is null");
            }

            if(selectedImageUri == null){
                //Toast.makeText(getActivity(), "Uri is null", Toast.LENGTH_SHORT).show();
                Log.i("uri", "null");
            }


            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("usersProfilePics").child(CONSTANT.encodeEmail(mUserEmailString))
                    .child(selectedImageUri.getLastPathSegment());

            mCompositeSubscription.add(LiveAccountServices.getInstance()
                    .changeProfilePhoto(storageReference,selectedImageUri,mActivity,
                            mUserEmailString,mUserPicture,mSharedPreferences,mSocket));

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();

        if(mAllFriendRequestListener != null){
            mAllFriendRequestReference.removeEventListener(mAllFriendRequestListener);
        }

        if(mAllGameRequestListener != null){
            mAllGameRequestReference.removeEventListener(mAllGameRequestListener);
        }

        if (mUsersNewMessagesListener!=null){
            mUsersNewMessagesReference.removeEventListener(mUsersNewMessagesListener);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseFragmentActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }


    private class ListAdapter extends BaseAdapter {
        private LayoutInflater mLayoutInflater;
        private ArrayList<String> mListData;
        private OnMenuClickListenser mOnMenuClickListenser = new OnMenuClickListenser();
        private class ViewHolder {
            public ViewHolder (View viewRoot) {
                root = viewRoot;
                txt = (TextView)viewRoot.findViewById(R.id.listview_menu_item_txt);
                menu = viewRoot.findViewById(R.id.listview_menu_item_menu);
                btnToast = (Button)viewRoot.findViewById(R.id.listview_menu_item_connect3);
                gobang = (Button)viewRoot.findViewById(R.id.listview_menu_item_gobang);
                maze = (Button) viewRoot.findViewById(R.id.listview_menu_item_maze);
                btnCollapse = (Button)viewRoot.findViewById(R.id.listview_menu_item_menu_collapse);
            }
            public View root;
            public TextView txt;
            public View menu;
            public Button btnToast;
            public Button gobang;
            public Button maze;
            public Button btnCollapse;
        }
        public ListAdapter(Context context, ArrayList<String> data) {
            mLayoutInflater = LayoutInflater.from(context);
            mListData = data;
        }
        @Override
        public int getCount() {
            return mListData == null ? 0 : mListData.size();
        }
        @Override
        public Object getItem(int position) {
            return mListData == null ? 0 : mListData.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.listview_menu_item, parent, false);
                convertView.setTag(new ViewHolder(convertView));
            }
            if (convertView != null && convertView.getTag() instanceof ViewHolder) {
                final ViewHolder holder = (ViewHolder)convertView.getTag();
                holder.txt.setText(String.valueOf(getItem(position)));
                holder.menu.setVisibility(position == mExpandedMenuPos ? View.VISIBLE : View.GONE);
                holder.btnToast.setText("Connect3");
                holder.gobang.setText("GoBang");
                holder.btnCollapse.setText("Collapse");
                holder.btnToast.setOnClickListener(mOnMenuClickListenser);
                holder.gobang.setOnClickListener(mOnMenuClickListenser);
                holder.maze.setOnClickListener(mOnMenuClickListenser);
                holder.btnCollapse.setOnClickListener(mOnMenuClickListenser);
            }
            return convertView;
        }
        private class OnMenuClickListenser implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                final int id = v.getId();
                if (id == R.id.listview_menu_item_connect3) {
                    Toast.makeText(getActivity(), "Play Connect3", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), GameActivity.class));
                } else if(id == R.id.listview_menu_item_gobang){
                    Toast.makeText(getActivity(), "Play GoBang" + mExpandedMenuPos, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), InitAty.class));
                } else if(id == R.id.listview_menu_item_maze){
                    Toast.makeText(getActivity(), "Play Maze" + mExpandedMenuPos, Toast.LENGTH_SHORT).show();

                } else if (id == R.id.listview_menu_item_menu_collapse) {
                    mExpandedMenuPos = -1;
                    notifyDataSetChanged();
                }
            }
        }
    }
    private class OnListItemClickListenser implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == mExpandedMenuPos) {
                mExpandedMenuPos = -1;
            } else {
                mExpandedMenuPos = position;
            }
            mListAdapter.notifyDataSetChanged();
        }
    }


}


