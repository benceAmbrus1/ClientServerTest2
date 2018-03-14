package com.codecool.server;


import com.codecool.common.Deck;
import com.codecool.common.DeckSwitcher;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Server {

    private static final int PORT = 38388;
    private static List<Thread> threads = new ArrayList<>();
    private Deck mainDeck;


    public void run(int numberOfPlayers) throws Exception {
        ServerSocket ss = new ServerSocket(PORT);
        System.out.println("Server is running: " + ss.getInetAddress().getLocalHost() +"|"+ PORT );

        System.out.println("Which cardgame you wish to play?");
        String[] deckOption = {"Kocsmatöltelék", "YoungTimerCar", "Pokemon"};
        mainDeck = new DeckSwitcher().choseTheDeck(deckOption);
        System.out.println(mainDeck.getCardsInDeck().size());

        try {
            while(threads.size() < numberOfPlayers) {
                Socket s = ss.accept();
                Thread t = new Handler(s);
                t.start();
                threads.add(t);
                System.out.println("New connection from: " + s.getInetAddress() + "|" + s.getPort());
                System.out.println(threads.size());
            }
        } finally {
            System.out.println("All player connected");
        }
    }

    public static int getThreads(){
        return threads.size();
    }
}
