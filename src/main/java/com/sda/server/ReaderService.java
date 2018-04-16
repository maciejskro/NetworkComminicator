package com.sda.server;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReaderService implements Runnable {

    private Socket clientSocket;
    private Cipher encrption;

    public ReaderService(Socket clientSocket) {
        this.clientSocket = clientSocket;
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
