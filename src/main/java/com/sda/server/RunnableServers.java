package com.sda.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RunnableServers implements Runnable {

    private Socket clientSocket;

    private long SLEEP_TIME = 5_000l;

    public RunnableServers(Socket socket) throws IOException {
        this.clientSocket = socket;
    }

    @Override
    public void run() {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream());) {
            //Odczytaj linijke od klienta
            String line = reader.readLine();
            System.out.println("Received: " + line);
            //Uspij na 5 sek. Symulacja obliczen
            //Thread.sleep(SLEEP_TIME);
            //Odpowiedz do klienta
            printWriter.println("Response from server: " + line);
            //flush - wypchnij z bufora
            printWriter.flush();
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }

    }
}
