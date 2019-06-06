package cn.cloudfk.taoalbum.data;

import android.provider.ContactsContract;

import cn.cloudfk.taoalbum.data.model.AlbumModel;

public class DataTool {
    private static DBAdapter dbAdapter = new DBAdapter(GlobalData.context);

    public static void insertAlbum(AlbumModel model){
        dbAdapter.insertAlbum(model);
    }

    public static void updateAlbum(AlbumModel model){
        dbAdapter.updateAlbum(model);
    }

    public static AlbumModel queryAlbumByFileName(String fileName){
        return dbAdapter.queryAlbumByFileName(fileName);
    }





}
