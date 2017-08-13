package com.example.android.habittrackerapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText mHabitNameEditText;
    private EditText mrepetitionEditText;

    private com.example.android.habittrackerapp.HabitDBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHabitNameEditText = (EditText)findViewById(R.id.habit_name);
        mrepetitionEditText = (EditText)findViewById(R.id.repetition_edit);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertHabit();
            }
        });

        mDBHelper = new com.example.android.habittrackerapp.HabitDBHelper(this);
        displayDatabase();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabase();
    }

    private void displayDatabase(){
        mDBHelper = new com.example.android.habittrackerapp.HabitDBHelper(this);

        Cursor cursor = readDatabase();

        TextView display = (TextView)findViewById(R.id.text_view);

        try {
            display.setText("The Habit Table Consist Of Following Habits "+cursor.getCount()+"\n"+"\n");
            display.append(com.example.android.habittrackerapp.HabitContract.HabitEntry._ID +"     |        " +
                    com.example.android.habittrackerapp.HabitContract.HabitEntry.COLUMN_HABIT_NAME + "     |       " +
                    com.example.android.habittrackerapp.HabitContract.HabitEntry.COLLUMN_HABIT_REPETITION + "     |       "
            );

            int idColumnIndex = cursor.getColumnIndex(com.example.android.habittrackerapp.HabitContract.HabitEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(com.example.android.habittrackerapp.HabitContract.HabitEntry.COLUMN_HABIT_NAME);
            int repetitionColumnIndex = cursor.getColumnIndex(com.example.android.habittrackerapp.HabitContract.HabitEntry.COLLUMN_HABIT_REPETITION);

            while (cursor.moveToNext()){
                int currentID = cursor.getInt(idColumnIndex);
                String currentHabitName = cursor.getString(nameColumnIndex);
                int currentrepetition = cursor.getInt(repetitionColumnIndex);

                display.append(("\n" + currentID + "     |       "
                + currentHabitName + "     |       "
                + currentrepetition + "      |      "));
            }
        }finally {
            cursor.close();
        }
    }

    private Cursor readDatabase(){
        Cursor temp;
        SQLiteDatabase db = mDBHelper.getReadableDatabase();

        String[] projection = {
                com.example.android.habittrackerapp.HabitContract.HabitEntry._ID,
                com.example.android.habittrackerapp.HabitContract.HabitEntry.COLUMN_HABIT_NAME,
                com.example.android.habittrackerapp.HabitContract.HabitEntry.COLLUMN_HABIT_REPETITION};
        temp = db.query(
                com.example.android.habittrackerapp.HabitContract.HabitEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);
        return temp;
    }

    private void insertHabit(){
        String nameText = mHabitNameEditText.getText().toString().trim();
        int repetitionText = Integer.parseInt(mrepetitionEditText.getText().toString().trim());

        mDBHelper = new com.example.android.habittrackerapp.HabitDBHelper(this);

        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(com.example.android.habittrackerapp.HabitContract.HabitEntry.COLUMN_HABIT_NAME, nameText);
        values.put(com.example.android.habittrackerapp.HabitContract.HabitEntry.COLLUMN_HABIT_REPETITION, repetitionText);

        long newRowID = db.insert(com.example.android.habittrackerapp.HabitContract.HabitEntry.TABLE_NAME, null, values);

        if (newRowID == -1 ){
            Toast.makeText(this,"Error while Saving in Database",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"Habit saved with ID: "+newRowID,Toast.LENGTH_SHORT).show();
        }
        displayDatabase();
    }

}
