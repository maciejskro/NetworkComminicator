package com.sda.encrypt;

public class AESCipher implements Cipher {

    private String key;

    public AESCipher(String key) {
        if (key != null) {
            this.key = key;
        } else {
            throw  new IllegalArgumentException("Klucz szyfrowania nie może być pusty");
        }
    }
    public String encrypt(String input) {
        return null;
    }

    public String decrypt(String input) {
        return null;
    }

    public Integer getKey() {
        return 0;
    }
}
