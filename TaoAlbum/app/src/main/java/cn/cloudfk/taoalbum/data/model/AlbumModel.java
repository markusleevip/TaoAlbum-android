package cn.cloudfk.taoalbum.data.model;

public class AlbumModel{
    public String fileName;
    // state 0 未上传 1上传成功 2 上传失败
    public Integer state;

    @Override
    public String toString() {
        return "AlbumEntity [fileName="+fileName+",state="+state+",]";
    }
}
