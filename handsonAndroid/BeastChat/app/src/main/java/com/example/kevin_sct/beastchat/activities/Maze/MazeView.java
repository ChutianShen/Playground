package com.example.kevin_sct.beastchat.activities.Maze;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.example.kevin_sct.beastchat.R;

/**
 * Created by nicolemoghaddas on 6/5/17.
 */

public class MazeView extends View implements SensorEventListener {

    private int mazeNumber;
    //width and height of the whole maze and width of lines which
    //make the walls
    private int width, height, lineWidth;
    //size of the maze i.e. number of cells in it
    private int mazeSizeX, mazeSizeY;
    //width and height of cells in the maze
    float cellWidth, cellHeight;
    //the following store result of cellWidth+lineWidth
    //and cellHeight+lineWidth respectively
    float totalCellWidth, totalCellHeight;
    //the finishing point of the maze
    private int mazeFinishX, mazeFinishY;
    private Maze maze;
    private Context context;
    private Paint line, ball, background;
    boolean[][] hLines;
    boolean[][] vLines;
    boolean dragging = false;
    SensorManager sensorManager;
    Sensor mAccelerometer;
    private float mSensorX;
    private float mSensorY;
    private int lastDirection;
    private static float ACCEL_THRESHOLD = 1;


    public MazeView(Context context, int mazeNo) {
        super(context);
        this.context = (Activity)context;
        this.mazeNumber = mazeNo;

        int bgColor = getResources().getColor(R.color.black);
        int lineColor = getResources().getColor(R.color.white);
        int ballColor = getResources().getColor(R.color.position);
        switch (mazeNumber) {
            case 1: ballColor = getResources().getColor(R.color.blue); break;
            case 2: ballColor = getResources().getColor(R.color.green); break;
            case 3: ballColor = getResources().getColor(R.color.red); break;
        }

        line = new Paint();
        line.setColor(lineColor);
        ball = new Paint();
        ball.setColor(ballColor);
        background = new Paint();
        background.setColor(bgColor);
        setFocusable(true);
        this.setFocusableInTouchMode(true);

        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
        mazeFinishX = maze.getFinalX();
        mazeFinishY = maze.getFinalY();
        mazeSizeX = maze.getMazeWidth();
        mazeSizeY = maze.getMazeHeight();
        hLines = maze.getHorizontalLines();
        vLines = maze.getVerticalLines();
    }
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = (w < h)?w:h;
        height = width;         //for now square mazes
        lineWidth = 1;          //for now 1 pixel wide walls
        cellWidth = (width - ((float)mazeSizeX*lineWidth)) / mazeSizeX;
        totalCellWidth = cellWidth+lineWidth;
        cellHeight = (height - ((float)mazeSizeY*lineWidth)) / mazeSizeY;
        totalCellHeight = cellHeight+lineWidth;
        ball.setTextSize(cellHeight*0.75f);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    protected void onDraw(Canvas canvas) {
        //fill in the background
        canvas.drawRect(0, 0, width, height, background);

        //iterate over the boolean arrays to draw walls
        for(int i = 0; i < mazeSizeX; i++) {
            for(int j = 0; j < mazeSizeY; j++){
                float x = j * totalCellWidth;
                float y = i * totalCellHeight;
                if(j < mazeSizeX - 1 && vLines[i][j]) {
                    //we'll draw a vertical line
                    canvas.drawLine(x + cellWidth,   //start X
                            y,               //start Y
                            x + cellWidth,   //stop X
                            y + cellHeight,  //stop Y
                            line);
                }
                if(i < mazeSizeY - 1 && hLines[i][j]) {
                    //we'll draw a horizontal line
                    canvas.drawLine(x,               //startX
                            y + cellHeight,  //startY
                            x + cellWidth,   //stopX
                            y + cellHeight,  //stopY
                            line);
                }
            }
        }
        int currentX = maze.getCurrentX(),currentY = maze.getCurrentY();
        //draw the ball
        canvas.drawCircle((currentX * totalCellWidth)+(cellWidth/2),   //x of center
                (currentY * totalCellHeight)+(cellWidth/2),  //y of center
                (cellWidth*0.45f),                           //radius
                ball);
        //draw the finishing point indicator
        canvas.drawText("F",
                (mazeFinishX * totalCellWidth)+(cellWidth*0.25f),
                (mazeFinishY * totalCellHeight)+(cellHeight*0.75f),
                ball);
    }

