package com.example.recyclerdemo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "TodoNotes_table")
public class TodoNotes {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Todo_primaryID")
    private int primaryID;

    @ColumnInfo(name ="Todo_descrption")
    private String notesDescription;

    @ColumnInfo(name = "Todo_isCompleted")
    private boolean isCompleted;

    @ColumnInfo(name = "Todo_RemainderTime")
    private Date remainderTime;



    public TodoNotes(String notesDescription, boolean isCompleted,Date remainderTime) {
        this.notesDescription = notesDescription;
        this.isCompleted = isCompleted;
        this.remainderTime=remainderTime;
    }

    public Date getRemainderTime() {
        return remainderTime;
    }

    public void setRemainderTime(Date remainderTime) {
        this.remainderTime = remainderTime;
    }

    public int getPrimaryID() {
        return primaryID;
    }

    public void setPrimaryID(int primaryID) {
        this.primaryID = primaryID;
    }

    public String getNotesDescription() {
        return notesDescription;
    }

    public void setNotesDescription(String notesDescription) {
        this.notesDescription = notesDescription;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @NonNull
    @Override
    public String toString() {
        return "\nTodo{ "+
                " PrimaryId = "+primaryID +
                " Description = "+ notesDescription +
                " CompletedStatus = "+ isCompleted +
                " RemainderTime = " + remainderTime+ " }";
    }
}
