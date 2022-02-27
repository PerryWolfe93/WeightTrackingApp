package com.fitness_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fitness_app.BackgroundAnimator;
import com.fitness_app.ConSQL;
import com.fitness_app.DatabaseHelper;
import com.fitness_app.object_classes.User;
import com.fitness_app.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    // Variable declarations
    private EditText username, password;
    private Button login, forgotUsername, forgotPassword, newUser;
    DatabaseHelper fitnessAppDB;
    BackgroundAnimator backgroundAnimator;
    private SwitchCompat connectionSwitch;


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
        connectionSwitch = findViewById(R.id.swt_login_connection);
        fitnessAppDB = new DatabaseHelper(this);

        // Set click listeners for all buttons
        login.setOnClickListener(v -> {
            String usernameEntry = username.getText().toString();
            String passwordEntry = password.getText().toString();

            if(usernameEntry.equals("") || passwordEntry.equals("")) {
                Toast.makeText(LoginActivity.this, "Please Fill In Every Field", Toast.LENGTH_LONG).show();
                return;
            }

            // Login for remote user
            if(connectionSwitch.isChecked()) {
                User.connection = ConSQL.connectionClass(usernameEntry, passwordEntry);
                try {
                    if(User.connection != null) {
                        User.currentUser = usernameEntry;
                        User.online = true;
                        openUserProfileActivity();
                    }
                    else {
                        Toast.makeText(this, "Incorrect Username/Password Combination", Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception exception) {
                    Log.e("Error", exception.getMessage());
                }
            }

            // Login for local user
            else {
                if (!(fitnessAppDB.checkUsername(usernameEntry))) {
                    Toast.makeText(LoginActivity.this, "Username Does Not Exist", Toast.LENGTH_LONG).show();
                } else if (!(fitnessAppDB.checkPassword(usernameEntry, passwordEntry))) {
                    Toast.makeText(LoginActivity.this, "Password Incorrect", Toast.LENGTH_LONG).show();
                } else {
                    User.currentUser = usernameEntry;
                    User.online = false;
                    openUserProfileActivity();
                }
            }
        });

        connectionSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                connectionSwitch.setText("Online");
            } else {
                connectionSwitch.setText("Offline");
            }
        });

        forgotUsername.setOnClickListener(v -> {

        });

        forgotPassword.setOnClickListener(v -> {

        });

        newUser.setOnClickListener(v -> openNewUserActivity());
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
}

