package com.example.kevin_sct.beastchat.fragments;

import android.content.Context;
import android.os.Bundle;
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
import com.example.kevin_sct.beastchat.activities.BaseFragmentActivity;
import com.example.kevin_sct.beastchat.entites.Move;
import com.example.kevin_sct.beastchat.services.LiveFriendServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.URISyntaxException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by kevin_sct on 5/19/17.
 */

public class RSPGameFragment extends BaseFragment {

    public static final String GAME_DETAILS_EXTRA = "GAME_DETAILS_EXTRA";

    private ArrayList<String> list = new ArrayList<String>(3);
    private int wins = 0;
    private int losses = 0;
    private int numOfRounds = 0;
    private int ties = 0;

    @BindView(R.id.rockButton)
    ImageView RockButton;

    @BindView(R.id.paperButton)
    ImageView PaperButton;

    @BindView(R.id.scissorsButton)
    ImageView ScissorsButton;

    @BindView(R.id.endButton)
    Button EndButton;

    @BindView(R.id.computerChoice)
    ImageView comChoice;

    @BindView(R.id.CPUChoice)
    TextView opponentName;


    private DatabaseReference mGetAllCurrenUsersGameFriendsReference;
    private ValueEventListener mGetAllCurrentUsersGameFriendsListener;

    private DatabaseReference mGetAllMovesReference;
    private ValueEventListener mGetAllMovesListener;

    private LiveFriendServices mLiveFriendServices;
    private Unbinder mUnbinder;
    private Socket mSocket;
    private String mGameFriendEmailString;
    private String mGameFriendNameString;
    private String mUserEmailString;


    private String mComputerChoice;
    private String lastMoveUserEmail;
    //private boolean mLock;

    //private String mComputerChoice = list.get(randomNumGenerator());

    private BaseFragmentActivity mActivity;

    public static RSPGameFragment newInstance(ArrayList<String> gameFriendDetails){
        Bundle arguments = new Bundle();
        arguments.putStringArrayList(GAME_DETAILS_EXTRA, gameFriendDetails);
        RSPGameFragment gameFragment = new RSPGameFragment();
        gameFragment.setArguments(arguments);
        return gameFragment;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mSocket = IO.socket(CONSTANT.IP_LOCAL_HOST);
        } catch (URISyntaxException e) {
            Log.i(LoginFragment.class.getSimpleName(),e.getMessage());
            Toast.makeText(getActivity(),"Can't connect to the server",Toast.LENGTH_SHORT).show();
        }

        mSocket.connect();

