package com.xwtec.common.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @Author taogang
 * @ClassName AESUtil
 * @Date 2019/5/20 14:03
 * @Description To Do
 */
public class AESUtil {

   public static final String KEY = "fpcmspublickey";

   //密钥长度
   private static final int KEY_SIZE = 128;

    /**
     * AES加密字符串
     *
     * @param content
     *            需要被加密的字符串
     * @return 密文
     */
    public static String encrypt(String content) {
        try {
            SecureRandom sr = new SecureRandom();
            Key secureKey = getKey(AESUtil.KEY);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secureKey, sr);
            byte[] bt = cipher.doFinal(content.getBytes());
            return Base64.encodeBase64String(bt);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }  catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密AES加密过的字符串
     *
     * @param content
     *            AES加密过的内容
     * @return 明文
     */
    public static String decrypt(String content) {
        try {
            SecureRandom sr = new SecureRandom();
            Cipher cipher = Cipher.getInstance("AES");
            Key secureKey = getKey(AESUtil.KEY);
            cipher.init(Cipher.DECRYPT_MODE, secureKey, sr);
            byte[] res = Base64.decodeBase64(content);
            res = cipher.doFinal(res);
            return new String(res);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * getKey
     * @param key
     * @return java.security.Key
     * @author taogang
     * @date 2019/5/23 18:27
     * @description 根据传入的字符串获取公钥
     */
    public static Key getKey(String key){
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(key.getBytes());
            generator.init(AESUtil.KEY_SIZE, secureRandom);
            return generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(" 初始化密钥出现异常 ");
        }
    }

    public static void main(String[] args) {
        String content = "110067070132,bde6db78739b0f23763e210b49a4ebe9,2019-05-22 15:13:07";
        System.out.println("加密之前：" + content);

        System.out.println("key = "+Base64.encodeBase64String(AESUtil.getKey(AESUtil.KEY).getEncoded()));

        // 加密
        String encrypt = AESUtil.encrypt(content);
        System.out.println("加密后的内容：" + encrypt);

        // 解密
       String decrypt = AESUtil.decrypt(encrypt);
        System.out.println("解密后的内容：" + decrypt);
    }
}
