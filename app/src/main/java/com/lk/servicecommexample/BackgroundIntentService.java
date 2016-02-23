package com.lk.servicecommexample;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.util.Log;

public class BackgroundIntentService extends IntentService implements IConstants{
    private static final String TAG = BackgroundIntentService.class.getSimpleName();
    private ResultReceiver mResultReceiver;

    public BackgroundIntentService() {
        super("BackgroundIntentService");
    }

    public static void startActionDownloadApk(Context context,
                                              String appName,
                                              String apkUrl,
                                              ResultReceiver resultReceiver) {
        Intent intent = new Intent(context, BackgroundIntentService.class);
        intent.setAction(ACTION_DOWNLOAD_APK);
        intent.putExtra(EXTRA_RESULT_RECEIVER, resultReceiver);
        intent.putExtra(EXTRA_APP_NAME, appName);
        intent.putExtra(EXTRA_APK_URL, apkUrl);
        context.startService(intent);
    }

    public static void startActionDownloadInstallApk(Context context,
                                                     String appName,
                                                     String apkUrl,
                                                     ResultReceiver resultReceiver) {
        Intent intent = new Intent(context, BackgroundIntentService.class);
        intent.setAction(ACTION_INSTALL_POST_DOWNLOAD);
        intent.putExtra(EXTRA_RESULT_RECEIVER, resultReceiver);
        intent.putExtra(EXTRA_APP_NAME, appName);
        intent.putExtra(EXTRA_APK_URL, apkUrl);
        context.startService(intent);
    }


    public static void startActionInstallAPK(Context context,
                                             String appName,
                                             String apkPath,
                                             ResultReceiver resultReceiver) {
        Intent intent = new Intent(context, BackgroundIntentService.class);
        intent.setAction(ACTION_INSTALL_APK);
        intent.putExtra(EXTRA_RESULT_RECEIVER, resultReceiver);
        intent.putExtra(EXTRA_APP_NAME, appName);
        intent.putExtra(EXTRA_APK_PATH, apkPath);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            mResultReceiver = intent.getParcelableExtra(EXTRA_RESULT_RECEIVER);
            Log.d(TAG, "Received action - " + action);
            if (ACTION_DOWNLOAD_APK.equals(action)) {
                final String appName = intent.getStringExtra(EXTRA_APP_NAME);
                final String appUrl = intent.getStringExtra(EXTRA_APK_URL);
                handleActionDownloadApk(appName, appUrl);
            } else if (ACTION_INSTALL_APK.equals(action)) {
                final String appName = intent.getStringExtra(EXTRA_APP_NAME);
                final String apkPath = intent.getStringExtra(EXTRA_APK_PATH);
                handleActionInstallApk(appName, apkPath);
            } else if (ACTION_INSTALL_POST_DOWNLOAD.equals(action)) {
                final String appName = intent.getStringExtra(EXTRA_APP_NAME);
                final String apkUrl = intent.getStringExtra(EXTRA_APK_URL);
                handleActionDownloadInstallApk(appName, apkUrl);
            }
        }
    }

    private int handleActionDownloadInstallApk(String appName, String apkUrl) {
        Log.d(TAG, "Entered inside handleActionDownloadInstallApk");
        final String apkPath = handleActionDownloadApk(appName, apkUrl);
        handleActionInstallApk(appName, apkPath);
        Log.d(TAG, "Exited from handleActionDownloadInstallApk");
        return DOWNLOAD_INSTALL_SUCCESS;
    }

    private String handleActionDownloadApk(String appName, String apkUrl) {
        if (mResultReceiver != null) {
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_APP_NAME, appName);
            mResultReceiver.send(STATE_DOWNLOADING, bundle);
        }
        StateChangeNotifier.Instance.setState(STATE_DOWNLOADING, appName);
        Log.d(TAG, "Entered inside handleActionDownloadApk");
        Log.d(TAG, "Starting download from url : " + apkUrl);
        SystemClock.sleep(10000);
        final String downloadedApkPath = "/mnt/sdcard0/Downloads/" + appName + ".apk";
        Log.d(TAG, "Apk has been downloaded at path : " + downloadedApkPath);
        Log.d(TAG, "Exited from handleActionDownloadApk");
        if (mResultReceiver != null) {
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_APP_NAME, appName);
            mResultReceiver.send(STATE_IDLE, bundle);
        }
        StateChangeNotifier.Instance.setState(STATE_IDLE, appName);
        return downloadedApkPath;
    }

    private int handleActionInstallApk(String appName, String apkPath) {
        if (mResultReceiver != null) {
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_APP_NAME, appName);
            mResultReceiver.send(STATE_INSTALLING, bundle);
        }
        StateChangeNotifier.Instance.setState(STATE_INSTALLING, appName);
        Log.d(TAG, "Entered inside handleActionInstallApk");
        Log.d(TAG, "Installing from path : " + apkPath);
        SystemClock.sleep(5000);
        Log.d(TAG, "Installing completed for  : " + appName);
        Log.d(TAG, "Exited from handleActionInstallApk");
        if (mResultReceiver != null) {
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_APP_NAME, appName);
            mResultReceiver.send(STATE_IDLE, bundle);
        }
        StateChangeNotifier.Instance.setState(STATE_IDLE, appName);
        return INSTALL_SUCCESS;
    }
}
