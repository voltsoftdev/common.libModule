package com.dev.voltsoft.lib.view;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class SimpleActivityCallBack<T extends Activity> implements Application.ActivityLifecycleCallbacks {

    private final Class<T> TARGET_ACTIVITY_CLASS;

    public SimpleActivityCallBack(Class<T> aClass) {
        TARGET_ACTIVITY_CLASS = aClass;
    }

    @Override
    public final void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (isTargetActivity(activity)) {

            onTargetActivityCreated(activity, savedInstanceState);
        }
    }

    protected void onTargetActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public final void onActivityStarted(Activity activity) {
        if (isTargetActivity(activity)) {

        }
    }

    protected void onTargetActivityStarted(Activity activity) {

    }

    @Override
    public final void onActivityResumed(Activity activity) {
        if (isTargetActivity(activity)) {

        }
    }

    protected void onTargetActivityResumed(Activity activity) {

    }

    @Override
    public final void onActivityPaused(Activity activity) {
        if (isTargetActivity(activity)) {

        }
    }

    protected void onTargetActivityPaused(Activity activity) {

    }

    @Override
    public final void onActivityStopped(Activity activity) {
        if (isTargetActivity(activity)) {

            onTargetActivityStopped(activity);
        }
    }

    protected void onTargetActivityStopped(Activity activity) {

    }

    @Override
    public final void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        if (isTargetActivity(activity)) {

            onTargetActivityInstanceState(activity, outState);
        }
    }

    protected void onTargetActivityInstanceState(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public final void onActivityDestroyed(Activity activity) {
        if (isTargetActivity(activity)) {

            onTargetActivityDestroyed(activity);
        }
    }

    protected void onTargetActivityDestroyed(Activity activity) {

    }

    private boolean isTargetActivity(Activity activity) {
        return (activity != null && TARGET_ACTIVITY_CLASS.equals(activity.getClass()));
    }
}
