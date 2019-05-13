package com.example.yunlong.ocrproject.eventDAO;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.yunlong.ocrproject.Event;
import com.example.yunlong.ocrproject.NormalEvent;

import java.util.ArrayList;
import java.util.List;


public class NormalDao implements Dao {
    public NormalDao(Context context) {
        super();
        this.context = context;
    }

    private Context context;
    private MyDatabaseHelper myDatabaseHelper ;
    @Override
    public List<Event> findAllEvent() {
        List<Event> list = new ArrayList<Event>();
        myDatabaseHelper = new MyDatabaseHelper(context,"MyEvent.db",null,2);
        SQLiteDatabase database = myDatabaseHelper.getReadableDatabase();
        Cursor cursor = database.query("EventTable",null,null,null,null,null,null);
        String info = "";
        int id;
        String content = "";
        String label="";
        String date = "";

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("id"));
                content = cursor.getString(cursor.getColumnIndex("content"));
                label = cursor.getString(cursor.getColumnIndex("label"));
                date = cursor.getString(cursor.getColumnIndex("date"));

                Event event = new NormalEvent();
                event.setId(id);
                event.setContent(content);
                event.setLabel(label);
                event.setDate(date);
                list.add(event);

                info+=id;
                info+="  ";
                info+=content;
                info+="  ";
                info+=label;
                info+="  ";
                info+=date;
                info+="\n";
            }while (cursor.moveToNext());
        }
        cursor.close();
        Log.i("DB",info);
        return list;
    }


    @Override
    public Boolean deleteEvent(int id) {
        myDatabaseHelper = new MyDatabaseHelper(context,"MyEvent.db",null,2);
        SQLiteDatabase database = myDatabaseHelper.getWritableDatabase();
        database.delete("EventTable","id=?",new String[]{String.valueOf(id)});
        return true;
    }

    @Override
    public Event searchEvent(int id) {
        myDatabaseHelper = new MyDatabaseHelper(context,"MyEvent.db",null,2);
        SQLiteDatabase database = myDatabaseHelper.getReadableDatabase();
        Cursor cursor = database.query("EventTable",null,"id=?", new String[]{String.valueOf(id)},null,null,null);
        String info = "";
        String content = "";
        String label="";
        String date = "";
        Event event = new NormalEvent();
        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("id"));
                content = cursor.getString(cursor.getColumnIndex("content"));
                label = cursor.getString(cursor.getColumnIndex("label"));
                date = cursor.getString(cursor.getColumnIndex("date"));
                event.setId(id);
                event.setContent(content);
                event.setLabel(label);
                event.setDate(date);
            }while (cursor.moveToNext());
        }
        cursor.close();
        Log.i("DB",info);
        return event;
    }

    @Override
    public Boolean addEvent(Event event) {
        myDatabaseHelper = new MyDatabaseHelper(context,"MyEvent.db",null,2);
        SQLiteDatabase database = myDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content",event.getContent());
        values.put("label",event.getLabel());
        values.put("date",event.getDateByString());
        database.insert("EventTable",null,values);
        values.clear();
        return true;
    }

    @Override
    public Boolean updateEvent(int id, String content, String label, String date) {
        myDatabaseHelper = new MyDatabaseHelper(context,"MyEvent.db",null,2);
        SQLiteDatabase database = myDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content",content);
        values.put("label",label);
        values.put("date",date);
        database.update("EventTable",values,"id=?",new String[]{String.valueOf(id)});
        values.clear();
        return true;
    }
}
