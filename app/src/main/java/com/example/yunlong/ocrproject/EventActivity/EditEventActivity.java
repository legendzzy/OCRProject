package com.example.yunlong.ocrproject.EventActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yunlong.ocrproject.Event;
import com.example.yunlong.ocrproject.eventDAO.NormalDao;
import com.example.yunlong.ocrproject.R;

public class EditEventActivity extends AppCompatActivity {

    private static final String[] label={"A型","B型","O型","AB型","其他"};
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private EditText editText;
    private TextView dateView;
    int event_id;
    private Event event;
    NormalDao normalDao = new NormalDao(this);

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        event_id = intent.getIntExtra("id", 0);
        event = normalDao.searchEvent(event_id);
        editText = (EditText) findViewById(R.id.edit_text);
        dateView = (TextView) findViewById(R.id.edit_date);
        editText.setText(event.getContent());
        dateView.setText(event.getDate());
        spinner = (Spinner) findViewById(R.id.Spinner01);
        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, label);

        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);

        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

        //设置默认值
        spinner.setVisibility(View.VISIBLE);

        //删除事项
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabDelete);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                normalDao.deleteEvent(event_id);
                Toast.makeText(getApplicationContext(),"删除成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    //使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    //右上角菜单实例化
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.addcompleted, menu);
        return true;
    }

    //右上角菜单键的点击获取
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar list_item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_completed:
                String content;
                content = editText.getText().toString();
                Log.i("content: ", content);

                event.setContent(content);
                event.setLabel("default");
                event.updateDate();
                normalDao.updateEvent(event_id, content,"default",  event.getDateByString());
                Toast.makeText(this, "编辑成功", Toast.LENGTH_LONG).show();
                dateView.setText(event.getDateByString());
                break;
//                Intent intent = new Intent();
//                intent.setClass(this, MainActivity.class);
//                startActivity(intent);
            default:
        }

        return super.onOptionsItemSelected(item);
    }

}

