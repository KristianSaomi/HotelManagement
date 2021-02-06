package hotelmanagement2;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

public class HotelManagement2 {

    private static final String url = "jdbc:mysql://localhost:3306/HotelManagementTest?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String user = "root";
    private static final String password = "Elzouki12!";
    private static Statement st = null;
    private static ResultSet rs = null;
    private static PreparedStatement pst = null;

    static String email;
    static String passWord;

    static String customerId;
    static int roomNumber;
    static int hmdays;
    static int packageId;

    static boolean exit = false;

    static List<Customer> customer = new ArrayList<>();
    public static ArrayList<Customer> customerList = new ArrayList<>();

    //Custom culors for outprints
    static Scanner sc = new Scanner(System.in);
    static String red = (char) 27 + "[31m";
    static String white = (char) 27 + "[37m";
    static String green = (char) 27 + "[32m";
    static String purple = (char) 27 + "[35m";
    static String blue = (char) 27 + "[34m";
    static String lightBlue = (char) 27 + "[36m";
    static String yellow = (char) 27 + "[33m";
    static String reset = (char) 27 + "[0m";
    static String black = (char) 27 + "[30m";

    //Stamp in & Stamp out for employee
    static LocalDateTime stampIn = LocalDateTime.now();

    public static void main(String[] args) {
        loadAndSave.readFromFile();
        System.out.println(customer);
        music.bgMusic();
        hotel();
        try ( Connection connection = DriverManager.getConnection(url, user, password)) {

            System.out.println("Connection succeeded!");

            //st = connection.createStatement();
            st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            mainMenu();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Something went wrong, please try again!");
        }
    }

    public static void mainMenu() throws SQLException {
        while (!exit) {
            try {
                List<Employee> employee = new ArrayList<>();
                employee.add(new Employee("Kristian Saomi", "Kristian@HotelParadise.com", "1"));
                employee.add(new Employee("Saral Zamani", "Saral@HotelParadise.com", "2"));
                employee.add(new Employee("Mohamed Abdel Monem", "Mohamed@HotelParadise.com", "3"));

                System.out.println(black + "Welcome to Hotel Paradise" + reset);
                System.out.println(blue + "Would you like to sign in as employee or customer?\n" + reset);
                System.out.println("Press 1. for employee \nPress 2. for customer");
                System.out.println(red + "Press 3. for music panel " + red);
                System.out.println("Press 0. to exit");
                int signIn = sc.nextInt();
                sc.nextLine();

                switch (signIn) {
                    case 1:
                        printEmpolyees(employee);

                        System.out.println("Log in:");
                        System.out.println("Enter E-mail:");
                        email = sc.nextLine();
                        System.out.println("Please enter your password");
                        passWord = sc.nextLine();

                        boolean yesOrNo = employee.stream().anyMatch(HotelManagement2::emailCheck);
                        if (yesOrNo == true) {
                            // employee.stream().filter(o -> o.getEmail().equalsIgnoreCase(email))
                            //       .forEach(p -> System.out.println(blue + "Hi " + p.getName() + blue));
                            employee.stream().filter(o -> o.getEmail().equalsIgnoreCase(email)).filter(o -> o.getPassword().equals(passWord))
                                    .forEach(p -> System.out.println(blue + "Welcome " + p.getName() + " you are now logged in" + blue));
                            employeeMenu();
                        } else {
                            System.err.println(red + "Wrong E-mail or password!" + reset);
                            System.out.println("Please try again");
//                        mainMenu();
                        }
                        break;

                    case 2:
                        System.out.println("Welcome to Hotel Paradise!");
                        customerMenu();
                        break;
                    case 3:
                        System.out.println(blue + "\nDo you want to 'pause' or 'resume' the music?\n" + reset);
                        String pauseorResume = sc.nextLine();
                        if ("pause".equalsIgnoreCase(pauseorResume)) {
                            music.stopPlaying();
                            System.out.println(blue + "\nMusic is paused\n" + reset);
//                        mainMenu();
                        } else if ("resume".equalsIgnoreCase(pauseorResume)) {
                            music.startPlaying();
                            System.out.println(blue + "\nMusic is now playing\n");
//                        mainMenu();
                        } else {
                            System.out.println("not a valid option");
//                        mainMenu();
                        }
                        break;
                    case 0:
                        System.out.println("See you next time!");
                        exit = true;
                }
            } catch (InputMismatchException e) {
                System.err.println("Something went wrong");
                sc.next();
                mainMenu();
            }
        }
    }

