package com.codecool;

import com.codecool.client.Client;
import com.codecool.common.Deck;
import com.codecool.common.DeckSwitcher;
import com.codecool.server.Server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("Choose: SERVER/CLIENT");
        while(true){
            Scanner scanner = new Scanner(System.in);
            String s = scanner.nextLine();
            if (s.toUpperCase().equals("SERVER")){
                try {
                    while(true){
                        System.out.println("Enter number of players(2-4)");
                        int numberOfPlayers = scanner.nextInt();
                        if (numberOfPlayers >= 2 && numberOfPlayers <=4) {
                            Socket socket;
                            try {
                                socket = new Socket();
                                socket.connect(new InetSocketAddress("google.com", 80));
                            } catch (UnknownHostException unknown) {
                                socket = new Socket();
                            }

                            new Server(numberOfPlayers).run();
                            break;
                        }
                        System.out.println("ERROR: add number between 2-4");
                    }
                    break;
                }catch (Exception e){
                    System.out.println(e);
                }
            }else if(s.toUpperCase().equals("CLIENT")){
                try {
                    new Client().run();
                    break;
                }catch (Exception e){
                    System.out.println(e);
                }
            }else{
                System.out.println("ERROR: Type again.");
            }
        }
    }
}
