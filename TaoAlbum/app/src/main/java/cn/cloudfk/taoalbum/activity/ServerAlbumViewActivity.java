package cn.cloudfk.taoalbum.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.cloudfk.taoalbum.R;

public class ServerAlbumViewActivity extends AppCompatActivity {

    private ImageView imageview;
    private Bitmap bitmap =null;
    String imgUrl = null;
    private static final String TAG = "ServerAlbumViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_album_view);
        //server_image_view
        boolean isDownloadImg = false;
        imgUrl = "http://127.0.0.1:8000/show/1563332742538981600.jpg";

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    imageview = findViewById(R.id.server_image_view);
                    bitmap = getBitmap(imgUrl);
                    if (bitmap!=null){
                        Log.i(TAG,"heiget:"+ bitmap.getHeight());
                        Log.i(TAG,"width:"+ bitmap.getWidth());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        while (true){
            try {
                if (isDownloadImg){
                    break;
                }
                if (bitmap!=null ){
                    imageview = findViewById(R.id.server_image_view);
                    if (bitmap!=null){
                        Log.i(TAG,"heiget:"+ bitmap.getHeight());
                        Log.i(TAG,"width:"+ bitmap.getWidth());
                        imageview.setImageBitmap(bitmap);
                        imageview.setMaxHeight(bitmap.getHeight());
                        imageview.setMaxWidth(bitmap.getWidth());
                    }
                    isDownloadImg= true;
                }
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }



    private Bitmap getBitmap(String path) throws IOException {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
