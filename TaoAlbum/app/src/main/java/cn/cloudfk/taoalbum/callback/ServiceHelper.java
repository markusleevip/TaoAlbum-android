package cn.cloudfk.taoalbum.callback;

import android.graphics.Bitmap;
import android.util.Log;

import cn.cloudfk.taoalbum.data.GlobalData;
import cn.cloudfk.taoalbum.data.dto.ResultData;
import cn.cloudfk.taoalbum.utils.HttpAssist;
import cn.cloudfk.taoalbum.utils.KitJson;

public class ServiceHelper {

    private static final String TAG = "ServiceHelper";
    public static final String PHOTO_LIST_KEY = "photoList";
    public static final String SHOW_PHOTO_KEY= "showPhoto";

    public interface ServiceCallback{
        void onSuccess(String service, ResultData ret);
        void onFailure(String service, ResultData ret);
    }

    public static void photoList( String yyyymm,final ServiceCallback callback){
        Log.i(TAG,"call photoList.");
        Log.i(TAG,"call yyyymm="+yyyymm);
        String content = HttpAssist.getInstance().getImgList(yyyymm);
        if (content!=null){
            Log.i(TAG,"content.length="+content.length());
            ResultData ret = KitJson.toResultData(content);
            if (ret!=null){
                Log.i(TAG,"ret.state="+ret.getState());
                if(ret.isSuccess()){
                    callback.onSuccess(PHOTO_LIST_KEY,ret);
                    return;
                }
            }
            callback.onFailure(PHOTO_LIST_KEY,ret);
        }
    }

    public static void showPhoto(String fileName,final ServiceCallback callback){
        String imgUrl = GlobalData.param.getServerUrl()+"/show/"+fileName;
        Bitmap bitmap = HttpAssist.getInstance().getBitmap(imgUrl);
        ResultData ret = new ResultData();
        ret.setState(ResultData.FAIL);
        if (bitmap!=null){
            ret.setState(ResultData.SUCCESS);
            ret.setData(bitmap);
            callback.onSuccess(SHOW_PHOTO_KEY,ret);
        }
        callback.onFailure(SHOW_PHOTO_KEY,ret);
    }

}