    private static void employeeMenu() throws SQLException {
        while (!exit) {
            try {

                System.out.println("");
                System.out.println("1. Customer Details"); //SQL eller txt men txt är litle fucked up
                System.out.println("2. Search Customer Details"); //Hitta kunds "något" ..
                System.out.println("3. Update package, nights, roomtype"); //Klar diskutera rommtype
                System.out.println("4. Check out a customer "); //Kvitto klar
                System.out.println("5. Book a room for a customer"); //Method reference
                System.out.println("6. Order Food for Particular Room"); //Method reference
                System.out.println("7. Registered customers");
                System.out.println(red + "0. Stamp out" + reset); //Klart

                int empChoice = sc.nextInt();
                sc.nextLine();

                switch (empChoice) {
                    case 1:
                        select("customer");
//                        employeeMenu();
                        break;
                    case 2:
                        //Se booking tabellen + befintliga kvitton på en kund
                        System.out.println("What's the roomnumber?");
                        int roomnumber = sc.nextInt();
                        sc.nextLine();
                        printTable("Select * from test3 where roomNumber = " + roomnumber + ";");
//                        select("test3");

//                        employeeMenu();
                        break;
                    case 3:
                try {

                        //Uppatera packet och antal nätter, hotelrumstyp -->
                        select("test3");
                        System.out.println("\nWhat is your customerID");
                        int customerID = sc.nextInt();
                        sc.nextLine();
                        System.out.println("What would you like to change\n1. Nights\n2. Room type\n3. Package");
                        int var = sc.nextInt();
                        sc.nextLine();
                        switch (var) {
                            case 1:
                                System.out.println("How many nights would you like to stay for?");
                                int nights = sc.nextInt();
                                sc.nextLine();
                                pst = st.getConnection().prepareStatement("UPDATE bookings SET numberofnight = ? WHERE customerid = ?");

                                pst.setInt(1, nights);
                                pst.setInt(2, customerID);
                                pst.executeUpdate();
                                System.out.println(blue + "Number of nights for customer: " + customerID + " is now updated" + reset);
                                break;
                            case 2:
                                System.out.println("What room would you like to change to?");
                                select("test2");
                                int room = sc.nextInt();
                                sc.nextLine();
                                pst = st.getConnection().prepareStatement("UPDATE bookings SET roomnumber= ? WHERE customerid = ?");

                                pst.setInt(1, room);
                                pst.setInt(2, customerID);
                                pst.executeUpdate();

                                System.out.println("Room for customer: " + customerID + " is updated");
                                break;
                            case 3:
                                System.out.println("Which package would you like to change to?");
                                select("Package");
                                int packageid = sc.nextInt();
                                sc.nextLine();
                                pst = st.getConnection().prepareStatement("UPDATE bookings SET packageid= ? WHERE customerid = ?");

                                pst.setInt(1, packageid);
                                pst.setInt(2, customerID);
                                pst.executeUpdate();
                                System.out.println("Package for customer " + customerID + " is now updated!");
                                break;
                        }
//                        employeeMenu();
                    } catch (InputMismatchException e) {
                        System.out.println("Your choice is invalid");
                        sc.next();

                        employeeMenu();
                    }
                    break;
                    case 4:
                        //Radera en kund och dennes bokning, be kunden betala först.
                        printTable("select * from test3");
                        System.out.println("\nRoom number?");
                        int roomNumber = sc.nextInt();
                        sc.nextLine();
                        System.out.println("Bill will be: ");
                        printTable("select TotalPayments from test3 where roomNumber =" + roomNumber + ";");
                        checkout(roomNumber);
//                        employeeMenu();
                        break;
                    case 5:
                        //Boka åt en kund
                        checkIn();
//                        employeeMenu();
                        break;
                    case 6:
                        //Beställ mat på en kunds räkning
                        foodMenu();
                        orderFood();
//                        employeeMenu();
                        break;
                    case 7:
                        System.out.println(reset + "All customers who have been registered\n");
                        System.out.println(customer);
//                        employeeMenu();
                        break;
                    case 0:
                        LocalDateTime stampOut = LocalDateTime.now();
                        System.out.println(blue + "You stamped out: " + stampOut + blue);
                        long between = ChronoUnit.SECONDS.between(stampIn, stampOut);
                        System.out.println(blue + "Great you worked " + between + " seconds \n" + blue);
                        System.out.println("Good job!");
                        mainMenu();
                        exit = true;
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice");
                sc.next();
                employeeMenu();
            }
        }
    }

    private static void customerMenu() throws SQLException {
        while (!exit) {
            try {
                System.out.println("");
                System.out.println("1. Display room details"); //Klar
                System.out.println("2. Display room availability"); //Klar
                System.out.println("3. Checkin");  //Klart
                System.out.println("4. Order food"); //Klart dunder
                System.out.println("5. Checkout");  //Klart dunder
                System.out.println(red + "0. Exit" + reset);

                int cusChoice = sc.nextInt();
                sc.nextLine();

                switch (cusChoice) {
                    case 1:
                        roomType();
                        break;
                    case 2:
                        System.out.println(blue + "These rooms are avaible right now!" + reset);
                        select("test2");
                        break;
                    case 3:

                        System.out.println("Do you want to continue your checkin?\nEnter yes/no");
                        String bookARoom = sc.nextLine();
                        if ("yes".equalsIgnoreCase(bookARoom)) {
                            checkIn();
                        } else {
                            System.out.println(blue + "We hope you come back another time!" + blue);
                        }
                        break;
                    case 4:
                        System.out.println(blue + "This is our food menu: " + reset);
///////////////////////////////////////////////////////////////////////////////////////////////////////////
//                Scanner lol = new Scanner(System.in);
//
//                System.out.println("Enter sname:");
//                String sname = lol.nextLine();
//                System.out.println("Enter sid:");
//                String sid = lol.nextLine();
//                System.out.println("Enter sage:");
//                int sage = lol.nextInt();
//                lol.nextLine();
//
//                String f = "\n" + sid + "\n" + sname + "\n" + sage;
//                byte[] b = f.getBytes();
//
//                try {
//                    FileWriter fw = new FileWriter("roomType.txt", true);
//                    fw.write(f);
//                    fw.close();
//                } catch (IOException IOE) {
//                    System.err.println("Error");
//                }
///////////////////////////////////////////////////////////////////////////////////////////////////////////
                        foodMenu();
                        System.out.println("\nWould you like to order any of our food?");
                        System.out.println("Yes or no");
                        String orderFood = sc.nextLine();
                        if ("yes".equalsIgnoreCase(orderFood)) {
//                            System.out.println("Which ItemId would you like to order");
                            orderFood();

                        } else {
                            System.out.println("Okay! Kitchen closes 10:00 PM");
                        }
                        break;
                    case 5:
                        System.out.println("Hope you are enjoying stay at Hotel Paradise!");

                        System.out.println("Please enter room number");
                        int roomnumber = sc.nextInt();
                        sc.nextLine();

                        System.out.println("Your bill will be: ");
                        printTable("select TotalPayments from test3 where roomNumber =" + roomnumber + ";");

                        System.out.println("\nAre sure you want to pay and checkout now? \nyes/no?");
                        String yes = sc.nextLine();
                        if ("yes".equalsIgnoreCase(yes)) {
                            checkout(roomnumber);
                            System.out.println("\nThanks for staying at Hotel Paradise!");
                        }

                        break;
                    case 0:
                        System.out.println(blue + "Thanks for visiting, welcome back!" + reset);
                        mainMenu();
                        exit = true;
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Something went wrong!");
                sc.next();
                customerMenu();
            }
        }
    }

    public static void checkout(int roomnumber) throws SQLException {
        pst = st.getConnection().prepareStatement("UPDATE bookings SET checkout = ? WHERE roomNumber = ?");
        Date checkoutDate = Date.valueOf(LocalDate.now());
        pst.setDate(1, checkoutDate);
        pst.setInt(2, roomnumber);

        pst.executeUpdate();
        st.executeUpdate("delete from bookings where roomNumber =" + roomnumber + ";");
        System.out.println(blue + "Checkout is now complete" + reset);

    }

    public static <T extends Employee> void printEmpolyees(List<T> employees) {
        System.out.println(blue + "Registered employees: ");
        for (T Employee : employees) {
            Employee.callEmployees();
        }
    }
//java.sql.SQLIntegrityConstraintViolationException

    private static void checkIn() throws SQLException {
        try {
            select("test2");
            System.out.println(blue + "\nWhich room do you wish to have?" + reset);
            roomNumber = sc.nextInt();
            sc.nextLine();
            if (roomNumber <= 199 || roomNumber >= 212) {
                System.out.println(red + "\nYou have choosen a invalid room number\nPlease try again" + reset);
                checkIn();
            }
            select("Package");

            System.out.println(blue + "\nWhich package do you wish to have?" + reset);
            packageId = sc.nextInt();
            sc.nextLine();
            if (packageId <= 0 || packageId >= 4) {
                System.out.println(red + "\nYou have choosen a invalid package number\nPlease try again" + reset);
                checkIn();
            }

            System.out.println(blue + "How many persons will you be?" + reset);
            int totalpersons = sc.nextInt();
            sc.nextLine();
            if (totalpersons <= 0) {
                System.out.println(red + "\nMust be atleast 1 person\nPlease try again" + reset);
                checkIn();
            }

            System.out.println(blue + "How many nights do you wish to stay?" + reset);
            hmdays = sc.nextInt();
            sc.nextLine();
            if (hmdays <= 0) {
                System.out.println(red + "Have to be atleast 1 night\nPlease try again" + reset);
                checkIn();
            }

            System.out.println(blue + "\nEnter personal info to confirm the reservation" + reset);

            System.out.print("First name: ");
            String fnamn = sc.nextLine();
            System.out.print("Last name: ");
            String enamn = sc.nextLine();
            System.out.print("Social security number YYYYMMDD-XXXX: ");
            String ssn = sc.nextLine();
            System.out.print("E-post: ");
            String emailing = sc.nextLine();
            System.out.print("Phone number: ");
            String phoneNr = sc.nextLine();
            System.out.print("Payment details: ");
            String bank = sc.nextLine();

            //Save to file txt
            customer.add(new Customer(fnamn, enamn, ssn, emailing, phoneNr, bank, roomNumber, packageId, totalpersons, hmdays));
            Collections.sort(customer, (p1, p2) -> p1.getFirstName().compareTo(p2.getFirstName()));
            loadAndSave.saveToFile();

            //SQL - Save to customer
            st.executeUpdate("INSERT INTO Customer(firstName, lastName, ssn, email, phone, bankinfo, totalGuest) VALUE('" + fnamn + "','" + enamn + "','" + ssn + "','" + emailing + "','" + phoneNr + "','" + bank + "','" + totalpersons + "');");

            //Hämtar customerId från tabel: customerIdRecent för att sätta in i "Booking"
            rs = st.executeQuery("SELECT * FROM customerIdRecent;");
            rs.next();

            customerId = rs.getString("customerId");
//            String customerId = rs.getString("customerId");

            //Sätter data i booking
            st.executeUpdate("INSERT INTO Bookings(customerId, roomNumber, numberOfNight, packageId) VALUE('" + customerId + "','" + roomNumber + "','" + hmdays + "','" + packageId + "');");

            welcomeDrink();
        } catch (SQLException e) {
            System.out.println(red + "\nThe room you have chosen haven't been cleaned up yet, please try again" + reset);
            invalidRoom();
        } catch (Exception e) {
            System.out.println("Something went wrong, please try again.");
        }
    }

    private static void orderFood() throws SQLException {
        try {

            System.out.println("\nWhat is your room number?");
            int rn = sc.nextInt();
            sc.nextLine();
            rs = st.executeQuery("select * from bookings where roomNumber =" + rn + ";");
            rs.next();

            String roomNumbersCustomerId = rs.getString("customerId");
            order(roomNumbersCustomerId);
            System.out.println("Would you like to add more items to your current order? ");
            System.out.println("\nEnter yes/no");
            String orderMore = sc.nextLine();
            if ("yes".equalsIgnoreCase(orderMore)) {
                order(roomNumbersCustomerId);
            }
            System.out.println(blue + "Thank you for your order! Your food is on the way." + reset);

        } catch (Exception e) {
            System.err.println("Something went wrong, please try again later!");
        }

    }

    public static void order(String roomNumbersCustomerId) throws SQLException {

        try {
            System.out.println("How many items would you like to add to your order? ");
            int numberOfItems = sc.nextInt();
            sc.nextLine();
            for (int i = 1; i <= numberOfItems; i++) {
                System.out.println("Which item would you like to purchase?");
                int itemId = sc.nextInt();
                sc.nextLine();
                st.executeUpdate("INSERT INTO Purchase(customerId, itemId) VALUE('" + roomNumbersCustomerId + "','" + itemId + "');");
            }

        } catch (InputMismatchException e) {
            System.out.println("Something went wrong!");
            sc.next();
        }

    }

    private static boolean emailCheck(Employee employee) {
        if (employee.getEmail().equalsIgnoreCase(email) && employee.getPassword().equalsIgnoreCase(passWord)) {
            return true;
        } else {
            return false;
        }
    }

    private static void select(String tableName) throws SQLException {
        rs = st.executeQuery("SELECT * FROM " + tableName + ";");

        int columnCount = rs.getMetaData().getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnNames[i] = rs.getMetaData().getColumnName(i + 1);
        }

        for (String columnName : columnNames) {
            System.out.print(PadRight(columnName));
        }
        while (rs.next()) {
            System.out.println();
            for (String columnName : columnNames) {
                String value = rs.getString(columnName);

                if (value == null) {
                    value = "null";
                }

                System.out.print(PadRight(value));
            }
        }
        System.out.println("");
    }

    private static void printTable(String sql) throws SQLException {
        rs = st.executeQuery(sql);
        int columnCount = rs.getMetaData().getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnNames[i] = rs.getMetaData().getColumnName(i + 1);
        }

        for (String columnName : columnNames) {
            System.out.print(PadRight(columnName));
        }

        while (rs.next()) {
            System.out.println();
            for (String columnName : columnNames) {
                String value = rs.getString(columnName);

                if (value == null) {
                    value = "null";
                }

                System.out.print(PadRight(value));
            }
        }
        System.out.println();
    }

