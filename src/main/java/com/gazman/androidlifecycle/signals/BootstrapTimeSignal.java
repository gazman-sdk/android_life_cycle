// =================================================================================================
//	Life Cycle Framework for native android
//	Copyright 2014 Ilya Gazman. All Rights Reserved.
//
//	This is not free software. You can redistribute and/or modify it
//	in accordance with the terms of the accompanying license agreement.
//  https://github.com/Ilya-Gazman/android_life_cycle/blob/master/LICENSE.md
// =================================================================================================
package com.gazman.androidlifecycle.signals;

import com.gazman.androidlifecycle.task.Scheduler;

/**
 * Created by Gazman on 3/2/2015.
 */
public interface BootstrapTimeSignal {

    void onBootstrap(Scheduler scheduler);
}
