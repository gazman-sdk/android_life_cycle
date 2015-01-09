// =================================================================================================
//	Life Cycle Framework for native android
//	Copyright 2014 Ilya Gazman. All Rights Reserved.
//
//	This is not free software. You can redistribute and/or modify it
//	in accordance with the terms of the accompanying license agreement.
//  https://github.com/Ilya-Gazman/android_life_cycle/blob/master/LICENSE.md
// =================================================================================================
package com.gazman_sdk.androidlifecycle;

import android.app.Application;

/**
 * Created by user on 04-Dec-14.
 */
public abstract class LifeCycleApplication extends Application{
    private static LifeCycleApplication instance;

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getRegistrar().initialize();
            }
        }, "Registration Thread").start();
    }

    protected abstract Registrar getRegistrar();

    public static LifeCycleApplication getInstance() {
        return instance;
    }
}
