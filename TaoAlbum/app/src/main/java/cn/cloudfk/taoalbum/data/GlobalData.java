package cn.cloudfk.taoalbum.data;

import android.app.Activity;
import android.os.Handler;

public class GlobalData {
    public static Activity context;
    public static GlobalParam param;
    public static Handler mainThreadHandler = null;

    public static void runOnMainThread(Runnable runnable){
        if(mainThreadHandler != null){
            mainThreadHandler.post(runnable);
            return;
        }

        if(context != null){
            context.runOnUiThread(runnable);

        }
    }

}
