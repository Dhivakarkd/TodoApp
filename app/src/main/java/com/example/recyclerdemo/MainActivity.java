package com.example.recyclerdemo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.recyclerdemo.Alarm.AlarmReceiver;
import com.example.recyclerdemo.Alarm.NotificationHelper;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutionException;


import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity implements onclickinterface {
    private static final String TAG = "MainActivity";
    public static String NO_REMAINDER="noremainder";
    public static final String NIL=" - ";
    public static final String NOTIFICAATION_TITLE ="Task Remainder";
    public static final String ALARM_INTENT_STRING_NAME ="Alarm-Message";
    public static Date No_Date;
    private NotificationHelper mNotificationHelper;



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(keyboardstatus) {
            closekeyboard();
        }
        mTodoNotesList.removeObservers(this);
    }

    //Reamainder Date Variables
    SimpleDateFormat myDateFormat,mDayFormat;
    Date myDate;
    Calendar mCalendar=Calendar.getInstance();

    RecyclerView mRecyclerView;
    recyclerviewAdapter mRecyclerviewAdapter;
    taskDescription taskdescription;
    List<String> description;
    List<TodoNotes> mTodoNotestemp;
    FloatingActionButton mFloatingActionButton;
    Listedit_Fragment fragment;
    FragmentManager fragmentManager;
    LiveData<List<TodoNotes>> mTodoNotesList;
    private TodoNotesRepository TodoNotesRepository;
    boolean keyboardstatus;

    //BottomSheet Elements
    BottomSheetDialog bottomSheetDialog;
    View bottomSheetView;
    private Button finishButton;
    private EditText taskDescription;
    private SwitchDateTimeDialogFragment dateTimeDialogFragment;
    LinearLayout emptyLayout;
    private Chip remainderChip;
    private Toolbar mToolbar;





    //Elements for Checking whether the position is FAB or RecyclerViewClick;
    private boolean isfab;
    private int clickpos;

    //Menu Inflator
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Log.d(TAG, "onCreate: "+mCalendar.get(Calendar.HOUR_OF_DAY));
        No_Date=new Date(2000,12,12,12,12,12);

        //Theme Check
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            themeCheck();
        }
        //Reposiory Initialize
        TodoNotesRepository = new TodoNotesRepository(this);

        //BottomSheet Initialize
        bottomSheetInitialize();

        //DateTime Fragment Initialize
        datetimeFragment();

        //Notification Helper Initialize
        mNotificationHelper =new NotificationHelper(this);

        //Toolbar and menu creation
        mToolbar=findViewById(R.id.activity_toolBar);
        setSupportActionBar(mToolbar);






        //Initializing the tast list Array
        try {
            mTodoNotesList = TodoDatabase.getInstance(getApplicationContext()).todoDao().getallLiveTodo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mTodoNotestemp = TodoNotesRepository.getAllNotetask();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mTodoNotesList.observe(this, new Observer<List<TodoNotes>>() {
            @Override
            public void onChanged(List<TodoNotes> notes) {
                mRecyclerviewAdapter.notifyDataSetChanged();
            }
        });
        taskdescription = new taskDescription();

//        Creating Tempory Todo
//        mTodoNotestemp =new ArrayList<>();
//        mTodoNotestemp.add(new TodoNotes("1this is sample",false,No_Date));
//        mTodoNotestemp.add(new TodoNotes("2this is sample",false,n));
//        mTodoNotestemp.add(new TodoNotes("3this is sample",false));
//        mTodoNotestemp.add(new TodoNotes("4this is sample",false));
//        mTodoNotestemp.add(new TodoNotes("5this is sample",false));
//        mTodoNotestemp.add(new TodoNotes("6this is sample",false));
//        TodoNotesRepository.insertMultiNotes(mTodoNotestemp);

        emptyLayout = findViewById(R.id.emptylayout);
        //BottomSheet Initialize

        //EditText Initialize and TextChangeClickListener
        taskDescription = bottomSheetView.findViewById(R.id.edit_TaskDescription);
        taskDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    finishButton.setEnabled(false);
                } else {
                    finishButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //RemainderButton Initialize and OnclickListener
        remainderChip = bottomSheetView.findViewById(R.id.Remainder_chip);
        remainderChip.setCloseIconVisible(false);
        remainderChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closekeyboard();
                assert getFragmentManager() != null;
                dateTimeDialogFragment.show(getSupportFragmentManager(), "dialog_time");
            }
        });

        remainderChip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm(myDate);
                remainderChip.setText(R.string.set_a_remainder);
                remainderChip.setCloseIconVisible(false);
            }
        });

        //FinishButton Initialize and OnClickListener
        finishButton = bottomSheetView.findViewById(R.id.done_Button);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatingActionButton.show();
             closekeyboard();
               Date remainder = null;
                try {
                    remainder = myDate;
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (isfab) {
                    TodoNotes notes;
                    if(remainderChip.isCloseIconVisible() && remainder != null){
                        Log.d(TAG, "RemainderonClick: "+remainder);
                        notes = new TodoNotes(taskDescription.getText().toString(), false,remainder);
                        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT) {
                            startAlarm(remainder, taskDescription.getText().toString());
                        }
                        remainder =null;
                    }else {
                        Log.d(TAG, "RemaindercheckonClick: "+remainder);
                        notes = new TodoNotes(taskDescription.getText().toString(), false, No_Date);
                    }
                    TodoNotesRepository.insertNotetask(notes);
//                    description.add(taskDescription.getText().toString());
                } else {
                    if(remainderChip.isCloseIconVisible() && remainder != null) {
                        TodoNotesRepository.updateNotes(mRecyclerviewAdapter, clickpos, taskDescription.getText().toString(),remainder);
                        remainder=null;
                    }else{
                        TodoNotesRepository.updateNotes(mRecyclerviewAdapter, clickpos, taskDescription.getText().toString(),No_Date);
                    }

                    //description.set(clickpos,taskDescription.getText().toString());
                }
                mRecyclerviewAdapter.notifyDataSetChanged();
                bottomSheetDialog.dismiss();

            }
        });

        //Recycler View initializing and transmission
        mRecyclerView = findViewById(R.id.RecyclerView);
        mRecyclerviewAdapter = new recyclerviewAdapter(description, mTodoNotesList, mTodoNotestemp, TodoNotesRepository, this);
        mRecyclerView.setAdapter(mRecyclerviewAdapter);

        //Recycler view Move Up ,Down ,Right And Left
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
               return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                final int position = viewHolder.getAdapterPosition();
                final TodoNotes removed = mTodoNotesList.getValue().get(position);

                if (direction == ItemTouchHelper.LEFT) {
                    final int[] dummy = {0};
                   mTodoNotesList.getValue().remove(position);
                    mRecyclerviewAdapter.notifyItemRemoved(position);
                    Snackbar.make(mRecyclerView, "TaskRemoved", Snackbar.LENGTH_LONG)

                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                //    taskdescription.getDescription().add(position, removed);
                                    mTodoNotesList.getValue().add(position,removed);
                                    mRecyclerviewAdapter.notifyItemInserted(position);
                                    dummy[0] =1;
                                }
                            }).show();
                    CountDownTimer countDownTimer=new CountDownTimer(4000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            Log.d(TAG, "onTick: "+millisUntilFinished*1000);
                        }

                        @Override
                        public void onFinish() {
                            Log.d(TAG, "onFinish: completed");
                            if(dummy[0] != 1){
                                Log.d(TAG, "onFinish: entered");
                                TodoNotesRepository.removeNotes(removed.getNotesDescription());
                            }
                        }
                    }.start();

                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.delete))
                        .addActionIcon(R.drawable.ic_delete_sweep_black_24dp)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            }
        });
        //Setting TouchHelper

        helper.attachToRecyclerView(mRecyclerView);


        //Floating Action Button Initializayion and onclick Listener
        mFloatingActionButton = findViewById(R.id.floatingActionButton);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isfab = true;
                remainderChip.setCloseIconVisible(false);
                remainderChip.setText(R.string.set_a_remainder);
                taskDescription.setText("");
                finishButton.setEnabled(false);
                bottomSheetDialog.show();
                showKeyboard();
                //mFloatingActionButton.hide();

            }
        });

        //Listener for empty layout
        mRecyclerviewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkEmpty();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkEmpty();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkEmpty();
            }
        });
        checkEmpty();
    }


    @Override
    public void onItemClick(int position) {
        isfab = false;
        clickpos = position;
        taskDescription.setText(mTodoNotesList.getValue().get(position).getNotesDescription());
        Date remainderDate=mTodoNotesList.getValue().get(position).getRemainderTime();
        String remainderText =checkDateFormat(remainderDate);
        if(remainderDate.compareTo(No_Date) == 0){
            remainderChip.setText(R.string.set_a_remainder);
            remainderChip.setCloseIconVisible(false);
        }else {
            remainderChip.setText(remainderText);
            remainderChip.setCloseIconVisible(true);
        }
        bottomSheetDialog.show();
        //   mFloatingActionButton.hide();


    }

    public void bottomSheetInitialize() {
        bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        bottomSheetView = this.getLayoutInflater().inflate(R.layout.buttom_sheet_layout, null);
        bottomSheetDialog.setContentView(bottomSheetView);
    }

    public void checkfragmentandclose(View view) {

        NotificationCompat.Builder nb=mNotificationHelper.getNotification("NOTHING","Hello World");
        mNotificationHelper.getNofiticationManager().notify(1,nb.build());

    }

    private void closekeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
       // imm.hideSoftInputFromWindow(taskDescription.getWindowToken(, 0);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
        keyboardstatus=false;
        Log.d(TAG, "closekeyboard onDismiss");
    }


    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        keyboardstatus=true;
    }

    public void datetimeFragment() {
        Time time = new Time(Time.getCurrentTimezone());
        time.setToNow();
        Log.d(TAG, "datetimeFragment: "+time.toString());
        dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                "Task Remainder",
                "OK",
                "Cancel"
        );
        myDateFormat = new SimpleDateFormat("d MMM HH:mm", java.util.Locale.getDefault());
        mDayFormat =new SimpleDateFormat("HH:mm",java.util.Locale.getDefault());

