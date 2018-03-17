package com.codecool.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SendMessage extends Thread {

    private Socket socket;
    private BufferedReader outBufferedReader;
    private String outcoming = null;
    private DataOutputStream output;


    public SendMessage(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            while (true) {
                output = new DataOutputStream(socket.getOutputStream());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendOutput() throws IOException{
        outBufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Type your message");
        outcoming = outBufferedReader.readLine();
        output.writeUTF(outcoming);
        output.flush();
    }
}
