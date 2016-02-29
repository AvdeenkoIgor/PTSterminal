package com.example.root.ptsterminal;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//import java.util.HashMap;
//
//import android.util.Log;

public class myLib {
	
	// example: round(yourNumber, 3, BigDecimal.ROUND_HALF_UP);
	public static double round(double unrounded, int precision, int roundingMode)
	{
	    BigDecimal bd = new BigDecimal(unrounded);
	    BigDecimal rounded = bd.setScale(precision, roundingMode);
	    return rounded.doubleValue();
	}

	public static int power(int a, int b){
        int result = 1;
        for(int i=b; i>0; i--){
               result *= a;
        }
        return result;
	}
	
	public static final String md5(byte[] data, int length) {
	     try {
	         // Create MD5 Hash
	         MessageDigest digest = MessageDigest
	                 .getInstance("MD5");
	         digest.update(data, 0, length);
	         byte messageDigest[] = digest.digest();
	  
	         // Create Hex String
	         StringBuffer hexString = new StringBuffer();
	         for (int i = 0; i < messageDigest.length; i++) {
	             String h = Integer.toHexString(0xFF & messageDigest[i]);
	             while (h.length() < 2)
	                 h = "0" + h;
	             hexString.append(h);
	         }
	         return hexString.toString().toUpperCase();
	  
	     } catch (NoSuchAlgorithmException e) {
	         e.printStackTrace();
	     }
	     return "";
	}
	
