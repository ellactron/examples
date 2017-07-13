package com.ellactron.examples.signinsignup;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.facebook.internal.Utility;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by ji.wang on 2017-07-11.
 */
@RunWith(AndroidJUnit4.class)
public class IntegrationTest {
    @Test
    public void getFacebookApplicationId() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        final String appId = Utility.getMetadataApplicationId(appContext);
        assertNotNull(appId);
    }
}
