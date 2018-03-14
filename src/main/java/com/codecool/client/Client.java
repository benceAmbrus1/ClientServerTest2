package com.codecool.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public void run() throws Exception {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please add IP address: ");
        String IP = scanner.next();
        System.out.println("Add Port: ");
        int port = scanner.nextInt();
        Socket socket = new Socket(IP, port);
        System.out.println("Client connected to: " + socket.getInetAddress() +"|"+ socket.getPort());
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        new ClientHandler(socket, new RecieveMessage(socket), new SendMessage(socket)).run();



    }
}
