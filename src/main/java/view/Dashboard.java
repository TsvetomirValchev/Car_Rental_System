package view;

public interface Dashboard {

    void printMenu();
    void getOptions();
    void printExceptionMessage(String message);

    default void printSeparator(int length){
        for(int i=0;i<=length;i++){
            System.out.print("=");
        }
        System.out.println();
    }
}
