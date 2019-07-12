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
import cn.cloudfk.taoalbum.data.GlobalData;
import cn.cloudfk.taoalbum.data.model.AlbumModel;
import cn.cloudfk.taoalbum.utils.HttpAssist;
import cn.cloudfk.taoalbum.utils.ToolsView;

public class UploadService extends Service {

    public static int UploadFiles = 0;
    public static int UploadCurrent = 0;
    public static boolean UploadComplete=false;
    public static final String TAG = "UploadService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "UploadService.onCreate()");
        init();

    }

    private void init(){
        UploadFiles = 0;
        UploadCurrent=0;
        UploadComplete=false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "UploadService.onStartCommand");
        init();
        List<String> albumList = getSystemPhotoList(this);
        if (albumList!=null && albumList.size()>0){
            UploadFiles = albumList.size();
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
                }else if (albumModel.state !=null &&albumModel.state.intValue() ==AlbumModel.SUCCESS){
                    //ToolsView.ToastToCenter(this,filePath+",The file has been uploaded",Toast.LENGTH_SHORT);
                    Log.i(TAG,"The file has been uploaded:"+filePath);
                    isUpload = false;
                }
                File inFile = new File(filePath);
                if (isUpload && inFile != null &&inFile.exists()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run(){
                            String uploadState = HttpAssist.getInstance().uploadFile(new File(fileNameAll));
                            AlbumModel albumModel = new AlbumModel();
                            albumModel.fileName = fileNameAll;
                            if (uploadState!=null && HttpAssist.SUCCESS.equals(uploadState)) {
                                albumModel.state = AlbumModel.SUCCESS;
                            }else {
                                albumModel.state = AlbumModel.FAIL;
                                ToolsView.ToastToCenter(GlobalData.context,fileNameAll+",upload fail",Toast.LENGTH_SHORT);
                            }
                            DataTool.updateAlbum(albumModel);
                            UploadCurrent++;
                        }

                    }).start();
                }else{
                    UploadCurrent++;
                    Log.i(TAG,"The file has been uploaded or does not exist:"+filePath);
                    //ToolsView.ToastToCenter(this, "The file has been uploaded or does not exist。"+filePath, Toast.LENGTH_SHORT);
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static List<String> getSystemPhotoList(Context context){
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
