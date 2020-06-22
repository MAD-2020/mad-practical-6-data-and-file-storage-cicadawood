package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.PriorityQueue;

public class LoginActivity extends AppCompatActivity {

    /*
        1. This is the main page for user to log in
        2. The user can enter - Username and Password
        3. The user login is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user does not exist. This loads the level selection page.
        4. There is an option to create a new user account. This loads the create user page.
     */
    private static final String FILENAME = "LoginActivity.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    private EditText usernameEditTxt;
    private EditText pwEditTxt;
    private Button loginBtn;
    private TextView signUpTxtView;
    private MyDBHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* Hint:
            This method creates the necessary login inputs and the new user creation ontouch.
            It also does the checks on button selected.
            Log.v(TAG, FILENAME + ": Create new user!");
            Log.v(TAG, FILENAME + ": Logging in with: " + etUsername.getText().toString() + ": " + etPassword.getText().toString());
            Log.v(TAG, FILENAME + ": Valid User! Logging in");
            Log.v(TAG, FILENAME + ": Invalid user!");

        */
        usernameEditTxt = findViewById(R.id.loginUsernameEditTxt);
        pwEditTxt = findViewById(R.id.loginPWEditTxt);
        loginBtn = findViewById(R.id.loginBtn);
        signUpTxtView = findViewById(R.id.signUpTxtView);


        loginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String username = usernameEditTxt.getText().toString() ;
                String password = pwEditTxt.getText().toString();
                if ((!username.isEmpty()) && (!password.isEmpty())){
                    boolean validUser = isValidUser(username, password);
                    if (validUser == true){
                        Log.v(TAG, FILENAME + ": Valid User! Logging in");
                        Log.v(TAG, FILENAME + ": Logging in with: " + username + ": " + password);
                        Intent intent = new Intent(LoginActivity.this, LevelSelectionActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }
                    else{
                        Log.v(TAG, FILENAME + ": Invalid user!");
                        Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "Empty Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUpTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, FILENAME + ": Create new user!");
                Intent intent = new Intent(LoginActivity.this,CreateNewUserActivity.class);
                startActivity(intent);
            }
        });







    }

    protected void onStop(){
        super.onStop();
        finish();
    }

    public boolean isValidUser(String userName, String password){

        /* HINT:
            This method is called to access the database and return a true if user is valid and false if not.
            Log.v(TAG, FILENAME + ": Running Checks..." + dbData.getMyUserName() + ": " + dbData.getMyPassword() +" <--> "+ userName + " " + password);
            You may choose to use this or modify to suit your design.
         */
        boolean validUser = false;
        handler = new MyDBHandler(this,null,null,1);
        UserData loginUser = handler.findUser(userName);

        if (loginUser!=null){
            Log.v(TAG, FILENAME + ": Running Checks..." + loginUser.getMyUserName() + ": " + loginUser.getMyPassword() +" <--> "+ userName + " " + password);
            if (loginUser.getMyPassword().equals(password)){
                validUser = true;
            }
        }
        return validUser;




    }

}
