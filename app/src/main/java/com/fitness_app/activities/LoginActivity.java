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
import com.google.gson.Gson;

import RestAPI.APIClient;
import RestAPI.APIService;
import RestAPI.TokenResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    SwitchCompat connectionSwitch;
    DatabaseHelper fitnessAppDB;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Call class method for background animation
        BackgroundAnimator backgroundAnimator = new BackgroundAnimator();
        backgroundAnimator.animateBackground(findViewById(R.id.login_layout));

        // Assign values to widget variables
        username = findViewById(R.id.et_login_username);
        password = findViewById(R.id.et_login_password);
        Button login = findViewById(R.id.btn_login);
        Button forgotUsername = findViewById(R.id.btn_forgotUsername);
        Button forgotPassword = findViewById(R.id.btn_forgotPassword);
        Button newUser = findViewById(R.id.btn_newUser);
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



            }

            // Login for local user
            else {
                if (!(fitnessAppDB.checkUsername(usernameEntry))) {
                    Toast.makeText(LoginActivity.this, "Username Does Not Exist", Toast.LENGTH_LONG).show();
                } else if (!(fitnessAppDB.checkPassword(usernameEntry, passwordEntry))) {
                    Toast.makeText(LoginActivity.this, "Password Incorrect", Toast.LENGTH_LONG).show();
                } else {
                    User.currentUser = usernameEntry;
                    User.userID = fitnessAppDB.getUserID(usernameEntry);
                    User.online = false;
                    openUserProfileActivity();
                }
            }
        });

        connectionSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                connectionSwitch.setText(R.string.swt_login_online);
            } else {
                connectionSwitch.setText(R.string.swt_login_offline);
            }
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

