package com.zf.plugins.JiaGuLeGu;

public class JiaGuLeGuInfo {

    public String name

    String secretId
    String secretKey
    String proxy
    String downloadType
    String uploadType
    String uploadPath
    String downloadPath
    Boolean isOpenOutputDir

    public JiaGuLeGuInfo(String name) {
        this.name = name
        this.uploadType = JiaGuLeGuConfig.UPLOAD_DOWNLOAD_TYPE_FILE
        this.downloadType = JiaGuLeGuConfig.UPLOAD_DOWNLOAD_TYPE_FILE
    }

    void proxy(String proxy) {
        this.proxy = proxy
    }

    void downloadType(String downloadType) {
        this.downloadType = downloadType
    }

    void uploadType(String uploadType) {
        this.uploadType = uploadType
    }

    void isOpenOutputDir(boolean isOpenOutputDir) {
        this.isOpenOutputDir = isOpenOutputDir
    }

    void uploadPath(String uploadPath) {
        this.uploadPath = uploadPath
    }

    void downloadPath(String downloadPath) {
        this.downloadPath = downloadPath
    }

}



