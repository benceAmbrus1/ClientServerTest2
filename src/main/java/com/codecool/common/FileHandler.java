package com.codecool.common;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileHandler {

    private List<Card> cards = new ArrayList<>();

    public List<Card> loadCsv(String fileName) throws URISyntaxException {
        try {
            URI uri = getClass().getClassLoader().getResource(fileName).toURI();

            final Path path;

            if (uri.toString().contains(".jar!")) {
                final Map<String, String> env = new HashMap<>();
                final String[] array = uri.toString().split("!");
                final FileSystem fs = FileSystems.newFileSystem(URI.create(array[0]), env);
                path = fs.getPath(array[1]);
            } else
                path = Paths.get(uri);

            InputStream newInputStream = Files.newInputStream(path);
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(newInputStream) );

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
