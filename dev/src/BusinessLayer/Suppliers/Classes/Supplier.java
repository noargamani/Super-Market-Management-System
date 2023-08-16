package BusinessLayer.Suppliers.Classes;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import BusinessLayer.Constants.Pair;
import DataAcessLayer.DAO.Suppliers.SupplierDAO;

public class Supplier {

    /**
     *
     * @param supplier_name
     * @param address
     * @param manufactures
     * @param contact_List
     * @param discountByTotalPrice
     * @param discountByTotalQuantity
     * @param supplier_card
     * @param contract
     */
    public Supplier(String supplier_name, String address, ArrayList<String> manufactures, SortedMap<String, String> contact_List,
                    SortedMap<Double, Integer> discountByTotalPrice, SortedMap<Integer, Integer> discountByTotalQuantity, Supplier_Card supplier_card, Contract contract, SupplierDAO supplierDAO) {
        this.supplier_name = supplier_name;
        this.address = address;
        this.manufactures = manufactures;
        this.contact_List = contact_List;
        this.discountByTotalPrice = new TreeMap<>();
        if(discountByTotalPrice != null)
        {
            this.discountByTotalPrice.putAll(discountByTotalPrice);
        }
        this.discountByTotalQuantity = new TreeMap<>();
        if(discountByTotalQuantity != null)
        {
            this.discountByTotalQuantity.putAll(discountByTotalQuantity);
        }
        this.supplier_card = supplier_card;
        this.contract = contract;
        //orders= new Hashtable<>();
        FinalOrders = new Hashtable<>();
        this.supplierDAO = supplierDAO;
    }
    public Supplier(String supplier_name, int supplier_ID, String address, ArrayList<String> manufactures, SortedMap<String, String> contact_List, SortedMap<Double, Integer> discountByTotalPrice,
                    SortedMap<Integer, Integer> discountByTotalQuantity, Supplier_Card supplier_card, Contract contract, SupplierDAO supplierDAO) {
        this.supplier_name = supplier_name;
        this.supplier_ID = supplier_ID;
        this.address = address;
        this.manufactures = manufactures;
        this.contact_List = contact_List;
        this.discountByTotalPrice = new TreeMap<>();
        if(discountByTotalPrice != null)
        {
            this.discountByTotalPrice.putAll(discountByTotalPrice);
        }
        this.discountByTotalQuantity = new TreeMap<>();
        if(discountByTotalQuantity != null)
        {
            this.discountByTotalQuantity.putAll(discountByTotalQuantity);
        }
        this.supplier_card = supplier_card;
        this.contract = contract;
        // orders= new Hashtable<>();
        FinalOrders = new Hashtable<>();
        this.supplierDAO = supplierDAO;
    }
    final String supplier_name;
    int supplier_ID;
    String address;
    ArrayList<String> manufactures;
    Hashtable<Integer, OrderForInventory> FinalOrders;
    SortedMap<String,String> contact_List;// name->phone number
    SortedMap<Double,Integer> discountByTotalPrice;//price->discount
    SortedMap<Integer,Integer> discountByTotalQuantity;//Quantity->discount
    private SortedMap<Integer, SupplierItem> items;//catalog number->item

    SupplierDAO supplierDAO;

    //SortedMap<String , Item> itemsByName; //item name -> item
    Supplier_Card supplier_card;
    // Hashtable<Integer , Order> orders; //order id -> order
    Contract contract;

    @Override
    public String toString() {
        return "Supplier{" +
                "supplier_name='" + supplier_name + '\'' +
                ", supplier_ID=" + supplier_ID +
                ", address='" + address + '\'' +
                ", manufactures=" + manufactures +
                ", contact_List=" + contact_List +
                ", discountByTotalPrice=" + discountByTotalPrice +
                ", discountByTotalQuantity=" + discountByTotalQuantity +

                ", supplier_card=" + supplier_card +
                ", contract=" + contract +
                '}';
    }

