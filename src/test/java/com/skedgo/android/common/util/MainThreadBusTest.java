package com.skedgo.android.common.util;

import com.skedgo.android.bux.BuildConfig;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import rx.functions.Action1;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MainThreadBusTest {
  @Mock Action1<Throwable> errorHandler;
  private Bus bus;

  @Before public void before() {
    MockitoAnnotations.initMocks(this);
    bus = new MainThreadBus(errorHandler);
  }

  @Test public void catchIllegalArgumentException() {
    bus.unregister(new Object() {
      @Subscribe public void onEvent(String event) {}
    });
    verify(errorHandler).call(any(IllegalArgumentException.class));
  }
}