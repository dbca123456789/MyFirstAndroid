package com.example.myfirstandroid.Utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Encoder {
    public static String encode(String ss) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        // MessageDigest 类为应用程序提供信息摘要算法的功能，如 MD5 或 SHA 算法。
        // 信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。

        byte[] hash = MessageDigest.getInstance("MD5").digest(
                ss.getBytes("UTF-8"));
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b: hash){
            // oHexString传的参数应该是int类型32位(二进制)，此处传的是byte类型8位，所以前面需要补24个0
            // b & 0xff 就是把前面24个0去掉只要后8个(十六进制中两位数字表示)。
            if ((b & 0xFF) < 0x10){
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

}
