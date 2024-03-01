import org.nocrala.tools.texttablefmt.*;

import java.util.Scanner;

/**
 * This class represents a Bus Management System that allows users to check bus information,
 * book seats, cancel bookings, reset buses, and display information in a pagination.
 */
public class Main {

    // Scanner for user input
    private static final Scanner scanner = new Scanner(System.in);

    // init Constants for pagination
    private static final int pageSize = 5;

    // Variables to store user input and bus information
    private static int numberOfBuses;
    private static int numberSeatsPerBus;
    private static int[][] buses;
    private static int availableSeats;
    private static int unavailableSeats;
    private static int currentPage = 1;

    /**
     * Main method to start the Bus Management System.
     */
    public static void main(String[] args) {

        System.out.println("-------------- Setting up Buses --------------");

        // Input for the number of buses
        System.out.print("-> Enter number of Buses: ");
        String inputNumberOfBuses = scanner.next();
        while (!isInputValid(inputNumberOfBuses, 50)) {
            System.out.println(Color.ANSI_RED.getColor() + "-> Error: Invalid Input. Please enter a number between 1 and 50." + Color.ANSI_BLACK.getColor());
            System.out.print("-> Enter number of Buses: ");
            inputNumberOfBuses = scanner.next();
        }
        numberOfBuses = Integer.parseInt(inputNumberOfBuses);

        // Input for the number of seats per bus
        System.out.print("-> Enter number Seat of bus: ");
        String inputNumberSeatsPerBus = scanner.next();
        while (!isInputValid(inputNumberSeatsPerBus, 100)) {
            System.out.println(Color.ANSI_RED.getColor() + "-> Error: Invalid Input. Please enter a number between 1 and 100." + Color.ANSI_BLACK.getColor());
            System.out.print("-> Enter number Seat of bus: ");
            inputNumberSeatsPerBus = scanner.next();
        }
        numberSeatsPerBus = Integer.parseInt(inputNumberSeatsPerBus);

        // Initialize the buses array
        buses = new int[numberOfBuses][numberSeatsPerBus];

        // Menu options
        int option;

        do {
            System.out.println("-------------- Bus Management System --------------");
            System.out.println("1- Check Bus");
            System.out.println("2- Booking Bus");
            System.out.println("3- Cancel Booking");
            System.out.println("4- Reset Bus");
            System.out.println("5- Exit");
            System.out.println("---------------------------------------------------");
            System.out.print("-> Choose option(1-5): ");
            while (!scanner.hasNext("[1-9]*")) {
                System.out.println(Color.ANSI_RED.getColor() + "-> Error: Invalid Input. Please enter only numeric values." + Color.ANSI_BLACK.getColor());
                System.out.print("-> Choose option(1-5): ");
                scanner.next();
            }
            option = scanner.nextInt();

            switch (option) {
                case 1:
                    displayPaginatedBusInformation();
                    break;
                case 2:
                    bookingBus();
                    break;
                case 3:
                    cancelBooking();
                    break;
                case 4:
                    resetBus();
                    break;
                case 5:
                    System.out.println("-> Good bye!");
                    break;
                default:
                    System.out.println("-> Invalid option");
                    break;
            }
        } while (option != 5);

    }

    /**
     * Displays paginated information about buses, including available and unavailable seats.
     */
    private static void displayPaginatedBusInformation() {

        // Calculate start and end index for the current page
        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, numberOfBuses);

        // Define cell style for table
        CellStyle numberCellStyle = new CellStyle(CellStyle.HorizontalAlign.center);