    private static String PadRight(String string) {
        int totalStringLength = 30;
        int charsToPadd = totalStringLength - string.length();

        if (string.length() >= totalStringLength) {
            return string;
        }

        StringBuilder stringBuilder = new StringBuilder(string);
        for (int i = 0; i < charsToPadd; i++) {
            stringBuilder.append(" ");
        }

        return stringBuilder.toString();
    }

    private static void invalidRoom() throws SQLException {
        boolean tryit = true;
        while (tryit) {
            tryit = true;
            try {
                select("test2");
                System.out.println("Enter a valid room number");
                roomNumber = sc.nextInt();
                sc.nextLine();
                st.executeUpdate("INSERT INTO Bookings(customerId, roomNumber, numberOfNight, packageId) VALUE('" + customerId + "','" + roomNumber + "','" + hmdays + "','" + packageId + "');");
                welcomeDrink();
                tryit = false;
            } catch (SQLException e) {
                System.out.println(red + "\nTry with a room number from the display!" + reset);
            }

//            System.out.println(blue + "\nWhich room do you wish to have?" + reset);
//            roomNumber = sc.nextInt();
//            sc.nextLine();
//            if (roomNumber <= 199 || roomNumber >= 212) {
//                System.out.println("You have choosen an invalid room number\nPlease try again");
//                System.out.println("You have choosen an invalid room number\nPlease try again");
//                st.executeUpdate("INSERT INTO Bookings(customerId, roomNumber, numberOfNight, packageId) VALUE('" + customerId + "','" + roomNumber + "','" + hmdays + "','" + packageId + "');");
//roomNumber == 200 || roomNumber == 201 || roomNumber == 202 || roomNumber == 203 || roomNumber == 204 || roomNumber == 205 || roomNumber == 206 || roomNumber == 207 || roomNumber == 208 || roomNumber == 209 || roomNumber == 210 || roomNumber == 211) {
//            try {
        }
    }

