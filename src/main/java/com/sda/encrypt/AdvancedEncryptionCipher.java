package com.sda.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

public class AdvancedEncryptionCipher {

    private String algorithmName;
    private String keyString;

    public AdvancedEncryptionCipher(String algorithmName, String keyString) {
        this.algorithmName = algorithmName;
        this.keyString = keyString;
    }

    public String encrypt(String input) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(algorithmName);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal = c.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(encVal);
    }

    public String decrypt(String input) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(algorithmName);
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decoratedValue = Base64.getDecoder().decode(input);
        byte[] decValue = c.doFinal(decoratedValue);
        return new String(decValue);
    }


    private byte[] getKeyValue() {
        return this.keyString.getBytes();
    }

    private Key generateKey() throws Exception {
        return new SecretKeySpec(getKeyValue(),algorithmName);
    }
}
