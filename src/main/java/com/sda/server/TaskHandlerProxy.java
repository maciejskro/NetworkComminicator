package com.sda.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskHandlerProxy implements Runnable {

    private Socket inputStream;
    private Socket outputStream;
    private Map<String, Socket> clientMaps;

    public TaskHandlerProxy(Socket input, Socket output, Map<String, Socket> maps) {
        this.inputStream = input;
        this.outputStream = output;
        this.clientMaps = maps;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.inputStream.getInputStream()));
             PrintWriter printWriter = new PrintWriter(this.outputStream.getOutputStream())) {
            String name = reader.readLine();
            System.out.println(name);
            clientMaps.put(name,inputStream );
            // wysłanie klucza
            printWriter.println("klucz");
            while (true) {
                printWriter.println(reader.readLine());
                printWriter.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error połączenia");
        }
    }

    private String readName(Socket socket1) throws IOException {
        BufferedReader breader = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
        return breader.readLine();
    }
    private void createSocketServer(ServerSocket serverSocket, String name) throws IOException {
        if ( clientMaps != null) {
            this.clientMaps = new ConcurrentHashMap<>();
        }
        Socket socket = serverSocket.accept();
        this.clientMaps.put(name, socket);
    }
}

