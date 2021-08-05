package com.example.recyclerdemo.Async_Task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.recyclerdemo.recyclerviewAdapter;
import com.example.recyclerdemo.TodoDao;
import com.example.recyclerdemo.TodoNotes;


import java.util.Date;
import java.util.List;

import static com.example.recyclerdemo.MainActivity.NO_REMAINDER;
import static com.example.recyclerdemo.MainActivity.No_Date;

public class updataAsyncTask extends AsyncTask<String,Void,Void> {
    private static final String TAG = "updataAsyncTask";
    private TodoDao mTodoDao;
    private recyclerviewAdapter mRecyclerviewAdapter;
    private int position;
    private boolean iscompleted ;
    private Date remainderTime;
    public updataAsyncTask(TodoDao todoDao,recyclerviewAdapter adapter,int position) {
        this.mTodoDao = todoDao;
        this.mRecyclerviewAdapter=adapter;
        this.position =position;
    }

    public updataAsyncTask(TodoDao todoDao, recyclerviewAdapter recyclerviewAdapter, int position, boolean iscompleted) {

        mTodoDao = todoDao;
        mRecyclerviewAdapter = recyclerviewAdapter;
        this.position = position;
        this.iscompleted = iscompleted;
        this.remainderTime=No_Date;
    }

    public updataAsyncTask(TodoDao todoDao, recyclerviewAdapter recyclerviewAdapter, int position, Date remainderTime) {
        mTodoDao = todoDao;
        mRecyclerviewAdapter = recyclerviewAdapter;
        this.position = position;
        this.remainderTime = remainderTime;
    }

    @Override
    protected Void doInBackground(String... strings) {
        TodoNotes notes=mTodoDao.findTodobyID(position+1);
        if(remainderTime.compareTo(No_Date) != 0){
            notes.setRemainderTime(remainderTime);
        }
        if(!strings[0].equalsIgnoreCase("nothing")){
        notes.setNotesDescription(strings[0]);}
        else{
            notes.setCompleted(iscompleted);
        }


        Log.d(TAG, "doInBackground: "+ position+notes.getNotesDescription());
        mTodoDao.updataTodo(notes);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        mRecyclerviewAdapter.notifyDataSetChanged();
        super.onPostExecute(aVoid);
        mRecyclerviewAdapter.notifyDataSetChanged();
    }
}