        // Create a table with borders
        Table table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);

        // Set column widths for the table
        table.addCell("Display All Bus information (Page " + currentPage + "/" + getTotalPages() + ")", numberCellStyle, 4);
        table.addCell(Color.ANSI_GREEN.getColor() + "ID" + Color.ANSI_BLACK.getColor(), numberCellStyle);
        table.addCell(Color.ANSI_GREEN.getColor() + "Seat" + Color.ANSI_BLACK.getColor(), numberCellStyle);
        table.addCell(Color.ANSI_GREEN.getColor() + "Available" + Color.ANSI_BLACK.getColor(), numberCellStyle);
        table.addCell(Color.ANSI_RED.getColor() + "Unavailable" + Color.ANSI_BLACK.getColor(), numberCellStyle);

        // Set column widths for the table
        table.setColumnWidth(0, 10, 15);
        table.setColumnWidth(1, 20, 25);
        table.setColumnWidth(2, 20, 25);
        table.setColumnWidth(3, 20, 25);

        // Check if there is data to display for the current page
        if (startIndex >= numberOfBuses) {
            System.out.println(Color.ANSI_RED.getColor() + "-> Error: No data to display for the current page." + Color.ANSI_BLACK.getColor());
            return;
        }

        // Loop through buses and populate the table
        for (int i = startIndex; i < endIndex; i++) {
            availableSeats = 0; // reset available seats
            unavailableSeats = 0; // reset unavailable seats
            for (int j = 0; j < numberSeatsPerBus; j++) { // loop through seats for each bus
                if (buses[i][j] == 0) { // check if seat is available or unavailable
                    availableSeats++; // increment available seats
                } else {
                    unavailableSeats++; // increment unavailable seats
                }
            }
            table.addCell(String.valueOf(i + 1), numberCellStyle);
            table.addCell(Color.ANSI_BLUE.getColor() + numberSeatsPerBus + Color.ANSI_BLACK.getColor(), numberCellStyle);
            table.addCell(Color.ANSI_GREEN.getColor() + availableSeats + Color.ANSI_BLACK.getColor(), numberCellStyle);
            table.addCell(Color.ANSI_RED.getColor() + (unavailableSeats) + Color.ANSI_BLACK.getColor(), numberCellStyle);
        }

        // Render and display the table
        System.out.println(table.render());

        // Display pagination options
        System.out.println(Color.ANSI_BLUE.getColor() + "1. First" + "\t\t" + "2. Next Page" + "\t\t" + "3. Previous" + "\t\t" + "4. Last Page" + "\t\t" + "5. See Detail Each Bus" + "\t\t" + "6. Back" + Color.ANSI_BLACK.getColor());
        System.out.print("-> Choose option(1-6): ");
        while (!scanner.hasNext("[1-9]*")) {
            System.out.println(Color.ANSI_RED.getColor() + "-> Error: Invalid Input. Please enter only numeric values." + Color.ANSI_BLACK.getColor());
            System.out.print("-> Choose option(1-6): ");
            scanner.next();
        }
        int option = scanner.nextInt();

        // Handle pagination option
        paginationOption(option);
    }

    /**
     * Allows a user to book a seat on a specific bus.
     */
    private static void bookingBus() {

        // Input for bus ID
        System.out.print("-> Enter bus’s Id: ");
        String inputBusId = scanner.next();
        while (!isInputValid(inputBusId, numberOfBuses)) {
            System.out.println(Color.ANSI_RED.getColor() + "-> Error: Invalid Input. Please enter a bus ID between 1 and " + numberOfBuses + "." + Color.ANSI_BLACK.getColor());
            System.out.print("-> Enter bus’s Id: ");
            inputBusId = scanner.next();
        }
        int busId = Integer.parseInt(inputBusId);

        // Input for seat number
        System.out.print("-> Enter Seat number to booking: ");
        String inputSeatNumber = scanner.next();
        while (!isInputValid(inputSeatNumber, numberSeatsPerBus)) {
            System.out.println(Color.ANSI_RED.getColor() + "-> Error: Invalid Input. Please enter a seat number between 1 and " + numberSeatsPerBus + "." + Color.ANSI_BLACK.getColor());
            System.out.print("-> Enter Seat number to booking: ");
            inputSeatNumber = scanner.next();
        }
        int seatNumber = Integer.parseInt(inputSeatNumber);

        // Confirm booking choice
        System.out.print("=> Do you want to book Seat number " + seatNumber + "? (y/n): ");
        String inputChoice = scanner.next();
        while (!inputChoice.matches("[ynYN]")) {
            System.out.println(Color.ANSI_RED.getColor() + "-> Error: Invalid Input. Please enter 'y' or 'n'." + Color.ANSI_BLACK.getColor());
            System.out.print("=> Do you want to book Seat number " + seatNumber + "? (y/n): ");
            inputChoice = scanner.next();
        }
        char choice = inputChoice.charAt(0); // get first character of input

        // Process booking based on user choice
        if (choice == 'y' || choice == 'Y') {
            if (buses[busId - 1][seatNumber - 1] == 0) { // check if seat is available
                buses[busId - 1][seatNumber - 1] = 1; // book seat
                System.out.println(Color.ANSI_GREEN.getColor() + "-> Seat number " + seatNumber + " was booked successfully!" + Color.ANSI_BLACK.getColor());
            } else {
                System.out.println(Color.ANSI_RED.getColor() + "-> Error: Seat number " + seatNumber + " is already booked." + Color.ANSI_BLACK.getColor());
            }
        }

        // Display detailed information for the booked bus
        displayBusInformation(busId);
    }

    /**
     * Allows a user to cancel a booking for a seat on a specific bus.
     */
    private static void cancelBooking() {

        // Input for bus ID
        System.out.print("-> Enter bus’s Id: ");
        String inputBusId = scanner.next();
        while (!isInputValid(inputBusId, numberOfBuses)) {
            System.out.println(Color.ANSI_RED.getColor() + "-> Error: Invalid Input. Please enter a bus ID between 1 and " + numberOfBuses + "." + Color.ANSI_BLACK.getColor());
            System.out.print("-> Enter bus’s Id: ");
            inputBusId = scanner.next();
        }
        int busId = Integer.parseInt(inputBusId);

        // Display information for the selected bus
        displayBusInformation(busId);

        // Input for seat number to cancel booking
        System.out.print("-> Enter Seat number to cancel booking: ");
        String inputSeatNumber = scanner.next();
        while (!isInputValid(inputSeatNumber, numberSeatsPerBus)) {
            System.out.println(Color.ANSI_RED.getColor() + "-> Error: Invalid Input. Please enter a seat number between 1 and " + numberSeatsPerBus + "." + Color.ANSI_BLACK.getColor());
            System.out.print("-> Enter Seat number to cancel booking: ");
            inputSeatNumber = scanner.next();
        }
        int seatNumber = Integer.parseInt(inputSeatNumber);

        // Confirm cancellation choice
        System.out.print("=> Do you want to cancel booking seat number " + seatNumber + "? (y/n): ");
        String inputChoice = scanner.next();
        while (!inputChoice.matches("[ynYN]")) {
            System.out.println(Color.ANSI_RED.getColor() + "-> Error: Invalid Input. Please enter 'y' or 'n'." + Color.ANSI_BLACK.getColor());
            System.out.print("=> Do you want to cancel booking seat number " + seatNumber + "? (y/n): ");
            inputChoice = scanner.next();
        }
        char choice = inputChoice.charAt(0);

        // Process cancellation based on user choice
        if (choice == 'y' || choice == 'Y') {
            if (buses[busId - 1][seatNumber - 1] == 1) { // check if seat is unavailable
                buses[busId - 1][seatNumber - 1] = 0; // cancel booking
                System.out.println(Color.ANSI_GREEN.getColor() + "-> Seat number " + seatNumber + " was canceled booking successfully!" + Color.ANSI_BLACK.getColor());
            } else {
                System.out.println(Color.ANSI_RED.getColor() + "-> Error: Seat number " + seatNumber + " is already available." + Color.ANSI_BLACK.getColor());
            }
        }
    }

    /**
     * Resets all seats on a specific bus.
     */
    private static void resetBus() {

        // Input for bus ID
        System.out.print("-> Enter bus Id: ");
        String inputBusId = scanner.next();
        while (!isInputValid(inputBusId, numberOfBuses)) {
            System.out.println(Color.ANSI_RED.getColor() + "-> Error: Invalid Input. Please enter a bus ID between 1 and " + numberOfBuses + "." + Color.ANSI_BLACK.getColor());
            System.out.print("-> Enter bus’s Id: ");
            inputBusId = scanner.next();
        }
        int busId = Integer.parseInt(inputBusId);

        // Confirm reset choice
        System.out.print("=> Do you want to reset bus " + busId + "? (y/n): ");
        String inputChoice = scanner.next();
        while (!inputChoice.matches("[ynYN]")) {
            System.out.println(Color.ANSI_RED.getColor() + "-> Error: Invalid Input. Please enter 'y' or 'n'." + Color.ANSI_BLACK.getColor());
            System.out.print("=> Do you want to reset bus " + busId + "? (y/n): ");
            inputChoice = scanner.next();
        }
        char choice = inputChoice.charAt(0);

        // Process reset based on user choice
        if (choice == 'y' || choice == 'Y') {
            for (int i = 0; i < numberSeatsPerBus; i++) { // loop through seats for the selected bus
                buses[busId - 1][i] = 0; // reset seat
            }
            System.out.println(Color.ANSI_GREEN.getColor() + "-> Bus " + busId + " was reset successfully!" + Color.ANSI_BLACK.getColor());
        }
    }

    /**
     * Displays detailed information about available and unavailable seats on a specific bus.
     *
     * @param busId The ID of the bus to display information for.
     */
    private static void displayBusInformation(int busId) {

        availableSeats = 0; // reset available seats
        unavailableSeats = 0; // reset unavailable seats

        // Define cell style for table
        CellStyle numberCellStyle = new CellStyle(CellStyle.HorizontalAlign.center);

        // Create a table with borders
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        table.addCell("Bus " + busId + " Information", numberCellStyle, 5);

        table.addCell("Seat", numberCellStyle);
        table.addCell("Seat", numberCellStyle);
        table.addCell("Seat", numberCellStyle);
        table.addCell("Seat", numberCellStyle);
        table.addCell("Seat", numberCellStyle);

        table.setColumnWidth(0, 10, 15);
        table.setColumnWidth(1, 10, 15);
        table.setColumnWidth(2, 10, 15);
        table.setColumnWidth(3, 10, 15);
        table.setColumnWidth(4, 10, 15);

        for (int i = 0; i < numberSeatsPerBus; i++) {
            if (buses[busId - 1][i] == 0) {
                table.addCell(Color.ANSI_GREEN.getColor() + "(+) " + (i + 1) + Color.ANSI_GREEN.getColor(), numberCellStyle);
                availableSeats++;
            } else {
                table.addCell(Color.ANSI_RED.getColor() + "(-) " + (i + 1) + Color.ANSI_RED.getColor(), numberCellStyle);
                unavailableSeats++;
            }
        }

        System.out.println(table.render());
        System.out.println(Color.ANSI_RED.getColor() + "(-) : Unavailable(" + unavailableSeats + ")" + Color.ANSI_BLACK.getColor() + "\t\t\t\t" + Color.ANSI_GREEN.getColor() + "(+) : Available(" + availableSeats + ")" + Color.ANSI_BLACK.getColor());
    }

    /**
     * Displays the first page of all bus information.
     */
    private static void displayFirstPageOfAllBusInformation() {
        currentPage = 1; // reset current page to 1
        displayPaginatedBusInformation(); // display first page
    }

    /**
     * Displays the next page of all bus information.
     */
    private static void displayNextPageOfAllBusInformation() {
        int totalPages = getTotalPages();
        if (currentPage < totalPages) { // check if there is a next page
            currentPage++; // increment current page
        }
        displayPaginatedBusInformation();
    }

    /**
     * Displays the previous page of all bus information.
     */
    private static void displayPreviousPageOfAllBusInformation() {
        if (currentPage > 1) { // check if there is a previous page
            currentPage--; // decrement current page
        }
        displayPaginatedBusInformation();
    }

    /**
     * Displays the last page of all bus information.
     */
    private static void displayLastPageOfAllBusInformation() {
        int totalPages = (int) Math.ceil((double) numberOfBuses / pageSize); // round up to the nearest integer value
        currentPage = totalPages; // set current page to last page
        displayPaginatedBusInformation();
    }

    /**
     * Handles pagination options based on user input.
     *
     * @param option The selected pagination option.
     */
    private static void paginationOption(int option) {
        switch (option) {
            case 1:
                displayFirstPageOfAllBusInformation();
                break;
            case 2:
                displayNextPageOfAllBusInformation();
                break;
            case 3:
                displayPreviousPageOfAllBusInformation();
                break;
            case 4:
                displayLastPageOfAllBusInformation();
                break;
            case 5:
                System.out.print("-> Enter bus’s Id: ");
                String inputBusId = scanner.next();
                while (!isInputValid(inputBusId, numberOfBuses)) {
                    System.out.println(Color.ANSI_RED.getColor() + "-> Error: Invalid Input. Please enter a bus ID between 1 and " + numberOfBuses + "." + Color.ANSI_BLACK.getColor());
                    System.out.print("-> Enter bus’s Id: ");
                    inputBusId = scanner.next();
                }
                int busId = Integer.parseInt(inputBusId);

                if (busId > 0 && busId <= numberOfBuses) {
                    displayBusInformation(busId);
                } else {
                    System.out.println(Color.ANSI_RED.getColor() + "-> Error: Invalid Input. Please enter a bus ID between 1 and " + numberOfBuses + "." + Color.ANSI_BLACK.getColor());
                }
                break;
            case 6:
                break;
            default:
                System.out.println("-> Invalid option");
                break;
        }
    }

    /**
     * Calculates the total number of pages needed for pagination.
     *
     * @return The total number of pages.
     */
    private static int getTotalPages() {
        return (int) Math.ceil((double) numberOfBuses / pageSize); // round up to the nearest integer value
    }

    /**
     * Checks if user input is a valid numeric value within a specified range.
     *
     * @param input The user input to validate.
     * @param max   The maximum allowed value.
     * @return True if the input is valid, false otherwise.
     */
    private static boolean isInputValid(String input, int max) {
        if (!input.matches("^[1-9][0-9]*$")) {
            return false; // input is not a number
        }
        int number = Integer.parseInt(input);
        if (number < 1 || number > max) {
            return false; // number is not within range 1-max
        }
        return true;
    }

    /**
     * Enum representing ANSI color codes for console text color.
     */
    enum Color {
        ANSI_RED("\u001B[31m"),
        ANSI_BLACK("\u001B[30m"),
        ANSI_GREEN("\u001B[32m"),
        ANSI_BLUE("\u001B[34m");

        private final String color;

        Color(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }
    }
}
