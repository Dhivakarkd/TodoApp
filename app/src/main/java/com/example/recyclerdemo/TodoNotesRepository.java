package com.example.recyclerdemo;

import android.content.Context;
import com.example.recyclerdemo.Async_Task.insertAsyncTask;
import com.example.recyclerdemo.Async_Task.getAllAsyncTask;
import com.example.recyclerdemo.Async_Task.insertmultiAsyncTask;
import com.example.recyclerdemo.Async_Task.updataAsyncTask;
import com.example.recyclerdemo.Async_Task.updateRemainderAsyncTask;
import com.example.recyclerdemo.Async_Task.removeAsyncTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TodoNotesRepository {
    private TodoDatabase mTodoDatabase;

    public TodoNotesRepository(Context context) {
        mTodoDatabase =TodoDatabase.getInstance(context);
    }

    public void insertNotetask(TodoNotes todoNotes){
        new insertAsyncTask(mTodoDatabase.todoDao()).execute(todoNotes);
    }

    public List<TodoNotes> getAllNotetask() throws ExecutionException, InterruptedException {

        return new getAllAsyncTask(mTodoDatabase.todoDao()).execute().get();

    }

    public void insertMultiNotes(List<TodoNotes> notes) {
        new insertmultiAsyncTask(mTodoDatabase.todoDao()).execute(notes);

    }

    public void updateNotes(recyclerviewAdapter adapter,int position,String updatetext){
        new updataAsyncTask(mTodoDatabase.todoDao(),adapter,position).execute(updatetext);
    }

    public void updateNotes(recyclerviewAdapter adapter,int position,String updatetext,boolean iscompleted){
        new updataAsyncTask(mTodoDatabase.todoDao(),adapter,position,iscompleted).execute(updatetext);
    }
    public void updateNotes(recyclerviewAdapter adapter, int position, String updatetext, Date remainderTime){
        new updataAsyncTask(mTodoDatabase.todoDao(),adapter,position,remainderTime).execute(updatetext);
    }

    public void updateRemainder(Date remainder,int position){
        new updateRemainderAsyncTask(mTodoDatabase.todoDao(),position).execute(remainder);
    }

    public void removeNotes(String description){
        new removeAsyncTask(mTodoDatabase.todoDao()).execute(description);
    }

}
