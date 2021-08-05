package com.example.recyclerdemo.Async_Task;

import android.os.AsyncTask;

import com.example.recyclerdemo.TodoDao;
import com.example.recyclerdemo.TodoNotes;

public class removeAsyncTask extends AsyncTask<String,Void,Void> {
    private TodoDao mTodoDao;

    public removeAsyncTask(TodoDao todoDao) {
        mTodoDao = todoDao;
    }

    @Override
    protected Void doInBackground(String... strings) {
        TodoNotes notes=mTodoDao.findTodobyID(strings[0]);
        mTodoDao.removeTodo(notes);
        return null;
    }
}
