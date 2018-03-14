package com.codecool.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Handler extends Thread {

    private String name;
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private static List<String> names = new ArrayList<>();
    private static Set<PrintWriter> outputs = new HashSet<>();

    public Handler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream());

            while (true) {
                output.println("Add your name pls");
                name = input.readLine();
                if (name == null) {
                    return;
                }
                synchronized (names) {
                    if (!names.contains(name)) {
                        names.add(name);
                        break;
                    }
                }
            }

            output.println("Name accepted");
            outputs.add(output);

            while (true) {
                String incoming = input.readLine();
                if (incoming == null) {
                    return;
                }

                for (PrintWriter outpts : outputs) {
                    outpts.println(incoming);
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
