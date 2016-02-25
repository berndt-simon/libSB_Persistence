/* 
 * The MIT License
 *
 * Copyright 2015 Simon Berndt.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package libSB.byteTools;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Simon Berndt
 */
final public class StandardByteConverters {
    
    private StandardByteConverters() {
    }

    public static byte[] byteToBytes(Byte b) {
        if (b == null) {
	    throw new IllegalArgumentException();
	}
	return new byte[] {b};
    }

    public static Byte byteFromBytes(byte[] b) {
	if (b == null || b.length != Byte.BYTES) {
	    throw new IllegalArgumentException();
	}
	return b[0];
    }

    public static byte[] shortToBytes(Short s) {
	if (s == null) {
	    throw new IllegalArgumentException();
	}
	final ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES);
	buffer.putShort(0, s);
	return buffer.array();
    }

    public static Short shortFromBytes(byte[] b) {
	if (b == null || b.length != Short.BYTES) {
	    throw new IllegalArgumentException();
	}
	final ByteBuffer buffer = ByteBuffer.wrap(b);
	return buffer.getShort(0);
    }

    public static byte[] intToBytes(Integer i) {
	if (i == null) {
	    throw new IllegalArgumentException();
	}
	final ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
	buffer.putInt(0, i);
	return buffer.array();
    }

    public static Integer intFromBytes(byte[] b) {
	if (b == null || b.length != Integer.BYTES) {
	    throw new IllegalArgumentException();
	}
	final ByteBuffer buffer = ByteBuffer.wrap(b);
	return buffer.getInt(0);
    }

    public static byte[] longToBytes(Long l) {
	if (l == null) {
	    throw new IllegalArgumentException();
	}
	final ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
	buffer.putLong(0, l);
	return buffer.array();
    }

    public static Long longFromBytes(byte[] b) {
	if (b == null || b.length != Long.BYTES) {
	    throw new IllegalArgumentException();
	}
	final ByteBuffer buffer = ByteBuffer.wrap(b);
	return buffer.getLong(0);
    }

    public static byte[] floatToBytes(Float f) {
	if (f == null) {
	    throw new IllegalArgumentException();
	}
	final ByteBuffer buffer = ByteBuffer.allocate(Float.BYTES);
	buffer.putFloat(0, f);
	return buffer.array();
    }

    public static Float floatFromBytes(byte[] b) {
	if (b == null || b.length != Float.BYTES) {
	    throw new IllegalArgumentException();
	}
	final ByteBuffer buffer = ByteBuffer.wrap(b);
	return buffer.getFloat(0);
    }

    public static byte[] doubleToBytes(Double d) {
	if (d == null) {
	    throw new IllegalArgumentException();
	}
	final ByteBuffer buffer = ByteBuffer.allocate(Double.BYTES);
	buffer.putDouble(0, d);
	return buffer.array();
    }

    public static Double doubleFromBytes(byte[] b) {
	if (b == null || b.length != Double.BYTES) {
	    throw new IllegalArgumentException();
	}
	final ByteBuffer buffer = ByteBuffer.wrap(b);
	return buffer.getDouble(0);
    }

    public static byte[] booleanToBytes(Boolean b) {
	if (b == null) {
	    throw new IllegalArgumentException();
	}
	if (b) {
	    return new byte[] {(byte) 1};
	} else {
	    return new byte[] {(byte) 0};
	}
    }

    public static Boolean booleanFromBytes(byte[] b) {
	if (b == null || b.length != 1) {
	    throw new IllegalArgumentException();
	}
	return (b[0] > 0);
    }

    public static byte[] charToBytes(Character c) {
	if (c == null) {
	    throw new IllegalArgumentException();
	}
	final ByteBuffer buffer = ByteBuffer.allocate(Character.BYTES);
	buffer.putChar(0, c);
	return buffer.array();
    }

    public static Character charFromBytes(byte[] b) {
	if (b == null || b.length != Character.BYTES) {
	    throw new IllegalArgumentException();
	}
	final ByteBuffer buffer = ByteBuffer.wrap(b);
	return buffer.getChar(0);
    }

    public static byte[] stringToBytes(String s) {
	if (s == null) {
	    throw new IllegalArgumentException();
	}
	return s.getBytes(StandardCharsets.UTF_8);
    }

    public static String stringFromBytes(byte[] b) {
	if (b == null) {
	    throw new IllegalArgumentException();
	}
	return new String(b, StandardCharsets.UTF_8);
    }
}
