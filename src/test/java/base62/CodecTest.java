package base62;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;

public class CodecTest {

    @Test
    public void testToBase62() throws Exception {
        checkBase62("A", 0);
        checkBase62("C", 2);
        checkBase62("9", 61);
        checkBase62("Bm", 100);
        checkBase62("hoj", 0x01, 0xf9, 0x57);
    }

    private void checkBase62(String expected, int... digits) {
        byte[] bytes = new byte[32];
        for (int i = 0; i < digits.length; i++) {
            bytes[31 - i] = (byte) digits[digits.length - 1 - i];
        }
        assertEquals(expected, Codec.toBase62(bytes));
    }

    @Test
    public void testToByteArray() {
        checkByteArray("A", 0);
        checkByteArray("C", 2);
        checkByteArray("9", 61);
        checkByteArray("Bm", 100);
        checkByteArray("Ahoj", 0x01, 0xf9, 0x57);
    }

    private void checkByteArray(String encoded, int... expected) {

        int[] expectedExtended = new int[32];
        for (int i = 0; i < 32; i++) {
            int is = i < expected.length ? expected[expected.length - 1 - i] : 0;
            expectedExtended[31 - i] = is;
        }

        byte[] actual = Codec.toByteArray(encoded);

        for (int i = 0; i < 32; i++) {
            assertEquals(expectedExtended[i], actual[i] & 0xFF);
        }
    }


    @Test
    public void testThereAndBack() {
        long timeAcc = 0;
        for (int c = 0; c < 10000; c++) {
            byte[] input = new byte[32];
            ThreadLocalRandom.current().nextBytes(input);
            timeAcc -= System.currentTimeMillis();
            String encoded = Codec.toBase62(input);
            Assert.assertTrue(encoded.length() <= 43);
            byte[] result = Codec.toByteArray(encoded);

            timeAcc += System.currentTimeMillis();
            Assert.assertArrayEquals(input, result);
            System.out.println("OK: " + Arrays.toString(input));
        }
        System.out.printf("Average time for toBase62+toByteArray: %d us", timeAcc / 10);
    }
}