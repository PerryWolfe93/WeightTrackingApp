package com.fitness_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fitness_app.BackgroundAnimator;
import com.fitness_app.DatabaseHelper;
import com.fitness_app.object_classes.User;
import com.fitness_app.R;

import java.time.LocalDate;

// This class is used for creating a new user.
// The user can be created either locally using the SQLite database or remotely using a MySQL database

public class NewUserActivity extends AppCompatActivity {

    EditText username, password, passwordMatch, email, phoneNumber;
    SwitchCompat connectionType;
    DatabaseHelper fitnessAppDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        // Call class method for background animation
        BackgroundAnimator backgroundAnimator = new BackgroundAnimator();
        backgroundAnimator.animateBackground(findViewById(R.id.newUser_layout));

        // Connects each widget variable with its corresponding layout resource
        username = findViewById(R.id.et_newUser_username);
        password = findViewById(R.id.et_newUser_password);
        passwordMatch = findViewById(R.id.et_newUser_passwordMatch);
        email = findViewById(R.id.et_newUser_email);
        phoneNumber = findViewById(R.id.et_newUser_phoneNumber);
        Button create = findViewById(R.id.btn_newUser_create);
        connectionType = findViewById(R.id.swt_newUser_connection);
        fitnessAppDB = new DatabaseHelper(NewUserActivity.this);

        // Click listener used for creating a new user

        create.setOnClickListener(v -> {
            String usernameEntry = username.getText().toString();
            String passwordEntry = password.getText().toString();
            String passwordMatchEntry = passwordMatch.getText().toString();
            String emailEntry = email.getText().toString();
            String phoneNumberEntry = phoneNumber.getText().toString();

            // If any field is left blank or if password entries do not match, exits the method and displays toast message
            if(usernameEntry.equals("") || passwordEntry.equals("") || passwordMatchEntry.equals("") || emailEntry.equals("") || phoneNumberEntry.equals("")) {
                Toast.makeText(NewUserActivity.this, "Please Fill In Every Field", Toast.LENGTH_SHORT).show();
                return;
            } else if (!(passwordEntry.equals(passwordMatchEntry))) {
                Toast.makeText(NewUserActivity.this, "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create new remote user using MS SQL Server
            if(connectionType.isChecked()) {

            }

            // Create new local user using SQLite
            else {
                if (fitnessAppDB.checkUsername(usernameEntry)) {
                    Toast.makeText(NewUserActivity.this, "Username Taken", Toast.LENGTH_SHORT).show();
                } else {
                    User user = new User(usernameEntry, passwordEntry, phoneNumberEntry, emailEntry,
                            null, 0, 0, null, 0, 1.375,
                            LocalDate.now().toString(), 0, 0,
                            0, 0, 0);
                    boolean success = fitnessAppDB.addUser(user);
                    openLoginActivity();
                    if (success) {
                        Toast.makeText(NewUserActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(NewUserActivity.this, "Failed To Create User", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        connectionType.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                connectionType.setText(R.string.swt_newUser_online);
            } else {
                connectionType.setText(R.string.swt_newUser_offline);
            }
        });
    }

    public void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}