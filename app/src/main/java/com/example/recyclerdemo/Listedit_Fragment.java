package com.example.recyclerdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class Listedit_Fragment extends Fragment {
    private static final String TAG = "Listedit_Fragment";
    private Button remainder, finishFragment;
    private EditText taskDescription;
    private SwitchDateTimeDialogFragment dateTimeDialogFragment;

    private List<String> description;
    //  MainActivity mactivity=(MainActivity)getActivity();


    public Listedit_Fragment(taskDescription Description) {
        // Required empty public constructor
        description = Description.getDescription();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        finishFragment.setEnabled(false);
        super.onActivityCreated(savedInstanceState);

    }

    public void datetimeFragment(){
        Time time=new Time(Time.getCurrentTimezone());
        time.setToNow();
        dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                "Task Remainder",
                "OK",
                "Cancel"
        );
        final SimpleDateFormat myDateFormat = new SimpleDateFormat("d MMM yyyy HH:mm", java.util.Locale.getDefault());
// Assign values
        dateTimeDialogFragment.startAtTimeView();
        dateTimeDialogFragment.set24HoursMode(false);
        dateTimeDialogFragment.setMinimumDateTime(new GregorianCalendar(time.year, time.month, time.monthDay).getTime());
        dateTimeDialogFragment.setMaximumDateTime(new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime());
        dateTimeDialogFragment.setDefaultDateTime(new GregorianCalendar(time.year, time.month, time.monthDay, time.hour, time.minute).getTime());

// Define new day and month format
        try {
            dateTimeDialogFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("dd MMMM", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e(TAG, e.getMessage());
        }

// Set listener
        dateTimeDialogFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                // Date is get on positive button click
                // Do something
                Log.d(TAG, "onPositiveButtonClick: "+myDateFormat.format(date));

            }

            @Override
            public void onNegativeButtonClick(Date date) {
                // Date is get on negative button click
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Current Time Initialization
        datetimeFragment();

        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: ");
        final View fragmentView = inflater.inflate(R.layout.buttom_sheet_layout, container, false);


        //TaskDescriptionText handling
        taskDescription = fragmentView.findViewById(R.id.edit_TaskDescription);
        taskDescription.setText("");
        taskDescription.setSaveEnabled(false);

        //Keyboard opening

//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        //Remainder and onClick Listener

//        remainder = fragmentView.findViewById(R.id.Remainder_button);
//        remainder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                closekeyboard();
//                assert getFragmentManager() != null;
//                dateTimeDialogFragment.show(getFragmentManager(), "dialog_time");
//
//
//
//            }
//        });
        //Textchange Listener

        taskDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                finishFragment.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Done and OnClickListener

        finishFragment = fragmentView.findViewById(R.id.done_Button);
        finishFragment.setEnabled(false);
        finishFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) getActivity()).mFloatingActionButton.show();
                description.add(taskDescription.getText().toString());

                ((MainActivity) getActivity()).mRecyclerviewAdapter.getHold().checkbox_text.setText(taskDescription.getText().toString());
                ((MainActivity) getActivity()).mRecyclerviewAdapter.notifyDataSetChanged();
                //taskDescription.setText("");
               closeFragment();

            }
        });
        taskDescription = fragmentView.findViewById(R.id.edit_TaskDescription);
        return fragmentView;

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");

        super.onDestroy();
        finishFragment.setEnabled(false);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    public void closeFragment(){
        getFragmentManager().beginTransaction().remove(Listedit_Fragment.this).commit();
        closekeyboard();

    }

    private void closekeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }


}