// Assign values
        dateTimeDialogFragment.startAtTimeView();
        dateTimeDialogFragment.set24HoursMode(false);
        dateTimeDialogFragment.setMinimumDateTime(new GregorianCalendar(time.year, time.month, time.monthDay).getTime());
        dateTimeDialogFragment.setMaximumDateTime(new GregorianCalendar(2100, Calendar.DECEMBER, 31).getTime());
        dateTimeDialogFragment.setDefaultDateTime(new GregorianCalendar(time.year, time.month, time.monthDay, time.hour, time.minute).getTime());

// Define new day and month format
        try {
            dateTimeDialogFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("dd MMMM", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e(TAG, e.getMessage());
        }

// Set listener
        dateTimeDialogFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onPositiveButtonClick(Date date) {
                // Date is get on positive button click
                // Do something
                myDate=date;
                Log.d(TAG, "onPositiveButtonClick: " + myDateFormat.format(date));
              //  TodoNotesRepository.updateRemainder(myDateFormat.format(date),clickpos);
                String textDate =checkDateFormat();

                remainderChip.setText(textDate);
                remainderChip.setCloseIconVisible(true);

            }

            @Override
            public void onNegativeButtonClick(Date date) {
                // Date is get on negative button click
            }
        });
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.d(TAG, "onDismiss: workedfine");
                closekeyboard();
            }
        });

    }

    void checkEmpty() {
        emptyLayout.setVisibility(mRecyclerviewAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }


    String checkDateFormat(){
        Log.d(TAG, "checkDateFormat: "+mCalendar.get(Calendar.DATE) + " ==== "+ myDate.getDay());
        if(myDate.getDate() == mCalendar.get(Calendar.DATE)){
            return "Today "+mDayFormat.format(myDate);
        }
        if(myDate.getDate() == mCalendar.get(Calendar.DATE)+1){
            return "Tomorrow "+mDayFormat.format(myDate);
        }
        return myDateFormat.format(myDate);
    }
    String checkDateFormat(Date date){
        if(date.getDate() == mCalendar.get(Calendar.DATE)){
            return "Today "+mDayFormat.format(date);
        }
        if(date.getDate() == mCalendar.get(Calendar.DATE)+1){
            return "Tomorrow "+mDayFormat.format(date);
        }
        return myDateFormat.format(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void themeCheck(){

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;        Log.d(TAG, "themeCheck: yep");
        GradientDrawable draw;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                Log.d(TAG, "themeCheck: light nothing");
             draw=(GradientDrawable) getDrawable(R.drawable.bottom_sheet_backgroud);
                draw.setColor(Color.parseColor("#FFFFFFFF"));
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                Log.d(TAG, "themeCheck: dark nothing");
               draw=(GradientDrawable) getDrawable(R.drawable.bottom_sheet_backgroud);
                draw.setColor(Color.parseColor("#FF424242"));

                break;
            default:
                Log.d(TAG, "themeCheck: nothing");
        }
    }
    int num;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAlarm(Date date,String message){
        AlarmManager alarmManager =(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(this, AlarmReceiver.class);
        intent.putExtra(ALARM_INTENT_STRING_NAME,message);
        try {
           num= (mTodoNotesList.getValue().get(clickpos).getPrimaryID());
        }catch (Exception e){
        Random rand=new Random();
        num=rand.nextInt(100);
        }

        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,num,intent,0);

        assert alarmManager != null;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,date.getTime(),pendingIntent);
    }
    private void cancelAlarm(Date date){
        AlarmManager alarmManager =(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,num,intent,0);

        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
    }

}
