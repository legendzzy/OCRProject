package com.example.yunlong.ocrproject.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.example.yunlong.ocrproject.OCR.FileUtil;
import com.example.yunlong.ocrproject.OCR.RecognizeService;
import com.example.yunlong.ocrproject.model.Event;
import com.example.yunlong.ocrproject.DAO.NormalDao;
import com.example.yunlong.ocrproject.R;
import com.example.yunlong.ocrproject.model.Label;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.yunlong.ocrproject.DAO.LabelDao;

public class EditEventActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ACCURATE_BASIC = 107;
    private boolean hasGotToken = false;
    private AlertDialog.Builder alertDialog;

    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private EditText editText;
    private TextView dateView;
    private TextView labelView;
    private int event_id;
    private String label;
    private Event event;
    NormalDao normalDao = new NormalDao(this);
    private List<String> label_name_list = new ArrayList<String>();
    private List<Label> labelList = new ArrayList<>();
    LabelDao labelDao = new LabelDao(this);

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
        labelView = (TextView) findViewById(R.id.label);
        editText.setText(event.getContent());
        dateView.setText(event.getDate());
        labelView.setText(event.getLabel());
        spinner = (Spinner) findViewById(R.id.Spinner01);


        labelList = labelDao.findAllLabel();
        for (int i=0; i<labelList.size(); i++) {
            label_name_list.add(labelList.get(i).getLabelname());
        }
        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, label_name_list);

        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);

        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

        //设置默认值
        spinner.setVisibility(View.VISIBLE);

        //拍照
        FloatingActionButton fabCamera = (FloatingActionButton) findViewById(R.id.fabCamera);
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkTokenStatus()) {
                    return;
                }
                Intent intent = new Intent(EditEventActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_ACCURATE_BASIC);
            }
        });

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

        initAccessTokenWithAkSk();

    }

    //使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            label = label_name_list.get(arg2);

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
                //event.setLabel(label);
               // event.setContent(content);
                event.updateDate();
                normalDao.updateEvent(event_id, content, label,  event.getDateByString());
                Toast.makeText(this, "编辑成功", Toast.LENGTH_LONG).show();
                dateView.setText(event.getDateByString());
                labelView.setText(label);
                break;
//                Intent intent = new Intent();
//                intent.setClass(this, MainActivity.class);
//                startActivity(intent);
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean checkTokenStatus() {
        if (!hasGotToken) {
            Toast.makeText(getApplicationContext(), "token还未成功获取", Toast.LENGTH_LONG).show();
        }
        return hasGotToken;
    }

    /**
     * 以license文件方式初始化
     */
    private void initAccessToken() {
        OCR.getInstance(this).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("licence方式获取token失败", error.getMessage());
            }
        }, getApplicationContext());
    }

    private void initAccessTokenWithAkSk() {
        OCR.getInstance(this).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("AK，SK方式获取token失败", error.getMessage());
            }
        }, getApplicationContext(),  "fHHn8z7B4G4uHbpFDUhUOd2s", "x27Lyo8iBHPmMTUGGdQGPQADrf9xFnZm");
    }

    private void alertText(final String title, final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });
    }

    private void infoPopText(final String result) {
        alertText("", result);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initAccessToken();
        } else {
            Toast.makeText(getApplicationContext(), "需要android.permission.READ_PHONE_STATE", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 识别成功回调，通用文字识别（高精度版）
        if (requestCode == REQUEST_CODE_ACCURATE_BASIC && resultCode == Activity.RESULT_OK) {
            RecognizeService.recAccurateBasic(this, FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            try{
                                EditText editText = (EditText)findViewById(R.id.edit_text);
                                JSONObject jsonObject = new JSONObject(result);
                                JSONArray words_result = jsonObject.getJSONArray("words_result");
                                int words_result_num = jsonObject.getInt("words_result_num");
                                String info = editText.getText().toString();
                                for(int i=0;i<words_result_num;i++){
                                    String words = words_result.getJSONObject(i).getString("words")+'\n';
                                    Log.i("TAG",words);
                                    info+=words;
                                }
                                editText.setText(info);
                                editText.setSelection(editText.getText().toString().length());
                                Log.i("INF",editText.getText().toString());
                            }catch (Exception e){

                            }

                        }
                    });
        }

    }

    protected void onDestroy() {
        super.onDestroy();
        // 释放内存资源
        OCR.getInstance(this).release();
    }


}