    private static void welcomeDrink() throws SQLException {
        System.out.println(blue + "Thanks!\nYou have now registered and confirmed the booking" + reset);
        rs = st.executeQuery("SELECT * FROM bookings where customerId =" +customerId+ ";");
        rs.next();
        String roomNumberForWelcome = rs.getString("roomNumber");
        System.out.println(blue+"Your room number is: " + roomNumberForWelcome+reset);
        System.out.println(green + "Hotel Paradise offers a free welcome drink!" + reset + "\n");
        System.out.println(red + "\\    /" + red);
        System.out.println(red + " \\__/" + red);
        System.out.println(red + "  || " + red);
        System.out.println(red + "  || " + red);
        System.out.println(red + " ----" + red);
        System.out.println("");
        st.executeUpdate("INSERT INTO Purchase(customerId, itemId) VALUE('" + customerId + "',' 0 ');");
    }

    private static void foodMenu() {
        try {
            FileReader fin = new FileReader("food.txt");
            Scanner scr = new Scanner(fin);
            while (scr.hasNextLine()) {
                System.out.println(scr.nextLine());
            }
            scr.close();
        } catch (IOException IOE) {
            System.err.println(red + "Error - cant load food menu");
        }
    }
//     |
//    / \           v                               v
//   /   \                  v                               v
//  /     \                                                                            o                                 v      ____
// /_______\                             HOTEL + RESTAURANT                          -/-            v        v                 /___/|
//|         |           __________________|______________|___________________________/)                  _______________      |    ||
//|  _   _  |          /                                                      _______/.                /_______________/|     |    ||
//| |_| |_| |         / _______________________________________________________\_/      .              |  _        _  | |     |____||
//|  _   _  |_________|                    ________________                     |         .            | |_|      |_| | |    /    / |
//| |_| |_| |         |  _   _   _   _    | Hotel Paradise |    _   _   _   _   |          .           |  _        _  | |  /     /  |
//|  _   _  |         | |_| |_| |_| |_|   |________________|   |_| |_| |_| |_|  |           .          | |_|      |_| | |/      /   |
//| |_| |_| | P-HOUSE |  _   _   _   _                          _   _   _   _   |         __._______   |  _        _  | |_______|   |
//|  _   _  |         | |_| |_| |_| |_|        ______          |_| |_| |_| |_|  |        /  \/      /  | |_|      |_| | |       |   |
//| |_| |_| |         |  _   _   _   _        /______/\         _   _   _   _   |       /          /   |  _        _  | |       |  /|      / 
//|         |   ____  | |_| |_| |_| |_|      / _____\_/|       |_| |_| |_| |_|  |      /          /    | |_|  __  |_| | |       | |       /
//|    _    |  |||||| |                      | |  / / ||                        |     /__________/     |     |  |     | |       |/       /
//|___| |___|  |||||| | |____________________|_|_/ /__||________________________|______________________|_____|  |_____|/________/       /   
//|                                                                                                                                    /                                            
//|                                                                                                                                   /
//___________________________________________________________________________________________________________________________________/

