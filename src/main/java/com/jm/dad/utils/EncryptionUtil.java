package com.jm.dad.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;

public class EncryptionUtil {
	
	private final String alg = "AES/CBC/PKCS5Padding";
    private final String key = "discussanddecideprovidedbyjmhaes";
    private final String iv = "0j1m2d3a4d5a6789";
    
    public String encrypt(String str) {
        try {
            Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
            byte[] encrypted = cipher.doFinal(str.getBytes("UTF-8"));
            return Base64.getUrlEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String decrypt(String str) {
        try {
            Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);
            byte[] decodedBytes = Base64.getUrlDecoder().decode(str);
            byte[] decrypted = cipher.doFinal(decodedBytes);
            return new String(decrypted, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
