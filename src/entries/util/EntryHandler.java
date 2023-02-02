package entries.util;

import entries.Entry;

import java.util.List;

public class EntryHandler extends EntryDB{

    public static EntryHandler instance = new EntryHandler();

    private EntryHandler() {EntryDB.loadEntries();}
    public static EntryHandler getInstance() {
        return instance;
    }

    public Entry getEntryByIndex(int index){
        List<Entry> entries = loadEntries();
        return entries.get(index);
    }


}
