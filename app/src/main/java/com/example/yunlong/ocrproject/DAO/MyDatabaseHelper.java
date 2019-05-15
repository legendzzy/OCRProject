package com.example.yunlong.ocrproject.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by yunlong on 2019/4/1.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_EVENT = "create table EventTable(id integer primary key autoincrement, content text," +
            "label text, date text)";
    public static final String CREATE_LABEL = "create table LabelTable(id integer primary key autoincrement, labelname text)";
    public static final String INIT = "insert into LabelTable (labelname) VALUES ('默认')";
    private Context mContext;
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_EVENT);
        sqLiteDatabase.execSQL(CREATE_LABEL);
        sqLiteDatabase.execSQL(INIT);
        Toast.makeText(mContext,"created",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists EventTable");
        sqLiteDatabase.execSQL("drop table if exists LabelTable");
        onCreate(sqLiteDatabase);
    }
}
