package com.example.android.habittrackerapp;


import android.provider.BaseColumns;

public final class HabitContract {

    private HabitContract(){}

    public static final class HabitEntry implements BaseColumns{
        public final static String TABLE_NAME = "Habits";
        public  final static String _ID = BaseColumns._ID;
        public final static String COLUMN_HABIT_NAME = "Name";
        public final static String COLLUMN_HABIT_REPETITION = "Repetition";
    }
}
