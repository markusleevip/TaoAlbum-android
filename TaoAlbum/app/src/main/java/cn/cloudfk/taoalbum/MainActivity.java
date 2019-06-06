package cn.cloudfk.taoalbum;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import cn.cloudfk.taoalbum.app.AlbumActivity;
import cn.cloudfk.taoalbum.data.DBAdapter;
import cn.cloudfk.taoalbum.data.GlobalData;
import cn.cloudfk.taoalbum.data.GlobalParam;
import cn.cloudfk.taoalbum.data.model.ProfileModel;
import cn.cloudfk.taoalbum.service.UploadService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener ,View.OnFocusChangeListener{

    private static final String TAG = "MainActivity";
    private Activity context;
    private Application application;
    private Handler mainThreadHandler;

    private static MainActivity instance;

    private DBAdapter dbAdapter;

    private String fieldName= "address";
    private String initValue = "http://192.168.8.195:8000";
    private static String realValue = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        GlobalData.context = this;
        mainThreadHandler = new Handler(Looper.getMainLooper());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);
        findViewById(R.id.btn_album).setOnClickListener(this);
        findViewById(R.id.main_btn_upload).setOnClickListener(this);
        initParam();
        EditText editText1 = findViewById(R.id.txt_address);
        editText1.setText( GlobalData.param.getServerUrl());
        findViewById(R.id.txt_address).setOnFocusChangeListener(this);


    }

    private void initParam(){
        // init SQLite
        GlobalParam param = new  GlobalParam();

        dbAdapter = new DBAdapter(this);
        ProfileModel model = dbAdapter.queryProfileByName(fieldName);
        if (model==null){
            model = new ProfileModel();
            model.name = fieldName;
            model.value = initValue;
            dbAdapter.insertProfile(model);
        }
        param.setServerUrl(model.value);
        GlobalData.param = param;
        Log.i(TAG,"address:"+ model.value);
        realValue = model.value;



    }

    private void saveAddress(){
        ProfileModel model = dbAdapter.queryProfileByName(fieldName);
        EditText editText1 = findViewById(R.id.txt_address);
        realValue = editText1.getText().toString();
        if(model!=null && !model.value.equals(realValue)){
            Log.i(TAG,"before realValue:"+ realValue);
            Log.i(TAG,"before saveAddress:"+ model.value);
            model.value = realValue;
            dbAdapter.updateProfile(model);
            model = dbAdapter.queryProfileByName(fieldName);
            initParam();
            Log.i(TAG,"after saveAddress:"+ model.value);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_album: {
                saveAddress();
                startActivity(new Intent(this, AlbumActivity.class));
                break;
            }
            case R.id.main_btn_upload:{
                Log.i(TAG,"启动UploadService.");
                Intent startIntent = new Intent(this, UploadService.class);
                startService(startIntent);

            }

        }

    }

    public static MainActivity getInstance(){
        return instance;
    }
    public void runOnMainThread(Runnable runnable){
        if(mainThreadHandler != null){
            mainThreadHandler.post(runnable);
            return;
        }
        this.runOnUiThread(runnable);

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();
        switch (id){
            case R.id.txt_address:{
                if (!hasFocus)
                    saveAddress();
            }
        }
    }
}
