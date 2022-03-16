package com.example.bookrecommend;

import org.junit.Test;

import java.io.File;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        File file = new File("/storage/emulated/0/Android/data/com.example.bookrecommend/files");
        File file1 = new File(file, "Android/data/com.example.bookrecommend/files/Pictures");

        System.out.println(file1.getAbsolutePath());
    }
}