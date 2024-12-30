package com.suricatedevlab.jsdr.rtl;

import java.util.Arrays;

final class ByteUtils {
    private ByteUtils() {
        throw new IllegalStateException("Can not call new");
    }

    public static byte[] trim(byte[] bytes) {
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == 0) {
            --i;
        }
        return Arrays.copyOf(bytes, i + 1);
    }

    public static int[] trim(int[] intArray) {
        int i = intArray.length - 1;
        while (i >= 0 && intArray[i] == 0) {
            --i;
        }
        return Arrays.copyOf(intArray, i + 1);
    }
}
