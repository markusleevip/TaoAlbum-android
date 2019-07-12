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
import android.widget.Toast;

import cn.cloudfk.taoalbum.activity.UploadActivity;
import cn.cloudfk.taoalbum.app.AlbumActivity;
import cn.cloudfk.taoalbum.data.DBAdapter;
import cn.cloudfk.taoalbum.data.GlobalData;
import cn.cloudfk.taoalbum.data.GlobalParam;
import cn.cloudfk.taoalbum.data.model.ProfileModel;
import cn.cloudfk.taoalbum.service.UploadService;
import cn.cloudfk.taoalbum.utils.Tools;
import cn.cloudfk.taoalbum.utils.ToolsView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private Activity context;
    private Application application;
    private Handler mainThreadHandler;

    private static MainActivity instance;

    private DBAdapter dbAdapter;

    private String fieldName= "address";

    private String initIP="192.168.1.100";
    private String initPort = "8000";
    private String initValue = "http://"+initIP+":"+initPort;
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
        findViewById(R.id.btn_save_ip).setOnClickListener(this);
        initParam();
        EditText ip = findViewById(R.id.txt_address);
        EditText port = findViewById(R.id.txt_port);
        ip.setText( GlobalData.param.getIP());
        port.setText(GlobalData.param.getPort());
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
        param.setIP(Tools.UrlGetIP(model.value));
        param.setPort(Tools.UrlGetPort(model.value));
        GlobalData.param = param;
        Log.i(TAG,"address:"+ model.value);
        realValue = model.value;



    }

    private void saveAddress(){
        ProfileModel model = dbAdapter.queryProfileByName(fieldName);
        EditText ipTxt = findViewById(R.id.txt_address);
        EditText portTxt = findViewById(R.id.txt_port);
        String ip = ipTxt.getText().toString();
        String port = portTxt.getText().toString();

        realValue = Tools.getUrl(ip,port);
        if(model!=null && !model.value.equals(realValue)){
            Log.i(TAG,"before realValue:"+ realValue);
            Log.i(TAG,"before saveAddress:"+ model.value);
            model.value = realValue;
            dbAdapter.updateProfile(model);
            model = dbAdapter.queryProfileByName(fieldName);
            initParam();
            ToolsView.ToastToCenter(this,"Saved successfully.",Toast.LENGTH_LONG);
            Log.i(TAG,"after saveAddress:"+ model.value);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_save_ip: {
                saveAddress();
                break;
            }
            case R.id.btn_album: {
                startActivity(new Intent(this, AlbumActivity.class));
                break;
            }
            case R.id.main_btn_upload:{
                Log.i(TAG,"Start uploadService.");
                startActivity(new Intent(this, UploadActivity.class));

                break;
            }
        }

    }

}
