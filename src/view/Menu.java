package view;

import entries.Entry;
import entries.util.EntryDB;
import entries.util.EntryHandler;

import java.time.LocalDate;
import java.util.Scanner;

public class Menu{
    private static final EntryHandler entryHandler = EntryHandler.getInstance();
    public static void printMenuOptions(){
        System.out.println("Choose an action:");
        System.out.println("(1) - Create an entry.");
        System.out.println("(2) - Read all entries.");
        System.out.println("(3) - Update an entry.");
        System.out.println("(4) - Delete an entry.");
        System.out.println("(0) - Exit.");
    }
    public static void createEntryPrompt(){
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter first name: ");
        String firstName = scan.nextLine();

        System.out.println("Enter last name: ");
        String lastName = scan.nextLine();

        System.out.println("Enter birth date (YYYY-MM-DD): ");
        String birthDateString = scan.nextLine();
        LocalDate birthDate = LocalDate.parse(birthDateString);

        System.out.println("Enter e-mail: ");
        String eMail = scan.nextLine();

        entryHandler.createEntry(new Entry.Builder()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setBirthDate(birthDate)
                .setEMail(eMail)
                .build());

        System.out.println("Entry created!");
    }
    public static void readEntriesPrompt(){
        System.out.println("All entries: ");
        System.out.println(entryHandler.readEntries());
    }
    public static void updateEntryPrompt() {
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter the index of the entry you would like to change: ");
        int entryIndex = scan.nextInt();

        System.out.println("Enter the index of the property you would like to change for:\n"+ EntryDB.entryCollection.get(entryIndex));
        System.out.println("(1) for First Name");
        System.out.println("(2) for Last Name");
        System.out.println("(3) for Birth Date");
        System.out.println("(4) for E-mail Address");
        int propertyIndex = scan.nextInt();

        System.out.println("Enter the new value: ");
        String newValue = scan.next();

        entryHandler.updateEntry(entryIndex, propertyIndex, newValue);
        System.out.println("Successfully updated the entry at index "+ entryIndex+"!");
    }
    public static void deleteEntryPrompt(){
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter the index of the entry you would like to delete: ");
        int indexToBeDeleted = scan.nextInt();
        entryHandler.deleteEntry(indexToBeDeleted);
        System.out.println("Entry deleted!");
    }
    public static void getMenuSwitch(){
        Scanner scan = new Scanner(System.in);
        int choice;
        do{
            choice = scan.nextInt();
            switch (choice){
                case 1->createEntryPrompt();
                case 2->readEntriesPrompt();
                case 3->updateEntryPrompt();
                case 4->deleteEntryPrompt();
                case 0-> System.out.println("Exiting!..");
                default -> System.err.println("Invalid choice!");
            }
        }while(choice!=0);
    }
}