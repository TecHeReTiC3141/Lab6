package serialization;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String[] territoryInfo = {"У Испании 6 провинций", "У России 10 провинций", "У Франции 8 провинций"};
        String[] resourcesInfo = {"У Испании 100 золота", "У России 80 золота", "У Франции 90 золота"};
        String[] diplomacyInfo = {"Франция воюет с Россией, Испания заняла позицию нейтралитета"};

        SavedGame savedGame = new SavedGame(territoryInfo, resourcesInfo, diplomacyInfo);

//        FileOutputStream fileOutput = new FileOutputStream("save.ser");
//        ObjectOutputStream output = new ObjectOutputStream(fileOutput);
//        output.writeObject(savedGame);

        FileInputStream fileInput = new FileInputStream("save.ser");
        ObjectInputStream input = new ObjectInputStream(fileInput);
        SavedGame fromSave = (SavedGame) input.readObject();
        System.out.println(fromSave);
    }
}
