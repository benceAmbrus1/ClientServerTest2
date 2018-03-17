package com.codecool.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RecieveMessage extends Thread{

    private Socket socket;
    private DataInputStream input;
    private SendMessage send;
    private List<String> messages = new ArrayList<>();

    public RecieveMessage(Socket socket, SendMessage send) {
        this.socket = socket;
        this.send = send;
    }

    public void run(){
        try{
            input = new DataInputStream(socket.getInputStream());
            send.start();
            while(true){
                String incoming = input.readUTF();
                System.out.println(incoming);

                if(incoming.startsWith("NAMESUBMIT")){
                    send.sendOutput();
                }
                if(incoming.startsWith("CARDACHOOSE")){
                    send.sendOutput();
                }
                if(incoming.startsWith("GAMEOVER")){
                    break;
                }
                if(incoming.startsWith("THEWINNER")){
                    break;
                }
            }
        }catch (IOException e){
            System.out.println(e);
        }finally {
            try{
                socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
