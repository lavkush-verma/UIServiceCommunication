package com.lk.servicecommexample;

/**
 * Created by lavakush.v on 23-02-2016.
 */
public interface IConstants {
    String PACKAGE_NAME = "com.lk.servicecommexample";
    String ACTION_DOWNLOAD_APK = PACKAGE_NAME + ".action.DOWNLOAD_APK";
    String ACTION_INSTALL_APK = PACKAGE_NAME + ".action.INSTALL_APK";
    String ACTION_INSTALL_POST_DOWNLOAD = PACKAGE_NAME + ".INSTALL_POST_DOWNLOAD";
    String EXTRA_APK_URL = PACKAGE_NAME + ".extra.APK_URL";
    String EXTRA_APK_PATH = PACKAGE_NAME + ".extra.APK_PATH";
    String EXTRA_APP_NAME = PACKAGE_NAME + ".extra.APP_NAME";
    String EXTRA_RESULT_RECEIVER = PACKAGE_NAME + ".extra.RESULT_RECEIVER";
    int INSTALL_SUCCESS = 100;
    int DOWNLOAD_INSTALL_SUCCESS = 200;
    int STATE_IDLE = 0;
    int STATE_DOWNLOADING = 1;
    int STATE_INSTALLING = 2;
    String UNKNOWN = "UNKNOWN";
}
