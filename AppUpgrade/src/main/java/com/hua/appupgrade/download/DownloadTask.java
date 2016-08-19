package com.hua.appupgrade.download;

import com.hua.appupgrade.download.listener.DownloadListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;


public class DownloadTask {

    private static final int BUFFER_SIZE = 1024 * 8; //读写缓存大小

    private DownloadInfo mDownloadInfo;              //当前任务的信息
    private long mPreviousTime;                      //上次更新的时间，用于计算下载速度
    private boolean isRestartTask;                   //是否重新下载的标识位
    private boolean isPause;                         //当前任务是暂停还是停止， true 暂停， false 停止

    public DownloadTask(DownloadInfo downloadInfo, boolean isRestart, DownloadListener downloadListener) {
        mDownloadInfo = downloadInfo;
        isRestartTask = isRestart;
        mDownloadInfo.addDownloadListener(downloadListener);
    }

    public void download() {
        mDownloadInfo.setNetworkSpeed(0);
        mDownloadInfo.setState(DownloadManager.DOWNLOADING);

        long startPos = mDownloadInfo.getDownloadLength();


    }

    /** 文件读写 */
    private final class ProgressRandomAccessFile extends RandomAccessFile {
        private long lastDownloadLength = 0; //总共已下载的大小
        private long curDownloadLength = 0;  //当前已下载的大小（可能分几次下载）
        private long lastRefreshUiTime;

        public ProgressRandomAccessFile(File file, String mode, long lastDownloadLength) throws FileNotFoundException {
            super(file, mode);
            this.lastDownloadLength = lastDownloadLength;
            this.lastRefreshUiTime = System.currentTimeMillis();
        }

        @Override
        public void write(byte[] buffer, int offset, int count) throws IOException {
            super.write(buffer, offset, count);

            //已下载大小
            long downloadLength = lastDownloadLength + count;
            curDownloadLength += count;
            lastDownloadLength = downloadLength;
            mDownloadInfo.setDownloadLength(downloadLength);

            //计算下载速度
            long totalTime = (System.currentTimeMillis() - mPreviousTime) / 1000;
            if (totalTime == 0) {
                totalTime += 1;
            }
            long networkSpeed = curDownloadLength / totalTime;
            mDownloadInfo.setNetworkSpeed(networkSpeed);

            //下载进度
            float progress = downloadLength * 1.0f / mDownloadInfo.getTotalLength();
            mDownloadInfo.setProgress(progress);
            long curTime = System.currentTimeMillis();
            System.out.println(mDownloadInfo.getDownloadLength() + "  " + count);

            //每200毫秒刷新一次数据
            if (curTime - lastRefreshUiTime >= 200 || progress == 1.0f) {
                lastRefreshUiTime = System.currentTimeMillis();
            }
        }
    }
}