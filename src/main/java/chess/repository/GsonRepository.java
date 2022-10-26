package chess.repository;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import chess.player.GameHistory;

import java.io.*;

import java.util.Collections;


public class GsonRepository extends Repository{

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    /**
     * a method get data from json file.
     *
     * @return an object list data from json file or null if the file is empty.
     * @throws IOException On input error.
     */
    public GameHistory[] getJson(File file) throws IOException {
        //File file = new File("src/main/resources/gameHistory.json");
        if (file.exists()){
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            Gson gson = new GsonBuilder().create();

            return gson.fromJson(bufferedReader, GameHistory[].class);
        }
        return null;
    }

    /**
     * Save the data to json file.
     *
     * @param file from json file.
     * @throws IOException On input error.
     */
    public void saveToFile(File file) throws IOException {
        try (var writer = new FileWriter(file)) {
            gson.toJson(elements, writer);
        }
    }

    /**
     * load the file and save the data to element.
     *
     * @param file json file.
     * @throws IOException on input error.
     */
    public void loadFromFile(File file)throws IOException{
        if (file.length() != 0){
            GameHistory[] gameHistories = getJson(file);
            Collections.addAll(elements, gameHistories);
        }

    }

    /**
     * add player's data into GameHistory object list.
     *
     * @param element an object created in GameHistory class.
     */
    public void add(GameHistory element) {
        elements.add(element);
    }


}
