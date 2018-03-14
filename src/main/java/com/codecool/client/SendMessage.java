package com.codecool.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class SendMessage extends Thread {

    Socket socket;
    BufferedReader outBufferedReader;
    String outcoming;
    DataOutputStream output;

    public SendMessage(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            output = new DataOutputStream(socket.getOutputStream());
            while (true) {
                System.out.println("Type your message (EXIT for exit)");
                outBufferedReader = new BufferedReader(new InputStreamReader(System.in));
                outcoming = outBufferedReader.readLine();
                output.writeUTF(outcoming);
                output.flush();
                if (outcoming.equals("EXIT"))
                    break;
            }
            socket.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
