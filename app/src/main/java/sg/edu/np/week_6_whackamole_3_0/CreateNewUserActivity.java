package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateNewUserActivity extends AppCompatActivity {
    /* Hint:
        1. This is the create new user page for user to log in
        2. The user can enter - Username and Password
        3. The user create is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user already exists.
        4. For the purpose the practical, successful creation of new account will send the user
           back to the login page and display the "User account created successfully".
           the page remains if the user already exists and "User already exist" toastbox message will appear.
        5. There is an option to cancel. This loads the login user page.
     */


    private static final String FILENAME = "CreateNewUserActivity.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    private EditText createUsernameEditTxt;
    private EditText createPwEditTxt;
    private Button cancelBtn;
    private Button createBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_user);

        /* Hint:
            This prepares the create and cancel account buttons and interacts with the database to determine
            if the new user created exists already or is new.
            If it exists, information is displayed to notify the user.
            If it does not exist, the user is created in the DB with default data "0" for all levels
            and the login page is loaded.

            Log.v(TAG, FILENAME + ": New user created successfully!");
            Log.v(TAG, FILENAME + ": User already exist during new user creation!");

         */
        createUsernameEditTxt = findViewById(R.id.createUsernameEditTxt);
        createPwEditTxt = findViewById(R.id.createPWEditTxt);
        cancelBtn = findViewById(R.id.cancelBtn);
        createBtn = findViewById(R.id.createBtn);

        cancelBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNewUserActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String username = createUsernameEditTxt.getText().toString();
                String password = createPwEditTxt.getText().toString();

                if ((!username.isEmpty())&& (!password.isEmpty())){
                    MyDBHandler handler = new MyDBHandler (CreateNewUserActivity.this, null, null, 1);
                    UserData userData = handler.findUser(username);
                    if (userData==null){
                        ArrayList<Integer> levels = new ArrayList<Integer>();
                        ArrayList<Integer> scores = new ArrayList<Integer>();

                        for (Integer i=1; i<11;i++){
                            levels.add(i);
                            scores.add(0);
                        }
                        UserData newUser = new UserData(username,password,levels,scores);
                        handler.addUser(newUser);
                        Toast.makeText(CreateNewUserActivity.this, "User Created Successfully.", Toast.LENGTH_SHORT).show();
                        Log.v(TAG, FILENAME + ": New user created successfully!");
                        Intent intent = new Intent(CreateNewUserActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(CreateNewUserActivity.this, "User Already Exists.\nPlease Try Again.", Toast.LENGTH_SHORT).show();
                        Log.v(TAG, FILENAME + ": User already exist during new user creation!");
                    }
                }
                else{
                    Toast.makeText(CreateNewUserActivity.this, "Empty Username or Password!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void onStop() {
        super.onStop();
        finish();
    }
}
