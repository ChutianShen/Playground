package com.example.kevin_sct.beastchat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kevin_sct.beastchat.R;
import com.example.kevin_sct.beastchat.activities.BaseFragmentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by kevin_sct on 5/15/17.
 */

public class Connect3Fragment extends Fragment {

    @BindView(R.id.winnerMessage)
    TextView mWinnerMessage;

    @BindView(R.id.playAgainLayout)
    LinearLayout mlayout;

    @BindView(R.id.gridLayout)
    GridLayout mgridLayout;

    @BindView(R.id.playAgainButton)
    Button mPlayAgainButton;

    @BindView(R.id.imageView)
    ImageView mSpot11;

    @BindView(R.id.imageView2)
    ImageView mSpot12;

    @BindView(R.id.imageView3)
    ImageView mSpot13;

    @BindView(R.id.imageView4)
    ImageView mSpot21;

    @BindView(R.id.imageView5)
    ImageView mSpot22;

    @BindView(R.id.imageView6)
    ImageView mSpot23;

    @BindView(R.id.imageView7)
    ImageView mSpot31;

    @BindView(R.id.imageView8)
    ImageView mSpot32;

    @BindView(R.id.imageView9)
    ImageView mSpot33;

    @BindView(R.id.exitConnect3)
    Button mExitGame;

    private Unbinder mUnbinder;

    private BaseFragmentActivity mActivity;

    // 0 = yellow, 1 = red

    int activePlayer = 0;

    boolean gameIsActive = true;

