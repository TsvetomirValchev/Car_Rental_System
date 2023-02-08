package view;

import entries.Entry;
import entries.db.EntryHandler;

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
        System.out.println("All entries (in alphabetical order): ");
        for(Entry e: entryHandler.getSortedEntries()){
            System.out.println(e);
        }
    }

    public static void updateEntryPrompt() {
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter the e-mail of the entry you would like to change: ");
        String entryEmail = scan.nextLine();

        printUpdateEntryOptions();
        int propertyIndex = Integer.parseInt(scan.nextLine());

        System.out.println("Enter the new value: ");
        String newValue = scan.next();

        entryHandler.updateEntry(entryEmail, propertyIndex, newValue);
        System.out.println("Successfully updated the entry with e-mail:  "+ entryEmail+"!");
    }
    public static void printUpdateEntryOptions(){
        System.out.println("Enter the index of the property you would like to change for:\n");
        System.out.println("(1) for First Name");
        System.out.println("(2) for Last Name");
        System.out.println("(3) for Birth Date");
        System.out.println("(4) for E-mail Address");
    }

    public static void deleteEntryPrompt(){
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter the email of the entry you would like to delete: ");
        String emailToBeDeleted = scan.nextLine();
        entryHandler.deleteEntry(emailToBeDeleted);
        System.out.println("Entry deleted!");
    }
}
