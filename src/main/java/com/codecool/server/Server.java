package com.codecool.server;


import java.net.ServerSocket;


public class Server {

    private static final int PORT = 38388;

    public static void main(String[] args) throws Exception {
        System.out.println("Server is running...");
        ServerSocket ss = new ServerSocket(PORT);
        try{
            while(true){
                new Handler(ss.accept()).start();
            }
        }finally {
            ss.close();
        }

    }
}
