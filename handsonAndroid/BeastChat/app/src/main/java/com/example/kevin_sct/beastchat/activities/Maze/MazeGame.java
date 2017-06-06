package com.example.kevin_sct.beastchat.activities.Maze;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.kevin_sct.beastchat.R;

public class MazeGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze_game);

//        ActionBar ab = getSupportActionBar();
//
//        // Enable the Up button
//        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Maze maze = (Maze) extras.get("maze");
        int mazeNumber = (int) extras.get("mazeNumber");
        MazeView view = new MazeView(this, mazeNumber);
        view.setMaze(maze);
        setContentView(view);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(MazeGame.this, MazeActivity.class);
        startActivity(intent);
    }
}
