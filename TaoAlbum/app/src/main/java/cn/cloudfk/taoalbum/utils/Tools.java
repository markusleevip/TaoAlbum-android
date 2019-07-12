package cn.cloudfk.taoalbum.utils;

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

}
