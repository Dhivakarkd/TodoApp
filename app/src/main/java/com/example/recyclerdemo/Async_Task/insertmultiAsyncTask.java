package com.example.recyclerdemo.Async_Task;

import android.os.AsyncTask;

import com.example.recyclerdemo.TodoDao;
import com.example.recyclerdemo.TodoNotes;

import java.util.List;

public class insertmultiAsyncTask extends AsyncTask<List<TodoNotes>,Void,Void> {
    private TodoDao mTodoDao;

    public insertmultiAsyncTask(TodoDao todoDao) {
        mTodoDao = todoDao;
    }

    @Override
    protected Void doInBackground(List<TodoNotes>... lists) {
        mTodoDao.insertMultiTodo(lists[0]);
        return null;
    }
}
