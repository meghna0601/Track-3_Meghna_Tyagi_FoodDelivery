package com.user.util;

import com.user.exception.NoSuchAlgorithmException;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class PasswordEncryptionDecryptionUtil {

    // 16- char key for AES
    private static final String SECRET_KEY = "1234567890123456";

    //Encrypt
    public static String encrypt(String password)  {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(password.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new NoSuchAlgorithmException("InCorrect Algorithm Found to encrypt password");
        }
    }

    // Decrypt
    public static  String decrypt(String encryptedPassword)  {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
            return new String(decrypted);
        } catch (Exception e) {
            throw new NoSuchAlgorithmException("InCorrect Algorithm Found to decrypt password");
        }
    }
}
