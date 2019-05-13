package labelDAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyLabelDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_LABEL = "create table LabelTable(id integer primary key autoincrement, labelname text)";
    //public static final String INIT = "insert into LabelTable (labelname) VALUES ('默认')";


    private Context mContext;
    public MyLabelDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_LABEL);
        //sqLiteDatabase.execSQL(INIT);
        Toast.makeText(mContext,"created and initiate",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists LabelTable");
        onCreate(sqLiteDatabase);
    }
}