package com.tuhanbao.base.util.objutil;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {
    public static final String encode(byte[] data) {
        return new String(Base64.encodeBase64(data));
    }

    public static byte[] decode(String msg) {
        return decode(ByteUtil.string2Bytes(msg));
    }

    public static byte[] decode(byte[] data) {
        return Base64.decodeBase64(data);
    }
}
