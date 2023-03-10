package view;

public interface Dashboard {

    abstract void printMenu();
    abstract void getOptions();
    abstract void printExceptionMessage(String message);

    default void printSeparator(int length){
        for(int i=0;i<=length;i++){
            System.out.print("=");
        }
        System.out.println();
    }
}
