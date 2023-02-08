package entries.db;

import entries.Entry;

import java.util.Comparator;
import java.util.List;

public class EntryHandler extends EntryDOA {

    public static EntryHandler instance = new EntryHandler();

    private EntryHandler() {}

    public static EntryHandler getInstance() {
        return instance;
    }

    public Entry getEntryByEmail(String eMail){
        List<Entry> entries = readEntries();
        for(Entry e: entries){
            if(e.getEMail().equals(eMail)){
                return e;
            }
        }
        return null;
    }

    /*TODO
        Various access and sorting methods*/

    public List<Entry> getSortedEntries(){
        return readEntries()
                .stream()
                .sorted(Comparator.comparing(Entry::getFirstName))
                .toList();
    }
}