    public Hashtable<Integer, OrderForInventory> initFinalOrders()
    {
        FinalOrders = new Hashtable<>();
        for(Order order : getOrdersTable().values())
        {
            MakeOrderReport(order);
        }
        return FinalOrders;
    }

    public Hashtable<Integer , Order> getOrdersTable() {
        return supplierDAO.getOrdersTable(supplier_name);
    }

    public ArrayList<OrderForInventory> getFinalOrders() {
        return new ArrayList<>(initFinalOrders().values().stream().collect(Collectors.toList()));
    }

    public OrderForInventory getFinalOrder(int id) {
        return initFinalOrders().get(id);
    }
    public SupplierItem removeItem(int catalogNumber)
    {
        return supplierDAO.removeItem(supplier_name, catalogNumber);
    }

    /**
     *
     * @return supplier name
     */
    public String getSupplier_name() {
        return supplier_name;
    }

    /**
     *
     * @return supplier Id
     */
    public int getSupplier_ID() {
        return supplier_ID;
    }


    public String getAddress() {
        return address;
    }


    public ArrayList<String> getManufactures() {
        return manufactures;
    }

    public SortedMap<String, String> getContact_List() {
        return contact_List;
    }

// --Commented out by Inspection START (11/04/2023 03:48):
//    public void setContact_List(SortedMap<String, String> contact_List) {
//        this.contact_List = contact_List;
//    }
// --Commented out by Inspection STOP (11/04/2023 03:48)

    public SortedMap<Double, Integer> getDiscountByTotalPrice() {
        return discountByTotalPrice;
    }

    public void removeDiscountByTotalPrice(double TPrice) {
        this.discountByTotalPrice.remove(TPrice);
    }
    public void addDiscountByTotalPrice(double TPrice , Integer Discount) {
        if(discountByTotalPrice.containsKey(TPrice)) {
            this.discountByTotalPrice.remove(TPrice);
        }
        this.discountByTotalPrice.put(TPrice, Discount);
    }

    public SortedMap<Integer, Integer> getDiscountByTotalQuantity() {
        return discountByTotalQuantity;
    }

    public void removeDiscountByTotalQuantity(Integer Quantity) {
        this.discountByTotalQuantity.remove(Quantity);
    }
    public void addDiscountByTotalQuantity(Integer Quantity , Integer Discount) {
        if(discountByTotalQuantity.containsKey(Quantity)) {
            this.discountByTotalQuantity.remove(Quantity);
        }
        this.discountByTotalQuantity.put(Quantity, Discount);
    }

    public Order.Status updateOrderStatus(int orderId, String status) throws Exception
    {
        try {
            Order order = getOrdersTable().get(orderId);
            Order.Status isDelivery = supplierDAO.updateOrderStatus(supplier_name, orderId, status);
            this.initFinalOrders().get(orderId).setFINAL_ORDER_Status(order.getStatus());
            return isDelivery;
        }
        catch (Exception e)
        {
            throw new Exception("Order not found");
        }
    }

    public Supplier_Card getSupplier_card() {
        return supplier_card;
    }

