package com.example.kevin_sct.beastchat.activities.Maze;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.kevin_sct.beastchat.R;

/** Integrating an existing Maze Game into the application,
 * with added sensor functionality using the accelerometer.
 */

public class MazeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final Context context = getApplicationContext();
        Button enterMaze = (Button) findViewById(R.id.button);
        enterMaze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] levels = {"Maze 1", "Maze 2", "Maze 3"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MazeActivity.this);
                builder.setTitle(getString(R.string.levelSelect));
                builder.setItems(levels, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Intent game = new Intent(MazeActivity.this, MazeGame.class);
                        Maze maze = MazeCreator.getMaze(item+1);
                        game.putExtra("maze", maze);
                        game.putExtra("mazeNumber", item+1);
                        startActivity(game);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        //mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
