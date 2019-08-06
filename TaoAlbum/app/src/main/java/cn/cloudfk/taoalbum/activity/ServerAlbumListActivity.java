package cn.cloudfk.taoalbum.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.cloudfk.taoalbum.R;
import android.widget.SimpleAdapter.ViewBinder;
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
    List<Map<String, Object>> list;
    ListView photoListView;
    ServiceHelper.ServiceCallback callback;
    Activity context;

    private Spinner spinnerDate;
    private ArrayAdapter<String> adapterDate;
    List<String> dateList;


    private void init(){
        photoListView = null;
        list = null;
        photoListView = null;
        callback =null;
        adapterDate = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
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
                Map<String, Object> map = list.get(arg2);
                String fileName = map.get("fileName")+"";
                String filePath = map.get("filePath")+"";
                Log.i(TAG,"This was selected photo:"+fileName);
                Intent intent = new Intent(GlobalData.context, ServerAlbumViewActivity.class);
                intent.putExtra(IntentUtil.FILENAME_KEY,fileName);
                intent.putExtra(IntentUtil.FILEPATH_KEY,filePath);
                startActivity(intent);
            }
        });
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
                        list = new ArrayList();
                        for (final ResourceDto resDto : resList) {
                            if (resDto.getPreview()==null || "".equals(resDto.getPreview())){
                                continue;
                            }
                            // Get preview image
                            ServiceHelper.showPhoto(resDto.getFilePath()+"/"+resDto.getPreview(),new ServiceHelper.ServiceCallback(){
                                @Override
                                public void onSuccess(String service, ResultData ret) {
                                    if (ServiceHelper.SHOW_PHOTO_KEY.equals(service)  && ret!=null
                                            && ret.isSuccess() && ret.getData()!=null) {
                                        Map<String, Object> map = new HashMap();
                                        map.put("fileName",resDto.getFileName());
                                        map.put("filePath",resDto.getFilePath());
                                        Bitmap bitmap = (Bitmap)ret.getData();
                                        map.put("imageView",bitmap );
                                        list.add(map);
                                    }
                                }
                                @Override
                                public void onFailure(String service, ResultData ret) {

                                }
                            });
                        }
                        if (list!=null && !list.isEmpty() ){
                            final SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.activity_album_ui_img_item,
                                    new String[] { "imageView" },new int[] { R.id.album_ui_img_preview});
                            adapter.setViewBinder(new ViewBinder() {
                                public boolean setViewValue(View view, Object data,
                                                          String textRepresentation) {
                                  if(view instanceof ImageView && data instanceof Bitmap){
                                      ImageView iv = (ImageView) view;
                                      Bitmap bm = (Bitmap) data;
                                      iv.setImageBitmap(bm);
                                      iv.setMaxWidth(bm.getWidth());
                                      iv.setMaxHeight(bm.getHeight());
                                      return true;
                                  }else
                                      return false;
                                }
                            });
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
        Log.i(TAG,"Selected item is "+dateList.get(position));
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
