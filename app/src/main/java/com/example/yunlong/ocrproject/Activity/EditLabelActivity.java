package com.example.yunlong.ocrproject.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.yunlong.ocrproject.MyAdapter;
import com.example.yunlong.ocrproject.R;
import com.example.yunlong.ocrproject.model.Label;
import java.util.ArrayList;
import java.util.List;

import com.example.yunlong.ocrproject.DAO.LabelDao;

public class EditLabelActivity extends AppCompatActivity {

    private ListView listView;
    private String[] itemName;
    private List<String> list = new ArrayList<>();
    private List<Label> label_list;
    private int label_id;
    final LabelDao labelDao = new LabelDao(this);
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_label);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listView = (ListView) findViewById(R.id.label_list);
        label_list = labelDao.findAllLabel();
        itemName = new String[label_list.size()];


        Log.i("number", String.valueOf(label_list.size()));
        for (int i = 0; i<label_list.size(); i++) {
            itemName[i] = label_list.get(i).getLabelname();
            list.add(itemName[i]);
        }

        adapter = new MyAdapter(this, list);
        listView.setAdapter(adapter);
        adapter.setOnItemDeleteClickListener(new MyAdapter.onItemDeleteListener() {
            @Override
            public void onDeleteClick(int i) {
                label_id = label_list.get(i).getId();
                labelDao.deleteLabel(label_id);
                Log.i("数据库", "删除成功");
                list.remove(i);
                adapter.notifyDataSetChanged();
            }
        });
       adapter.setOnItemConfirmClickLister(new MyAdapter.onItemConfirmListener() {
           @Override
           public void onConfirmClick(int i, String s) {
               label_id = label_list.get(i).getId();
               labelDao.updateEvent(label_id, s);
               Log.i("数据库", "修改成功");

           }
       });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
