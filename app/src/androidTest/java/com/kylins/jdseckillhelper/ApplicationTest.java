package com.kylins.jdseckillhelper;

import android.app.Application;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.test.ApplicationTestCase;
import  android.support.test.uiautomator.UiDevice;

import java.io.IOException;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    UiDevice device = null;

    private boolean isRun = false;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {

        super.setUp();
//        device = UiDevice.getInstance(getInstrumentation());
        device.pressHome();
        isRun = true;
//        device.wait(5000);

    }

    public void test() throws Exception {

        UiObject object = device.findObject(new UiSelector().text("微信"));
        UiScrollable scrollable = new UiScrollable(new UiSelector().descriptionContains("主屏幕"));
        scrollable.setAsHorizontalList();


        while (isRun) {
            try {
                if (!object.click()) {
                    scrollable.scrollForward();
                } else {
                    break;
                }
            } catch (Exception ex) {
                if (!scrollable.scrollForward())
                    break;
            }
        }

        object = device.findObject(new UiSelector().text("微信团队"));
        object.clickAndWaitForNewWindow();
        while (isRun) {
            device.pressBack();
            device.waitForIdle();
            object.clickAndWaitForNewWindow();
        }
    }
}