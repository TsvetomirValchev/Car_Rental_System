package entries.util;

import entries.Entry;

import java.time.LocalDate;

public class EntryHandler {

    public static EntryHandler instance = new EntryHandler();

    private EntryHandler() {
        EntryDB.loadEntries();
    }
    public static EntryHandler getInstance() {
        return instance;
    }

    //C
    public void createEntry(Entry entry) {
        EntryDB.entryCollection.add(entry);
        EntryDB.saveEntries();
    }

    //R
    public String readEntries() {
        StringBuilder sb = new StringBuilder();
        EntryDB.entryCollection
                .forEach(i -> sb.append(EntryDB.entryCollection.indexOf(i))
                        .append(i).
                        append("\n"));
        return sb.toString();
    }

    //U
    public void updateEntry(int entryIndex, int propertyIndex, String updatedValue) {
        Entry entry = EntryDB.entryCollection.get(entryIndex);
        switch (propertyIndex) {
            case 1 -> entry.setFirstName(updatedValue);
            case 2 -> entry.setLastName(updatedValue);
            case 3 -> entry.setBirthDate(LocalDate.parse(updatedValue));
            case 4 -> entry.setEMail(updatedValue);
            default -> {
            }
        }
        EntryDB.entryCollection.set(entryIndex, entry);
        EntryDB.saveEntries();
    }

    //D
    public void deleteEntry(int index) {
        if (index >= 0 && index < EntryDB.entryCollection.size()) {
            EntryDB.entryCollection.remove(index);
            EntryDB.saveEntries();
        }
    }
}
