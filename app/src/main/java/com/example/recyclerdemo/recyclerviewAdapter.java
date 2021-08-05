package com.example.recyclerdemo;

import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.recyclerdemo.MainActivity.NIL;
import static com.example.recyclerdemo.MainActivity.No_Date;

public class recyclerviewAdapter extends RecyclerView.Adapter<recyclerviewAdapter.recyclerviewholder> {
    private static final String TAG = "recyclerviewAdapter";
    private List<String> Description ;
    private List<TodoNotes> tempTodo;
    private LiveData<List<TodoNotes>> todoNotesList;
    private onclickinterface onclickitemlistener;
    public int pos;
    public recyclerviewholder hold;
    private TodoNotesRepository TodoNotesRepository;
    Calendar mCalendar;

    SimpleDateFormat myDayformat=new SimpleDateFormat("HH:mm",java.util.Locale.getDefault());
    SimpleDateFormat myDateformat=new SimpleDateFormat("d MMM",java.util.Locale.getDefault());

    public recyclerviewAdapter(List<String> description, LiveData<List<TodoNotes>> todoNotesList,List<TodoNotes> notes,TodoNotesRepository todoNotesRepository ,onclickinterface onclickitemlistener) {
       this.Description = description;
        this.onclickitemlistener=onclickitemlistener;
        this.todoNotesList =todoNotesList;
        this.tempTodo =notes;
        this.TodoNotesRepository=todoNotesRepository;
        mCalendar=Calendar.getInstance();
        Log.d(TAG, "recyclerviewAdapter: "+ notes.toString());


    }

    @NonNull
    @Override
    public recyclerviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view =layoutInflater.inflate(R.layout.list_layout,parent,false);
        return new recyclerviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final recyclerviewholder holder, final int position) {
        pos=position;
        hold=holder;
        TodoNotes notes;
        Date remainder;


        try {
            notes = todoNotesList.getValue().get(position);
        }catch (Exception e){
            e.printStackTrace();
            notes =tempTodo.get(position);
        }
        remainder=notes.getRemainderTime();
        String date=setDate(remainder);
        holder.textView_Date.setText(date);
        if(date.equalsIgnoreCase(NIL)){
            holder.textView_Time.setText("");
        }else {
            holder.textView_Time.setText(myDayformat.format(remainder));
        }
        holder.checkbox_text.setText(notes.getNotesDescription());
        if(notes.isCompleted()){
            holder.mCheckBox.setChecked(true);
            holder.checkbox_text.setPaintFlags(holder.mCheckBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.mCheckBox.setChecked(false);
            holder.checkbox_text.setPaintFlags(holder.mCheckBox.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
       // holder.checkbox_text.setText(Description.get(position));
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked =holder.mCheckBox.isChecked();
                if(checked){
                   holder.checkbox_text.setPaintFlags(holder.mCheckBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    TodoNotesRepository.updateNotes(recyclerviewAdapter.this,position,"nothing",true);
                }else
                {
                    holder.checkbox_text.setPaintFlags(holder.mCheckBox.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                    TodoNotesRepository.updateNotes(recyclerviewAdapter.this,position,"nothing",false);
                }
            }
        });



    }

    private String setDate(Date remainder) {
        Log.d(TAG, "checkDateFormat: "+mCalendar.get(Calendar.DATE) + " ==== "+ remainder.getDay());
        if (remainder.compareTo(No_Date)==0){
            return NIL;
        }
        if(remainder.getDate() == mCalendar.get(Calendar.DATE)){
            return "Today ";
        }
        if(remainder.getDate() == mCalendar.get(Calendar.DATE)+1){
            return "Tomorrow ";
        }
        return myDateformat.format(remainder);
    }

    public int getPos() {
        return pos;
    }

    public recyclerviewholder getHold() {
        return hold;
    }

    @Override
    public int getItemCount() {

        try {
           return todoNotesList.getValue().size();
        }catch (Exception e) {
            return tempTodo.size();
        }
    }

    class recyclerviewholder extends RecyclerView.ViewHolder{
            CheckBox mCheckBox;
            TextView checkbox_text,textView_Date,textView_Time;

            
        public recyclerviewholder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclickitemlistener.onItemClick(getAdapterPosition());
                }
            });

            mCheckBox =itemView.findViewById(R.id.checkBox);
            checkbox_text =itemView.findViewById(R.id.checkbox_textView);
            textView_Date=itemView.findViewById(R.id.textView_Date);
            textView_Time=itemView.findViewById(R.id.textview_Time);
        }
    }
}
