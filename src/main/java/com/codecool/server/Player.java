package com.codecool.server;


import com.codecool.common.Card;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Player extends Thread {

    private String name;
    private Socket socket;
    private static int numberOfPlayers;
    private DataInputStream input;
    private DataOutputStream output;
    private static List<DataOutputStream> outputs = new ArrayList<>();
    private static List<Player> players = new ArrayList<>();
    private Hand hand = new Hand();
    private boolean winner;


    public Player(Socket socket, boolean winner, int numberOfPlayers) {
        this.socket = socket;
        this.winner = winner;
        this.numberOfPlayers = numberOfPlayers;
    }


    @Override
    public void run() {
        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            while (true) {
                output.writeUTF("NAMESUBMIT: First add your name please: ");
                name = input.readUTF();
                if (name.equals("")) {
                    return;
                }
                synchronized (Server.getNames()) {
                    if (!Server.getNames().contains(name)) {
                        Server.getNames().add(name);
                        break;
                    }
                }
                output.writeUTF("ERROR: This name already exist!!");
            }

            output.writeUTF("Name accepted, have a good Playing");

            while (true) {
                System.out.println("SERVERRUNNING: thread of" + name);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }


        } catch (IOException e) {
            System.out.println(e);
            //yet not reached finnaly
        } finally {
            if (name != null) {
                Server.getNames().remove(name);
            }
            if (output != null) {
                outputs.remove(output);
            }
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public Boolean getWinner() {
        return winner;
    }

    public Socket getSocket() {
        return socket;
    }

    public DataOutputStream getOutput() {
        return output;
    }

    public DataInputStream getInput(){
        return input;
    }

    public String getPlayerName() {
        return name;
    }

    public void setWinner(Boolean ready) {
        this.winner = ready;
    }


    public Hand getHand() {

        return hand;
    }


    public List<Card> getCardsInHand() {
        List<Card> cards = getHand().getCapsulatedHand();
        return cards;
    }

    private class Hand {

        private List<Card> cardsInHand;

        public Hand() {
            cardsInHand = new ArrayList<>();
        }

        public List<Card> getCapsulatedHand() {
            return cardsInHand;
        }
    }
}
