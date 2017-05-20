package com.example.kevin_sct.beastchat.fragments;

import android.content.Context;
import android.content.Intent;
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
import com.example.kevin_sct.beastchat.activities.RSPActivity;
import com.example.kevin_sct.beastchat.services.LiveFriendServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private LiveFriendServices mLiveFriendServices;
    private Unbinder mUnbinder;
    private Socket mSocket;
    private String mUserEmailString;

    private BaseFragmentActivity mActivity;

    public static RSPGameFragment newinstance(){return new RSPGameFragment();}

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
        mUserEmailString = mSharedPreferences.getString(CONSTANT.USER_EMAIL, "");

    }

    @OnClick(R.id.rockButton)
    public void playRock(){
        numOfRounds++;
        String computerChoice = list.get(randomNumGenerator());
        if (computerChoice.equals("rock")) {
            Toast.makeText(getActivity(),
                    "Tie", Toast.LENGTH_SHORT).show();
            ties++;
            comChoice.setImageResource(R.drawable.rock);
        }

        if (computerChoice.equals("paper")) {
            Toast.makeText(getActivity().getBaseContext(),
                    "You Win", Toast.LENGTH_SHORT).show();
            wins++;
            comChoice.setImageResource(R.drawable.paper);
        }

        if (computerChoice.equals("scissors")) {
            Toast.makeText(getActivity().getBaseContext(),
                    "You Lose", Toast.LENGTH_SHORT).show();
            losses++;
            comChoice.setImageResource(R.drawable.scissors);

        }
    }

    @OnClick(R.id.scissorsButton)
    public void playScissor(){
        numOfRounds++;
        String computerChoice = list.get(randomNumGenerator());
        if (computerChoice.equals("rock")) {
            Toast.makeText(getActivity(),
                    "You Lose", Toast.LENGTH_SHORT).show();
            losses++;
            comChoice.setImageResource(R.drawable.rock);
        }

        if (computerChoice.equals("paper")) {
            Toast.makeText(getActivity(),
                    "You Win", Toast.LENGTH_SHORT).show();
            wins++;
            comChoice.setImageResource(R.drawable.paper);
        }

        if (computerChoice.equals("scissors")) {
            Toast.makeText(getActivity(),
                    "Tie", Toast.LENGTH_SHORT).show();
            ties++;
            comChoice.setImageResource(R.drawable.scissors);
        }
    }

    @OnClick(R.id.paperButton)
    public void playPaper(){
        numOfRounds++;
        String computerChoice = list.get(randomNumGenerator());
        if (computerChoice.equals("rock")) {
            Toast.makeText(getActivity(),
                    "You Win", Toast.LENGTH_SHORT).show();
            wins++;
            comChoice.setImageResource(R.drawable.rock);
        }

        if (computerChoice.equals("paper")) {
            Toast.makeText(getActivity(),
                    "Tie", Toast.LENGTH_SHORT).show();
            ties++;
            comChoice.setImageResource(R.drawable.paper);
        }

        if (computerChoice.equals("scissors")) {
            Toast.makeText(getActivity(),
                    "You Lose", Toast.LENGTH_SHORT).show();
            losses++;
            comChoice.setImageResource(R.drawable.scissors);
        }
    }

    @OnClick(R.id.endButton)
    public void end(){
        Toast.makeText(getActivity(), "Wins: " + wins + " | Losses: " + losses + " | Ties:" + ties, Toast.LENGTH_LONG).show();
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
                .child(CONSTANT.FIRE_BASE_PATH_USER_GAME_FRIENDS).child(CONSTANT.encodeEmail(mUserEmailString));

        Intent intent = new Intent(getActivity(), RSPActivity.class);
        mGetAllCurrentUsersGameFriendsListener = mLiveFriendServices.getGameFriendName(opponentName);

        mGetAllCurrenUsersGameFriendsReference.addValueEventListener(mGetAllCurrentUsersGameFriendsListener);

        return rootView;
        //setContentView(R.layout.activity_main);



/**
 * Action listener for each of the image buttons
 */
    /*
        if (RockButton != null) {
            RockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    numOfRounds++;
                    String computerChoice = list.get(randomNumGenerator());
                    if (computerChoice.equals("rock")) {
                        Toast.makeText(getApplication().getBaseContext(),
                                "Tie", Toast.LENGTH_SHORT).show();
                        ties++;
                        comChoice.setImageResource(R.drawable.rock);
                    }

                    if (computerChoice.equals("paper")) {
                        Toast.makeText(getApplication().getBaseContext(),
                                "You Win", Toast.LENGTH_SHORT).show();
                        wins++;
                        comChoice.setImageResource(R.drawable.paper);
                    }

                    if (computerChoice.equals("scissors")) {
                        Toast.makeText(getApplication().getBaseContext(),
                                "You Lose", Toast.LENGTH_SHORT).show();
                        losses++;
                        comChoice.setImageResource(R.drawable.scissors);

                    }
                }

            });
        }


        if (PaperButton != null) {
            PaperButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numOfRounds++;
                    String computerChoice = list.get(randomNumGenerator());
                    if (computerChoice.equals("rock")) {
                        Toast.makeText(getApplication().getBaseContext(),
                                "You Win", Toast.LENGTH_SHORT).show();
                        wins++;
                        comChoice.setImageResource(R.drawable.rock);
                    }

                    if (computerChoice.equals("paper")) {
                        Toast.makeText(getApplication().getBaseContext(),
                                "Tie", Toast.LENGTH_SHORT).show();
                        ties++;
                        comChoice.setImageResource(R.drawable.paper);
                    }

                    if (computerChoice.equals("scissors")) {
                        Toast.makeText(getApplication().getBaseContext(),
                                "You Lose", Toast.LENGTH_SHORT).show();
                        losses++;
                        comChoice.setImageResource(R.drawable.scissors);
                    }
                }
            });
        }


        if (ScissorsButton != null) {
            ScissorsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numOfRounds++;
                    String computerChoice = list.get(randomNumGenerator());
                    if (computerChoice.equals("rock")) {
                        Toast.makeText(getApplication().getBaseContext(),
                                "You Lose", Toast.LENGTH_SHORT).show();
                        losses++;
                        comChoice.setImageResource(R.drawable.rock);
                    }

                    if (computerChoice.equals("paper")) {
                        Toast.makeText(getApplication().getBaseContext(),
                                "You Win", Toast.LENGTH_SHORT).show();
                        wins++;
                        comChoice.setImageResource(R.drawable.paper);
                    }

                    if (computerChoice.equals("scissors")) {
                        Toast.makeText(getApplication().getBaseContext(),
                                "Tie", Toast.LENGTH_SHORT).show();
                        ties++;
                        comChoice.setImageResource(R.drawable.scissors);
                    }
                }
            });
        }



        if (EndButton != null) {
            EndButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplication().getBaseContext(), "Wins: " + wins + " | Losses: " + losses + " | Ties:" + ties, Toast.LENGTH_LONG).show();
                }
            });
        }

        return super.onCreateView(inflater, container, savedInstanceState);
        */
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    //  Generates a number between 1 and 3 for to get Computer Choice
    private int randomNumGenerator() {
        double randomNum = Math.floor((Math.random() * 3));
        return (int) randomNum;
    }
}
