package cn.cloudfk.taoalbum.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.cloudfk.taoalbum.R;
import cn.cloudfk.taoalbum.callback.ServiceHelper;
import cn.cloudfk.taoalbum.common.IntentUtil;
import cn.cloudfk.taoalbum.data.GlobalData;
import cn.cloudfk.taoalbum.data.dto.ResourceDto;
import cn.cloudfk.taoalbum.data.dto.ResultData;
import cn.cloudfk.taoalbum.utils.Tools;

public class ServerAlbumListActivity extends AppCompatActivity implements ServiceHelper.ServiceCallback , AdapterView.OnItemSelectedListener {

    private static final String TAG = "ServerAlbumListActivity";
    private Toolbar mToolbar;
    private List<ResourceDto> resList = null;
    List<Map<String, String>> list;
    ListView photoListView;
    ServiceHelper.ServiceCallback callback;
    Activity context;

    private Spinner spinnerDate;
    private ArrayAdapter<String> adapterDate;
    List<String> dateList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_album_list);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        photoListView = findViewById(R.id.server_listView);
        callback = this;
        context = this;
        dateList = Tools.getDateList();
        spinnerDate = findViewById(R.id.selectDate);
        adapterDate = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,dateList);
        adapterDate.setDropDownViewResource(R.layout.activity_server_album_list_date_spinner_item);
        spinnerDate.setAdapter(adapterDate);
        spinnerDate.setOnItemSelectedListener(this);
        Log.i(TAG,"ServerAlbumListActivity.onCreate");
        photoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                Map<String, String> map = list.get(arg2);
                String fileName = map.get("fileName");
                Log.i(TAG,"This was selected photo:"+fileName);
                Intent intent = new Intent(GlobalData.context, ServerAlbumViewActivity.class);
                intent.putExtra(IntentUtil.FILENAME_KEY,fileName);
                startActivity(intent);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServiceHelper.photoList(dateList.get(1),callback);
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                finish();
                break;
            }
        }
        return true;
    }

    @Override
    public void onSuccess(String service, ResultData ret) {
        Log.i(TAG,"service="+service+".onSuccess");
        if (ServiceHelper.PHOTO_LIST_KEY.equals(service)){
            if (ret != null && ret.isSuccess()) {
                if (ret.getData()!=null){
                    resList = ( List<ResourceDto>)ret.getData();
                    if (resList!=null && !resList.isEmpty()) {
                        list = new ArrayList<Map<String, String>>();
                        for (ResourceDto res : resList) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("fileName", res.getFileName());
                            Log.i(TAG,"fileName="+res.getFileName());
                            list.add(map);
                        }
                        if (list!=null && !list.isEmpty() ){
                            final SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.activity_album_ui_img_item,
                                    new String[] { "fileName" },new int[] { R.id.album_ui_img_fileName});
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    photoListView.setAdapter(adapter);
                                }
                            });

                        }
                    }
                }
            }
        }
    }

    @Override
    public void onFailure(String service, ResultData data) {
        Log.i(TAG,"service="+service+".onFailure");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG,"Selected date is "+dateList.get(position));
        photoListView.setAdapter(null);
        final int index = position;
        new Thread(new Runnable() {
            @Override
            public void run() {

                ServiceHelper.photoList(dateList.get(index),callback);
            }
        }).start();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
