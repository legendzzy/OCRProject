package com.example.yunlong.ocrproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.yunlong.ocrproject.Activity.EditLabelActivity;
import com.example.yunlong.ocrproject.DAO.NormalDao;
import com.example.yunlong.ocrproject.Activity.AddEventActivity;
import com.example.yunlong.ocrproject.Activity.EditEventActivity;
import com.example.yunlong.ocrproject.model.Event;
import com.example.yunlong.ocrproject.model.Label;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.yunlong.ocrproject.DAO.LabelDao;

public class MainActivity extends AppCompatActivity
        {

    private String[] itemText;
    private String[] itemDate;
    private List<Event> event_list = new ArrayList<>();
    private List<Label> label_list = new ArrayList<>();
    private View headerLayout;
    private ListView listView;
    private NavigationView navigationView;
    private Button addLabel;
    private Button editLabel;
    private AlertDialog.Builder builder;
    private String labelname;
    final LabelDao labelDao = new LabelDao(this);
    private SearchView mSearchView;
    private List<Event> event_search_list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        addLabel = (Button) headerLayout.findViewById(R.id.add_label);
        editLabel = (Button) headerLayout.findViewById(R.id.edit_label);

        final Label label = new Label();

        //toolbar位于app_bar_main.xml,顶部导航栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //activity_main布局框实例化
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //加载事项列表
        init();

        //加载标签菜单
        init_label();

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
                                init_label();

                            }
                        });
                builder.create().show();

            }
        });

        editLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditLabelActivity.class);
                startActivity(intent);
            }
        });


        //nav_view为左侧滑动菜单
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Log.i("菜单大小" , String.valueOf(label_list.size()));
                Log.i("点击事件" , "点击菜单" + id+";");
                String labName="";
                if(-1==id){
                    init();
                    return false;
                }else{
                    labName=label_list.get(id-1).getLabelname();
                }
                Log.i("点击事件" , "点击菜单" + id+";菜单文本："+labName);

                //清空搜索集
                event_search_list.clear();
                //搜索匹配结果
                for(Event event:event_list){
                    //匹配成功
                    if(event.getLabel().equals(labName)){
                        event_search_list.add(event);
                    }

                }
                //刷新界面
                if(event_search_list.size()==0){
                    Toast.makeText(MainActivity.this,"未搜索到:"+labName,Toast.LENGTH_SHORT).show();
                }

                //刷新界面
                listView = (ListView) findViewById(R.id.list);
                List<Map<String, Object>> listItem = new ArrayList<>();
                itemText = new String[event_search_list.size()];
                itemDate = new String[event_search_list.size()];


                Log.i("111", String.valueOf(event_search_list.size()));
                for (int i = 0; i<event_search_list.size(); i++) {
                    itemText[i] = event_search_list.get(i).getContent();
                    itemDate[i] = event_search_list.get(i).getDate();
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
                return false;
            }
        });


        //搜索框
        mSearchView=(SearchView)findViewById(R.id.search_view) ;

        //搜索框初始化
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mSearchView.getLayoutParams();
        //将文字内容略微下移，SearchView  bug
        params.bottomMargin = -3;
        mSearchView.setLayoutParams(params);
        mSearchView.onActionViewExpanded();
        initSearchView();

        //搜索监听事件
        mSearchView.setOnQueryTextListener(mQueryListener);

    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("123", "onStart------------------");
        init();
        init_label();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("123", "onResume------------------");
        init();
        init_label();
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
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//       getMenuInflater().inflate(R.menu.main, menu);
//
//
//        return true;
//    }

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
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view list_item clicks here.
//        int id = item.getItemId();
//        Log.i("menu", "点击菜单");
//        if (id == R.id.all) {
//            // Handle the camera action
//            onStart();
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

    public void init() {
        Log.i("1", "init()");
        listView = (ListView) findViewById(R.id.list);
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

                //获取文本内容
                HashMap<String,Object> mapSelected=(HashMap<String,Object>)listView.getItemAtPosition(i);
                String textSelected=(String)mapSelected.get("text");
                Log.i("001", "textSelected:"+textSelected);
                //遍历事项获取对应id
                int event_id=0;
                for(Event event:event_list){
                    //文本匹配
                    if(event.getContent().equals(textSelected)){
                        event_id=event.getId();
                        break;
                    }
                }
                Intent intent=new Intent();
                intent.putExtra("id", event_id);
                intent.setClass(MainActivity.this, EditEventActivity.class);
                startActivity(intent);
            }
        });

    }

    public void init_label() {
        label_list = labelDao.findAllLabel();
        navigationView.getMenu().clear();
        navigationView.getMenu().add(2, -1, 0,"全部").setIcon(R.drawable.ic_menu_send);
        for(int i=0; i< label_list.size(); i++) {
            navigationView.getMenu().add(1, i+1, i, label_list.get(i).getLabelname()).setIcon(R.drawable.ic_menu_send);
        }
    }

//初始化搜索框
    private void initSearchView() {
        //一处searchView进入屏幕时候的焦点
        mSearchView.clearFocus();
        Class<? extends SearchView> aClass = mSearchView.getClass();
        try {
            //去掉SearchView自带的下划线
            Field mSearchPlate = aClass.getDeclaredField("mSearchPlate");
            mSearchPlate.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //搜索事件监听
    private SearchView.OnQueryTextListener mQueryListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            //做本地查询
            Toast.makeText(MainActivity.this,"搜索:"+newText,Toast.LENGTH_SHORT).show();

            if(newText.equals("")){
                init();
                return true;
            }else {
                event_search_list.clear();
                //把查询到的事项放置于结果集event_search_list
                for(Event event:event_list){
                    //事项中的文本内容
                    String content=event.getContent();
                    Log.i("000", "文本内容:"+content);
                    //字符串查找，小于零表示未匹配
                    if(content.indexOf(newText)<0)
                        continue;
                    else {
                        event_search_list.add(event);
                    }
                }
                //event_list=event_search_list;

                if(event_search_list.size()==0){
                    Toast.makeText(MainActivity.this,"未搜索到:"+newText,Toast.LENGTH_SHORT).show();
                }

                //刷新界面
                listView = (ListView) findViewById(R.id.list);
                List<Map<String, Object>> listItem = new ArrayList<>();
                itemText = new String[event_search_list.size()];
                itemDate = new String[event_search_list.size()];


                Log.i("111", String.valueOf(event_search_list.size()));
                for (int i = 0; i<event_search_list.size(); i++) {
                    itemText[i] = event_search_list.get(i).getContent();
                    itemDate[i] = event_search_list.get(i).getDate();
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
            }
            return true;
        }
    };

}
