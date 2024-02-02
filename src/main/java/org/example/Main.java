package org.example;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_");
        System.out.println("_-_-_-_-_-_-_-_--_-_-_-_-_-_-_ CSTAD HALL BOOKING SYSTEM _-_-_-_-_-_-_-_-_-_-_-_-_-_");
        System.out.println("-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_\n");
        int numRows = getIntWithRegex(scanner, "^[1-9][0-9]*$", " => Config total rows in hall :  ", "Number");
        int numCols = getIntWithRegex(scanner, "^[1-9][0-9]*$", " => Config total seats per row in hall : ", "Number");
        String [][] morning = new String[numRows][numCols];
        String [][] afternoon = new String[numRows][numCols];
        String [][] night = new String[numRows][numCols];
        String [] bookingHistory = new String[0];
        initializeHall(morning,afternoon,night);
        char ch ;
        do {
            System.out.println("-----------------------------------------------------------------------------------");
            System.out.println(" # Menu ");
            System.out.println(" A :  Booking ");
            System.out.println(" B :  Hall ");
            System.out.println(" C :  Showtime ");
            System.out.println(" D :  Reboot Showtime ");
            System.out.println(" E :  History ");
            System.out.println(" F :  Exit ");
            System.out.println("-----------------------------------------------------------------------------------");
            System.out.print(" => Please select menu no :  ");
            ch = getCharWithRegex(scanner, "[a-fA-F]","( A-F )"," => Please select menu no : ");
            switch (Character.toUpperCase(ch)){
                case 'A' :{
                    bookingHistory=Booking(morning,afternoon,night,bookingHistory);
                    break ;
                }
                case 'B' :{
                    Hall(morning,afternoon,night);
                    break;
                }
                case 'C' : {
                    showTimeInformation();
                    break;
                }
                case 'D' :{
                    rebootSeat(morning,afternoon,night,bookingHistory);
                    break ;
                }
                case 'E' :{
                    displayBookingHistory(bookingHistory);
                    break;
                }
                case 'F' :{
                    System.out.println("-----------------------------------------------------------------------------------");
                    System.out.println("                                 Thank You !                                       ");
                    System.out.println("-----------------------------------------------------------------------------------");
                    System.exit(0);
                }
            }
        }while (true);
    }
    public static String[] Booking (String[][] morning , String[][] afternoon , String [][] night , String [] History){
        Scanner scanner = new Scanner(System.in);
        showTimeInformation(); // Show all time in hall !!
        System.out.print(" => Please Select Show Time ( A | B | C  ) :  ");
        char showTimeChoice = getCharWithRegex(scanner, "[a-cA-C]" , "( A-C )"," => Please Select Show Time ( A | B | C  ) : ");
        String[][] hall = getHall(morning, afternoon, night, showTimeChoice);
        assert hall != null;
        System.out.println("-----------------------------------------------------------------------------------");
        displayHall(hall,showTimeChoice);  // Show One hall by time
        String[] selectedSeats = selectedSeatsHall();
        String[] updatedHistory = Arrays.copyOf(History, History.length + 1);
        boolean isBookingSuccessful = true;
        boolean validateInputUser = true;
        String userName ="";
        char confirmation = 0;
        for (String input: selectedSeats) {
            String getUserInput = input.replaceAll("-", "");
            int number = Integer.parseInt(getUserInput.replaceAll("[^0-9]", ""));
            char letter = getUserInput.replaceAll("[^a-zA-Z]", "").charAt(0);
            for (int i = 0; i < hall.length; i++) {
                for (int j = 0; j < hall[i].length; j++) {
                    char seatAlphabet = (char) ('A' + i);
                    letter = Character.toUpperCase(letter);
                    if (seatAlphabet == letter && (number -1) == j) {
                        if (hall[i][j].equals("BO")) {
                            System.out.println("-----------------------------------------------------------------------------------");
                            System.out.println("                               !  ["+seatAlphabet+"-"+(1+j)+"] Already booked !");
                            System.out.println("-----------------------------------------------------------------------------------");
                            isBookingSuccessful = false;
                        }
                        else {
                            if(validateInputUser) {
                                userName = getStringWithRegex(scanner, "^[a-zA-Z0-9_]+$","your name", "=> Please enter userName : ");
                                System.out.print("> Are you sure to book? (Y/N) : ");
                                confirmation = getCharWithRegex(scanner, "[yYNn]+", "( Y or N )","=> Are you sure to book? (Y/N) :");
                                validateInputUser = false;
                            }
                            if (confirmation == 'y' || confirmation == 'Y' ) {
                                hall[i][j] = "BO";
                                String history = addToHidtorys( Arrays.toString(selectedSeats), userName ,showTimeChoice);
                                updatedHistory[updatedHistory.length - 1] = history;
                            }else {
                                System.out.println("-----------------------------------------------------------------------------------");
                                System.out.println("                               Cancel Booking !");
                                System.out.println("-----------------------------------------------------------------------------------");
                                isBookingSuccessful = false;
                            }
                        }
                    }
                }
            }
        }
        if(isBookingSuccessful) {
            System.out.println("-----------------------------------------------------------------------------------");
            System.out.println( "                            "+ Arrays.toString(selectedSeats)+" booked successfully.");
        }
        return updatedHistory;
    }
    private static void showTimeInformation() {
        System.out.println("\n---------------------------{ Start Booking Process }-----------------------------");
        System.out.println("\n # Showtime Information ");
        System.out.println(" > A < : Morning ( 10:00AM - 11:40PM )");
        System.out.println(" > B < : Afternoon ( 12:00AM - 1:50PM )");
        System.out.println(" > C < : Night ( 7:40AM - 9:00PM )\n");
    }
    private static String[] selectedSeatsHall() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("\n# INSTRUCTION :");
        System.out.println("# Single : A-1 ");
        System.out.println("# Multiple (separate by comma) : A-1,A-2");
        String userInput = getStringWithRegex(scanner,"^([A-Za-z]-\\d+)(?:,(?:[A-Za-z]-\\d+))*$"," A-1 or A-1,A-2)","=> Select seats (e.g., A-1 or A-1,A-2): ");
        String[] seatArray = userInput.split(",");
        return seatArray;
    }
    private static void Hall(String[][] morning, String[][] afternoon, String[][] night) {
        System.out.println("---------------------------------------------------------------------------------");
        System.out.println("# Hall: (A) Morning");
        for (int i = 0; i < morning.length; i++) {
            for (int j = 0; j < morning[i].length; j++) {
                System.out.print("|" + (char) ('A' + i) + "-" + (j + 1) + " :: " + morning[i][j] + "|  ");
            }
            System.out.println();
        }
        System.out.println("---------------------------------------------------------------------------------");
        System.out.println("# Hall: (B) Afternoon");
        for (int i = 0; i < afternoon.length; i++) {
            for (int j = 0; j < afternoon[i].length; j++) {
                System.out.print("|" + (char) ('A' + i) + "-" + (j + 1) + " :: " + afternoon[i][j] + "|  ");
            }
            System.out.println();
        }
        System.out.println("---------------------------------------------------------------------------------");
        System.out.println("# Hall: (C) Night");
        for (int i = 0; i < night.length; i++) {
            for (int j = 0; j < night[i].length; j++) {
                System.out.print("|" + (char) ('A' + i) + "-" + (j + 1) + " :: " + night[i][j] + "|  ");
            }
            System.out.println();
        }
    }
    public static String[] displayHall(String[][] hall,char ch) {
        System.out.println("# Hall : "+ ch);
        for (int i = 0; i < hall.length; i++) {
            for (int j = 0; j < hall[i].length; j++) {
                System.out.print("|" + (char) ('A' + i) + "-" + (j + 1) + " :: " + hall[i][j] + "|  ");
            }
            System.out.println();
        }

        return new String[0];
    }
    private static String addToHidtorys(String seat, String userName, char ch) {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/y hh:mm");
        String formattedDateTime = localDateTime.format(formatter);
        char hall = Character.toUpperCase(ch);
        return String.format(
                "#No : " +
                        "\n#SEATS : " + seat +
                        "\n#HALL           #USER                                     #Crete at" +
                        "\n  %-10s   %-20s                  %-15s"
                ,hall,userName,formattedDateTime
        );
    }
    public static void displayBookingHistory(String[] bookingHistory) {
        System.out.println("# Booking History:");
        if (bookingHistory[0] == null) {
            System.out.println("-----------------------------------------------------------------------------------");
            System.out.println("                          There is no history");
        } else {
            for (String history : bookingHistory) {
                System.out.println("-----------------------------------------------------------------------------------");
                System.out.println(history);
            }
        }
    }
    public static String[][] getHall(String[][] morning, String[][] afternoon, String[][] night, char ch) {
        switch (Character.toUpperCase(ch)) {
            case 'A':
                return morning;
            case 'B':
                return afternoon;
            case 'C':
                return night;
            default:
                // Handle the case when ch is not 'A', 'B', or 'C'
                System.out.println("Invalid showtime choice.");
                return null; // Or return an appropriate default value or throw an exception.
        }
    }
    public static void initializeHall (String[][] morning, String[][] afternoon, String[][] night) {
        initHall(morning);
        initHall(afternoon);
        initHall(night);
    }
    public static void initHall(String[][] hall){
        for (String[] strings: hall) {
            Arrays.fill(strings, "AV");
        }
    }
    public static void rebootSeat(String[][] morning, String[][] afternoon, String[][] night, String[] bookingHistory) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(" => Are you Soure to Reboot Hall ? (Y/N) : ");
        char isSure = getCharWithRegex(scanner,"[YyNn]+","( Y or N )"," Please input Y or N :");
        isSure = Character.toUpperCase(isSure);
        if (isSure == 'Y'){
            initHall(morning);
            initHall(afternoon);
            initHall(night);
            clearHistory(bookingHistory);
        }

    }
    public static void clearHistory(String[] bookingHistory) {
        System.out.println("-----------------------------------------------------------------------------------");
        for (int i = 0; i < bookingHistory.length; i++) {
            bookingHistory[i] = null;
        }
        System.out.println("                           Booking history cleared.");
    }

    //------------------------------{ Regex format }---------------------------------
    public static char getCharWithRegex(Scanner scanner, String regex,String message ,String input ) {
        char ch;
        do {
            String inputs = scanner.next();
            if (inputs.matches(regex)) {
                ch = inputs.charAt(0);
                break;
            } else {
                System.out.println("    Invalid input. Please enter " + message + " !");
                System.out.print(input);
            }
        } while (true);
        return ch;
    }
    public static int getIntWithRegex(Scanner scanner, String regex, String prompt,String message) {
        int value = 0;
        while (value <= 0) {
            System.out.print(prompt);
            String input = scanner.next();
            if (input.matches(regex)) {
                value = Integer.parseInt(input);
            } else {
                System.out.println("    Invalid input. Please enter "+ message +"!");
            }
        }
        return value;
    }
    public static String getStringWithRegex(Scanner scanner, String regex,String message, String prompt) {

        while (true) {
            System.out.print(prompt);
            String input = scanner.next();
            if (input.matches(regex)) {
                return input ;
            } else {
                System.out.println("Invalid input. Please enter a string " + message + "!!");
            }
        }
    }








}