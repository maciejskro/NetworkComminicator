package com.sda.server;

import com.sda.encrypt.CipherFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import com.sda.encrypt.Cipher;

public class ReaderService implements Runnable {

    private Socket clientSocket;
    private Cipher encrption;

    public ReaderService(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.encrption = CipherFactory.create("Ceasar");
    }

    @Override
    public void run() {

        // Cipher
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream())))
        {
            String line = null;
            String key = reader.readLine();

            while (( line = reader.readLine() ) != null ) {
                System.out.println("Received: " + line);
            }
        }
        catch (IOException e) {
            System.out.println("error" );
            e.printStackTrace();
        }

    }
}
