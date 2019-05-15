package com.example.yunlong.ocrproject.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.yunlong.ocrproject.model.Label;

import java.util.ArrayList;
import java.util.List;

public class LabelDao {
    private Context context;
    private MyDatabaseHelper myDatabaseHelper ;
    public LabelDao(Context context){
        this.context = context;
    }
    public List<Label>findAllLabel(){
        List<Label> list = new ArrayList<Label>();
        myDatabaseHelper = new MyDatabaseHelper(context,"MyEvent.db",null,3);
        SQLiteDatabase database = myDatabaseHelper.getReadableDatabase();
        Cursor cursor = database.query("LabelTable",null,null,null,null,null,null);
        String info="";
        int id;
        String labelname;
        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("id"));
                labelname = cursor.getString(cursor.getColumnIndex("labelname"));
                Label label = new Label();
                label.setId(id);
                label.setLabelname(labelname);
                list.add(label);
                info+=id;
                info+="  ";
                info+=labelname;
                info+="\n";
            }while (cursor.moveToNext());
        }
        cursor.close();
        Log.i("DB",info);
        return list;
    }

    public Boolean deleteLabel(int id) {
        myDatabaseHelper = new MyDatabaseHelper(context,"MyEvent.db",null,3);
        SQLiteDatabase database = myDatabaseHelper.getWritableDatabase();
        database.delete("LabelTable","id=?",new String[]{String.valueOf(id)});
        return true;
    }

    public Boolean addLabel(Label label) {
        myDatabaseHelper = new MyDatabaseHelper(context,"MyEvent.db",null,3);
        SQLiteDatabase database = myDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("labelname",label.getLabelname());
        database.insert("LabelTable",null,values);
        values.clear();
        Log.i("add", "添加成功");
        return true;
    }

    public Boolean updateEvent(int id, String labelname) {
        myDatabaseHelper = new MyDatabaseHelper(context,"MyEvent.db",null,3);
        SQLiteDatabase database = myDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("labelname",labelname);
        database.update("LabelTable",values,"id=?",new String[]{String.valueOf(id)});
        values.clear();
        return true;
    }
}