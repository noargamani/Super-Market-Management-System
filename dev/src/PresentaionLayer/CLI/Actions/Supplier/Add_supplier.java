package Presentaion_layer.CLI.Actions.Supplier;

import BusinessLayer.Suppliers.Classes.Contract;
import BusinessLayer.Suppliers.Classes.Supplier_Card;
import Service_layer.Suppliers_Service;

import java.sql.SQLException;
import java.util.*;

import static BusinessLayer.Constants.PhoneNumregex;

public class Add_supplier {

    /**
     * Adds a new supplier to the Suppliers_Service.
     *
     * @param service The Suppliers_Service object to add the supplier to.
     */
    public static void Add(Suppliers_Service service) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        //name
        String name=terminal_IO(scanner,"Supplier name");
        while (service.ContainsSupplierName(name)){
            System.out.println("Supplier exist. please enter new supplier");
            name=terminal_IO(scanner,"Supplier name");
        }
        //address
        String address=terminal_IO(scanner,"Supplier address");

        //manufactures
        ArrayList<String > manufactures=new ArrayList<>();
        String manf="";
        init_manufactures(manf,scanner,manufactures);

        //Contact list
        SortedMap<String,String > contact_list=new TreeMap<>();
        String input="";
        init_ContactList(input,scanner,contact_list);

        //discounts
        SortedMap<Double,Integer > discountByTotalPrice=new TreeMap<>();
        SortedMap<Integer,Integer> discountByTotalQuantity=new TreeMap<>();
        input="";
        init_discountByTotalPrice(input,scanner,discountByTotalPrice,discountByTotalQuantity);

        //Supplier card
        Supplier_Card card=initSupplier_Card(scanner);

        //Contract
        Contract contract=initContract(scanner);

        service.addSupplier(name,address,manufactures,contact_list,discountByTotalPrice,discountByTotalQuantity,card,contract);
        System.out.println("supplier saved!");
    }

    /**
     * Initializes a new Contract object based on user input.
     *
     * @param scanner The Scanner object used for input.
     * @return The initialized Contract object.
     */

    public static Contract initContract(Scanner scanner) {
        try {
            String ship = terminal_IO(scanner, "Is supplier doing delivers, in format true/false");
            boolean isShip = Boolean.parseBoolean(ship);


            //init days
            String day="";
            ArrayList<Contract.Day> days=new ArrayList<>();
            while(isShip && !day.equals( "0")){
                try {


                    System.out.println("Enter all days of shipment by their number of the week. to stop enter days of shipment enter 0");
                    day = terminal_IO(scanner, "Day");
                    int i = Integer.parseInt(day);
                    int count=1;
                    for (Contract.Day d : Contract.Day.values()) {
                        if(count==i){
                            days.add(d);
                        }
                        count++;
                    }
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }

            //init date
            String input= terminal_IO(scanner, "sign date, in format <year> <month> <day> ");
            String[] split=input.split("\\s");
            ArrayList<Integer> times=new ArrayList<>();
            for(String s:split){
                times.add(Integer.valueOf(s));
            }
            Date date = new Date(times.get(0),times.get(1),times.get(2));

            Contract contract=new Contract(isShip,days,date);

            System.out.println(contract);
            return contract;

        }
        catch (Exception e){
            System.out.println("Invalid input try again");
            initContract(scanner);
        }
        return null;
    }

    /**
     * Initializes a new Supplier_Card object based on user input.
     *
     * @param scanner The Scanner object used for input.
     * @return The initialized Supplier_Card object.
     */
    private static Supplier_Card initSupplier_Card(Scanner scanner) {
        System.out.println("Enter supplier details in this format <BN_number> <bank_account> <pay_condition>");
        String card=terminal_IO(scanner,"Card");
        String[] split = card.split("\\s");
        try{
            return new Supplier_Card(Integer.parseInt(split[0]),split[1],split[2]);
        }
        catch (Exception e){
            System.out.println("Error occur");
            initSupplier_Card(scanner);
        }
        return null;
    }

    /**
     * Initializes the discountByTotalPrice and discountByTotalQuantity maps based on user input.
     *
     * @param input               The user input string.
     * @param scanner             The Scanner object used for input.
     * @param discountByTotalPrice The SortedMap to store discounts by total price.
     * @param discountByTotalQuantity The SortedMap to store discounts by total quantity.
     */
    private static void init_discountByTotalPrice(String input, Scanner scanner, SortedMap<Double, Integer> discountByTotalPrice, SortedMap<Integer, Integer> discountByTotalQuantity) {
        while(!input.equals( "0")){
            System.out.println("Enter all Discounts in this format Q/P <price or quantity> <discount>,such that P is discount per price and Q is discount per quantity. to stop enter 0");
            input=terminal_IO(scanner,"Discounts");
            if(!input.equals("0")){
                try {


                    String[] split = input.split("\\s");
                    if(split[0].equals("Q")){
                        try {
                            int quantity =Integer.parseInt(split[1]);
                            int discount =Integer.parseInt(split[2]);
                            discountByTotalQuantity.put(quantity,discount);
                        }
                        catch (Exception e){
                            System.out.println(e.getMessage());
                        }
                    }
                    else{
                        try {
                            double price =Double.parseDouble(split[1]);
                            int discount =Integer.parseInt(split[2]);
                            discountByTotalPrice.put(price,discount);
                        }
                        catch (Exception e){
                            System.out.println(e.getMessage());
                        }
                    }
                    System.out.println("price->discount"+discountByTotalPrice);
                    System.out.println("quantity->discount"+discountByTotalQuantity);
                }
                catch (Exception e){
                    System.out.println("Invalid input");
                }
            }
        }
    }

    /**
     * Initializes the contact_list map based on user input.
     *
     * @param contact      The user input string.
     * @param scanner      The Scanner object used for input.
     * @param contact_list The SortedMap to store the contact list.
     */
    private static void init_ContactList(String contact, Scanner scanner, SortedMap<String, String> contact_list) {
        while(!contact.equals( "0")){
            System.out.println("Enter all contact list in this format <name> <phone>. to stop enter 0");
            contact=terminal_IO(scanner,"contact list");
            if(!contact.equals("0")){
                try {


                    String[] split = contact.split("\\s");
                    contact_list.put(split[0],split[1]);
                    if(!split[1].matches(PhoneNumregex))
                    {
                        throw new Exception("Invalid Phone Number");
                    }
                    System.out.println(contact_list);
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    /**
     * Performs input/output operations in the terminal and returns the user input.
     *
     * @param scanner The Scanner object used for input.
     * @param input   The prompt message for the user.
     * @return The user input.
     */
    public static String terminal_IO(Scanner scanner, String input) {
        System.out.println("Enter "+input);
        String name = scanner.nextLine();
        System.out.println(input+": "+name);
        return name;
    }

    /**
     * Initializes the manufactures list based on user input.
     *
     * @param manf        The user input string.
     * @param scanner     The Scanner object used for input.
     * @param manufactures The ArrayList to store the manufactures.
     */
    private static void init_manufactures(String manf, Scanner scanner, ArrayList<String> manufactures){
        while(!manf.equals( "0")){
            System.out.println("Enter all manufactures. to stop enter manufactures enter 0");
            manf=terminal_IO(scanner,"manufacture");
            if(!manf.equals("0")){
                if(manufactures.contains(manf)){
                    System.out.println("manufacture already exist");
                    continue;
                }
                manufactures.add(manf);
                System.out.println(manufactures);
            }
        }
    }
}
