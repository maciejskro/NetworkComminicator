package com.sda.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReaderService implements Runnable {

    private Socket clientSocket;

    public ReaderService(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream())))
        {
            String line = null;
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
