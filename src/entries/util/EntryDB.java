package entries.util;

import entries.Entry;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EntryDB {
    public static final List<Entry> entryCollection = new ArrayList<>();
    private static final Path filePath = Paths.get("entries.bin");

    protected static void saveEntries() {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(entryCollection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static void loadEntries() {
        if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
                entryCollection.clear();
                entryCollection.addAll((List<Entry>) ois.readObject());
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


