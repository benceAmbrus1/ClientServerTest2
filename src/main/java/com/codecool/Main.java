package com.codecool;

import com.codecool.client.Client;
import com.codecool.server.Server;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("Choose: SERVER/CLIENT");
        while(true){
            Scanner scanner = new Scanner(System.in);
            String s = scanner.nextLine();
            if (s.toUpperCase().equals("SERVER")){
                try {
                    new Server().run();
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
