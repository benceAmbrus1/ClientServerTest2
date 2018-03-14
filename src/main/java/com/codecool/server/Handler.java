package com.codecool.server;

import com.codecool.common.Card;
import com.codecool.common.Deck;
import com.codecool.common.Player;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Handler extends Thread {

    private String name;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private static List<String> names = new ArrayList<>();
    private static List<Player> players = new ArrayList<>();
    private Player player;

    public Handler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            while (true) {
                output.writeUTF("First add your name please");
                name = input.readUTF();
                if (name.equals("")) {
                    return;
                }
                synchronized (names) {
                    if (!names.contains(name)) {
                        break;
                    }
                    output.writeUTF("ERROR: This name already exist!!");
                }
            }

            output.writeUTF("Name accepted, have a good Playing");
            player = new Player(name, true, output);
            player.addHand();
            players.add(player);

            while(true){
                if(!(players.size() == Server.getThreads())){
                    output.writeUTF("Waiting for other players");
                    return;
                    }
                for (Player ply:players){
                    ply.getOutput().writeUTF("All player connected rdy to fight");
                }
                break;
            }


            while (true) {
                String incoming = input.readUTF();
                for (Player ply : players) {
                    ply.getOutput().writeUTF("Message from "+ name +": "+ incoming);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }finally {
            if (name != null){
                names.remove(name);
            }
            if (player.getOutput() != null){
                players.remove(player);
            }
            try{
                socket.close();
            }catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    private void dealOutCards(Player player, Deck deck) {
        List<Card> cards = deck.getCardsInDeck();

        for (Player ply : players) {
            ply.addHand();
            List<Card> hand = player.getCardsInHand();
            int n = 0;
            while (n < 4) {
                Card tempCard1 = cards.get(0);
                cards.remove(0);
                hand.add(tempCard1);
                n++;
            }
        }
    }
}
