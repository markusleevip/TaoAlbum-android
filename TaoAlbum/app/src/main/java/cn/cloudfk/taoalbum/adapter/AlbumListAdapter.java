package cn.cloudfk.taoalbum.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.cloudfk.taoalbum.R;
import java.util.List;

import cn.cloudfk.taoalbum.callback.ServiceHelper;
import cn.cloudfk.taoalbum.data.dto.ResourceDto;
import cn.cloudfk.taoalbum.data.dto.ResultData;

public class AlbumListAdapter extends ArrayAdapter {

    private List<ResourceDto> list;

    private Bitmap bitmap =null;
    private static final String TAG = "AlbumListAdapter";
    Activity context;
    private ImageView imageview;
    public AlbumListAdapter(Context _context, int resource, List<ResourceDto> _list) {
        super(_context, resource,  _list);
        list = _list;
        context = (Activity)_context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ResourceDto resDto = (ResourceDto)getItem(position);
        View view=convertView;
        //R.id.album_ui_img_fileName
        //album_ui_img_preview
        imageview= context.findViewById(R.id.album_ui_img_preview);

        ServiceHelper.showPhoto(resDto.getFilePath()+"/"+resDto.getPreview(),new ServiceHelper.ServiceCallback(){
            @Override
            public void onSuccess(String service, ResultData ret) {
                if (ServiceHelper.SHOW_PHOTO_KEY.equals(service)  && ret!=null
                        && ret.isSuccess() && ret.getData()!=null) {
                    bitmap = (Bitmap)ret.getData();
                    imageview.setImageBitmap(bitmap);
                    imageview.setMaxHeight(bitmap.getHeight());
                    imageview.setMaxWidth(bitmap.getWidth());
                    Log.i(TAG,"height="+bitmap.getHeight());
                    Log.i(TAG,"width="+bitmap.getWidth());

                }
            }
            @Override
            public void onFailure(String service, ResultData ret) {

            }
        });

        return view;
    }

}
