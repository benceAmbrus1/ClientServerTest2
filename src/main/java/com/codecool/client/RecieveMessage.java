package com.codecool.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class RecieveMessage extends Thread{

    private Socket socket;
    private DataInputStream input;

    public RecieveMessage(Socket socket) {
        this.socket = socket;
    }

    public void run(){
        try{
            input = new DataInputStream(socket.getInputStream());
            while(true){
                String incoming = input.readUTF();
                System.out.println(incoming);
            }
        }catch (IOException e){
            System.out.println(e);
        }
    }
}
