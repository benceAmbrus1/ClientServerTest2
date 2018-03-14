package com.codecool.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Handler extends Thread {

    private String name;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private static List<String> names = new ArrayList<>();
    private static List<DataOutputStream> outputs = new ArrayList<>();

    public Handler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            while (true) {
                output.writeUTF("First add your name please");
                name = input.readUTF();
                if (name.equals("")) {
                    return;
                }
                synchronized (names) {
                    if (!names.contains(name)) {
                        names.add(name);
                        break;
                    }
                    output.writeUTF("ERROR: This name already exist!!");
                }
            }

            output.writeUTF("Name accepted, have a good chating");
            outputs.add(output);

            while (true) {
                String incoming = input.readUTF();
                for (DataOutputStream outpts : outputs) {
                    outpts.writeUTF("Message from "+ name +": "+ incoming);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }finally {
            if (name != null){
                names.remove(name);
            }
            if (output != null){
                outputs.remove(output);
            }
            try{
                socket.close();
            }catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
