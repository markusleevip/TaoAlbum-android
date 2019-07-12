package cn.cloudfk.taoalbum.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.cloudfk.taoalbum.R;
import cn.cloudfk.taoalbum.service.UploadService;
import cn.cloudfk.taoalbum.view.UploadProgressBar;

public class UploadActivity extends AppCompatActivity {
    UploadProgressBar progressBar;
    int i = 0;
    int progressBarMax = 0;

    private static final String TAG = "UploadActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_progress);
        Intent startIntent = new Intent(this, UploadService.class);
        startService(startIntent);
        progressBar = findViewById(R.id.uploadProgressBar);
        progressBarMax = progressBar.getMax();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    //Log.i(TAG,"UploadCurrent="+ UploadService.UploadCurrent+",UploadFiles="+UploadService.UploadFiles);
                    if(UploadService.UploadFiles >0
                            &&(UploadService.UploadCurrent==UploadService.UploadFiles)){
                        progressBar.setTargetProgress(progressBarMax);
                    }else if(UploadService.UploadFiles>0) {
                        float valueF =((float)UploadService.UploadCurrent/(float)UploadService.UploadFiles);
                        //Log.i(TAG,"UploadComplete="+ valueF+"%");
                        progressBar.setTargetProgress((int)(valueF*100));
                    }
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        break;
                    }
                }
            }
        }).start();

    }
}
