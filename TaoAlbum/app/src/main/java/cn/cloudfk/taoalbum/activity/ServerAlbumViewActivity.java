package cn.cloudfk.taoalbum.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import cn.cloudfk.taoalbum.R;
import cn.cloudfk.taoalbum.callback.ServiceHelper;
import cn.cloudfk.taoalbum.common.IntentUtil;
import cn.cloudfk.taoalbum.data.dto.ResultData;

public class ServerAlbumViewActivity extends AppCompatActivity implements ServiceHelper.ServiceCallback{

    private ImageView imageview;
    private Bitmap bitmap =null;
    String fileName;
    private static final String TAG = "ServerAlbumViewActivity";
    private Toolbar mToolbar;
    ServiceHelper.ServiceCallback callback;
    Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_album_view);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        fileName = getIntent().getStringExtra(IntentUtil.FILENAME_KEY);
        callback = this;
        context = this;
        //server_image_view
        imageview = findViewById(R.id.server_image_view);
        Log.i(TAG,"fileName="+fileName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServiceHelper.showPhoto(fileName,callback);
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

        if (ServiceHelper.SHOW_PHOTO_KEY.equals(service)  && ret!=null
                && ret.isSuccess() && ret.getData()!=null) {
            bitmap = (Bitmap)ret.getData();
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imageview.setImageBitmap(bitmap);
                    imageview.setMaxHeight(bitmap.getHeight());
                    imageview.setMaxWidth(bitmap.getWidth());
                    Log.i(TAG,"height="+bitmap.getHeight());
                    Log.i(TAG,"width="+bitmap.getWidth());
                }
            });
        }
    }

    @Override
    public void onFailure(String service, ResultData ret) {
        Log.i(TAG,"service="+service+".onFailure");
    }
}
