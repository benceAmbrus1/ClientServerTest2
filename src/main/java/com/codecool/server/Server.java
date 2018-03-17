package com.codecool.server;

import com.codecool.common.Card;
import com.codecool.common.Deck;
import com.codecool.common.DeckSwitcher;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class Server {

    private final int PORT = 37777;
    private ServerSocket ss;
    private static List<Thread> threads = new ArrayList<>();
    private static List<Player> players = new ArrayList<>();
    private static Set<String> names = new HashSet<>();
    private Deck mainDeck;
    private Deck tempDeck = new Deck(new ArrayList<>());
    private Player player;
    private final int NUMBEROFCARDSDEALTOUT = 4;
    private Player nextPlayer;
    private Player winnerPlayer;
    private int numberOfPlayers;

    public Server(int numberOfPlayers) throws Exception {
        System.setProperty("java.net.preferIPv4Stack", "true");
        this.ss = new ServerSocket(PORT, 1, InetAddress.getByName("0.0.0.0"));
        this.numberOfPlayers = numberOfPlayers;
    }

    public void run() throws Exception {
        System.out.println("Server is running: " + ss.getInetAddress().getHostAddress() + "|" + PORT);

        //choose then create deck
        System.out.println("Which cardgame you wish to play?");
        String[] deckOption = {"Kocsmatöltelék", "YoungTimerCar", "Pokemon"};
        mainDeck = new DeckSwitcher().choseTheDeck(deckOption);
        mainDeck.cardShuffle();
        System.out.println("Deck Size: " + mainDeck.getCardsInDeck().size());

        try {
            do {
                Socket s = ss.accept();
                player = new Player(s, false, numberOfPlayers);
                players.add(player);
                player.start();
                System.out.println("New connection from: " + s.getInetAddress() + "|" + s.getPort());
                sendMessageToPlayer(player, "Please wait while others connecting...");
                System.out.println(players.size() + "=threadsize");

            } while ((players.size() < numberOfPlayers));
        } finally {
            while (names.size() != numberOfPlayers) {
                for (Player player : players) {
                    if (!(player.getPlayerName() == null)) {
                        player.getOutput().writeUTF("Not every player rdy please wait");
                    }
                }
                Thread.sleep(3000);
            }
            sendMessageToAllPlayer("All player connected");
        }

        dealOutCards(getDeck());
        System.out.println(getDeck().getCardsInDeck().size());
        sendMessageToAllPlayer("Everyone rdy, Lets Rock");
        for (Player ply : players) {
            System.out.println(ply.getPlayerName() + ": " + ply.getCardsInHand().size());
        }

        //Start the rounds
        while (players.size() > 1) {
            while (true) {
                nextPlayer = getWinnerPlayer();
                if (nextPlayer == null) {
                    nextPlayer = players.get(0);
                }
                senMessageAllButNextPlayer(nextPlayer, nextPlayer.getPlayerName() + " thinking, please wait.");
                sendMessageToPlayer(nextPlayer, "It's your Turn");
                listOutAttributOptions(nextPlayer);
                String nextPlayerChose;
                while(true) {
                    sendMessageToPlayer(nextPlayer, "CARDACHOOSE: " + nextPlayer.getPlayerName() + " choose an attribute number.");
                    nextPlayerChose = nextPlayer.getInput().readUTF();
                    System.out.println(nextPlayerChose);
                    if(nextPlayerChose.equals("1")
                        || nextPlayerChose.equals("2")
                        || nextPlayerChose.equals("3")
                        || nextPlayerChose.equals("4")){
                        break;
                    }
                    sendMessageToPlayer(nextPlayer, "ERROR: add a new number (1-4)");
                }

                try {
                    compareCards(nextPlayerChose);
                    System.out.println("CHO");
                    listOutAllPlayerCard(numberOfPlayers);
                    System.out.println("CH1");
                    if(winnerPlayer == null){
                        addCardstoTheTempDeck(getComparedCards());
                    }else{
                        addCardsToTheWinner(getComparedCards());
                    }
                    for (Player player : players) {
                        System.out.print("|" + player.getPlayerName() + ": got " + player.getCardsInHand().size() + "cards|");
                    }
                    senMessageAllButNextPlayer(winnerPlayer, "You lost this round :(");
                    sendMessageToPlayer(winnerPlayer, "You won this round");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    for(Player player:players){
                        player.setWinner(false);
                    }
                    if(winnerPlayer != null) {
                        winnerPlayer.setWinner(true);
                    }else{
                        nextPlayer.setWinner(true);
                    }
                    break;
                }
            }
            if (players.size() != 1) {
                for (int n = 0; n < players.size(); n++) {
                    if (players.get(n).getCardsInHand().size() < 1) {
                        players.get(n).getOutput().writeUTF("GAMEOVER: Sry you loose");
                        players.get(n).getSocket().close();
                        players.remove(players.get(n));

                    }
                }
            }
        }
        sendMessageToPlayer(players.get(0),"\nTHEWINNER is: " + players.get(0).getPlayerName() + "\n");
        players.get(0).getSocket().close();
        System.exit(0);
    }

    public static List<Player> getPlayers() {
        return players;
    }

    public static Set<String> getNames() {
        return names;
    }

    public Deck getDeck() {
        return mainDeck;
    }

    private void sendMessageToAllPlayer(String message) throws IOException {
        for (Player player : players) {
            player.getOutput().writeUTF(message);
        }
    }

    private void sendMessageToPlayer(Player player, String message) throws IOException {
        player.getOutput().writeUTF(message);
    }

    private void senMessageAllButNextPlayer (Player nextPlayer, String message) throws IOException{
        for(Player player : players){
            if(!(player.equals(nextPlayer))){
                player.getOutput().writeUTF(message);
            }
        }
    }

    private void dealOutCards(Deck deck) {
        List<Card> cards = deck.getCardsInDeck();
        for (Player ply : players) {
            List<Card> hand = ply.getCardsInHand();
            int n = 0;
            while (n < NUMBEROFCARDSDEALTOUT) {
                Card tempCard1 = cards.get(0);
                cards.remove(0);
                hand.add(tempCard1);
                n++;
            }
        }
    }

    private Player getWinnerPlayer() {
        for (Player player : players) {
            if (player.getWinner().equals(true)) {
                return player;
            }
        }
        return null;
    }

    private void listOutAttributOptions(Player player) throws IOException {
        player.getOutput().writeUTF("\tCardname: " + player.getCardsInHand().get(0).getName());
        player.getOutput().writeUTF("\t1. " + player.getCardsInHand().get(0).getAttribute1Name() + ": " + player.getCardsInHand().get(0).getAttribute1());
        player.getOutput().writeUTF("\t2. " + player.getCardsInHand().get(0).getAttribute2Name() + ": " + player.getCardsInHand().get(0).getAttribute2());
        player.getOutput().writeUTF("\t3. " + player.getCardsInHand().get(0).getAttribute3Name() + ": " + player.getCardsInHand().get(0).getAttribute3());
        player.getOutput().writeUTF("\t4. " + player.getCardsInHand().get(0).getAttribute4Name() + ": " + player.getCardsInHand().get(0).getAttribute4());
    }

    private void compareCards(String menuChoose) {
        switch (menuChoose) {
            case "1":
                winnerPlayer = compareAttribute1();
                break;
            case "2":
                winnerPlayer = compareAttribute2();
                break;
            case "3":
                winnerPlayer = compareAttribute3();
                break;
            case "4":
                winnerPlayer = compareAttribute4();
                break;
            default:
                System.out.println("smth faild at comparecards!!!");
        }
    }

    private Player compareAttribute4() {
        int max = 0;
        int temp;
        List<Integer> tempResulsts = new ArrayList<>();
        for(Player player:players) {
            temp = player.getCardsInHand().get(0).getAttribute4();
            tempResulsts.add(temp);
            if (temp > max || max == 0) {
                max = temp;
            }
        }
        int occurences = Collections.frequency(tempResulsts, max);
        if(occurences>1){
            return null;
        }else{
            for(Player playr : players) {
                if (max == playr.getCardsInHand().get(0).getAttribute4()) {
                    return playr;
                }
            }
        }
        return null;
    }

    private Player compareAttribute3() {
        int max = 0;
        int temp;
        List<Integer> tempResulsts = new ArrayList<>();
        for(Player player:players) {
            temp = player.getCardsInHand().get(0).getAttribute3();
            tempResulsts.add(temp);
            if (temp > max || max == 0) {
                max = temp;
            }
        }
        int occurences = Collections.frequency(tempResulsts, max);
        if(occurences>1){
            return null;
        }else{
            for(Player playr : players) {
                if (max == playr.getCardsInHand().get(0).getAttribute3()) {
                    return playr;
                }
            }
        }
        return null;
    }

    private Player compareAttribute2() {
        int max = 0;
        int temp;
        List<Integer> tempResulsts = new ArrayList<>();
        for(Player player:players) {
            temp = player.getCardsInHand().get(0).getAttribute2();
            tempResulsts.add(temp);
            if (temp > max || max == 0) {
                max = temp;
            }
        }
        int occurences = Collections.frequency(tempResulsts, max);
        if(occurences>1){
            return null;
        }else{
            for(Player playr : players) {
                if (max == playr.getCardsInHand().get(0).getAttribute2()) {
                    return playr;
                }
            }
        }
        return null;
    }

    private Player compareAttribute1() {
        int max = 0;
        int temp;
        List<Integer> tempResulsts = new ArrayList<>();
        for(Player player:players) {
            temp = player.getCardsInHand().get(0).getAttribute1();
            tempResulsts.add(temp);
            if (temp > max || max == 0) {
                max = temp;
            }
        }
        int occurences = Collections.frequency(tempResulsts, max);
        if(occurences>1){
            return null;
        }else{
            for(Player playr : players) {
                if (max == playr.getCardsInHand().get(0).getAttribute1()) {
                    return playr;
                }
            }
        }
        return null;
    }

    public void listOutAllPlayerCard(int numberOfPlayers) throws IOException {
        List<String> message = new ArrayList<>();
        for(Player player:players){
            message.add( player.getPlayerName() + "'s card:\t\t");
            message.add("Cardname: " + player.getCardsInHand().get(0).getName());
            message.add("1. " + player.getCardsInHand().get(0).getAttribute1Name() + ": " + player.getCardsInHand().get(0).getAttribute1());
            message.add("2. " + player.getCardsInHand().get(0).getAttribute2Name() + ": " + player.getCardsInHand().get(0).getAttribute2()+"\t");
            message.add("3. " + player.getCardsInHand().get(0).getAttribute3Name() + ": " + player.getCardsInHand().get(0).getAttribute3()+"\t");
            message.add("4. " + player.getCardsInHand().get(0).getAttribute4Name() + ": " + player.getCardsInHand().get(0).getAttribute4());
        }

        int stride = 6;
        for(Player player:players) {
            for(int i = 0; i < message.size()/numberOfPlayers; i++)
               sendMessageToPlayer(player, message.get(i) +"\t | " + message.get(i +stride));
        }

    }

    private void addCardstoTheTempDeck(List<Card> comparedCards) {
        tempDeck.getCardsInDeck().addAll(comparedCards);
    }

    private void addCardsToTheWinner(List<Card> comparedCards) {
        winnerPlayer.getCardsInHand().addAll(comparedCards);
    }

    private List<Card> getComparedCards() {
        List<Card> tempCardList = new ArrayList<>();
        if(tempDeck.getCardsInDeck().size() != 0) {
            tempCardList.addAll(tempDeck.getCardsInDeck());
            tempDeck = new Deck(new ArrayList<>());
        }
        for (Player player : players) {
            Card tempCard = player.getCardsInHand().get(0);
            player.getCardsInHand().remove(tempCard);
            tempCardList.add(tempCard);
        }
        return tempCardList;
    }
}

