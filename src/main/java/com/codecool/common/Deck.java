package com.codecool.common;

import java.util.Collections;
import java.util.List;

public class Deck {

    private List<Card> cardsInDeck;

    public Deck(List<Card> cardsInDeck) {
        this.cardsInDeck = cardsInDeck;
    }

    public List<Card> getCardsInDeck() {
        return cardsInDeck;
    }

    public void cardShuffle() {
        Collections.shuffle(cardsInDeck);
    }
}