	public static String hashPassword(String password) {
		String hashword = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(password.getBytes());
			BigInteger hash = new BigInteger(1, md5.digest());
			hashword = hash.toString(16);
		} catch (NoSuchAlgorithmException nsae) {
			// ignore
		}
		if (hashword.length() < 32) {
			char[] nl = {0x30};
			String str = new String(nl, 0, 32-hashword.length());
			return str.concat(hashword);
		}
		else {
			return hashword;
		}
	}	

	public static final String byteArrayToHexString(byte[] b, int size) {
		StringBuffer sb = new StringBuffer(b.length * 2);
		for (int i = 0; i < size; i++) {
			int v = b[i] & 0xff;
			if (v < 16) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString().toUpperCase();
	}

	public static int hexStringToByteArray(String s, byte[] buff, int offset) {
		String str = null;
		int len = s.length();
		if ((len % 2) == 1) {
			str = "0" + s;
			len++;
		}
		else {
			str = s;
		}
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4)
	                             + Character.digit(str.charAt(i+1), 16));
	    }
		System.arraycopy(data, 0, buff, offset, len / 2);
		return len / 2;
	}

	public static void testFunc(byte[] data, int offset) {
//  	HashMap<String, Integer> myMap = new HashMap<String, Integer>();
//  	myMap.put("Строка", 12345);
//  	int y = myMap.get("Строка");
//  	byte[] buff = new byte[2*1024];
		byte[] buff = data;
		char hexData[] = {
			0xAB, 0xCD
		};
		for (int i = 0; i < 2; i++) {
			buff[i] = (byte) hexData[i];
		}

		System.arraycopy(buff, 0, data, 0, 2);
	}

    /**
     * Convert the byte array to an int.
     *
     * @param b The byte array
     * @return The integer
     */
    public static int byteArrayToInt(byte[] b) {
        return byteArrayToInt(b, 0);
    }

    /**
     * Convert the byte array to an int starting from the given offset.
     *
     * @param b The byte array
     * @param offset The array offset
     * @return The integer
     */
    public static int byteArrayToBool(byte[] b, int offset) {
        int value = 0;
        value += (b[offset] & 0x000000FF);
        return value;
    }

    public static int byteArrayToInt(byte[] b, int offset) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i + offset] & 0x000000FF) << shift;
        }
        return value;
    }

    public static boolean memcmp(byte[] a, byte[] b, int length) {
        for (int i = 0; i < length; i++) {
            if (a[i] != b[i]) {
            	return false;
			}
        }
        return true;
    }

    public static boolean memcmpOffset(byte[] a, int offset_a, byte[] b, int offset_b, int length) {
        for (int i = 0; i < length; i++) {
            if (a[offset_a + i] != b[offset_b + i]) {
            	return false;
			}
        }
        return true;
    }

    public static int getIntFromByteArray(byte[] a, int offset, int size) {
    	int result = 0;
    	result += (char)a[offset] & 0xFF;
        for (int i = 1; i < size; i++) {
        	result += ((char)a[i+offset] & 0xFF) * power(256, i);
        }
    	return result;
    }

    public static int getIntFromByteArrayBE(byte[] a, int offset, int size) {
        byte[] b = new byte[4];
        System.arraycopy(a, offset, b, 0, 4);
        return ByteBuffer.wrap(b).getInt();
    }

    public static long getUintFromByteArrayBE(byte[] a, int offset, int size) {
        byte[] b = new byte[8];
        System.arraycopy(a, offset, b, 4, 4);
        return ByteBuffer.wrap(b).getLong();
    }

    public static long getLongFromByteArray(byte[] a, int offset, int size) {
    	long result = 0;
    	result += (char)a[offset] & 0xFF;
        for (int i = 1; i < size; i++) {
        	result += ((char)a[i+offset] & 0xFF) * i * 256;
        }
    	return result;
    }

    public static double getDoubleFromByteArray(byte[] a, int offset, int size) {
    	double result = 0;
    	result += (char)a[offset] & 0xFF;
        for (int i = 1; i < size; i++) {
        	result += ((char)a[i+offset] & 0xFF) << i * 8;
        }
    	return result;
    }

    public static double getDoubleFromByteArrayBE(byte[] bytes, int offset) {
        byte[] b = new byte[8];
        System.arraycopy(bytes, offset, b, 0, 8);
        return ByteBuffer.wrap(b).getDouble();
    }

    public static final byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)value,
                (byte)(value >>> 8),
                (byte)(value >>> 16),
                (byte)(value >>> 24)
        };
    }

    public static final byte[] intToByteArrayBE(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value
        };
    }

    public static final byte[] longToByteArray(long value) {
        return new byte[] {
                (byte)value,
        		(byte)(value >>> 8),
                (byte)(value >>> 16),
                (byte)(value >>> 24),
                (byte)(value >>> 32),
                (byte)(value >>> 48),
                (byte)(value >>> 40),
                (byte)(value >>> 56)
        };
    }
    
    public static final byte[] longToByteArrayBE(long value) {
        return new byte[] {
                (byte)(value >>> 56),
                (byte)(value >>> 40),
                (byte)(value >>> 48),
                (byte)(value >>> 32),
                (byte)(value >>> 24),
                (byte)(value >>> 16),
        		(byte)(value >>> 8),
                (byte)value
        };
    }
    
    public static final byte[] uintToByteArrayBE(long value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
        		(byte)(value >>> 8),
                (byte)value
        };
    }
    
    public static byte[] doubleToByteArray(double d) {
        long l = Double.doubleToRawLongBits(d);
        return new byte[] {
            (byte)((l >> 56) & 0xff),
            (byte)((l >> 48) & 0xff),
            (byte)((l >> 40) & 0xff),
            (byte)((l >> 32) & 0xff),
            (byte)((l >> 24) & 0xff),
            (byte)((l >> 16) & 0xff),
            (byte)((l >> 8) & 0xff),
            (byte)((l >> 0) & 0xff),
        };
    }
    
    public static byte[] doubleToByteArrayAlternate(double value) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(value);
        return bytes;
    }

    int littleToBig(int i)
    {
        int b0,b1,b2,b3;

        b0 = (i&0xff)>>0;
        b1 = (i&0xff00)>>8;
        b2 = (i&0xff0000)>>16;
        b3 = (i&0xff000000)>>24;

        return ((b0<<24)|(b1<<16)|(b2<<8)|(b3<<0));
    }
}