    private static void hotel() {
        System.out.println(blue + "      / \\");
        System.out.println(blue + "    / \\  \\          v                               v");
        System.out.println(blue + "   /   \\  \\                    v                               v");
        System.out.println(blue + "  /     \\  /                                                                          o                                  v   ____");
        System.out.println(blue + " /_______\\/|                           HOTEL + RESTAURANT                          -/-            v        v                /___/|");
        System.out.println(blue + "|         | |         __________________|______________|___________________________/)                   _______________      |    ||");
        System.out.println(blue + "|  _   _  | |        /                                                      _______/.                  /_______________/|    |    ||");
        System.out.println(blue + "| |_| |_| | |       /________________________________________________________\\_/|      .              |  _        _  | |    |____||");
        System.out.println(blue + "|  _   _  |/______|                    ________________                       |   |      .             | |_|      |_| | |   /     /|");
        System.out.println(blue + "| |_| |_| |         |  _   _   _   _    | Hotel Paradise |    _   _   _   _   |   |       .            |  _        _  | |  /     / |");
        System.out.println(blue + "|  _   _  |         | |_| |_| |_| |_|   |________________|   |_| |_| |_| |_|  |   |        .           | |_|      |_| | | /     /  |");
        System.out.println(blue + "| |_| |_| | P-HOUSE |  _   _   _   _                          _   _   _   _   |   |      __._______    |  _        _  | |/_____|   |");
        System.out.println(blue + "|  _   _  |         | |_| |_| |_| |_|        ______          |_| |_| |_| |_|  |   |    /  \\/      /   | |_|      |_| | |      |   |");
        System.out.println(blue + "| |_| |_| |         |  _   _   _   _        /______/\\         _   _   _   _  |   /   /          /     |  _        _  | |      |  /|     /");
        System.out.println(blue + "|         |   ____  | |_| |_| |_| |_|      / _____\\_/|       |_| |_| |_| |_| |  /   /          /      | |_|  __  |_| | |      | |      /");
        System.out.println(blue + "|    _    |  |||||| |                      | |  / / ||                        | /   /__________/       |     |  |     | |      |/      /");
        System.out.println(blue + "|___| |___|  |||||| | |____________________|_|_/ /__||________________________|/_______________________|_____|  |_____|/_______/      / ");
        System.out.println(purple + "|                                                                                                                                  /  ");
        System.out.println(purple + "|                                                                                                                                 /");
        System.out.println(purple + "_________________________________________________________________________________________________________________________________/");
    }

    private static void roomType() {
        try {
            FileReader fin = new FileReader("roomType.txt");
            Scanner scr = new Scanner(fin);

            while (scr.hasNextLine()) {
                System.out.println(scr.nextLine());
            }
            scr.close();
        } catch (IOException IOE) {
            System.err.println(red + "Error");
        }
    }
}
