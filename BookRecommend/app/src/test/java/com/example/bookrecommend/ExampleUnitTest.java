package com.example.bookrecommend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {
    @Test
    public void contextTest() {
        int[] a1 = {1, 2, 3, 4};
        int[] a2 = {5, 6, 7, 8};
        System.out.println(Arrays.toString(a2));
        System.arraycopy(a1, 0, a2, 0, 4);
        System.out.println(Arrays.toString(a2));
    }
}