package com.example.kevin_sct.beastchat.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kevin_sct.beastchat.R;
import com.example.kevin_sct.beastchat.Utils.CONSTANT;
import com.example.kevin_sct.beastchat.Utils.MarshMellowPermisson;
import com.example.kevin_sct.beastchat.activities.BaseFragmentActivity;
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

    public static ProfileFragment newInstance(){
        return new ProfileFragment();
    }

    private Unbinder mUnbinder;

    private LiveFriendServices mLiveFriendServices;
    private DatabaseReference mAllFriendRequestReference;
    private ValueEventListener mAllFriendRequestListener;
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


        mUsersNewMessagesReference = FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_USER_NEW_MESSAGES).child(CONSTANT.encodeEmail(mUserEmailString));
        mUsersNewMessagesListener = mLiveFriendServices.getAllNewMessages(mBottomBar,R.id.tab_messages);

        mUsersNewMessagesReference.addValueEventListener(mUsersNewMessagesListener);
        return rootView;
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
            intent.putExtra(MediaStore.EXTRA_OUTPUT,mTempUri);
            startActivityForResult(intent,REQUEST_CODE_CAMERA);
        }
    }

    @OnClick(R.id.fragment_profile_signOut)
    public void setmSignOutButton(){
        mSharedPreferences.edit().putString(CONSTANT.USER_PICTURE,"").apply();
        mSharedPreferences.edit().putString(CONSTANT.USER_NAME,"").apply();
        mSharedPreferences.edit().putString(CONSTANT.USER_EMAIL,"").apply();
        FirebaseAuth.getInstance().signOut();
        getActivity().finish();
    }

    private static File getOutputFile(){
        File mesdiaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES),"BeastChat");

        if (!mesdiaStorageDir.exists()){
            if (!mesdiaStorageDir.mkdir()){
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mesdiaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
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
            Uri selectedImageUri = mTempUri;


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
}
