package com.lk.servicecommexample;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by lavakush.v on 30-11-2015.
 */

public class StateChangeNotifier {

    public static final StateChangeNotifier Instance = new StateChangeNotifier();
    private CopyOnWriteArrayList<OnStateChangeListener> mListeners = new CopyOnWriteArrayList<OnStateChangeListener>();
    private int mState = IConstants.STATE_IDLE;
    private String mMessage = IConstants.UNKNOWN;

    private StateChangeNotifier() {
    }

    public void addListener(OnStateChangeListener listener, boolean notifyOnAdd) {
        this.mListeners.add(listener);
        if (notifyOnAdd) {
            listener.onStateChange(mState, mMessage);
        }
    }

    private void notifyState() {
        for (OnStateChangeListener listener : this.mListeners) {
            listener.onStateChange(mState, mMessage);
        }
    }

    public int getState() {
        return mState;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setState(int updaterState, String message) {
        if (mState != updaterState) {
            mState = updaterState;
            mMessage = message;
            notifyState();
        } else {
            mState = updaterState;
            mMessage = message;
        }
    }

    public void removeListener(OnStateChangeListener listener) {
        this.mListeners.remove(listener);
    }

    public interface OnStateChangeListener {
        void onStateChange(int state, String message);
    }

}

