package cn.cloudfk.taoalbum.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Tools {

    /**
     *
     * @param url as http://127.0.0.1:8000
     * @return
     */
    public static String UrlGetIP(String url){
        String ip = "";
        if(url!=null && !"".equals(url)){
            ip = url.substring(url.lastIndexOf("/")+1, url.lastIndexOf(":"));
        }
        return ip;

    }

    /**
     *
     * @param url
     * @return
     */
    public static String UrlGetPort(String url){
        String port = "";
        if (url != null && !"".equals(url)) {
            port = url.substring(url.lastIndexOf(":")+1);
        }
        return port;

    }

    public static String getUrl(String ip,String port) {
        return "http://"+ip+":"+port;
    }

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");

    public static List<String> getDateList(){
        Calendar calendar = Calendar.getInstance();
        List<String> dateList = new ArrayList<>();
        for (int i=0; i < 18; i++){
            dateList.add(simpleDateFormat.format(calendar.getTime()));
            calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)-1);
        }
        // show all photos.
        dateList.add("all");
        return dateList;
    }

}
