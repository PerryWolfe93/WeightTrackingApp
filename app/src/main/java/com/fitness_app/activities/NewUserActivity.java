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
import java.text.DateFormat;
import java.time.LocalDate;

public class NewUserActivity extends AppCompatActivity {

    // Variable Declarations
    private EditText username, password, passwordMatch, email, phoneNumber;
    private Button create;
    private SwitchCompat connectionType;
    DatabaseHelper fitnessAppDB;
    BackgroundAnimator backgroundAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        // Call class method for background animation
        backgroundAnimator = new BackgroundAnimator();
        backgroundAnimator.animateBackground(findViewById(R.id.newUser_layout));

        // Assign values to widget variables
        username = findViewById(R.id.et_newUser_username);
        password = findViewById(R.id.et_newUser_password);
        passwordMatch = findViewById(R.id.et_newUser_passwordMatch);
        email = findViewById(R.id.et_newUser_email);
        phoneNumber = findViewById(R.id.et_newUser_phoneNumber);
        create = findViewById(R.id.btn_newUser_create);
        connectionType = findViewById(R.id.swt_newUser_connection);
        fitnessAppDB = new DatabaseHelper(NewUserActivity.this);

        // Set click listeners for buttons
        create.setOnClickListener(v -> {
            String usernameEntry = username.getText().toString();
            String passwordEntry = password.getText().toString();
            String passwordMatchEntry = passwordMatch.getText().toString();
            String emailEntry = email.getText().toString();
            String phoneNumberEntry = phoneNumber.getText().toString();
            String date = LocalDate.now().toString();

            if(usernameEntry.equals("") || passwordEntry.equals("") || passwordMatchEntry.equals("") || emailEntry.equals("") || phoneNumberEntry.equals("")) {
                Toast.makeText(NewUserActivity.this, "Please Fill In Every Field", Toast.LENGTH_SHORT).show();
                return;
            } else if (!(passwordEntry.equals(passwordMatchEntry))) {
                Toast.makeText(NewUserActivity.this, "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create new remote user using MS SQL Server
            if(connectionType.isChecked()) {
                User.connection = ConSQL.connectionClass("sa", "sysadmin");
                try {
                    if(User.connection != null) {
                        String sqlInsert = "INSERT INTO User_Information_Table (Username, Password, PhoneNumber, Email, ActivityLevel, LastEvaluation) " +
                                "VALUES ('" + usernameEntry + "', '" + passwordEntry + "', " + phoneNumberEntry + ", '" + emailEntry + "', 1.375, '" + date + "');";
                        Statement statement = User.connection.createStatement();
                        int result = statement.executeUpdate(sqlInsert);

                        if(result == 1) {
                            String sqlInsertTwo = "Create Login " + usernameEntry + " with password = '" + passwordEntry + "' "
                                    + "Create User " + usernameEntry + " for login " + usernameEntry + " "
                                    + "EXEC sp_addrolemember 'db_datareader', '" + usernameEntry + "' "
                                    + "EXEC sp_addrolemember 'db_datawriter', '" + usernameEntry + "'";
                            Statement statementTwo = User.connection.createStatement();
                            statementTwo.execute(sqlInsertTwo);
                            openLoginActivity();
                            Toast.makeText(NewUserActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(this, "Connection Failed", Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception exception) {
                    Log.e("Error", exception.getMessage());
                }
                try {
                    User.connection.close();
                }
                catch (Exception exception) {
                    Log.e("Error", exception.getMessage());
                }
            }

            // Create new local user using SQLite
            else {
                if (fitnessAppDB.checkUsername(usernameEntry)) {
                    Toast.makeText(NewUserActivity.this, "User Already Exists", Toast.LENGTH_SHORT).show();
                } else {
                    User user = new User(usernameEntry, passwordEntry, phoneNumberEntry, emailEntry,
                            null, 0, 0, null, 0, 1.375,
                            LocalDate.now().toString(), 0, 0,
                            0, 0, 0);
                    boolean success = fitnessAppDB.addOneUser(user);
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
                connectionType.setText("Online User");
            } else {
                connectionType.setText("Offline User");
            }
        });
    }

    public void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}