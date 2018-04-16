package com.sda.encrypt;

public interface Cipher {

    String encrypt (String input);
    String decrypt (String input);
    Integer getKey();
}
