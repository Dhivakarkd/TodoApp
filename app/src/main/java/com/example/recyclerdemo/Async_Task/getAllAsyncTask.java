package com.example.recyclerdemo.Async_Task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.recyclerdemo.MainActivity;
import com.example.recyclerdemo.TodoDao;
import com.example.recyclerdemo.TodoNotes;

import java.util.List;

public class getAllAsyncTask extends AsyncTask<Void,Void, List<TodoNotes>> {
    private TodoDao mTodoDao;
    private static final String TAG = "getAllAsyncTask";

    public getAllAsyncTask(TodoDao todoDao) {
        mTodoDao = todoDao;
    }

    @Override
    protected List<TodoNotes> doInBackground(Void... voids) {
        return mTodoDao.getallTodo();
    }

    @Override
    protected void onPostExecute(List<TodoNotes> todoNotesList) {
        super.onPostExecute(todoNotesList);
        Log.d(TAG, "onPostExecute: produced");

    }
}
