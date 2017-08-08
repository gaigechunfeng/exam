package cn.gov.baiyin.court.core.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * Created by 005689 on 2017/8/8.
 */
public final class CodeUtil {

    private static final java.lang.String AL = "AES";
    private static final String ENCODING = "UTF-8";
    private static final int KEY_LEN = 128;
    private static final String PWD = "gaigechunfeng";

    private CodeUtil() {
    }

    public static String encrypt(String s) {

        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AL);
            keyGenerator.init(KEY_LEN, new SecureRandom(Base64.decodeBase64(PWD)));

            SecretKey secretKey = keyGenerator.generateKey();
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), AL);
            Cipher cipher = Cipher.getInstance(AL);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            return Base64.encodeBase64String(cipher.doFinal(s.getBytes(ENCODING)));
        } catch (Exception e) {
            throw new RuntimeException("encrypt error{" + s + "}", e);
        }
    }

    public static String decrypt(String s) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AL);
            keyGenerator.init(KEY_LEN, new SecureRandom(Base64.decodeBase64(PWD)));

            SecretKey secretKey = keyGenerator.generateKey();
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), AL);
            Cipher cipher = Cipher.getInstance(AL);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            return new String(cipher.doFinal(Base64.decodeBase64(s)), ENCODING);
        } catch (Exception e) {
            throw new RuntimeException("encrypt error{" + s + "}", e);
        }
    }
}
