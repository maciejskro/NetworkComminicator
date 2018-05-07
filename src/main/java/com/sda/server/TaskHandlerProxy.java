package com.sda.server;

import com.sda.client.ContactList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class TaskHandlerProxy implements Runnable {

    //private Socket inputStream;
    private Map<String, Socket> clientMaps;

    public TaskHandlerProxy(Map<String, Socket> maps) {
        this.clientMaps = maps;
    }

    @Override
    public void run() {
        ContactList cl = null;
        while (cl != null) {
            try (ObjectInputStream reader = new ObjectInputStream(clientMaps.get(cl.getListContact().get(0)).getInputStream())) {

                cl = (ContactList) reader.readObject();
                for (String s : cl.getListContact() ) {
                    ObjectOutputStream printWriter = new ObjectOutputStream(clientMaps.get(s).getOutputStream());
                    System.out.println(cl.getMessageBody());
                    //clientMaps.put(name,inputStream );
                    // wysłanie klucza
                    printWriter.writeObject("klucz");
                }
                while (clientMaps.size() > 1) {
                    sendMessageToAll(cl);
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("error połączenia");
            } catch (ClassNotFoundException e) {
                System.err.println("Class ContactList not sended.");
                e.printStackTrace();
            }
        }
    }
    protected void sendMessageToAll(ContactList cl) {
        try {
            while (cl.getListContact().size() > 1) {
                for (int i = 0; i < cl.getListContact().size(); i++) {
                    Socket socket = clientMaps.get(cl.getListContact().get(i));
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(cl);
                    oos.flush();
                }
            }
        } catch (IOException e) {
            System.err.println("IO Exception  socket closed or somthings");
            e.printStackTrace();
        }
    }
    protected void sendMessageToList(ContactList cl) {

    }

}

