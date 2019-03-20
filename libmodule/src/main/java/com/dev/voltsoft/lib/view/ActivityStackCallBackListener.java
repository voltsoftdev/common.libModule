package com.dev.voltsoft.lib.view;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public abstract class ActivityStackCallBackListener implements Application.ActivityLifecycleCallbacks {

    private Class<?> aClass;

    public ActivityStackCallBackListener(Activity targetActivity) {
        aClass = targetActivity.getClass();
    }

    @Override
    public final void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (aClass != null && aClass.equals(activity.getClass())) {

            onTargetCreated(activity, savedInstanceState);
        }
    }

    protected void onTargetCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public final void onActivityStarted(Activity activity) {
        if (aClass != null && aClass.equals(activity.getClass())) {

        }
    }

    @Override
    public final void onActivityResumed(Activity activity) {
        if (aClass != null && aClass.equals(activity.getClass())) {

            onTargetResumed(activity);
        }
    }

    protected void onTargetResumed(Activity activity) {

    }

    @Override
    public final void onActivityPaused(Activity activity) {
        if (aClass != null && aClass.equals(activity.getClass())) {

            onTargetPaused(activity);
        }
    }

    protected void onTargetPaused(Activity activity) {

    }

    @Override
    public final void onActivityStopped(Activity activity) {
        if (aClass != null && aClass.equals(activity.getClass())) {

            onTargetStopped(activity);
        }
    }

    protected void onTargetStopped(Activity activity) {

    }

    @Override
    public final void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        if (aClass != null && aClass.equals(activity.getClass())) {

        }
    }

    @Override
    public final void onActivityDestroyed(Activity activity) {
        if (aClass != null && aClass.equals(activity.getClass())) {

        }
    }
}
