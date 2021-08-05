package com.example.recyclerdemo.Async_Task;

import android.os.AsyncTask;

import com.example.recyclerdemo.TodoDao;
import com.example.recyclerdemo.TodoDatabase;
import com.example.recyclerdemo.TodoNotes;

public class insertAsyncTask extends AsyncTask<TodoNotes,Void,Void> {
    private TodoDao mTodoDao;

    public insertAsyncTask(TodoDao todoDao) {
        mTodoDao = todoDao;
    }

    @Override
    protected Void doInBackground(TodoNotes... todoNotes) {
        mTodoDao.insertTodo(todoNotes[0]);
        return null;
    }
}
