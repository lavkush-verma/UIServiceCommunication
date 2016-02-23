package com.lk.servicecommexample;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements IConstants, StateChangeNotifier.OnStateChangeListener {

    private static final boolean USE_RESULT_RECEIVER = false;
    private TextView mStatus;
    private ResultReceiver mResultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStatus = (TextView) findViewById(R.id.txtStatus);
        if (USE_RESULT_RECEIVER) {
            mResultReceiver = new ResultReceiver(new Handler()) {
                @Override
                protected void onReceiveResult(int resultCode, Bundle resultData) {
                    String appName = null;
                    if (resultData != null) {
                        appName = resultData.getString(EXTRA_APP_NAME);
                    }
                    updateStatus(resultCode, appName);
                    super.onReceiveResult(resultCode, resultData);
                }
            };
        }
    }

    private void updateStatus(final int resultCode, final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (STATE_DOWNLOADING == resultCode) {
                    mStatus.setText("Downloading " + message + "...");
                } else if (STATE_INSTALLING == resultCode) {
                    mStatus.setText("Installing " + message + "...");
                } else {
                    mStatus.setText(null);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!USE_RESULT_RECEIVER) {
            StateChangeNotifier.Instance.addListener(this, true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!USE_RESULT_RECEIVER) {
            StateChangeNotifier.Instance.removeListener(this);
        }
    }

    public void downloadAPK(View view) {
        final String appName = "Music";
        final String appUrl = "www.google.com/apps/Music.apk";
        BackgroundIntentService.startActionDownloadApk(this, appName, appUrl, mResultReceiver);
    }

    public void installAPK(View view) {
        final String appName = "Camera";
        final String apkPath = "/mnt/sdcard0/Downloads/apps/Camera.apk";
        BackgroundIntentService.startActionInstallAPK(this, appName, apkPath, mResultReceiver);
    }

    public void downloadInstallAPK(View view) {
        final String appName = "Photos";
        final String apkUrl = "www.google.com/apps/Photos.apk";
        BackgroundIntentService.startActionDownloadInstallApk(this, appName, apkUrl, mResultReceiver);
    }

    @Override
    public void onStateChange(int state, String message) {
        updateStatus(state, message);
    }
}