    // 2 means unplayed

    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};

    int[][] winningPositions = {{0,1,2}, {3,4,5}, {6,7,8}, {0,3,6}, {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}};

    public static Connect3Fragment newInstance(){return new Connect3Fragment();}

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

    /*
    public void dropIn(View view) {
        ImageView counter = (ImageView) view;
        int tappedCounter = Integer.parseInt(counter.getTag().toString());

        if (gameState[tappedCounter] == 2 && gameIsActive) {

            gameState[tappedCounter] = activePlayer;

            counter.setTranslationY(-1000f);

            if (activePlayer == 0) {

                counter.setImageResource(R.drawable.yellow);

                activePlayer = 1;

            } else {

                counter.setImageResource(R.drawable.red);

                activePlayer = 0;

            }

            counter.animate().translationYBy(1000f).rotation(360).setDuration(300);

            for (int[] winningPosition : winningPositions) {

                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                        gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                        gameState[winningPosition[0]] != 2) {

                    // Someone has won!

                    gameIsActive = false;

                    String winner = "Red";

                    if (gameState[winningPosition[0]] == 0) {

                        winner = "Yellow";

                    }

                    //TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);

                    mWinnerMessage.setText(winner + " has won!");

                    //LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);

                    mlayout.setVisibility(View.VISIBLE);

                } else {

                    boolean gameIsOver = true;

                    for (int counterState : gameState) {

                        if (counterState == 2) gameIsOver = false;

                    }

                    if (gameIsOver) {

                        //TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);

                        mWinnerMessage.setText("It's a draw");

                        //LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);

                        mlayout.setVisibility(View.VISIBLE);

                    }

                }

            }
        }
    }
    */
    /*
    public void playAgain(View view) {

        gameIsActive = true;

        //LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);

        mlayout.setVisibility(View.INVISIBLE);

        activePlayer = 0;

        for (int i = 0; i < gameState.length; i++) {

            gameState[i] = 2;

        }

        //GridLayout gridLayout = (GridLayout)findViewById(R.id.gridLayout);

        for (int i = 0; i< mgridLayout.getChildCount(); i++) {

            ((ImageView) mgridLayout.getChildAt(i)).setImageResource(0);

        }

    }
    */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_connect3, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @OnClick(R.id.playAgainButton)
    public void playAgain(){
        gameIsActive = true;

        //LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);

        mlayout.setVisibility(View.INVISIBLE);

        activePlayer = 0;

        for (int i = 0; i < gameState.length; i++) {

            gameState[i] = 2;

        }

        //GridLayout gridLayout = (GridLayout)findViewById(R.id.gridLayout);

        for (int i = 0; i< mgridLayout.getChildCount(); i++) {

            ((ImageView) mgridLayout.getChildAt(i)).setImageResource(0);

        }
    }


    @OnClick(R.id.imageView)
    public void dropIn(){
        ImageView counter = (ImageView) mSpot11;
        int tappedCounter = Integer.parseInt(counter.getTag().toString());

        if (gameState[tappedCounter] == 2 && gameIsActive) {

            gameState[tappedCounter] = activePlayer;

            counter.setTranslationY(-1000f);

            if (activePlayer == 0) {

                counter.setImageResource(R.drawable.yellow);

                activePlayer = 1;

            } else {

                counter.setImageResource(R.drawable.red);

                activePlayer = 0;

            }

            counter.animate().translationYBy(1000f).rotation(360).setDuration(300);

            for (int[] winningPosition : winningPositions) {

                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                        gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                        gameState[winningPosition[0]] != 2) {

                    // Someone has won!

                    gameIsActive = false;

                    String winner = "Red";

                    if (gameState[winningPosition[0]] == 0) {

                        winner = "Yellow";

                    }

                    //TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);

                    mWinnerMessage.setText(winner + " has won!");

                    //LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);

                    mlayout.setVisibility(View.VISIBLE);

                } else {

                    boolean gameIsOver = true;

                    for (int counterState : gameState) {

                        if (counterState == 2) gameIsOver = false;

                    }

                    if (gameIsOver) {

                        //TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);

                        mWinnerMessage.setText("It's a draw");

                        //LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);

                        mlayout.setVisibility(View.VISIBLE);

                    }
                }
            }
        }
    }

    @OnClick(R.id.imageView2)
    public void dropIn12(){
        ImageView counter = (ImageView) mSpot12;
        int tappedCounter = Integer.parseInt(counter.getTag().toString());

        if (gameState[tappedCounter] == 2 && gameIsActive) {

            gameState[tappedCounter] = activePlayer;

            counter.setTranslationY(-1000f);

            if (activePlayer == 0) {

                counter.setImageResource(R.drawable.yellow);

                activePlayer = 1;

            } else {

                counter.setImageResource(R.drawable.red);

                activePlayer = 0;

            }

            counter.animate().translationYBy(1000f).rotation(360).setDuration(300);

            for (int[] winningPosition : winningPositions) {

                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                        gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                        gameState[winningPosition[0]] != 2) {

                    // Someone has won!

                    gameIsActive = false;

                    String winner = "Red";

                    if (gameState[winningPosition[0]] == 0) {

                        winner = "Yellow";

                    }

                    //TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);

                    mWinnerMessage.setText(winner + " has won!");

                    //LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);

                    mlayout.setVisibility(View.VISIBLE);

                } else {

                    boolean gameIsOver = true;

                    for (int counterState : gameState) {

                        if (counterState == 2) gameIsOver = false;

                    }

                    if (gameIsOver) {

                        //TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);

                        mWinnerMessage.setText("It's a draw");

                        //LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);

                        mlayout.setVisibility(View.VISIBLE);

                    }
                }
            }
        }
    }

    @OnClick(R.id.imageView3)
    public void dropIn13(){
        ImageView counter = (ImageView) mSpot13;
        int tappedCounter = Integer.parseInt(counter.getTag().toString());

        if (gameState[tappedCounter] == 2 && gameIsActive) {

            gameState[tappedCounter] = activePlayer;

            counter.setTranslationY(-1000f);

            if (activePlayer == 0) {

                counter.setImageResource(R.drawable.yellow);

                activePlayer = 1;

            } else {

                counter.setImageResource(R.drawable.red);

                activePlayer = 0;

            }

            counter.animate().translationYBy(1000f).rotation(360).setDuration(300);

            for (int[] winningPosition : winningPositions) {

                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                        gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                        gameState[winningPosition[0]] != 2) {

                    // Someone has won!

                    gameIsActive = false;

                    String winner = "Red";

                    if (gameState[winningPosition[0]] == 0) {

                        winner = "Yellow";

                    }

                    //TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);

                    mWinnerMessage.setText(winner + " has won!");

                    //LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);

                    mlayout.setVisibility(View.VISIBLE);

                } else {

                    boolean gameIsOver = true;

                    for (int counterState : gameState) {

                        if (counterState == 2) gameIsOver = false;

                    }

                    if (gameIsOver) {

                        //TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);

                        mWinnerMessage.setText("It's a draw");

                        //LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);

                        mlayout.setVisibility(View.VISIBLE);

                    }
                }
            }
        }
    }

    @OnClick(R.id.imageView4)
    public void dropIn21(){
        ImageView counter = (ImageView) mSpot21;
        int tappedCounter = Integer.parseInt(counter.getTag().toString());

        if (gameState[tappedCounter] == 2 && gameIsActive) {

            gameState[tappedCounter] = activePlayer;

            counter.setTranslationY(-1000f);

            if (activePlayer == 0) {

                counter.setImageResource(R.drawable.yellow);

                activePlayer = 1;

            } else {

                counter.setImageResource(R.drawable.red);

                activePlayer = 0;

            }

            counter.animate().translationYBy(1000f).rotation(360).setDuration(300);

            for (int[] winningPosition : winningPositions) {

                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                        gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                        gameState[winningPosition[0]] != 2) {

                    // Someone has won!

                    gameIsActive = false;

                    String winner = "Red";

                    if (gameState[winningPosition[0]] == 0) {

                        winner = "Yellow";

                    }

                    //TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);

                    mWinnerMessage.setText(winner + " has won!");

                    //LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);

                    mlayout.setVisibility(View.VISIBLE);

                } else {

                    boolean gameIsOver = true;

                    for (int counterState : gameState) {

                        if (counterState == 2) gameIsOver = false;

                    }

                    if (gameIsOver) {

                        //TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);

                        mWinnerMessage.setText("It's a draw");

                        //LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);

                        mlayout.setVisibility(View.VISIBLE);

                    }
                }
            }
        }
    }

    @OnClick(R.id.imageView5)
    public void dropIn22(){
        ImageView counter = (ImageView) mSpot22;
        int tappedCounter = Integer.parseInt(counter.getTag().toString());

        if (gameState[tappedCounter] == 2 && gameIsActive) {

            gameState[tappedCounter] = activePlayer;

            counter.setTranslationY(-1000f);

            if (activePlayer == 0) {

                counter.setImageResource(R.drawable.yellow);

                activePlayer = 1;

            } else {

                counter.setImageResource(R.drawable.red);

                activePlayer = 0;

            }

            counter.animate().translationYBy(1000f).rotation(360).setDuration(300);

            for (int[] winningPosition : winningPositions) {

                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                        gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                        gameState[winningPosition[0]] != 2) {

                    // Someone has won!

                    gameIsActive = false;

                    String winner = "Red";

                    if (gameState[winningPosition[0]] == 0) {

                        winner = "Yellow";

                    }

                    //TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);

                    mWinnerMessage.setText(winner + " has won!");

                    //LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);

                    mlayout.setVisibility(View.VISIBLE);

                } else {

                    boolean gameIsOver = true;

                    for (int counterState : gameState) {

                        if (counterState == 2) gameIsOver = false;

                    }

                    if (gameIsOver) {

                        //TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);

                        mWinnerMessage.setText("It's a draw");

                        //LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);

                        mlayout.setVisibility(View.VISIBLE);

                    }
                }
            }
        }
    }

    @OnClick(R.id.imageView6)
    public void dropIn23(){
        ImageView counter = (ImageView) mSpot23;
        int tappedCounter = Integer.parseInt(counter.getTag().toString());

        if (gameState[tappedCounter] == 2 && gameIsActive) {

            gameState[tappedCounter] = activePlayer;

            counter.setTranslationY(-1000f);

            if (activePlayer == 0) {

                counter.setImageResource(R.drawable.yellow);

                activePlayer = 1;

            } else {

                counter.setImageResource(R.drawable.red);

                activePlayer = 0;

            }

            counter.animate().translationYBy(1000f).rotation(360).setDuration(300);

            for (int[] winningPosition : winningPositions) {

                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                        gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                        gameState[winningPosition[0]] != 2) {

                    // Someone has won!

                    gameIsActive = false;

                    String winner = "Red";

                    if (gameState[winningPosition[0]] == 0) {

                        winner = "Yellow";

                    }

                    //TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);

                    mWinnerMessage.setText(winner + " has won!");

                    //LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);

                    mlayout.setVisibility(View.VISIBLE);

                } else {

                    boolean gameIsOver = true;

                    for (int counterState : gameState) {

                        if (counterState == 2) gameIsOver = false;

                    }

                    if (gameIsOver) {

                        //TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);

                        mWinnerMessage.setText("It's a draw");

                        //LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);

                        mlayout.setVisibility(View.VISIBLE);

                    }
                }
            }
        }
    }

    @OnClick(R.id.imageView7)
    public void dropIn31(){
        ImageView counter = (ImageView) mSpot31;
        int tappedCounter = Integer.parseInt(counter.getTag().toString());

        if (gameState[tappedCounter] == 2 && gameIsActive) {

            gameState[tappedCounter] = activePlayer;

            counter.setTranslationY(-1000f);

            if (activePlayer == 0) {

                counter.setImageResource(R.drawable.yellow);

                activePlayer = 1;

            } else {

                counter.setImageResource(R.drawable.red);

                activePlayer = 0;

            }

            counter.animate().translationYBy(1000f).rotation(360).setDuration(300);

            for (int[] winningPosition : winningPositions) {

                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                        gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                        gameState[winningPosition[0]] != 2) {

                    // Someone has won!

                    gameIsActive = false;

                    String winner = "Red";

                    if (gameState[winningPosition[0]] == 0) {

                        winner = "Yellow";

                    }

                    //TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);

                    mWinnerMessage.setText(winner + " has won!");

                    //LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);

                    mlayout.setVisibility(View.VISIBLE);

                } else {

                    boolean gameIsOver = true;

                    for (int counterState : gameState) {

                        if (counterState == 2) gameIsOver = false;

                    }

                    if (gameIsOver) {

                        //TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);

                        mWinnerMessage.setText("It's a draw");

                        //LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);

                        mlayout.setVisibility(View.VISIBLE);

                    }
                }
            }
        }
    }

    @OnClick(R.id.imageView8)
    public void dropIn32(){
        ImageView counter = (ImageView) mSpot32;
        int tappedCounter = Integer.parseInt(counter.getTag().toString());

        if (gameState[tappedCounter] == 2 && gameIsActive) {

            gameState[tappedCounter] = activePlayer;

            counter.setTranslationY(-1000f);

            if (activePlayer == 0) {

                counter.setImageResource(R.drawable.yellow);

                activePlayer = 1;

            } else {

                counter.setImageResource(R.drawable.red);

                activePlayer = 0;

            }

            counter.animate().translationYBy(1000f).rotation(360).setDuration(300);

            for (int[] winningPosition : winningPositions) {

                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                        gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                        gameState[winningPosition[0]] != 2) {

                    // Someone has won!

                    gameIsActive = false;

                    String winner = "Red";

                    if (gameState[winningPosition[0]] == 0) {

                        winner = "Yellow";

                    }

                    //TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);

                    mWinnerMessage.setText(winner + " has won!");

                    //LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);

                    mlayout.setVisibility(View.VISIBLE);

                } else {

                    boolean gameIsOver = true;

                    for (int counterState : gameState) {

                        if (counterState == 2) gameIsOver = false;

                    }

                    if (gameIsOver) {

                        //TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);

                        mWinnerMessage.setText("It's a draw");

                        //LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);

                        mlayout.setVisibility(View.VISIBLE);

                    }
                }
            }
        }
    }

    @OnClick(R.id.imageView9)
    public void dropIn33(){
        ImageView counter = (ImageView) mSpot33;
        int tappedCounter = Integer.parseInt(counter.getTag().toString());

        if (gameState[tappedCounter] == 2 && gameIsActive) {

            gameState[tappedCounter] = activePlayer;

            counter.setTranslationY(-1000f);

            if (activePlayer == 0) {

                counter.setImageResource(R.drawable.yellow);

                activePlayer = 1;

            } else {

                counter.setImageResource(R.drawable.red);

                activePlayer = 0;

            }

            counter.animate().translationYBy(1000f).rotation(360).setDuration(300);

            for (int[] winningPosition : winningPositions) {

                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                        gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                        gameState[winningPosition[0]] != 2) {

                    // Someone has won!

                    gameIsActive = false;

                    String winner = "Red";

                    if (gameState[winningPosition[0]] == 0) {

                        winner = "Yellow";

                    }

                    //TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);

                    mWinnerMessage.setText(winner + " has won!");

                    //LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);

                    mlayout.setVisibility(View.VISIBLE);

                } else {

                    boolean gameIsOver = true;

                    for (int counterState : gameState) {

                        if (counterState == 2) gameIsOver = false;

                    }

                    if (gameIsOver) {

                        //TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);

                        mWinnerMessage.setText("It's a draw");

                        //LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);

                        mlayout.setVisibility(View.VISIBLE);

                    }
                }
            }
        }
    }

    @OnClick(R.id.exitConnect3)
    public void setmExitGame(){
        getActivity().finish();
        //getFragmentManager().popBackStackImmediate();
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.connect3_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
