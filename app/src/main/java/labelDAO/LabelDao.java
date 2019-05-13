package labelDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.yunlong.ocrproject.Label;
import java.util.ArrayList;
import java.util.List;

public class LabelDao {
    private Context context;
    private MyLabelDatabaseHelper myLabelDatabaseHelper ;
    public LabelDao(Context context){
        this.context = context;
    }
    public List<Label>findAllLabel(){
        List<Label> list = new ArrayList<Label>();
        myLabelDatabaseHelper = new MyLabelDatabaseHelper(context,"MyEvent.db",null,2);
        SQLiteDatabase database = myLabelDatabaseHelper.getReadableDatabase();
        Cursor cursor = database.query("LabelTable",null,null,null,null,null,null);
        String info="";
        int id;
        String labelname;
        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("id"));
                labelname = cursor.getString(cursor.getColumnIndex("labelname"));
                Label event = new Label();
                event.setId(id);
                event.setLabelname(labelname);
                list.add(event);
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
        myLabelDatabaseHelper = new MyLabelDatabaseHelper(context,"MyEvent.db",null,2);
        SQLiteDatabase database = myLabelDatabaseHelper.getWritableDatabase();
        database.delete("LabelTable","id=?",new String[]{String.valueOf(id)});
        return true;
    }

    public Boolean addLabel(Label label) {
        myLabelDatabaseHelper = new MyLabelDatabaseHelper(context,"MyEvent.db",null,2);
        SQLiteDatabase database = myLabelDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("labelname",label.getLabelname());
        database.insert("LabelTable",null,values);
        values.clear();
        Log.i("add", "添加成功");
        return true;
    }

    public Boolean updateEvent(int id, String labelname) {
        myLabelDatabaseHelper = new MyLabelDatabaseHelper(context,"MyEvent.db",null,2);
        SQLiteDatabase database = myLabelDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("labelname",labelname);
        database.update("LabelTable",values,"id=?",new String[]{String.valueOf(id)});
        values.clear();
        return true;
    }
}