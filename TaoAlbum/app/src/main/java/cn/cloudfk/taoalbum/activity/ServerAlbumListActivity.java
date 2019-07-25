package cn.cloudfk.taoalbum.activity;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.cloudfk.taoalbum.R;
import cn.cloudfk.taoalbum.data.dto.ResourceDto;
import cn.cloudfk.taoalbum.data.dto.ResultData;
import cn.cloudfk.taoalbum.utils.HttpAssist;
import cn.cloudfk.taoalbum.utils.KitJson;

public class ServerAlbumListActivity extends AppCompatActivity {

    private List<ResourceDto> resList = null;
    List<Map<String, String>> list;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_album_list);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String content = HttpAssist.getInstance().getImgList();
                if (content!=null){
                    Log.i(TAG,"content.length="+content.length());
                    ResultData ret = KitJson.toResultData(content);
                    if (ret!=null){
                        Log.i(TAG,"ret.state="+ret.getState());
                        if(ret.isSuccess()){
                            if (ret.getData()!=null){
                                resList = ( List<ResourceDto>)ret.getData();
                                if (resList!=null && !resList.isEmpty()) {
                                    list = new ArrayList<Map<String, String>>();
                                    for (ResourceDto res : resList) {
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("fileName", res.getFileName());
                                        list.add(map);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }).start();
        boolean isGetList = false;
        ListView lv = findViewById(R.id.server_listView);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                Map<String, String> map = list.get(arg2);
                String str = "";
                str += map.get("fileName");
                Log.i(TAG,"This was selected photo:"+str);
            }
        });

        while (true){
            try {
                if (isGetList){
                    break;
                }
                if (list!=null && !list.isEmpty() ){
                    SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.activity_album_ui_img_item,
                            new String[] { "fileName" },new int[] { R.id.album_ui_img_fileName});
                    lv.setAdapter(adapter);
                    isGetList= true;
                }
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
