package cn.cloudfk.taoalbum.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import cn.cloudfk.taoalbum.data.dto.ResourceDto;

public class AlbumListAdapter extends ArrayAdapter<ResourceDto> {

    private List<ResourceDto> list;
    public AlbumListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public AlbumListAdapter(Context context, int resource, List<ResourceDto> _list) {
        super(context, resource,  _list);
        list = _list;
    }

}
