package com.codecool.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    private List<Card> cards = new ArrayList<>();

    public List<Card> loadCsv(String fileName) {
        try {

            BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
            String firstLine = fileReader.readLine();
            String[] listFirstline = firstLine.split(",");
            String tempAttribute1Name = listFirstline[0];
            String tempAttribute2Name = listFirstline[1];
            String tempAttribute3Name = listFirstline[2];
            String tempAttribute4Name = listFirstline[3];

            String line = "";
            int rank = 1;
            while ((line = fileReader.readLine()) != null) {
                String[] list = line.split(",");
                if(list.length > 0) {
                    Card tempCard = new Card(list[0], rank, Integer.parseInt(list[1]),
                        tempAttribute1Name, Integer.parseInt(list[2]),
                        tempAttribute2Name, Integer.parseInt(list[3]),
                        tempAttribute3Name, Integer.parseInt(list[4]),
                        tempAttribute4Name);
                    cards.add(tempCard);
                    rank++;
                }
            }
        }catch (IOException e){
            System.out.println("Fail at loadCSV");
            e.printStackTrace();
        }
        return cards;
    }
}
