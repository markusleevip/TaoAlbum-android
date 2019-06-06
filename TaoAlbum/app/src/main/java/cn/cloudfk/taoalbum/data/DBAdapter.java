package cn.cloudfk.taoalbum.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.cloudfk.taoalbum.data.model.AlbumModel;
import cn.cloudfk.taoalbum.data.model.ProfileModel;

public class DBAdapter {

    private DBHelper dbHelper;
    Context context;
    final int DATABASE_VERSION = 23;

    public DBAdapter(Context _context){
        context = _context;
        dbHelper = new DBHelper(context,DATABASE_VERSION);
    }

    public synchronized void insertAlbum(AlbumModel model){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(" select * from "+ DBHelper.TABLE_ALBUM +" where "+
                DBHelper.KEY_ALBUM_FILE_NAME + "=?" ,new String[]{model.fileName});
        if (cursor!= null && cursor.getCount()>0){
        }else{
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_ALBUM_FILE_NAME, model.fileName);
            values.put(DBHelper.KEY_ALBUM_STATE,model.state);
            db.insert(DBHelper.TABLE_ALBUM,null, values);
        }
        close(cursor);
    }

    public synchronized AlbumModel queryAlbumByFileName(String fileName){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(" select fileName,state from "+DBHelper.TABLE_ALBUM + " where fileName=? ",new String[]{fileName});
        AlbumModel model = null;
        if (cursor!= null && cursor.getCount()>0 && cursor.moveToNext()){
            model = new AlbumModel();
            model.fileName = cursor.getString(0);
            model.state = cursor.getInt(1);
        }
        close(cursor);
        return model;
    }

    /**
     * insert profile data
     * @param model
     */
    public synchronized  void insertProfile(ProfileModel model){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(" select * from "+ DBHelper.TABLE_PROFILE +" where "+
                DBHelper.KEY_PROFILE_NAME + "=?" ,new String[]{model.name});
        if (cursor!= null && cursor.getCount()>0){
        }else{
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_PROFILE_NAME, model.name);
            values.put(DBHelper.KEY_PROFILE_VALUE,model.value);
            db.insert(DBHelper.TABLE_PROFILE,null, values);
        }
        close(cursor);
    }

    public synchronized  void updateAlbum(AlbumModel model){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(" select * from "+ DBHelper.TABLE_ALBUM +" where "+
                DBHelper.KEY_ALBUM_FILE_NAME + "=?" ,new String[]{model.fileName});

        if (cursor!= null && cursor.getCount()>0){
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_ALBUM_STATE,model.state);
            db.update(DBHelper.TABLE_ALBUM,values,DBHelper.KEY_ALBUM_FILE_NAME+"= ? ",new String[] { model.fileName });
        }
    }


    /**
     * update profile data
     * @param model
     */
    public synchronized  void updateProfile(ProfileModel model){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(" select * from "+ DBHelper.TABLE_PROFILE +" where "+
                DBHelper.KEY_PROFILE_NAME + "=?" ,new String[]{model.name});
        if (cursor!= null && cursor.getCount()>0){
            ContentValues values = new ContentValues();
            values.put(DBHelper.KEY_PROFILE_VALUE,model.value);
            db.update(DBHelper.TABLE_PROFILE,values,"name = ? ",new String[] { model.name });
        }
        close(cursor);
    }

    public synchronized ProfileModel queryProfileByName(String name){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(" select name,value from "+DBHelper.TABLE_PROFILE + " where name=? ",new String[]{name});
        ProfileModel model = null;
        if (cursor!= null && cursor.moveToNext()){
            model = new ProfileModel();
            model.name = cursor.getString(0);
            model.value =cursor.getString(1);
        }
        return model;
    }



    private void close(Cursor cursor){
        if (cursor!=null)
            cursor.close();
    }


    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
