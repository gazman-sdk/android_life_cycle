// =================================================================================================
//	Life Cycle Framework for native android
//	Copyright 2014 Ilya Gazman. All Rights Reserved.
//
//	This is not free software. You can redistribute and/or modify it
//	in accordance with the terms of the accompanying license agreement.
//  https://github.com/Ilya-Gazman/android_life_cycle/blob/master/LICENSE.md
// =================================================================================================
package com.gazman.androidlifecycle.signal;

import android.util.Log;

import com.gazman.androidlifecycle.Factory;
import com.gazman.androidlifecycle.utils.UnhandledExceptionHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;

/**
 * Created by Gazman on 2/24/2015.
 */
public final class Signal<T> {

    public final T dispatcher;
    LinkedList<T> listeners = new LinkedList<>();
    LinkedList<Class<? extends T>> classListeners = new LinkedList<>();
    LinkedList<T> oneTimeListeners = new LinkedList<>();
    LinkedList<Class<T>> oneTimeClassListeners = new LinkedList<>();

    Signal(Class<T> type) {
        dispatcher = (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, invocationHandler);
    }

    public void addListener(T listener) {
        listeners.add(listener);
    }

    public void addListener(Class<? extends T> listener) {
        classListeners.add(listener);
    }

    public void addListenerOnce(T listener){
        oneTimeListeners.add(listener);
    }

    public void addListenerOnce(Class<T> listener){
        oneTimeClassListeners.add(listener);
    }

    public void removeListener(T listener) {
        listeners.remove(listener);
        oneTimeListeners.remove(listener);
    }

    public void removeListener(Class<? extends T> listener) {
        classListeners.remove(listener);
        oneTimeClassListeners.remove(listener);
    }

    private InvocationHandler invocationHandler = new InvocationHandler() {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            for (Object listener : listeners) {
                invoke(method, args, listener);
            }
            for (Object listener : oneTimeListeners) {
                invoke(method, args, listener);
            }
            for (Class<? extends T> classListener : classListeners) {
                T listener = Factory.inject(classListener);
                invoke(method, args, listener);
            }
            for (Class<T> classListener : oneTimeClassListeners) {
                T listener = Factory.inject(classListener);
                invoke(method, args, listener);
            }

            oneTimeListeners.clear();
            oneTimeClassListeners.clear();

            return null;
        }

        private void invoke(Method method, Object[] args, Object listener) {
            try {
                method.invoke(listener, args);
            } catch (Throwable e) {
                e.printStackTrace();
                if (UnhandledExceptionHandler.callback == null) {
                    Log.e("LifeCycle", "Unhandled Exception", e);
                    throw new Error("Unhandled Exception, consider providing UnhandledExceptionHandler.callback");
                } else {
                    UnhandledExceptionHandler.callback.onApplicationError(e);
                }
            }
        }
    };


}
