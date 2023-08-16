package BusinessLayer.Suppliers.Classes;

import BusinessLayer.Constants;
import DataAcessLayer.DAO.Suppliers.SupplierDAO;

import java.time.LocalDate;
import java.util.*;

import static BusinessLayer.Constants.binarySearch;

public class Order {

    public enum Status{
        OnMaking , Sent, Received,Delivered,Canceled
    }
    private int orderID;
    private LocalDate dateOrderIssued;
    private String supplier;
    private static Status Status;
    private int totalQuantity;
    private Hashtable<Integer , ItemForOrder> items; //CatalogId-> ItemForOrder
    private double TPriceWithoutDiscount;

    public int getOrderID() {
        return orderID;
    }

    public LocalDate getDateOrderIssued() {
        return dateOrderIssued;
    }

    public String getSupplier() {
        return supplier;
    }


    // --Commented out by Inspection START (11/04/2023 03:44):
//    public String getSupplierName() {
//        return supplierName;
//    }
// --Commented out by Inspection STOP (11/04/2023 03:44)

// --Commented out by Inspection START (11/04/2023 03:43):
//    public int getSupplierID() {
//        return supplierID;
//    }
// --Commented out by Inspection STOP (11/04/2023 03:43)

// --Commented out by Inspection START (11/04/2023 03:45):
//    public void setSupplierID(int supplierID) {
//        this.supplierID = supplierID;
//    }
// --Commented out by Inspection STOP (11/04/2023 03:45)

    public Order.Status getStatus() {
        return Status;
    }

    public void setStatus(Order.Status status) {
        Status = status;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }


    public void addItems(Hashtable<Integer , Integer> QuantityByItemNames , Supplier supplier) {
        if (QuantityByItemNames != null) {
            for (Integer item : QuantityByItemNames.keySet()) {
                addItem(item, QuantityByItemNames.get(item), supplier);
            }
        }
    }


    public Order(int orderID, Status status,LocalDate dateOrderIssued, int supplierID, String supplier, int totalQuantity, Hashtable<Integer, ItemForOrder> items, double TPriceWithoutDiscount) {
        this.orderID = orderID;
        this.dateOrderIssued = dateOrderIssued;
        this.supplier = supplier;
        this.totalQuantity = totalQuantity;
        this.items = items;
        this.TPriceWithoutDiscount = TPriceWithoutDiscount;
        setStatus(status);

    }

    /**
     * Constructor
     * @param items
     * @param supplier
     */
    public Order(Hashtable<Integer , Integer> items, SupplierDAO supplierDAO, String supplier) throws Exception {
        Order Border = new Order(1 , items, LocalDate.now(), supplierDAO.getSupplierByName(supplier));
        Order order = supplierDAO.CreateOrder(Border);
        this.orderID = order.getOrderID();
        this.dateOrderIssued = order.getDateOrderIssued();
        this.Status = order.getStatus();
        this.totalQuantity = order.getTotalQuantity();
        this.items = order.getItems();
        this.TPriceWithoutDiscount = order.getPriceBeforeDiscount();
    }

    private Order(int orderID, Hashtable<Integer , Integer> items, LocalDate dateOrderIssued, Supplier supplier) {
        this.orderID = orderID;
        this.items = new Hashtable<>();
        this.totalQuantity = 0;
        this.TPriceWithoutDiscount = 0;
        addItems(items , supplier);
        this.dateOrderIssued = dateOrderIssued;
        this.supplier = supplier.getSupplier_name();
        Status = Order.Status.OnMaking;
    }

    private double getPriceBeforeDiscount() {
        return TPriceWithoutDiscount;
    }


    public Hashtable<Integer , ItemForOrder> getItems() {
        return items;
    }

