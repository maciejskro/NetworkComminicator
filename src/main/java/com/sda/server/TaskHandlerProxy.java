package com.sda.server;

import java.io.*;

public class TaskHandlerProxy implements Runnable {

    private InputStream inputStream;
    private OutputStream outputStream;

    public TaskHandlerProxy(InputStream input, OutputStream output) {
        this.inputStream = input;
        this.outputStream = output;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.inputStream));
             PrintWriter printWriter = new PrintWriter(this.outputStream)) {
            while (true) {
                printWriter.println(reader.readLine());
                printWriter.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error połączenia");
        }
    }
}

