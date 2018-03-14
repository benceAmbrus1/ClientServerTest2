package com.codecool.common;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private Hand hand;
    private Boolean ready;
    private DataOutputStream output;

    public Player(String name, Boolean ready, DataOutputStream output) {

        this.name = name;
        this.ready = ready;
        this.output = output;
    }
    public Boolean getReady() {
        return ready;
    }

    public DataOutputStream getOutput() {
        return output;
    }

    public String getName() {
        return name;
    }

    public void setReady(Boolean ready) {
        this.ready = ready;
    }


    public Hand getHand() {
        return hand;
    }

    public void addHand() {
        hand = new Hand();
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
