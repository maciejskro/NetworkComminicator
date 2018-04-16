package com.sda.client;

import com.sda.server.ReaderService;
import com.sda.server.WriteService;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    public static void main(String[] args) throws IOException {

        //host serwera
        String host = "localhost";
        //port serwera
        int port = 4444;
        Random rand = new Random();

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Scanner scan = new Scanner(System.in);


            //Tworzymy gniazdo do polaczenia z serwerem
            Socket socket = new Socket(host,port);
            WriteService writeService = new WriteService(socket, "ClientName" + (rand.nextInt(65533)+1));
            executorService.submit(writeService);

            executorService.submit(new ReaderService(socket));

    }
}
