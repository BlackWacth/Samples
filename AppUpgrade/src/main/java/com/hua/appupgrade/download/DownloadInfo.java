package com.hua.appupgrade.download;

import com.hua.appupgrade.download.listener.DownloadListener;

import okhttp3.Request;

public class DownloadInfo {

    private String id;                  //id自增长
    private String url;                 //文件URL
    private String targetFolder;        //保存文件夹
    private String targetPath;          //保存文件地址
    private String fileName;            //保存的文件名
    private float progress;             //下载进度
    private long totalLength;           //总大小
    private long downloadLength;        //已下载大小
    private long networkSpeed;          //下载速度
    private int state = 0;              //当前状态
    private Request request;        //当前任务的网络请求

    private DownloadTask task;          //执行当前下载的任务
    private DownloadListener listener;  //当前下载任务的监听

    public DownloadInfo(Builder builder) {
        this.id = builder.id;
        this.url = builder.url;
        this.targetFolder = builder.targetFolder;
        this.targetPath = builder.targetPath;
        this.fileName = builder.fileName;
        this.progress = builder.progress;
        this.totalLength = builder.totalLength;
        this.downloadLength = builder.downloadLength;
        this.networkSpeed = builder.networkSpeed;
        this.state = builder.state;
        this.request = builder.request;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getTargetFolder() {
        return targetFolder;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public String getFileName() {
        return fileName;
    }

    public float getProgress() {
        return progress;
    }

    public long getTotalLength() {
        return totalLength;
    }

    public long getDownloadLength() {
        return downloadLength;
    }

    public long getNetworkSpeed() {
        return networkSpeed;
    }

    public int getState() {
        return state;
    }

    public Request getRequest() {
        return request;
    }

    public DownloadTask getTask() {
        return task;
    }

    public DownloadListener getListener() {
        return listener;
    }

    public void addDownloadListener(DownloadListener listener) {
        this.listener = listener;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTargetFolder(String targetFolder) {
        this.targetFolder = targetFolder;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public void setTotalLength(long totalLength) {
        this.totalLength = totalLength;
    }

    public void setDownloadLength(long downloadLength) {
        this.downloadLength = downloadLength;
    }

    public void setNetworkSpeed(long networkSpeed) {
        this.networkSpeed = networkSpeed;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void setTask(DownloadTask task) {
        this.task = task;
    }

    public static class Builder {
        private String id;                  //id自增长
        private String url;                 //文件URL
        private String targetFolder;        //保存文件夹
        private String targetPath;          //保存文件地址
        private String fileName;            //保存的文件名
        private float progress;             //下载进度
        private long totalLength;           //总大小
        private long downloadLength;        //已下载大小
        private long networkSpeed;          //下载速度
        private int state = 0;              //当前状态
        private Request request;        //当前任务的网络请求

        public Builder() {
        }

        public Builder(DownloadInfo info) {
            this.id = info.id;
            this.url = info.url;
            this.targetFolder = info.targetFolder;
            this.targetPath = info.targetPath;
            this.fileName = info.fileName;
            this.progress = info.progress;
            this.totalLength = info.totalLength;
            this.downloadLength = info.downloadLength;
            this.networkSpeed = info.networkSpeed;
            this.state = info.state;
            this.request = info.request;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setTargetFolder(String targetFolder) {
            this.targetFolder = targetFolder;
            return this;
        }

        public Builder setTargetPath(String targetPath) {
            this.targetPath = targetPath;
            return this;
        }

        public Builder setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder setProgress(float progress) {
            this.progress = progress;
            return this;
        }

        public Builder setTotalLength(long totalLength) {
            this.totalLength = totalLength;
            return this;
        }

        public Builder setDownloadLength(long downloadLength) {
            this.downloadLength = downloadLength;
            return this;
        }

        public Builder setNetworkSpeed(long networkSpeed) {
            this.networkSpeed = networkSpeed;
            return this;
        }

        public Builder setState(int state) {
            this.state = state;
            return this;
        }

        public Builder setRequest(Request request) {
            this.request = request;
            return this;
        }

        public DownloadInfo builder() {
            return new DownloadInfo(this);
        }
    }

    /** taskKey 相同就认为是同一个任务 */
    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof DownloadInfo) {
            DownloadInfo info = (DownloadInfo) o;
            return getId().equals(info.getId());
        }
        return false;
    }
}