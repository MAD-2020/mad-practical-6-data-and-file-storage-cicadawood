package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    /* Hint:
        1. This creates the Whack-A-Mole layout and starts a countdown to ready the user
        2. The game difficulty is based on the selected level
        3. The levels are with the following difficulties.
            a. Level 1 will have a new mole at each 10000ms.
                - i.e. level 1 - 10000ms
                       level 2 - 9000ms
                       level 3 - 8000ms
                       ...
                       level 10 - 1000ms
            b. Each level up will shorten the time to next mole by 100ms with level 10 as 1000 second per mole.
            c. For level 1 ~ 5, there is only 1 mole.
            d. For level 6 ~ 10, there are 2 moles.
            e. Each location of the mole is randomised.
        4. There is an option return to the login page.
     */
    private static final String FILENAME = "Main4Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    CountDownTimer readyTimer;
    CountDownTimer newMolePlaceTimer;
    private MyDBHandler handler;
    private Button backToSelectionBtn;
    private static final int[] btnIDArray = {R.id.button0,R.id.button1,R.id.button2,R.id.button3,R.id.button4,
            R.id.button5,R.id.button6,R.id.button7,R.id.button8};

    private Button[] buttonList = new Button[btnIDArray.length];
    private int gameScore = 0;
    private int currentLevel;
    private TextView scoreTxtView;
    private int highestScore;
    private String username;

    private void readyTimer(){
        /*  HINT:
            The "Get Ready" Timer.
            Log.v(TAG, "Ready CountDown!" + millisUntilFinished/ 1000);
            Toast message -"Get Ready In X seconds"
            Log.v(TAG, "Ready CountDown Complete!");
            Toast message - "GO!"
            belongs here.
            This timer countdown from 10 seconds to 0 seconds and stops after "GO!" is shown.
         */
        readyTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Toast.makeText(GameActivity.this, "Get Ready in " + millisUntilFinished / 1000 + "seconds", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Ready CountDown!" + millisUntilFinished / 1000);

            }
            @Override
            public void onFinish() {
                Toast.makeText(GameActivity.this, "GO!", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Ready Countdown Complete");
                readyTimer.cancel();
                placeMoleTimer();
            }
        };
        readyTimer.start();
    }
    private void placeMoleTimer(){
        /* HINT:
           Creates new mole location each second.
           Log.v(TAG, "New Mole Location!");
           setNewMole();
           belongs here.
           This is an infinite countdown timer.
         */
        int countDownInterval = (currentLevel * 1000) - ((currentLevel-1)*1100) ;
        newMolePlaceTimer = new CountDownTimer(Long.MAX_VALUE,countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                setNewMole(currentLevel);
                Log.v(TAG, "New Mole Location!");
            }
            @Override
            public void onFinish() {
                newMolePlaceTimer.cancel();
            }
        };
        newMolePlaceTimer.start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        /*Hint:
            This starts the countdown timers one at a time and prepares the user.
            This also prepares level difficulty.
            It also prepares the button listeners to each button.
            You may wish to use the for loop to populate all 9 buttons with listeners.
            It also prepares the back button and updates the user score to the database
            if the back button is selected.
         */


        //for(final int id : btnIDArray){
            /*  HINT:
            This creates a for loop to populate all 9 buttons with listeners.
            You may use if you wish to remove or change to suit your codes.
            */
        //}
        scoreTxtView = findViewById(R.id.scoreTxtView);
        Intent receiveIntent = getIntent();
        username = receiveIntent.getStringExtra("username");
        currentLevel = receiveIntent.getIntExtra("level", 0);
        highestScore = receiveIntent.getIntExtra("score", 0);

        for(int i=0; i<btnIDArray.length;i++){
            final int index = i;
            buttonList[i]=(Button)findViewById(btnIDArray[i]);
            buttonList[i].setText("O");
            buttonList[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doCheck(buttonList[index]);
                }
            });
        }
        readyTimer();
        placeMoleTimer();
        backToSelectionBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if (gameScore > highestScore)
                {
                    updateUserScore();
                }
                Intent intent2 = new Intent(GameActivity.this, LevelSelectionActivity.class);
                intent2.putExtra("username", username);
                startActivity(intent2);
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        readyTimer();
    }
    private void doCheck(Button checkButton)
    {
        /* Hint:
            Checks for hit or miss
            Log.v(TAG, FILENAME + ": Hit, score added!");
            Log.v(TAG, FILENAME + ": Missed, point deducted!");
            belongs here.
        */

        if (checkButton.getText()=="*"){
            gameScore+=1;
            Log.v(TAG, "Hit, score added!");
        }
        else{
            gameScore-=1;
            Log.v(TAG, "Missed, point deducted!");
        }
        scoreTxtView.setText(String.valueOf(gameScore));

    }

    public void setNewMole(int level)
    {
        /* Hint:
            Clears the previous mole location and gets a new random location of the next mole location.
            Sets the new location of the mole. Adds additional mole if the level difficulty is from 6 to 10.
         */
        Random ran = new Random();
        int randomLocation = ran.nextInt(9);
        for (Button i :buttonList){
            i.setText("O");
        }
        buttonList[randomLocation].setText("*");

        if (level > 5 ){
            int randomLocation2 = ran.nextInt(9);
            while (randomLocation2 == randomLocation){
                randomLocation2 = ran.nextInt(9);
            }
            buttonList[randomLocation2].setText("*");
        }
    }

    private void updateUserScore()
    {
     /* Hint:
        This updates the user score to the database if needed. Also stops the timers.
        Log.v(TAG, FILENAME + ": Update User Score...");
      */

        newMolePlaceTimer.cancel();
        readyTimer.cancel();
        Log.v(TAG, FILENAME + ": Update User Score...");
        MyDBHandler handler = new MyDBHandler(this,null,null,1);
        UserData currUserData = handler.findUser(username);
        handler.deleteAccount(username);
        currUserData.getScores().set(currentLevel-1,gameScore);
        handler.addUser(currUserData);

    }

}
