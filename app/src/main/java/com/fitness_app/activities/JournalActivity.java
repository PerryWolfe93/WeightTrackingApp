package com.fitness_app.activities;


import com.fitness_app.DatabaseHelper;
import com.fitness_app.R;
import com.fitness_app.object_classes.Journal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.time.LocalDate;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class JournalActivity extends AppCompatActivity {

    private EditText journalEntry;
    DatabaseHelper fitnessAppDB;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        // Initialize instance of DatabaseHelper
        fitnessAppDB = new DatabaseHelper(JournalActivity.this);

        // Set TextView widget beneath title to the current date
        TextView date = findViewById(R.id.tv_journal_date);
        date.setText(LocalDate.now().toString());

        // Set TextView for today's completed journal entries
        TextView todaysEntries = findViewById(R.id.tv_journal_todays_entry);
        Journal todaysJournal = fitnessAppDB.getTodaysJournalEntry();
        todaysEntries.setText(todaysJournal.getJournalEntry());

        // Initialize other widgets
        journalEntry = findViewById(R.id.et_journal_entry);
        Button submit = findViewById(R.id.btn_journal_submit);

        submit.setOnClickListener(view -> {
            String journalText = journalEntry.getText().toString();
            Journal journal = new Journal(LocalDate.now().toString(), journalText);
            fitnessAppDB.addJournal(journal);
        });
    }
}
