package com.codecool.common;

public class Card implements Comparable<Card> {

    private String name;
    private int rank;
    private int attribute1;
    private String attribute1Name;
    private int attribute2;
    private String attribute2Name;
    private int attribute3;
    private String attribute3Name;
    private int attribute4;
    private String attribute4Name;

    public Card(String name, int rank, int attribute1,
                String attribute1Name, int attribute2,
                String attribute2Name, int attribute3,
                String attribute3Name, int attribute4,
                String attribute4Name) {

        this.name = name;
        this.rank = rank;
        this.attribute1 = attribute1;
        this.attribute1Name = attribute1Name;
        this.attribute2 = attribute2;
        this.attribute2Name = attribute2Name;
        this.attribute3 = attribute3;
        this.attribute3Name = attribute3Name;
        this.attribute4 = attribute4;
        this.attribute4Name = attribute4Name;
    }

    @Override
    public String toString() {
        return "Card " +
            "name: " + name +
            ", rank: " + rank +
            ", " + getAttribute1Name() + ": " + getAttribute1() +
            ", " + getAttribute2Name() + ": " + getAttribute2() +
            ", " + getAttribute3Name() + ": " + getAttribute3() +
            ", " + getAttribute4Name() + ": " + getAttribute4();
    }

    public String getName() {
        return name;
    }

    public int getRank() {
        return rank;
    }

    public int getAttribute1() {
        return attribute1;
    }

    public String getAttribute1Name() {
        return attribute1Name;
    }

    public int getAttribute2() {
        return attribute2;
    }

    public String getAttribute2Name() {
        return attribute2Name;
    }

    public int getAttribute3() {
        return attribute3;
    }

    public String getAttribute3Name() {
        return attribute3Name;
    }

    public int getAttribute4() {
        return attribute4;
    }

    public String getAttribute4Name() {
        return attribute4Name;
    }

    @Override
    public int compareTo(Card o) {
        return 0;
    }
}
