package cn.cloudfk.taoalbum.data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DBHelper  extends SQLiteOpenHelper{

    public static final String DB_NAME = "taoAlbum.db";

    public static final String TABLE_ALBUM = "album";
    public static final String TABLE_PROFILE = "profile";


    public static final String KEY_ALBUM_FILE_NAME = "fileName";
    public static final String KEY_ALBUM_STATE = "state";
    public static final String KEY_PROFILE_NAME = "name";
    public static final String KEY_PROFILE_VALUE = "value";

    private static final String CREATE_ALBUM = "CREATE TABLE IF NOT EXISTS " +
            TABLE_ALBUM + " (rId INTEGER AUTO_INCREMENT, " + KEY_ALBUM_FILE_NAME +" VARCHAR(100) , " +
            KEY_ALBUM_STATE+ " INTEGER , PRIMARY KEY(rId));";
    private static final String CREATE_PROFILE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_PROFILE + "  (rId INTEGER AUTO_INCREMENT, " + KEY_PROFILE_NAME  + " VARCHAR(20) ," +
            KEY_PROFILE_VALUE +"  VARCHAR(100) , PRIMARY KEY(rId)); ";


    DBHelper(Context context, int version) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db,CREATE_ALBUM);
        createTable(db,CREATE_PROFILE);
    }

    private void createTable(SQLiteDatabase db, String name) {
        if (!tabIsExist(db, name))
            try {
                db.execSQL(name);
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 判断某张表是否存在
     */
    public boolean tabIsExist(SQLiteDatabase db, String tabName) {
        boolean result = false;
        if (tabName == null) {
            return false;
        }

        Cursor cursor = null;
        try {
            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"
                    + tabName.trim() + "' ";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }
}
