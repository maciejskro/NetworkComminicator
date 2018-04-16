package com.sda.encrypt;

public class AESCipher implements Cipher {

    private String algorithmName;
    private AdvancedEncryptionCipher aes;

    public AESCipher(String key) {
        if (key != null) {
            if (key.length() == 16) {
                this.aes = new AdvancedEncryptionCipher("AES", key);
            } else if (key.length() == 24) {
                this.aes = new AdvancedEncryptionCipher("AES",key);
            } else
                this.aes = new AdvancedEncryptionCipher("AES", key.substring(0,16));
        } else {
            throw  new IllegalArgumentException("Klucz szyfrowania nie może być pusty");
        }
    }

    @Override
    public String encrypt(String input)  {
        try {
            return aes.encrypt(input);
        } catch (Exception e) {
            throw new IllegalArgumentException("nieprawidłowy argunent");
        }
    }

    @Override
    public String decrypt(String input) {
        try{
            return aes.decrypt(input);
        } catch (Exception e) {
            throw new IllegalArgumentException("Nieprawidłowy argument");
        }
    }

    public Integer getKey() {
        return 0;
    }
}
