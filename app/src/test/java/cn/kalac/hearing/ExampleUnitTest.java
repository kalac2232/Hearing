package cn.kalac.hearing;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void arrLength() {
        int[][] arr = new int[4][7];
        System.out.println(arr[1].length);
    }

    @Test
    public void arrClone() {
        int[] arr = new int[4];
        arr[0] = 5;
        int[] clone = arr.clone();
        clone[1] = 6;
        System.out.println(arr[1]);
        System.out.println(clone[1]);
    }
}