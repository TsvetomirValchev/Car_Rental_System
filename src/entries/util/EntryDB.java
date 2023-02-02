package entries.util;

import entries.Entry;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EntryDB {
    private static final List<Entry> entryCollection = new ArrayList<>();
    private static final Path filePath = Paths.get("entries.bin");

    EntryDB(){}

    //C
    public void createEntry(Entry entry) {
        entryCollection.add(entry);
        saveEntries();
    }

    //R
    public String readEntries() {
        StringBuilder sb = new StringBuilder();
        entryCollection
                .forEach(i -> sb
                        .append("|")
                        .append(EntryDB.entryCollection.indexOf(i))
                        .append(i)
                        .append("\n"));
        return sb.toString();
    }

    //U
    public void updateEntry(int entryIndex, int propertyIndex, String updatedValue) {
        Entry entry = entryCollection.get(entryIndex);
        switch (propertyIndex) {
            case 1 -> entry.setFirstName(updatedValue);
            case 2 -> entry.setLastName(updatedValue);
            case 3 -> entry.setBirthDate(LocalDate.parse(updatedValue));
            case 4 -> entry.setEMail(updatedValue);
            default -> System.err.println("Invalid choice!");
        }
        EntryDB.entryCollection.set(entryIndex, entry);
        EntryDB.saveEntries();
    }

    //D
    public void deleteEntry(int index) {
        if (index >= 0 && index < EntryDB.entryCollection.size()) {
            entryCollection.remove(index);
            EntryDB.saveEntries();
        }
    }

    protected static void saveEntries() {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(entryCollection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static List<Entry> loadEntries() {
        if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
                entryCollection.clear();
                entryCollection.addAll((List<Entry>) ois.readObject());
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return new ArrayList<>(entryCollection);
    }
}


