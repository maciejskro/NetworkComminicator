package com.sda.encrypt;

public class CesarCipher implements Cipher {

    private  Integer key;

    public String encrypt(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            result.append( (char) (((int) c) + getKey()) % Character.MAX_VALUE   );
        }
        return result.toString();
    }

    public String decrypt(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            result.append((char) ((int) c +Character.MAX_VALUE  - getKey() ) %Character.MAX_VALUE );
        }
        return result.toString();
    }

    public Integer getKey() {
        return this.key;
    }

    public CesarCipher(Integer key) {
        if ((key % Character.MAX_VALUE) == 0 ) {
            throw new IllegalArgumentException("Illegal key for encrypted - should be between  1 - 65534" );
        } else
         this.key = key % Character.MAX_VALUE ;
       }
}
