package com.example.yunlong.ocrproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.example.yunlong.ocrproject.eventDAO.NormalDao;
import com.example.yunlong.ocrproject.EventActivity.AddEventActivity;
import com.example.yunlong.ocrproject.EventActivity.EditEventActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import labelDAO.LabelDao;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String[] itemText;
    private String[] itemDate;
    private List<Event> event_list = new ArrayList<>();
    private List<Label> label_list = new ArrayList<>();
    private View headerLayout;
    NavigationView navigationView;
    private Button addLabel;
    private Button editLabel;
    private AlertDialog.Builder builder;
    private String labelname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        //toolbar位于app_bar_main.xml,顶部导航栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //主页面下的“+”按钮
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, AddEventActivity.class);
                startActivity(intent);
            }
        });

        //activity_main布局框实例化
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //nav_view为左侧滑动菜单
        final LabelDao labelDao = new LabelDao(this);
        final Label label = new Label();
//      label_list = labelDao.findAllLabel();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        addLabel = (Button) headerLayout.findViewById(R.id.add_label);
        editLabel = (Button) headerLayout.findViewById(R.id.edit_label);

        addLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(MainActivity.this);
                builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("输入新建标签")
                        .setView(editText)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                labelname = editText.getText().toString();
                                Toast.makeText(MainActivity.this, "输入内容为：" + labelname
                                        , Toast.LENGTH_SHORT).show();
                                label.setLabelname(labelname);
                                labelDao.addLabel(label);
                            }
                        });
                builder.create().show();

            }
        });

//        for(int i=1; i< label_list.size(); i++) {
//            navigationView.getMenu().add(Menu.NONE, label_list.get(i).getId(), i, label_list.get(i).getLabelname());
//        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return false;
            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("123", "onStart------------------");
        init();

    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.i("123", "onStart------------------");
//        init();
//    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("123", "onResume------------------");
        init();
    }

    //
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //右上角菜单实例化
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }

    //右上角菜单键的点击获取
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar list_item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings1) {
            return true;
        }
        else if(id == R.id.action_settings2) {
            return true;
        }else if(id == R.id.action_settings3) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //左侧滑动窗口编写
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view list_item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_travel) {
            // Handle the camera action
        } else if (id == R.id.nav_self) {

        } else if (id == R.id.nav_live) {

        } else if (id == R.id.nav_work) {

        } else if (id == R.id.nav_none) {

        }

         else if (id == R.id.nav_manager) {

       }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void init() {
        Log.i("1", "init()");
        ListView listView = (ListView) findViewById(R.id.list);
        List<Map<String, Object>> listItem = new ArrayList<>();
        NormalDao normalDao = new NormalDao(this);
        event_list = normalDao.findAllEvent();

        itemText = new String[event_list.size()];
        itemDate = new String[event_list.size()];


        Log.i("111", String.valueOf(event_list.size()));
        for (int i = 0; i<event_list.size(); i++) {
            itemText[i] = event_list.get(i).getContent();
            itemDate[i] = event_list.get(i).getDate();
        }


        for(int i=0;i<itemText.length;i++)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", itemText[i]);
            map.put("date", itemDate[i]);
            listItem.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(MainActivity.this,listItem,//需要绑定的数据
                R.layout.list_item,//每一行的布局
                //动态数组中的数据源的键对应到定义布局的View中
                new String[] {"text","date"},
                new int[] {R.id.listtext,R.id.listdate});

        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "事项" + (i+1) , Toast.LENGTH_SHORT).show();

                Event event = event_list.get(i);
                int event_id = event.getId();
                Intent intent=new Intent();
                intent.putExtra("id", event_id);
                intent.setClass(MainActivity.this, EditEventActivity.class);
                startActivity(intent);
            }
        });
    }


}
