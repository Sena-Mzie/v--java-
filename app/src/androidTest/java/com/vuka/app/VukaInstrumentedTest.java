package com.vuka.app;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented tests for VUKA — run on Android device/emulator.
 */
@RunWith(AndroidJUnit4.class)
public class VukaInstrumentedTest {

    @Test
    public void testApplicationContext() {
        Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.vuka.app", ctx.getPackageName());
    }
}