    /**
     * Get Item For Order
     * @param item
     * @return
     */
    public ItemForOrder getItemOrderByCatalogID(int item)
    {
        if(items.containsKey(item))
        {
            return items.get(item);
        }
        else
        {
            try {
                throw new Exception("Item Catalog ID doesn't exist in this order");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
    }

    /**
     * function to Update Quantity
     * @param Quantity
     */
    private void addQuantity(int Quantity)
    {
        this.totalQuantity = this.totalQuantity + Quantity;
    }

    /**
     * function to UpdateUndiscountedPrice
     * @param cost
     */
    private void UpdateUnDiscountedPrice(double cost)
    {
        this.TPriceWithoutDiscount = this.TPriceWithoutDiscount + cost;
    }

    /**
     * add Item to Order
     * @param itemCatalogId
     * @param Quantity
     * @param mySupplier
     */
    public void addItem(int itemCatalogId , int Quantity , Supplier mySupplier)
    {
        ItemForOrder OrderMe = new ItemForOrder(Quantity , itemCatalogId , mySupplier);
        items.put(itemCatalogId , OrderMe);
        addQuantity(Quantity);
        //UpdatePrice(OrderMe.getTotalPriceWithItemDisc());
        UpdateUnDiscountedPrice(OrderMe.getTotalPriceWithoutItemDisc());
    }

    private Constants.Pair<Double , ArrayList<Constants.Pair<Integer , Double>>> CalculateTotalPrice(double discount)
    {
        ArrayList<Constants.Pair<Integer , Double>> finalDiscountPerItem = new ArrayList<>();
        double price = 0;
        for(Integer addMe : items.keySet())
        {
            Constants.Pair<Double , Double> TotalPriceWithItemDisc = items.get(addMe).getTotalPriceWithItemDisc(discount);
            finalDiscountPerItem.add(new Constants.Pair<>(addMe , TotalPriceWithItemDisc.second));
            price = price + TotalPriceWithItemDisc.first;
        }
        return new Constants.Pair<>(price , finalDiscountPerItem);

    }

    /**
     * Calculate Discounted Price
     * @param discountByQuantity
     * @param discountByTotalPrice
     * @return
     */
    // first is total price, second is Item for Order and final discount;
    public Constants.Pair<Double, ArrayList<Constants.Pair<Integer, Double>>> CalcDiscountedPrice(Map<Integer, Integer> discountByQuantity , Map<Double, Integer> discountByTotalPrice)
    {
        double totalDiscount = getTotalDiscount(discountByQuantity, discountByTotalPrice);
        return CalculateTotalPrice(totalDiscount);
    }

    /**
     * get Total discount for the order (Supplier Order)
     * @param discountByQuantity
     * @param discountByTotalPrice
     * @return
     */
    public double getTotalDiscount(Map<Integer, Integer> discountByQuantity, Map<Double, Integer> discountByTotalPrice) {
        return (10000 - ((100.0 - getQuantityDiscount(discountByQuantity)) * (100.0 - getPriceDiscount(discountByTotalPrice))))/100.0;
    }

    /**
     * Update Status
     */
    public Status updateStatus(String NEW_STATUS)
    {
            /*
            Status{
            Sent, Received,Delivered,Canceled , NEXT -> TO UPDATE STATE {OnMaking -> Sent -> Received -> Delivered)
            }
             */
        try {
            if(this.Status == Order.Status.Canceled)
            {
                throw new Exception("Order is Already canceled");
            }
            if(this.Status == Order.Status.Delivered)
            {
                throw new Exception("Order is Already Delivered");
            }
            if(NEW_STATUS == "NEXT")
            {
                if(this.Status == Order.Status.OnMaking)
                {
                    this.Status = Order.Status.Sent;
                }
                else if(this.Status == Order.Status.Sent)
                {
                    this.Status = Order.Status.Received;
                }
                else if(this.Status == Order.Status.Received)
                {
                    this.Status = Order.Status.Delivered;
                }
            }
            if (NEW_STATUS.equals("Sent")) {
                if(Status != Order.Status.OnMaking)
                {
                    throw new Exception("Order is not OnMaking yet");
                }
                Status = Order.Status.Sent;
            } else if (NEW_STATUS.equals("Received")) {
                if(Status != Order.Status.Sent)
                {
                    throw new Exception("Order is not Sent yet");
                }
                Status = Order.Status.Received;
            } else if (NEW_STATUS.equals("Delivered")) {
                Status = Order.Status.Delivered;
            } else if (NEW_STATUS.equals("Canceled")) {
                Status = Order.Status.Canceled;
            } else {
                throw new Exception("Status doesn't exist");
            }
            //GIVE INVENTORY THE ORDER
            return this.Status;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private int getQuantityDiscount(Map<Integer, Integer> discountByQuantity)
    {
        //else
        int[] quantityArray = discountByQuantity.keySet().stream().mapToInt(i->i).toArray();
        int discQuantityIdx = -1;
        if(quantityArray.length != 0) {
            discQuantityIdx = binarySearch(quantityArray, 0, quantityArray.length, totalQuantity);
        }
        if(discQuantityIdx == -1)
        {
            return 0;
        }
        return discountByQuantity.get(quantityArray[discQuantityIdx]);
    }

    private int getPriceDiscount(Map<Double, Integer> discountByTotalPrice)
    {
        //else
        double[] priceArray = discountByTotalPrice.keySet().stream().mapToDouble(i->i).toArray();
        int discPriceIdx = -1;
        if(priceArray.length != 0) {
            discPriceIdx = binarySearch(priceArray, 0, priceArray.length, this.TPriceWithoutDiscount);
        }
        if(discPriceIdx == -1)
        {
            return 0;
        }
        return discountByTotalPrice.get(priceArray[discPriceIdx]);
    }



    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", dateOrderIssued=" + dateOrderIssued +
                ", dateOrderIssued=" + dateOrderIssued +
                ", Status=" + Status +
                ", totalQuantity=" + totalQuantity +
                ", items=" + items +
                '}';
    }

    public double getTPriceWithoutDiscount() {
        return TPriceWithoutDiscount;
    }
}
