package jamine.utils;

import org.python.core.PyString;
import org.python.core.PyTuple;

import static org.python.modules.struct.*;

/**
 * Created by sorac on 5/30/2017.
 */
public class Binary {
    private static final int BIG_ENDIAN = 0x00;
    private static final int LITTLE_ENDIAN = 0x01;

    private static int len;

    private static void checkLength(String str, int expect) {
        len = str.length();
        assert len == expect : "Expected " + expect + " bytes, got " + len;
    }

    public static PyTuple readTriad(String str) {
        checkLength(str, 3);
        return unpack("L", "\\x00" + str);
    }

    public static PyString writeTriad(PyTuple value) {
        return pack();
    }
}
