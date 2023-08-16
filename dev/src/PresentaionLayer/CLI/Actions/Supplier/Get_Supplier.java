package Presentaion_layer.CLI.Actions.Supplier;

import BusinessLayer.Suppliers.Classes.Supplier;
import Presentaion_layer.CLI.Actions.Supplier_Item.Get_Item;
import Presentaion_layer.CLI.Actions.Supplier_Item.Remove_Item;
import Service_layer.Suppliers_Service;

import java.util.Scanner;

public class Get_Supplier {

    /**
     * Displays information about a specific supplier and their fields based on user input.
     *
     * @param service The Suppliers_Service object.
     */
    public static void Get(Suppliers_Service service, boolean can_update){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Suppliers list");
        System.out.println(service.getSuppliersNames());
        System.out.println("Suppliers fields");
        System.out.println("1.address\n" +
                "2.manufactures\n" +
                "3.contact_List\n" +
                "4.discount By Total Price\n" +
                "5.discount By Total Quantity\n" +
                "6.items\n" +
                "7.supplier_card\n" +
                "8.contract\n" +
                "9.OrdersSentToSupplier\n" +
                "10.get Item\n" +
                "11.Orders\n" +
                "12.remove Item\n" +
                "13.updateOrderStatus\n" +
                "14.Make New Contract");

        //name
        String input=terminal_IO(scanner,"<Supplier name> <field>");
        try{
            String[] split=input.split("\\s");
            Supplier supplier=service.getSupplier(split[0]);
            int request=Integer.parseInt(split[1]);
            if(!can_update && request > 11)
            {
                System.out.println("Invalid request");
                return;
            }
            if(request==1){
                System.out.println("Supplier address: "+supplier.getAddress());
            }
            else if(request==2){
                System.out.println("Supplier manufactures:"+supplier.getManufactures());
            }
            else if(request==3){
                System.out.println("Supplier contact list:"+supplier.getContact_List());
            }
            else if(request==4){
                System.out.println("Supplier discount by price:"+supplier.getDiscountByTotalPrice());
            }
            else if(request==5){
                System.out.println("Supplier discount by quantity:"+supplier.getDiscountByTotalQuantity());
            }
            else if(request==6){
                System.out.println("Supplier's items:"+supplier.getItems());
            }
            else if(request==7){
                System.out.println("Supplier card:"+supplier.getSupplier_card());
            }
            else if(request==8){
                System.out.println("Supplier Contract:"+supplier.getContract());
            }
            else if (request == 9){
                System.out.println("Supplier's orders:"+supplier.getFinalOrders());
            }
            else if(request == 10){
                System.out.println(supplier.getItems());
                Get_Item.Get(service , split[0]);
            }
            else if (request == 11) {
                System.out.println("Supplier orders:"+supplier.getOrders());
            }
            else if(request == 12){
                System.out.println(supplier.getItems());
                Remove_Item.remove_Item(service , split[0]);
            }
            else if(request == 13)
            {
                System.out.println("Enter status command");
                System.out.println("Sent, Received, Delivered, Canceled ,\nNEXT -> TO UPDATE STATE {OnMaking -> Sent -> Received -> Delivered}");
                String status = scanner.nextLine();
                UpdateOrderStatus(service , split[0] , scanner, status);
            }
            else if(request == 14){
                MakeNewContract.makeNewContract(service , split[0]);
            }
            else
            {
                System.out.println("Invalid request");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates the order status for a specific supplier based on user input.
     *
     * @param service  The Suppliers_Service object.
     * @param supplier The name of the supplier.
     * @param scanner  The Scanner object used for input.
     * @param status   The status command for updating the order status.
     */
    private static void UpdateOrderStatus(Suppliers_Service service, String supplier, Scanner scanner, String status)
    {
        try
        {
            if(!status.equals("Sent") && !status.equals("Received") && !status.equals("Delivered") && !status.equals("Canceled"))
            {
                System.out.println("Invalid status command");
                return;
            }
            String input=terminal_IO(scanner,"OrderID");
            service.updateOrderStatus(Integer.parseInt(input) , supplier, status);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
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
}
