package com.fitness_app.object_classes;

public class Journal {

    private final String date;
    private String journalEntry;
    private String note;

    public Journal(String date, String journalEntry) {
        this.date = date;
        this.journalEntry = journalEntry;
    }

    public String getDate() {
        return date;
    }

    public String getJournalEntry() {
        return journalEntry;
    }

    public String getNote() { return note; }

    public void setJournalEntry(String journalEntry) {
        this.journalEntry = journalEntry;
    }
}