        mLiveFriendServices = LiveFriendServices.getInstance();
        ArrayList<String> gameFriendDetails = getArguments().getStringArrayList(GAME_DETAILS_EXTRA);
        mGameFriendEmailString = gameFriendDetails.get(0);
        mGameFriendNameString = gameFriendDetails.get(1);
        mUserEmailString = mSharedPreferences.getString(CONSTANT.USER_EMAIL,"");

    }

    @OnClick(R.id.rockButton)
    public void playRock(){
        numOfRounds++;

        /*
        DatabaseReference newMoveReference = mGetAllMovesReference.push();
        Move move = new Move(newMoveReference.getKey(), "RockButton"
                , mUserEmailString);
        newMoveReference.setValue(move);

        mCompositeSubscription.add(
                mLiveFriendServices.sendMove(mSocket, mUserEmailString,
                        "RockButton",
                        mGameFriendEmailString,
                        mSharedPreferences.getString(CONSTANT.USER_NAME,""))
        );
        */

        //String computerChoice = list.get(randomNumGenerator());
        //String computerChoice;
        /*
        if (mComputerChoice.equals("rock")) {
            Toast.makeText(getActivity(),
                    "Tie", Toast.LENGTH_SHORT).show();
            ties++;
            comChoice.setImageResource(R.drawable.rock);
        }

        if (mComputerChoice.equals("paper")) {
            Toast.makeText(getActivity().getBaseContext(),
                    "You Win", Toast.LENGTH_SHORT).show();
            wins++;
            comChoice.setImageResource(R.drawable.paper);
        }

        if (mComputerChoice.equals("scissors")) {
            Toast.makeText(getActivity().getBaseContext(),
                    "You Lose", Toast.LENGTH_SHORT).show();
            losses++;
            comChoice.setImageResource(R.drawable.scissors);
        }
        */




        final String[] tmpChoice = new String[1];
        Query lastMoveQuery = FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_USER_MOVE)
                .child(CONSTANT.encodeEmail(mUserEmailString))
                .child(CONSTANT.encodeEmail(mGameFriendEmailString)).orderByKey().limitToLast(1);
        lastMoveQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        lastMoveUserEmail = snapshot.child("moveSenderEmail").getValue().toString();
                        Log.i("last", lastMoveUserEmail);
                        tmpChoice[0] = snapshot.child("moveText").getValue().toString();
                        Log.i("last", snapshot.child("moveText").getValue().toString());


                        if (!lastMoveUserEmail.equals(mUserEmailString)) {

                            DatabaseReference newMoveReference = mGetAllMovesReference.push();
                            Move move = new Move(newMoveReference.getKey(), "RockButton"
                                    , mUserEmailString);
                            newMoveReference.setValue(move);

                            mCompositeSubscription.add(
                                    mLiveFriendServices.sendMove(mSocket, mUserEmailString,
                                            "RockButton",
                                            mGameFriendEmailString,
                                            mSharedPreferences.getString(CONSTANT.USER_NAME, ""))
                            );

                            mComputerChoice = tmpChoice[0];
                            Log.i("last", "Opponent choice: " + tmpChoice[0]);
                            if (mComputerChoice.equals("RockButton")) {
                                comChoice.setImageResource(R.drawable.rock);
                                comChoice.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(),
                                        "Tie", Toast.LENGTH_SHORT).show();
                                ties++;

                            }

                            if (mComputerChoice.equals("PaperButton")) {
                                comChoice.setImageResource(R.drawable.paper);
                                comChoice.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity().getBaseContext(),
                                        "You Win", Toast.LENGTH_SHORT).show();
                                wins++;

                            }

                            if (mComputerChoice.equals("ScissorButton")) {
                                comChoice.setImageResource(R.drawable.scissors);
                                comChoice.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity().getBaseContext(),
                                        "You Lose", Toast.LENGTH_SHORT).show();
                                losses++;

                            }

                            FirebaseDatabase.getInstance().getReference()
                                    .child(CONSTANT.FIRE_BASE_PATH_USER_MOVE).removeValue();


                        } else {
                            Toast.makeText(getActivity(), "Wait for " + lastMoveUserEmail, Toast.LENGTH_SHORT).show();
                            comChoice.setVisibility(View.GONE);
                        }

                    }
                } else {
                    lastMoveUserEmail = null;
                    comChoice.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "You first", Toast.LENGTH_SHORT).show();
                    DatabaseReference newMoveReference = mGetAllMovesReference.push();
                    Move move = new Move(newMoveReference.getKey(), "RockButton"
                            , mUserEmailString);
                    newMoveReference.setValue(move);

                    mCompositeSubscription.add(
                            mLiveFriendServices.sendMove(mSocket, mUserEmailString,
                                    "RockButton",
                                    mGameFriendEmailString,
                                    mSharedPreferences.getString(CONSTANT.USER_NAME, ""))
                    );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.scissorsButton)
    public void playScissor(){
        numOfRounds++;
        Log.i("last", "||||||PUSH BUTTON||||||||");

        final String[] tmpChoice = new String[1];
        Query lastMoveQuery = FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_USER_MOVE)
                .child(CONSTANT.encodeEmail(mUserEmailString))
                .child(CONSTANT.encodeEmail(mGameFriendEmailString)).orderByKey().limitToLast(1);
        lastMoveQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        lastMoveUserEmail = snapshot.child("moveSenderEmail").getValue().toString();
                        Log.i("last", lastMoveUserEmail);
                        tmpChoice[0] = snapshot.child("moveText").getValue().toString();
                        Log.i("last", snapshot.child("moveText").getValue().toString());


                        if (!lastMoveUserEmail.equals(mUserEmailString)) {

                            DatabaseReference newMoveReference = mGetAllMovesReference.push();
                            Move move = new Move(newMoveReference.getKey(), "ScissorButton"
                                    , mUserEmailString);
                            newMoveReference.setValue(move);

                            mCompositeSubscription.add(
                                    mLiveFriendServices.sendMove(mSocket, mUserEmailString,
                                            "ScissorButton",
                                            mGameFriendEmailString,
                                            mSharedPreferences.getString(CONSTANT.USER_NAME, ""))
                            );

                            mComputerChoice = tmpChoice[0];
                            Log.i("last", "Opponent choice: " + tmpChoice[0]);
                            if (mComputerChoice.equals("rockButton")) {
                                comChoice.setImageResource(R.drawable.rock);
                                comChoice.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(),
                                        "You Lose", Toast.LENGTH_SHORT).show();
                                losses++;

                            }

                            if (mComputerChoice.equals("paperButton")) {
                                comChoice.setImageResource(R.drawable.paper);
                                comChoice.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(),
                                        "You Win", Toast.LENGTH_SHORT).show();
                                wins++;

                            }

                            if (mComputerChoice.equals("scissorsButton")) {
                                comChoice.setImageResource(R.drawable.scissors);
                                comChoice.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(),
                                        "Tie", Toast.LENGTH_SHORT).show();
                                ties++;

                            }

                            FirebaseDatabase.getInstance().getReference()
                                    .child(CONSTANT.FIRE_BASE_PATH_USER_MOVE).removeValue();

                        } else {
                            Toast.makeText(getActivity(), "Wait for " + lastMoveUserEmail, Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    lastMoveUserEmail = null;
                    Toast.makeText(getActivity(), "You first", Toast.LENGTH_SHORT).show();
                    DatabaseReference newMoveReference = mGetAllMovesReference.push();
                    Move move = new Move(newMoveReference.getKey(), "ScissorButton"
                            , mUserEmailString);
                    newMoveReference.setValue(move);

                    mCompositeSubscription.add(
                            mLiveFriendServices.sendMove(mSocket, mUserEmailString,
                                    "ScissorButton",
                                    mGameFriendEmailString,
                                    mSharedPreferences.getString(CONSTANT.USER_NAME, ""))
                    );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /*
        if(lastMoveUserEmail == null){
            Toast.makeText(getActivity(), "You first", Toast.LENGTH_SHORT).show();
            DatabaseReference newMoveReference = mGetAllMovesReference.push();
            Move move = new Move(newMoveReference.getKey(), "ScissorButton"
                    , mUserEmailString);
            newMoveReference.setValue(move);

            mCompositeSubscription.add(
                    mLiveFriendServices.sendMove(mSocket, mUserEmailString,
                            "ScissorButton",
                            mGameFriendEmailString,
                            mSharedPreferences.getString(CONSTANT.USER_NAME, ""))
            );
        }
        */


        /*
        //String computerChoice = list.get(randomNumGenerator());
        if (mComputerChoice.equals("rock")) {
            Toast.makeText(getActivity(),
                    "You Lose", Toast.LENGTH_SHORT).show();
            losses++;
            comChoice.setImageResource(R.drawable.rock);
        }

        if (mComputerChoice.equals("paper")) {
            Toast.makeText(getActivity(),
                    "You Win", Toast.LENGTH_SHORT).show();
            wins++;
            comChoice.setImageResource(R.drawable.paper);
        }

        if (mComputerChoice.equals("scissors")) {
            Toast.makeText(getActivity(),
                    "Tie", Toast.LENGTH_SHORT).show();
            ties++;
            comChoice.setImageResource(R.drawable.scissors);
        }
        */
    }

    @OnClick(R.id.paperButton)
    public void playPaper(){
        numOfRounds++;

        /*
        DatabaseReference newMoveReference = mGetAllMovesReference.push();
        Move move = new Move(newMoveReference.getKey(), "PaperButton"
                , mUserEmailString);
        newMoveReference.setValue(move);

        mCompositeSubscription.add(
                mLiveFriendServices.sendMove(mSocket, mUserEmailString,
                        "PaperButton",
                        mGameFriendEmailString,
                        mSharedPreferences.getString(CONSTANT.USER_NAME,""))
        );
        */

        /*
        //String computerChoice = list.get(randomNumGenerator());
        if (mComputerChoice.equals("rock")) {
            Toast.makeText(getActivity(),
                    "You Win", Toast.LENGTH_SHORT).show();
            wins++;
            comChoice.setImageResource(R.drawable.rock);
        }

        if (mComputerChoice.equals("paper")) {
            Toast.makeText(getActivity(),
                    "Tie", Toast.LENGTH_SHORT).show();
            ties++;
            comChoice.setImageResource(R.drawable.paper);
        }

        if (mComputerChoice.equals("scissors")) {
            Toast.makeText(getActivity(),
                    "You Lose", Toast.LENGTH_SHORT).show();
            losses++;
            comChoice.setImageResource(R.drawable.scissors);
        }
        */

        final String[] tmpChoice = new String[1];
        Query lastMoveQuery = FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_USER_MOVE)
                .child(CONSTANT.encodeEmail(mUserEmailString))
                .child(CONSTANT.encodeEmail(mGameFriendEmailString)).orderByKey().limitToLast(1);
        lastMoveQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        lastMoveUserEmail = snapshot.child("moveSenderEmail").getValue().toString();
                        Log.i("last", lastMoveUserEmail);
                        tmpChoice[0] = snapshot.child("moveText").getValue().toString();
                        Log.i("last", snapshot.child("moveText").getValue().toString());


                        if (!lastMoveUserEmail.equals(mUserEmailString)) {

                            DatabaseReference newMoveReference = mGetAllMovesReference.push();
                            Move move = new Move(newMoveReference.getKey(), "PaperButton"
                                    , mUserEmailString);
                            newMoveReference.setValue(move);

                            mCompositeSubscription.add(
                                    mLiveFriendServices.sendMove(mSocket, mUserEmailString,
                                            "PaperButton",
                                            mGameFriendEmailString,
                                            mSharedPreferences.getString(CONSTANT.USER_NAME, ""))
                            );

                            mComputerChoice = tmpChoice[0];
                            Log.i("last", "Opponent choice: " + tmpChoice[0]);
                            if (mComputerChoice.equals("RockButton")) {
                                comChoice.setImageResource(R.drawable.rock);
                                comChoice.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(),
                                        "You Win", Toast.LENGTH_SHORT).show();
                                wins++;

                            }

                            if (mComputerChoice.equals("PaperButton")) {
                                comChoice.setImageResource(R.drawable.paper);
                                comChoice.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(),
                                        "Tie", Toast.LENGTH_SHORT).show();
                                ties++;

                            }

                            if (mComputerChoice.equals("ScissorButton")) {
                                comChoice.setImageResource(R.drawable.scissors);
                                comChoice.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(),
                                        "You Lose", Toast.LENGTH_SHORT).show();
                                losses++;

                            }

                            FirebaseDatabase.getInstance().getReference()
                                    .child(CONSTANT.FIRE_BASE_PATH_USER_MOVE).removeValue();

                        } else {
                            Toast.makeText(getActivity(), "Wait for " + lastMoveUserEmail, Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    lastMoveUserEmail = null;
                    Toast.makeText(getActivity(), "You first", Toast.LENGTH_SHORT).show();
                    DatabaseReference newMoveReference = mGetAllMovesReference.push();
                    Move move = new Move(newMoveReference.getKey(), "PaperButton"
                            , mUserEmailString);
                    newMoveReference.setValue(move);

                    mCompositeSubscription.add(
                            mLiveFriendServices.sendMove(mSocket, mUserEmailString,
                                    "PaperButton",
                                    mGameFriendEmailString,
                                    mSharedPreferences.getString(CONSTANT.USER_NAME, ""))
                    );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.endButton)
    public void end(){
        Toast.makeText(getActivity(), "Wins: " + wins + " | Losses: " + losses + " | Ties:" + ties, Toast.LENGTH_LONG).show();
        FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_USER_MOVE).removeValue();
        FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_USER_NEW_MOVE).removeValue();
        FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_USER_GAME_FRIENDS).removeValue();
        getActivity().finish();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rspgame, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        list.add("rock");
        list.add("paper");
        list.add("scissors");

        mGetAllCurrenUsersGameFriendsReference = FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_USER_GAME_FRIENDS)
                .child(CONSTANT.encodeEmail(mUserEmailString));
        mGetAllCurrentUsersGameFriendsListener = mLiveFriendServices.getGameFriendName(opponentName);
        mGetAllCurrenUsersGameFriendsReference.addValueEventListener(mGetAllCurrentUsersGameFriendsListener);

        mGetAllMovesReference = FirebaseDatabase.getInstance().getReference()
                .child(CONSTANT.FIRE_BASE_PATH_USER_MOVE)
                .child(CONSTANT.encodeEmail(mUserEmailString))
                .child(CONSTANT.encodeEmail(mGameFriendEmailString));

        mGetAllMovesListener = mLiveFriendServices.getAllMoves(mUserEmailString, mGameFriendEmailString);

        mGetAllMovesReference.addValueEventListener(mGetAllMovesListener);

        return rootView;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        if(mGetAllCurrentUsersGameFriendsListener != null){
            mGetAllCurrenUsersGameFriendsReference.removeEventListener(mGetAllCurrentUsersGameFriendsListener);
        }

        if(mGetAllMovesListener != null){
            mGetAllMovesReference.removeEventListener(mGetAllMovesListener);
        }
    }

    //  Generates a number between 1 and 3 for to get Computer Choice
    private int randomNumGenerator() {
        double randomNum = Math.floor((Math.random() * 3));
        return (int) randomNum;
    }
}
