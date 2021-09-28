package com.zybooks.perrywolfe_weighttrackingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewUserActivity extends AppCompatActivity {

    // Variable Declarations
    private EditText username, password, passwordMatch, email, phoneNumber;
    private Button create;
    DatabaseHelper weightTrackerDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        // Assign values to widget variables
        username = findViewById(R.id.et_newUser_username);
        password = findViewById(R.id.et_newUser_password);
        passwordMatch = findViewById(R.id.et_newUser_passwordMatch);
        email = findViewById(R.id.et_newUser_email);
        phoneNumber = findViewById(R.id.et_newUser_phoneNumber);
        create = findViewById(R.id.btn_newUser_create);
        weightTrackerDB = new DatabaseHelper(NewUserActivity.this);

        // Set click listeners for buttons
        create.setOnClickListener(v -> {
            String usernameEntry = username.getText().toString();
            String passwordEntry = password.getText().toString();
            String passwordMatchEntry = passwordMatch.getText().toString();
            String emailEntry = email.getText().toString();
            String phoneNumberEntry = phoneNumber.getText().toString();

            // Check all fields before attempting to create user
            if(usernameEntry.equals("") || passwordEntry.equals("") || passwordMatchEntry.equals("") || emailEntry.equals("") || phoneNumberEntry.equals("")) {
                Toast.makeText(NewUserActivity.this, "Please Fill In Every Field", Toast.LENGTH_SHORT).show();
            } else if(!(passwordEntry.equals(passwordMatchEntry))) {
                Toast.makeText(NewUserActivity.this, "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
            } else if(weightTrackerDB.checkUsername(usernameEntry)) {
                Toast.makeText(NewUserActivity.this, "User Already Exists", Toast.LENGTH_SHORT).show();
            } else {
                User user = new User(usernameEntry, passwordEntry, phoneNumberEntry, emailEntry);
                boolean success = weightTrackerDB.addOneUser(user);
                openLoginActivity();
                if(success) {
                    Toast.makeText(NewUserActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewUserActivity.this, "Failed To Create User", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openLoginActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}