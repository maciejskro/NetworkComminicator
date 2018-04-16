package com.sda.encrypt;

public class CipherFactory {

    private static Cipher cipher;

    public static Cipher create(String algorithm) {

        if ("Caesar" . equals(algorithm)) {
            cipher = new CesarCipher(45);
        } else if ("AES".equals(algorithm)) {
            cipher = new AESCipher("string");
        } else {
            throw new IllegalArgumentException("Unknown algorithm");
        }
        return cipher;
    }
}
