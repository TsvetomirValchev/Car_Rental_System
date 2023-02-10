package entries.db;

import entries.Entry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;


public class EntryHandler extends EntryDOA {

    public static final EntryHandler instance = new EntryHandler();

    private EntryHandler() {}

    public static EntryHandler getInstance() {
        return instance;
    }


    /*TODO
        Various access and sorting methods*/

    public List<Entry> getSortedEntries(){
        return new ArrayList<>(readEntries().values())
                .stream()
                .sorted(Comparator.comparing(Entry::getFirstName))
                .toList();
    }

    public List<String> getSortedKeys(){
        return new HashSet<>(readEntries().keySet())
                .stream()
                .sorted()
                .toList();
    }


}
