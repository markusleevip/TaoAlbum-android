package cn.cloudfk.taoalbum.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import cn.cloudfk.taoalbum.R;
import cn.cloudfk.taoalbum.service.UploadService;
import cn.cloudfk.taoalbum.view.UploadProgressBar;

public class UploadActivity extends AppCompatActivity {
    private UploadProgressBar progressBar;
    private Toolbar mToolbar;
    private TextView uploadSizeTxt;
    String uploadSizeStr = "0/0";
    int progressBarMax = 0;

    private static final String TAG = "UploadActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_progress);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Intent startIntent = new Intent(this, UploadService.class);
        startService(startIntent);
        progressBar = findViewById(R.id.uploadProgressBar);
        progressBarMax = progressBar.getMax();
        uploadSizeTxt = findViewById(R.id.uploadSizeTxt);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    //Log.i(TAG,"UploadCurrent="+ UploadService.UploadCurrent+",UploadFiles="+UploadService.UploadFiles);
                    if(UploadService.UploadFiles >0
                            &&(UploadService.UploadCurrent==UploadService.UploadFiles)){
                        progressBar.setTargetProgress(progressBarMax);
                        uploadSizeStr = UploadService.UploadFiles+"/"+UploadService.UploadFiles;

                    }else if(UploadService.UploadFiles>0) {
                        float valueF =(float)UploadService.UploadCurrent/(float)UploadService.UploadFiles;
                        //Log.i(TAG,"UploadComplete="+ valueF+"%");
                        uploadSizeStr = UploadService.UploadCurrent+"/"+UploadService.UploadFiles;
                        progressBar.setTargetProgress((int)(valueF*100));
                    }
                    uploadSizeTxt.setText(uploadSizeStr);
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        break;
                    }
                }
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
}
