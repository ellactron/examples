package com.ellactron.datapersistence;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.core.deps.guava.primitives.Primitives;
import android.support.test.runner.AndroidJUnit4;

import com.ellactron.storage.PrivateStorage;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.ellactron.datapersistence", appContext.getPackageName());
    }

    @Test
    public void testReadWritePrivateFile() throws IOException {
        String testData = "ABCDEFGabcdefg";
        String appendData = "123456789";

        Context appContext = InstrumentationRegistry.getTargetContext();
        PrivateStorage storage = new PrivateStorage(appContext, "testPrivateStorage");

        storage.save(testData.getBytes("UTF-8"));
        byte[] data = storage.read();
        Assert.assertEquals(testData, new String(data, "UTF-8"));

        storage.append(appendData.getBytes("UTF-8"));
        data = storage.read();
        Assert.assertEquals(testData + appendData, new String(data, "UTF-8"));

        data = storage.read(appendData.length(), testData.length());
        Assert.assertEquals(appendData, new String(data, "UTF-8"));

        storage.delete();
        Assert.assertTrue(!storage.isExisting());
    }
}
