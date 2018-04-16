package com.sda.encrypt;

public class CipherFactory {

    public static Cipher create(String algorithm) {

        if ("Caesar" . equals(algorithm)) {
            return new CesarCipher(45);
        } else if ("AES".equals(algorithm)) {
            return new AESCipher("string");
        } else {
            throw new IllegalArgumentException("Unknown algorithm");
        }
    }
}
