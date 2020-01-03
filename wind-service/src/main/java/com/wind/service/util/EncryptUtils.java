package com.wind.service.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 加密工具类
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2019/11/19 10:03
 **/
@Slf4j
public class EncryptUtils {

    public static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    public static final String MD5 = "md5";
    public static final String AES = "AES";
    public static final String ENCODINGFORMAT = "UTF-8";

    private EncryptUtils(){

    }
    /**
     * MD5加密
     *
     * @param str
     * @return
     */
    public static String encryptByMd5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance(MD5);
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            log.error("md5加密失败" + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 生成秘钥
     *
     * @param password
     * @param length
     * @return
     * @throws Exception
     */
    public static String generateDesKey(String password, Integer length) throws NoSuchAlgorithmException {
        //实例化
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(password.getBytes());
        kgen.init(length, secureRandom);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        return Hex.encodeHexString(enCodeFormat);
    }

    public static byte[] processAES(String sSrc, String sKey, int type, String ivParameter) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        byte[] enCodeFormat = sKey.getBytes(ENCODINGFORMAT);
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
        SecretKeySpec skeySpec = new SecretKeySpec(enCodeFormat, AES);
        cipher.init(type, skeySpec, iv);
        byte[] sSrcBytes = null;
        if (type == Cipher.DECRYPT_MODE) {
            sSrcBytes = Hex.decodeHex(sSrc);
        } else {
            sSrcBytes = sSrc.getBytes(ENCODINGFORMAT);
        }
        byte[] encrypted = cipher.doFinal(sSrcBytes);
        return encrypted;
    }

    /**
     * 加密
     *
     * @param sSrc
     * @param apiSecret
     * @return
     * @throws Exception
     */
    public static String encrypt(String sSrc, String apiSecret) throws Exception {
        String sKey = apiSecret.substring(0, 16);
        String ivParameter = apiSecret.substring(16, 32);
        byte[] bytes = processAES(sSrc, sKey, Cipher.ENCRYPT_MODE, ivParameter);
        return Hex.encodeHexString(bytes);
    }

    /**
     * 解密
     *
     * @param sSrc
     * @param apiSecret
     * @return
     * @throws Exception
     */
    public static String decrypt(String sSrc, String apiSecret) throws Exception {
        String sKey = apiSecret.substring(0, 16);
        String ivParameter = apiSecret.substring(16, 32);
        byte[] bytes = processAES(sSrc, sKey, Cipher.DECRYPT_MODE, ivParameter);
        return new String(bytes, ENCODINGFORMAT);
    }


}
