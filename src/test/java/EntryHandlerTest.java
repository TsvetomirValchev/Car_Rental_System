import entries.Entry;
import entries.util.EntryHandler;
import org.junit.jupiter.api.Test;
import view.Menu;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EntryHandlerTest {
    EntryHandler entryHandler = EntryHandler.getInstance();

    @Test
    void testGetEntryByIndex() {
        assertNotNull(entryHandler.getEntryByEmail("nikolovnikolai@abv.bg"));
    }

    @Test
    void testReading(){
        assertNotNull(entryHandler.readEntries());
        System.out.println(entryHandler.readEntries());
    }

    @Test
    void testCreate(){
        entryHandler.createEntry(new Entry.Builder()
                .setFirstName("Yaroslav")
                .setLastName("Siderov")
                .setBirthDate(LocalDate.parse("2001-11-11"))
                .setEMail("siderkata@abv.bg")
                .build());

    }

    @Test
    void testDelete() {
        entryHandler.deleteEntry("siderkata@abv.bg");
    }

    @Test
    void testUpdate(){
        entryHandler.updateEntry("zlateff@mail.bg",1,"Stoyan");
    }
}