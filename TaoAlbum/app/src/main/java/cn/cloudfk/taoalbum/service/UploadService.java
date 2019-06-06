package cn.cloudfk.taoalbum.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.cloudfk.taoalbum.data.DataTool;
import cn.cloudfk.taoalbum.data.model.AlbumModel;
import cn.cloudfk.taoalbum.utils.HttpAssist;

public class UploadService extends Service {

    public static final String TAG = "UploadService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "UploadService.onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "UploadService.onStartCommand");
        List<String> albumList = getSystemPhotoList(this);
        if (albumList!=null && albumList.size()>0){
            for (String filePath : albumList) {
                Log.d(TAG, "path="+filePath);
                final String fileNameAll = filePath;
                AlbumModel albumModel = DataTool.queryAlbumByFileName(filePath);
                boolean isUpload = true;
                if (albumModel==null){
                    albumModel = new AlbumModel();
                    albumModel.fileName = filePath;
                    albumModel.state=0;
                    DataTool.insertAlbum(albumModel);
                }else if (albumModel.state !=null &&albumModel.state.intValue() ==1){
                    Toast.makeText(this,filePath+",已经上传过了",Toast.LENGTH_LONG);
                    Log.i(TAG,"文件已经上传过了:"+filePath);
                    isUpload = false;
                }
                File inFile = new File(filePath);
                if (isUpload && inFile != null &&inFile.exists()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run(){
                            HttpAssist.getInstance().uploadFile(new File(fileNameAll));
                            AlbumModel albumModel = new AlbumModel();
                            albumModel.fileName = fileNameAll;
                            albumModel.state=1;
                            DataTool.updateAlbum(albumModel);
                        }

                    }).start();
                }else{
                    Log.i(TAG,"文件已经上传或不存在:"+filePath);
                    Toast.makeText(this, "文件已经上传或不存在。"+filePath, Toast.LENGTH_LONG).show();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static List<String> getSystemPhotoList(Context context)
    {
        List<String> result = new ArrayList<String>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null || cursor.getCount() <= 0) return null; // 没有图片
        while (cursor.moveToNext())
        {
            int index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(index); // 文件地址
            File file = new File(path);
            if (file.exists())
            {
                result.add(path);
                Log.i(TAG, path);
            }
        }

        return result ;
    }
}
