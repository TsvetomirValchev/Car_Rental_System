package entries.util;

import entries.Entry;

import java.util.List;

public class EntryHandler extends EntryDB{

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
}
