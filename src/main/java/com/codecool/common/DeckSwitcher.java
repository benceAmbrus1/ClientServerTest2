package com.codecool.common;

import java.util.Scanner;

public class DeckSwitcher {

    public Deck choseTheDeck(String[] deckOptions) {
        int n = 1;
        for (String option : deckOptions) {
            System.out.println(n + ". " + option + " Card Game");
            n++;
        }
        while (true) {
            try {
                String deckChose = new Scanner(System.in).next();
                switch (deckChose) {
                    case "1":
                        return new Deck(new FileHandler().loadCsv("drunkenShits.csv"));
                    case "2":
                        return new Deck(new FileHandler().loadCsv("youngtimerCarCardGame.csv"));
                    case "3":
                        return new Deck(new FileHandler().loadCsv("Csicskamon.csv"));
                    default:
                        System.out.println("Wrong option, please chose another one, DEFAULT");
                }
            } catch (Exception e) {
                System.out.println("Wrong option, please chose another one");
                e.printStackTrace();
            }
        }
    }
}
