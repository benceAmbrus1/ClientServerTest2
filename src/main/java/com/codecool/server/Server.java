package com.codecool.server;


import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    private static final int PORT = 38388;

    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(PORT);
        System.out.println("Server is running: " + ss.getInetAddress().getLocalHost() +"|"+ PORT );
        try{
            while(true){
                Socket s = ss.accept();
                System.out.println("New connection from: " + s.getInetAddress()+"|"+ s.getPort());
                new Handler(s).start();
            }
        }finally {
            ss.close();
        }

    }
}
