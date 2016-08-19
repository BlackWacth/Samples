package com.hua.appupgrade.download;

import java.io.File;

/**
 * Created by hzw on 2016/8/19.
 */
public class DownloadManager {

    public static final String DM_TARGET_FOLDER = File.separator + "download" + File.separator; //下载管理目标文件夹

    //定义下载状态常量
    public static final int NONE = 0;         //无状态  --> 等待
    public static final int WAITING = 1;      //等待    --> 下载，暂停
    public static final int DOWNLOADING = 2;  //下载中  --> 暂停，完成，错误
    public static final int PAUSE = 3;        //暂停    --> 等待，下载
    public static final int FINISH = 4;       //完成    --> 重新下载
    public static final int ERROR = 5;        //错误    --> 等待

    private static class DownloadManagerHolder {
        public static final DownloadManager DOWNLOAD_MANAGER = new DownloadManager();
    }

    public static DownloadManager getInstance() {
        return DownloadManagerHolder.DOWNLOAD_MANAGER;
    }
}