    void showFinishDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getText(R.string.finished_title));
        final LayoutInflater inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.finish, null);
        builder.setView(view);
        final AlertDialog finishDialog = builder.create();
        View closeButton = view.findViewById(R.id.closeGame);
        closeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View clicked) {
                if(clicked.getId() == R.id.closeGame) {
                    finishDialog.dismiss();
                    ((Activity)context).finish();
                }
            }
        });
        View nextLevelButton = view.findViewById(R.id.nextLevel);
        if (mazeNumber == MazeCreator.NUM_MAZES) {
            nextLevelButton.setVisibility(INVISIBLE);
        } else {
            nextLevelButton = view.findViewById(R.id.nextLevel);
            nextLevelButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.nextLevel) {
                        finishDialog.dismiss();
                        //((Activity)context).finish(); // need to exit the last activity first
                        Intent intent = new Intent(getContext(), MazeGame.class);
                        Maze maze = MazeCreator.getMaze(mazeNumber+1);
                        intent.putExtra("maze", maze);
                        intent.putExtra("mazeNumber", mazeNumber+1);
                        ((Activity)getContext()).startActivity(intent);
                    }
                }
            });
        }

        finishDialog.show();
        sensorManager.unregisterListener(this);
    }

    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        int currentX = maze.getCurrentX();
        int currentY = maze.getCurrentY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                //touch gesture started
                if(Math.floor(touchX/totalCellWidth) == currentX &&
                        Math.floor(touchY/totalCellHeight) == currentY) {
                    //touch gesture in the cell where the ball is
                    dragging = true;
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                //touch gesture completed
                dragging = false;
                return true;
            case MotionEvent.ACTION_MOVE:
                if(dragging) {
                    int cellX = (int)Math.floor(touchX/totalCellWidth);
                    int cellY = (int)Math.floor(touchY/totalCellHeight);

                    if((cellX != currentX && cellY == currentY) ||
                            (cellY != currentY && cellX == currentX)) {
                        //either X or Y changed
                        boolean moved = false;
                        //check horizontal ball movement
                        switch(cellX-currentX) {
                            case 1:
                                moved = maze.move(Maze.RIGHT);
                                break;
                            case -1:
                                moved = maze.move(Maze.LEFT);
                        }
                        //check vertical ball movement
                        switch(cellY-currentY) {
                            case 1:
                                moved = maze.move(Maze.DOWN);
                                break;
                            case -1:
                                moved = maze.move(Maze.UP);
                        }
                        if(moved) {
                            //the ball was moved so we'll redraw the view
                            invalidate();
                            if(maze.isGameComplete()) {
                                //game is finished
                                showFinishDialog();
                            }
                        }
                    }
                    return true;
                }
        }

        return false;
    }

    private int getDirection(float sensorX, float sensorY) {
        int direction = 0;
        if (Math.abs(mSensorX) > Math.abs(mSensorY)) {
            if (mSensorX < -ACCEL_THRESHOLD) {
                direction = Maze.RIGHT;
            }
            if (mSensorX > ACCEL_THRESHOLD) {
                direction = Maze.LEFT;
            }
        } else {
            if (mSensorY < -ACCEL_THRESHOLD) {
                direction = Maze.UP;
            }
            if (mSensorY > ACCEL_THRESHOLD) {
                direction = Maze.DOWN;
            }
        }
        return direction;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mSensorX = sensorEvent.values[0];
            mSensorY = sensorEvent.values[1];

//            Log.d("mSensorX: ", Float.toString(mSensorX));
//            Log.d("mSensorY: ", Float.toString(mSensorY));

            boolean moved = false;

            // if moving in same direction, need to tilt more
            //if (lastDirection)

            moved = maze.move(getDirection(mSensorX, mSensorY));

            if(moved) {
                //the ball was moved so we'll redraw the view
                invalidate();
                if(maze.isGameComplete()) {
                    showFinishDialog();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
