package cn.cloudfk.taoalbum.callback;

import android.util.Log;

import cn.cloudfk.taoalbum.data.dto.ResultData;
import cn.cloudfk.taoalbum.utils.HttpAssist;
import cn.cloudfk.taoalbum.utils.KitJson;

public class ServiceHelper {

    private static final String TAG = "ServiceHelper";
    public static final String PHOTO_LIST = "photoList";
    public static final String SHOW_PHOTO = "showPhoto";

    public interface ServiceCallback{
        void onSuccess(String service, ResultData ret);
        void onFailure(String service, ResultData ret);
    }

    public static void photoList( final ServiceCallback callback){
        Log.i(TAG,"call photoList.");
        String content = HttpAssist.getInstance().getImgList();
        if (content!=null){
            Log.i(TAG,"content.length="+content.length());
            ResultData ret = KitJson.toResultData(content);
            if (ret!=null){
                Log.i(TAG,"ret.state="+ret.getState());
                if(ret.isSuccess()){
                    callback.onSuccess(PHOTO_LIST,ret);
                    return;
                }
            }
            callback.onFailure(PHOTO_LIST,ret);
        }
    }

    public static void showPhoto(String filename,final ServiceCallback callback){

    }
}
