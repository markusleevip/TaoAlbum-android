package cn.cloudfk.taoalbum.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.cloudfk.taoalbum.data.dto.ResourceDto;
import cn.cloudfk.taoalbum.data.dto.ResultData;

public class KitJson {

    public static ResultData toResultData(String json){
        ResultData ret =new ResultData();
        try {
            JSONObject object = new JSONObject(json);
            if (object!=null){
                ret.setState(object.getInt("State"));
                try {
                    JSONArray data = object.getJSONArray("Data");
                    if (data!=null && data.length()>0){
                        List<ResourceDto> resList = new ArrayList<ResourceDto>();
                        for (int i=0; i < data.length();i++) {
                            ResourceDto resDto = new ResourceDto();
                            JSONObject value = data.getJSONObject(i);
                            resDto.setFileName(value.getString("FileName"));
                            resDto.setFilePath(value.getString("FilePath"));
                            resDto.setFileSize(value.getInt("FileSize"));
                            resList.add(resDto);
                        }
                        ret.setData(resList);
                    }
                }catch (RuntimeException e){
                    ret.setData(object.getJSONObject("Data"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;
    }

}
