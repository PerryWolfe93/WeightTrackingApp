package com.fitness_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fitness_app.BackgroundAnimator;
import com.fitness_app.DatabaseHelper;
import com.fitness_app.object_classes.User;
import com.fitness_app.R;

public class LoginActivity extends AppCompatActivity {

    // Variable declarations
    private EditText username, password;
    private Button login, forgotUsername, forgotPassword, newUser;
    DatabaseHelper fitnessAppDB;
    BackgroundAnimator backgroundAnimator;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Call class method for background animation
        backgroundAnimator = new BackgroundAnimator();
        backgroundAnimator.animateBackground(findViewById(R.id.login_layout));

        // Assign values to widget variables
        username = findViewById(R.id.et_login_username);
        password = findViewById(R.id.et_login_password);
        login = findViewById(R.id.btn_login);
        forgotUsername = findViewById(R.id.btn_forgotUsername);
        forgotPassword = findViewById(R.id.btn_forgotPassword);
        newUser = findViewById(R.id.btn_newUser);
// TODO: add online/offline mode switch
//  Switch onlineModeSwitch = findViewById(R.id.sw_login_onlineMode);
        fitnessAppDB = new DatabaseHelper(this);

        // Set click listeners for all buttons
        login.setOnClickListener(v -> {
            String usernameEntry = username.getText().toString();
            String passwordEntry = password.getText().toString();

            if(usernameEntry.equals("") || passwordEntry.equals("")) {
                Toast.makeText(LoginActivity.this, "Please Fill In Every Field", Toast.LENGTH_SHORT).show();
            } else if(!(fitnessAppDB.checkUsername(usernameEntry))) {
                Toast.makeText(LoginActivity.this, "Username Does Not Exist", Toast.LENGTH_SHORT).show();
            } else if(!(fitnessAppDB.checkPassword(usernameEntry, passwordEntry))) {
                Toast.makeText(LoginActivity.this, "Password Incorrect", Toast.LENGTH_SHORT).show();
            } else {
                User.currentUser = usernameEntry;
                openUserProfileActivity();
            }
        });

        forgotUsername.setOnClickListener(v -> {

        });

        forgotPassword.setOnClickListener(v -> {

        });

        newUser.setOnClickListener(v -> openNewUserActivity());

//        onlineModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if(isChecked) {
//                Connection connection = connectionClass();
//                try {
//                    if(connection != null) {
//
//                    }
//                } catch (Exception exception) {
//                    Log.e("Error",exception.getMessage());
//                }
//            } else {
//
//            }
//        });
    }

    // Activity change methods
    public void openNewUserActivity() {
        Intent intent = new Intent(this, NewUserActivity.class);
        startActivity(intent);
    }
    public void openUserProfileActivity() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

//    public Connection connectionClass() {
//        Connection con = null;
//        String ip = "10.0.0.224", port = "1433", username = "sa", password = "2fy9xjcdv2", databaseName = "fitnessAppDB";
//        StrictMode.ThreadPolicy tp = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(tp);
//        try {
//            Class.forName("net.sourceforge.jtds.jdbc.Driver");
//            String connectionUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";databasename=" + databaseName + ";User=" + username + ";password=" + password + ";";
//            con = DriverManager.getConnection(connectionUrl);
//        }
//        catch (Exception exception) {
//            Log.e("Error", exception.getMessage());
//        }
//        return con;
//    }
}

