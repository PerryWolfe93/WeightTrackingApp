package com.zybooks.perrywolfe_weighttrackingapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    // Variable declarations
    private EditText username, password;
    private Button login, forgotUsername, forgotPassword, newUser;
    DatabaseHelper weightTrackerDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assign values to widget variables
        username = findViewById(R.id.et_login_username);
        password = findViewById(R.id.et_login_password);
        login = findViewById(R.id.btn_login);
        forgotUsername = findViewById(R.id.btn_forgotUsername);
        forgotPassword = findViewById(R.id.btn_forgotPassword);
        newUser = findViewById(R.id.btn_newUser);
        weightTrackerDB = new DatabaseHelper(this);

        // Set click listeners for all buttons
        login.setOnClickListener(v -> {
            String usernameEntry = username.getText().toString();
            String passwordEntry = password.getText().toString();

            if(usernameEntry.equals("") || passwordEntry.equals("")) {
                Toast.makeText(MainActivity.this, "Please Fill In Every Field", Toast.LENGTH_SHORT).show();
            } else if(!(weightTrackerDB.checkUsername(usernameEntry))) {
                Toast.makeText(MainActivity.this, "Username Does Not Exist", Toast.LENGTH_SHORT).show();
            } else if(!(weightTrackerDB.checkPassword(usernameEntry, passwordEntry))) {
                Toast.makeText(MainActivity.this, "Password Incorrect", Toast.LENGTH_SHORT).show();
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

