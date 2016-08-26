package com.skedgo.android.common.util;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import rx.functions.Action1;

/**
 * A custom {@link Bus} that posts events from any thread and
 * lets subscribers receive them on the main thread.
 */
public class MainThreadBus extends Bus {
  private final Action1<Throwable> errorHandler;

  /**
   * A Handler used to communicate with the main thread.
   */
  private Handler handler = new Handler(Looper.getMainLooper());

  public MainThreadBus(Action1<Throwable> errorHandler) {
    super(ThreadEnforcer.ANY);
    this.errorHandler = errorHandler;
  }

  /**
   * Posts an event and expects to handle it on the main thread.
   *
   * @param event The event that we want to post.
   */
  @Override public final void post(final Object event) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
      // We're on the main thread.
      super.post(event);
    } else {
      // The operation inside run() will be called on the main thread.
      handler.post(new Runnable() {
        @Override public void run() {
          MainThreadBus.super.post(event);
        }
      });
    }
  }

  @Override public void unregister(Object object) {
    try {
      super.unregister(object);
    } catch (IllegalArgumentException e) {
      // See https://github.com/square/otto/issues/127.
      errorHandler.call(e);
    }
  }
}