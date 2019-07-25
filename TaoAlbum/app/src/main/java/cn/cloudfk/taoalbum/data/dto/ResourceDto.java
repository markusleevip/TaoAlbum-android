package cn.cloudfk.taoalbum.data.dto;

import java.io.Serializable;

public class ResourceDto implements Serializable {

    private int fileSize;
    private String fileName;
    private String filePath;

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