    public void setSupplier_card(Supplier_Card supplier_card) {
        this.supplier_card = supplier_card;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public SortedMap<Integer, SupplierItem> getItems() {
        return supplierDAO.getItemsBySupplier(supplier_name);
    }

    public SupplierItem getItemByCatalogID(int catalogId)
    {
        return supplierDAO.getItemByCatalogID(catalogId, supplier_name);
    }

    public String getOrders() {
        /*
        StringBuilder order = new StringBuilder();
        for(Integer orderID: orders.keySet())
        {
            order.insert(0, orders.get(orderID).toString() + "\n");
        }
        return order.toString();*/
        return getOrdersTable().values().toString();
    }

    /**
     * Create Order
     */
    public Order CreateOrder(Hashtable<Integer , Integer> items , SupplierDAO supplierDAO) throws Exception {
        Order order = new Order(items , supplierDAO, supplier_name);
        return order;
    }
    /**
     * Update Order With Item
     */
    public void UpdateOrderWithItem(int orderID , int CatalogID , int Quantity)
    {
        Order UpdateMe = this.getOrdersTable().get(orderID);
        UpdateMe.addItem(CatalogID , Quantity , this);
    }
    /**
     * Calculate Order Cost and Discount
     */
    public Pair<Double, ArrayList<Pair<Integer, Double>>> CalcOrderCostAndDiscounts(int orderID)
    {
        Order order = getOrdersTable().get(orderID);
        if(order == null)
        {
            try {
                throw new Exception("Invalid Order ID");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return order.CalcDiscountedPrice(discountByTotalQuantity, discountByTotalPrice);
    }

    /**
     * Calculate Order
     */
    public double CalcOrderCost(int orderID)
    {
        return CalcOrderCostAndDiscounts(orderID).first;
    }
    public ArrayList<Pair<Integer , Double>> CalcTotalCost(int orderID)
    {
        return CalcOrderCostAndDiscounts(orderID).second;
    }
    /**
     * remove unWanted Order
     */
    public void removeOnMakeOrder(int orderID)
    {
        try {
            Hashtable<Integer, Order> orderTables = getOrdersTable();
            if (orderTables.containsKey(orderID) && orderTables.get(orderID).getStatus() == Order.Status.OnMaking) {
                supplierDAO.removeOrder(orderID, supplier_name);
            }
        }
        catch (Exception e)
        {
            System.out.println("Invalid Order ID");
        }
    }
    /**
     * Send Order
     */
    public OrderForInventory sendOrder(int orderID)
    {
        Hashtable<Integer, Order> orders = getOrdersTable();
        if(orders.containsKey(orderID))
        {
            Order order = orders.get(orderID);
            try {
                supplierDAO.updateOrderStatus(supplier_name, orderID, "Sent");
            } catch (Exception e) {
                System.out.println("Invalid Order ID");
            }
            supplierDAO.updateQuantity(orderID, supplier_name, false);
            return MakeOrderReport(order);
        }
        else
        {
            try {
                throw new Exception();
            } catch (Exception e) {
                System.out.println("InvalidOrderID");
                return null;
            }
        }
    }
    /**
     * Make Order Report for Supplier
     */
    private OrderForInventory MakeOrderReport(Order order)
    {;
        OrderForInventory finalOrder = new OrderForInventory(this , order, this.supplierDAO);
        FinalOrders.put(order.getOrderID(), finalOrder);
        return finalOrder;
    }

    public Order getOrder(int orderId) {
        return getOrdersTable().get(orderId);
    }

    public void cancelOrder(int orderId) {
        //check if order is not delivered yet
        try {
            if (getOrdersTable().get(orderId).getStatus() != Order.Status.Delivered)
            {
                supplierDAO.updateQuantity(orderId, supplier_name, true);
                supplierDAO.removeOrder(orderId, supplier_name);
            }
            else {
                throw new Exception("Order already delivered");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public int getBn_number() {
        return supplier_card.getBN_number();
    }


    /*
    public ArrayList<Order> makePeriodicOrder(Contract.Day currentDay) {
        ArrayList<Order> oldOrders = new ArrayList<>();
        ArrayList<Integer> orders = periodicOrders.get(currentDay);
        if(orders == null)
            return oldOrders;
        for(Integer orderID: orders)
        {
            Order order = this.orders.get(orderID);
            oldOrders.add(order);
        }
        return oldOrders;
    }



     */


    /**
     * Make Order for Supplier
     */
    public static class OrderForInventory
    {
        final String supplierName;
        final int supplierID;
        final String address;
        final LocalDate orderDate;
        final int orderID;
        final String phone_number;
        final ArrayList<itemsInInventoryOrder> Order_items;
        private Order.Status status;

        public Order.Status getStatus() {
            return status;
        }
        public void setFINAL_ORDER_Status(Order.Status status) {
            this.status = status;
        }

        public String getSupplierName() {
            return supplierName;
        }

        public int getSupplierID() {
            return supplierID;
        }

        public String getAddress() {
            return address;
        }

        public LocalDate getOrderDate() {
            return orderDate;
        }

        public int getOrderID() {
            return orderID;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public ArrayList<itemsInInventoryOrder> getItems() {
            return Order_items;
        }

        @Override
        public String toString() {
            return "OrderForSupplier{" +
                    "supplierName='" + supplierName + '\'' +
                    ", supplierID=" + supplierID +
                    ", address='" + address + '\'' +
                    ", orderDate=" + orderDate +
                    ", orderID=" + orderID +
                    ", phone_number='" + phone_number + '\'' +
                    ", items=" + Order_items +
                    '}';
        }


        private OrderForInventory(Supplier supplier , Order order, SupplierDAO supplierDAO)
        {
            supplierName = supplier.getSupplier_name();
            supplierID = supplier.getSupplier_ID();
            address = supplier.getAddress();
            orderDate = order.getDateOrderIssued();
            orderID = order.getOrderID();
            phone_number = supplier.getContact_List().get(supplier.getContact_List().firstKey());
            double discountForTotalOrder = order.getTotalDiscount(supplier.getDiscountByTotalQuantity() , supplier.getDiscountByTotalPrice());
            ArrayList<itemsInInventoryOrder> items = new ArrayList<>();
            for(Integer item : order.getItems().keySet())
            {
                items.add(new itemsInInventoryOrder(order.getItemOrderByCatalogID(item) , discountForTotalOrder , supplier.getItemByCatalogID(item), supplierDAO));
            }
            this.Order_items = items;
        }
    }
    public static class itemsInInventoryOrder
    {
        final SupplierDAO supplierDAO;
        final int itemID;
        final String itemName;
        final int Quantity;
        final double listPrice;
        final double discount;
        final double finalCost;
        private LocalDate expirationDate;

        public LocalDate getExpirationDate() {
            return expirationDate;
        }

        public SupplierItem getMySupplierItem() {
            return supplierDAO.getItemID_To_Item().get(itemID);
        }

        public void setExpirationDate(LocalDate expirationDate) {
            this.expirationDate = expirationDate;
        }

        public int getItemID() {
            return itemID;
        }

        public String getItemName() {
            return itemName;
        }

        public int getQuantity() {
            return Quantity;
        }

        public double getListPrice() {
            return listPrice;
        }

        public double getDiscount() {
            return discount;
        }

        public double getFinalCost() {
            return finalCost;
        }

        @Override
        public String toString() {
            return "itemsInSupplierOrder{" +
                    "itemID=" + itemID +
                    ", itemName='" + itemName + '\'' +
                    ", Quantity=" + Quantity +
                    ", listPrice=" + listPrice +
                    ", discount=" + discount +
                    ", finalCost=" + finalCost +
                    '}';
        }

        private itemsInInventoryOrder(ItemForOrder item , double discountForTotalOrder , SupplierItem mySupplierItem, SupplierDAO supplierDAO) {
            this.itemID = mySupplierItem.getCatalogNumber();
            this.itemName = mySupplierItem.getName();
            Quantity = item.getOrderAmount();
            mySupplierItem.removeQuantity(Quantity);
            this.listPrice = mySupplierItem.getItem_list_price();
            LocalDate localDate = LocalDate.now();
            localDate = localDate.plusDays(7);
            this.expirationDate = localDate;

            Pair<Double, Double> discAndFCost = item.getTotalPriceWithItemDisc(discountForTotalOrder);
            this.discount = discAndFCost.second;
            this.finalCost = discAndFCost.first;
            this.supplierDAO = supplierDAO;
        }

    }

    public void setItems(SortedMap<Integer, SupplierItem> items) {
        this.items = items;
    }

}
