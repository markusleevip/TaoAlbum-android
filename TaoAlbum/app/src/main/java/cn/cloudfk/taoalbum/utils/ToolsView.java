package cn.cloudfk.taoalbum.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.widget.Toast;

public class ToolsView {

    /**
     *
     * @param context
     * @param msg
     */
    public static void ToastToCenter(Context context,String msg,int duration){

        Toast toast = Toast.makeText(context,msg,duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        show(toast);
    }

    private static void show(Toast toast){
        toast.show();
    }

    public static void ToastToCenter(Context context, @StringRes int resId, int duration){

        Toast toast = Toast.makeText(context,context.getResources().getText(resId),duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        show(toast);
    }
}
