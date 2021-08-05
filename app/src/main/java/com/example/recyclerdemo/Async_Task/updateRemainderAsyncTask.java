package com.example.recyclerdemo.Async_Task;

import android.os.AsyncTask;

import com.example.recyclerdemo.TodoDao;
import com.example.recyclerdemo.TodoNotes;

import java.util.Date;

public class updateRemainderAsyncTask extends AsyncTask<Date,Void,Void> {
    private TodoDao mTodoDao;
    private int position;

    public updateRemainderAsyncTask(TodoDao todoDao,int position) {
        this.mTodoDao = todoDao;
        this.position=position;
    }

    @Override
    protected Void doInBackground(Date... dates) {
        TodoNotes notes=mTodoDao.findTodobyID(position+1);
        notes.setRemainderTime(dates[0]);
        mTodoDao.updataTodo(notes);
        return null;
    }
}
