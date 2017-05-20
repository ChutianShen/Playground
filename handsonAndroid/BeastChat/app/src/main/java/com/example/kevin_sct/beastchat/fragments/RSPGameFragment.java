package com.example.kevin_sct.beastchat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kevin_sct.beastchat.R;
import com.example.kevin_sct.beastchat.activities.BaseFragmentActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by kevin_sct on 5/19/17.
 */

public class RSPGameFragment extends Fragment {

    private ArrayList<String> list = new ArrayList<String>(3);
    private int wins = 0;
    private int losses = 0;
    private int numOfRounds = 0;
    private int ties = 0;

    @BindView(R.id.rockButton)
    ImageView RockButton;
    //ImageButton RockButton = (ImageButton) findViewById(R.id.rockButton);

    @BindView(R.id.paperButton)
    ImageView PaperButton;
    //ImageButton PaperButton = (ImageButton) findViewById(R.id.paperButton);

    @BindView(R.id.scissorsButton)
    ImageView ScissorsButton;
    //ImageButton ScissorsButton = (ImageButton) findViewById(R.id.scissorsButton);

    @BindView(R.id.endButton)
    Button EndButton;
    //Button EndButton = (Button) findViewById(R.id.endButton);

    @BindView(R.id.computerChoice)
    ImageView comChoice;
    //final ImageView comChoice = (ImageView) findViewById(R.id.computerChoice);

    private DatabaseReference mGetAllCurrenUsersGameFriendsReference;
    private ValueEventListener mGetAllCurrentUsersGameFriendsListener;

    private Unbinder mUnbinder;

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
