package com.fitness_app.activities;

public class JournalEntry {

    private String date;
    private String journalEntry;

    public JournalEntry(String date, String journalEntry) {
        this.date = date;
        this.journalEntry = journalEntry;
    }

    public String getDate() {
        return date;
    }

    public String getJournalEntry() {
        return journalEntry;
    }

    public void setJournalEntry(String journalEntry) {
        this.journalEntry = journalEntry;
    }
}
