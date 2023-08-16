package BusinessLayer.Suppliers.Controllers;

import BusinessLayer.Constants;
import BusinessLayer.Suppliers.Classes.*;
import BusinessLayer.Inventory.Controllers.ItemController;
import DataAcessLayer.DAO.Suppliers.SupplierDAO;

import java.sql.SQLException;
import java.util.*;


public class Suppliers_Controller {
    static SupplierDAO supplierDAO;

    public Suppliers_Controller(SupplierDAO supplierDA) {
        supplierDAO=supplierDA;
    }


    public boolean SupplierExist(String name) {
        return supplierDAO.containsSupplier(name);
    }

    public void updateOrderStatus(int orderId, String supplier, String status)  throws Exception{
        try {
            Order.Status newStatus = supplierDAO.getSupplierByName(supplier).updateOrderStatus(orderId, status);
            if (newStatus == Order.Status.Delivered) {
                this.sendOrderReportToInventory(orderId, supplier);
            } else if (newStatus == Order.Status.Canceled) {
                this.cancelOrder(orderId, supplier);
            }
        } catch (Exception e) {
            throw e;
        }
    }


    private void cancelOrder(int orderId, String supplier) {
        try {
            supplierDAO.getSupplierByName(supplier).cancelOrder(orderId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendOrderReportToInventory(int orderId, String supplier) {
        try {
            ItemController.ReceiveOrderFromSupplier(supplierDAO.getSupplierByName(supplier).getFinalOrder(orderId));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Supplier.OrderForInventory> MakeNeedOrder(String item_name, String item_man, int quantity) throws Exception {
        ArrayList<Supplier.OrderForInventory> orders;
        Hashtable<Constants.Pair<String, String>, Integer> wrapper = new Hashtable<>();
        wrapper.put(new Constants.Pair<>(item_name, item_man), quantity);
        orders = addInventoryOrder(wrapper, Contract.Day.None);
        if(orders == null || orders.size() == 0) {
            //continue; - doesn't matter
        }

        return orders;
    }

    public SupplierItem getItem(String itemName, String itemManufacture, String supplier) {
        for (Integer itemID : supplierDAO.getItemCode_To_Items().get(itemName + Constants.Code + itemManufacture)) {
            SupplierItem supplierItem = supplierDAO.getItemID_To_Item().get(itemID);
            if (supplierItem.getSupplier().equals(supplier)) {
                return supplierItem;
            }
        }
        return null;
    }

    public boolean ItemExists(String itemID, String itemManufacture) {
        return supplierDAO.getItemCode_To_Items().containsKey(itemID + Constants.Code + itemManufacture);
    }


    private ArrayList<Hashtable<Integer, Integer>> generateHashTables(ArrayList<ItemsToOrder> A) {
        ArrayList<Hashtable<Integer, Integer>> Hashtable = new ArrayList<>();
        Hashtable<Integer, Integer> currentHash = new Hashtable<>();
        generateHashTablesHelper(A, 0, currentHash, Hashtable);
        return Hashtable;
    }

    private void generateHashTablesHelper(ArrayList<ItemsToOrder> A, int currentIndex,
                                          Hashtable<Integer, Integer> currentHash, ArrayList<Hashtable<Integer, Integer>> Hashtables) {
        if (currentIndex == A.size()) {
            // Add the current set to the list of sets
            Hashtables.add(new Hashtable<>(currentHash));
            return;
        }

        // Get the current ArrayList from ArrayList A
        ArrayList<Integer> currentArrayList = A.get(currentIndex).items;
        Integer order_quantity = A.get(currentIndex).QuantityNeeded;

        // Iterate through each element in the current ArrayList
        for (Integer element : currentArrayList) {
            // Add the element to the current set
            currentHash.put(supplierDAO.getItemID_To_Item().get(element).getCatalogNumber(), order_quantity);

            // Recursively generate sets for the next ArrayList
            generateHashTablesHelper(A, currentIndex + 1, currentHash, Hashtables);

            // Remove the added element from the current set for backtracking
            currentHash.remove(element);
        }
    }


    /**
     * Add supplier
     *
     * @param name
     * @param address
     * @param manufactures
     * @param contact_list
     * @param discountByTotalPrice
     * @param discountByTotalQuantity
     * @param supplier_card
     * @param contract
     */
    public void add_Supplier(String name, String address, ArrayList<String> manufactures, SortedMap<String, String> contact_list, SortedMap<Double, Integer> discountByTotalPrice, SortedMap<Integer, Integer> discountByTotalQuantity, Supplier_Card supplier_card, Contract contract) throws SQLException {
        Supplier supplier = new Supplier(name,  address, manufactures, contact_list, discountByTotalPrice, discountByTotalQuantity, supplier_card, contract, supplierDAO);
        supplierDAO.insert(supplier);
    }

    /**
     * get supplier by name
     *
     * @param username
     * @return
     */
    public Supplier getSupplier(String username) throws Exception {
        return supplierDAO.getSupplierByName(username);
    }

    /**
     * add item to supplier
     *
     * @param itemName
     * @param supplier_catalogID
     * @param item_list_price
     * @param discountByQuantity
     * @param quantity
     */
    public void addItem(String supplier, String itemName, String itemManufacture, int supplier_catalogID, double item_list_price, SortedMap<Integer, Integer> discountByQuantity, int quantity, Date expiration) throws Exception {
        //int itemCatalogId = Item.getItemCatalogNumber(itemName, itemManufacture);
        Supplier my_supplier = supplierDAO.getSupplierByName(supplier);
        if (my_supplier == null) {
            throw new Exception("Supplier does not exist");
        }
        for (SupplierItem item : my_supplier.getItems().values())
        {
            if(item.getName().equals(itemName) && item.getItemManufacture().equals(itemManufacture)) {
                System.out.println(item.toString());
                throw new Exception("Item already exists");
            }
            if(item.getCatalogNumber() == supplier_catalogID) {
                throw new Exception("Supplier catalog ID already exists");
            }

        }
        try {
            SupplierItem supplierItem = new SupplierItem(supplier_catalogID, itemName, itemManufacture, supplier_catalogID, item_list_price, discountByQuantity,
                    quantity, supplier, expiration, 1);
            supplierDAO.addSupplierItem(supplierItem, supplier);
        } catch (Exception e) {
            throw e;
        }
    }

    public Hashtable<String, Supplier> getSuppliers() {
        return supplierDAO.getSuppliers();
    }


    public void addOrder(Hashtable<Integer, Integer> items, String supplierName) throws Exception {
        Supplier supplier = supplierDAO.getSupplierByName(supplierName);
        supplier.sendOrder(CreateOrder(supplierName, items));
    }

    public boolean removeItem(String itemName, String itemManufacture, String supplierName) {
        try {
            SupplierItem supplierItem = getItem(itemName, itemManufacture, supplierName);
            if (supplierDAO.removeItem(supplierItem.getSupplier(), supplierItem.getSupplier_catalogID()) == null) {
                return false;
            }
            supplierDAO.getItemCode_To_Items().get(supplierItem.getItemCode()).remove(supplierItem.getCatalogNumber());
            supplierDAO.getItemID_To_Item().remove(supplierItem.getItemId());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void UpdateContract(String supplierName, Contract contract) throws Exception {
        supplierDAO.getSupplierByName(supplierName).setContract(contract);
    }

    public ArrayList<Supplier.OrderForInventory> PeriodicOrder(Contract.Day currentDay) throws Exception {
        Hashtable<Constants.Pair<String, String>, Integer> QuantityByItemProps = new Hashtable<>();
        Hashtable<Integer, Hashtable<Contract.Day, Hashtable<Constants.Pair<String , String>, Integer>>> periodicOrders = supplierDAO.getAllPeriodicOrders();
        for(int orderID: periodicOrders.keySet())
        {
            periodicOrders.get(orderID).get(currentDay).forEach((item, quantity) -> {
                if(!QuantityByItemProps.containsKey(item))
                {
                    QuantityByItemProps.put(item, 0);
                }
                QuantityByItemProps.put(item, QuantityByItemProps.get(item) + quantity);
            });
        }
        if (QuantityByItemProps == null) {
            return null;
        }
        //TODO: add order to DB
        return addInventoryOrder(QuantityByItemProps,currentDay);
    }

    public void AddPeriodicOrder(Hashtable<Constants.Pair<String, String>, Integer> itemsToOrder, ArrayList<Contract.Day> days) {
        Hashtable<Contract.Day, Hashtable<Constants.Pair<String, String>, Integer>> Order_for_day = new Hashtable<>();
        for(Constants.Pair<String, String> itemProp: itemsToOrder.keySet())
        {
            try {
                getItemsID_from_ItemProp(itemProp);
            } catch (Exception e) {
                throw new IllegalArgumentException("Item " + itemProp + " does not exist in the system");
            }
        }
        for(Contract.Day day: days)
        {
            Order_for_day.put(day, itemsToOrder);
        }
        supplierDAO.AddPeriodicOrder(Order_for_day);
    }

    public void RemovePeriodicOrder(int orderID) throws SQLException {
        supplierDAO.RemovePeriodicOrder(orderID);
    }

    public void UpdatePeriodicOrder(int orderID, Hashtable<Constants.Pair<String, String>, Integer> itemsToOrder, ArrayList<Contract.Day> days) throws SQLException {
        supplierDAO.UpdatePeriodicOrder(orderID, itemsToOrder, days);
    }

    public SupplierDAO getSupplierDAO() {
        return supplierDAO;
    }

    public boolean ItemExist(String itemName, String itemManufacture) {
        try
        {
            getItemsID_from_ItemProp(new Constants.Pair<>(itemName, itemManufacture));
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private static class ItemsToOrder {
        // public boolean SuppHaveQuantity;
        public int QuantityNeeded;
        public ArrayList<Integer> items; //items ID
    }

    /**
     * order
     *
     * @param QuantityByItemProps
     */
    public ArrayList<Supplier.OrderForInventory> addInventoryOrder(Hashtable<Constants.Pair<String, String>, Integer> QuantityByItemProps, Contract.Day day) throws Exception {
        Hashtable<Integer, Integer> MultiplySupplierOrder = new Hashtable<>();

        ArrayList<ItemsToOrder> itemsToOrderList = new ArrayList<>();

        ArrayList<Constants.Pair<String, String>> itemProperties = new ArrayList<>(QuantityByItemProps.keySet());
        for (Constants.Pair<String, String> itemProp : itemProperties) {
            int quantity = QuantityByItemProps.get(itemProp);
            int sumQuantity = 0;
            Set<Integer> items = getItemsID_from_ItemProp(itemProp);
            ArrayList<Integer> itemList = new ArrayList<>();
            ArrayList<Constants.Pair<Integer, Integer>> BreakOrderItemList = new ArrayList<>(); //ItemQuantity , itemIdd

            for (Integer itemId : items) {
                int myQuantity = supplierDAO.getItemID_To_Item().get(itemId).getQuantity();
                if (myQuantity - quantity >= 0) {
                    //goodItems
                    itemList.add(itemId);
                } else {
                    BreakOrderItemList.add(new Constants.Pair<>(myQuantity, itemId));
                    sumQuantity += myQuantity;
                }
            }
            //SuppHaveQauntity = false;
            if (itemList.isEmpty()) {
                if(sumQuantity < quantity)
                {
                    return null;
                }
                Integer[] SortedItemArr = getSortedArray(BreakOrderItemList);

                MultiplySupplierOrder.putAll(MoreThenOneSuppForItem(SortedItemArr, quantity, day));
            } else {

                ItemsToOrder itemsToOrder = new ItemsToOrder();
                itemsToOrder.items = itemList;
                itemsToOrder.QuantityNeeded = quantity;
                //itemsToOrder.SuppHaveQuantity = SuppHaveQuantity;

                itemsToOrderList.add(itemsToOrder);
            }
        }
        //we Finished ItemsToOrderForSplitSuppliers
        Hashtable<String, Hashtable<Integer, Integer>> OrderBySupplier = ToSuppliers(MultiplySupplierOrder, day);
        ArrayList<Hashtable<Integer, Integer>> PossibleOrders = generateHashTables(itemsToOrderList); //itemId -> Quantity Every Possible Order Combination :)

        return CalculateMinimumOrderPossible(day, OrderBySupplier, PossibleOrders);
    }

    private Set<Integer> getItemsID_from_ItemProp(Constants.Pair<String, String> itemProp) {
        Set<Integer> items = getAllItemsByProp(itemProp.first, itemProp.second);
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Item " + itemProp.toString() + " does not exist in the system");
        }
        return items;
    }
    private Integer[] getSortedArray(ArrayList<Constants.Pair<Integer, Integer>> inp) //quantity -> itemId
    {
        Integer[] result = new Integer[inp.size()];
        inp.sort(Constants.Pair::compareTo);
        int i = 0;
        for (Constants.Pair<Integer, Integer> p : inp) {
            result[i] = p.second;
            i++;
        }
        return result;
    }

    private ArrayList<Supplier.OrderForInventory> CalculateMinimumOrderPossible(Contract.Day day, Hashtable<String, Hashtable<Integer, Integer>> OrderBySupplier, ArrayList<Hashtable<Integer, Integer>> PossibleOrders) throws Exception {
        Hashtable<String, Integer> currMin = new Hashtable<>();
        Hashtable<String, Hashtable<Integer, Integer>> findMinHelper; //Supplier -> order
        double minPrice = Double.POSITIVE_INFINITY;

        if (PossibleOrders.isEmpty()) {
            Constants.Pair<Double, Hashtable<String, Integer>> minOrder = CalcPriceForInventoryOrder(OrderBySupplier, day);
            minPrice = minOrder.first;
            currMin = minOrder.second;
        }

        for (Hashtable<Integer, Integer> possibleOrder : PossibleOrders) {
            findMinHelper = getMinHelper(OrderBySupplier);
            Hashtable<String, Hashtable<Integer, Integer>> newPossibleOrders = ToSuppliers(possibleOrder, day);
            for (String supplier : newPossibleOrders.keySet()) {
                if (!findMinHelper.containsKey(supplier)) {
                    findMinHelper.put(supplier, new Hashtable<>());
                }
                findMinHelper.get(supplier).putAll(new Hashtable<>(newPossibleOrders.get(supplier)));
            }
            Constants.Pair<Double, Hashtable<String, Integer>> currOrder = CalcPriceForInventoryOrder(findMinHelper, day);
            double TotalPrice = currOrder.first;

            if (TotalPrice < minPrice) {
                minPrice = TotalPrice;
                removeOnMakeOrders(currMin);
                currMin = new Hashtable<>(currOrder.second);
            }
        }
        return sendInventoryOrder(currMin);
    }

    private Hashtable<String, Hashtable<Integer, Integer>> getMinHelper(Hashtable<String, Hashtable<Integer, Integer>> OrderBySupplier) {
        Hashtable<String, Hashtable<Integer, Integer>> findMinHelper;
        findMinHelper = new Hashtable<>();
        for (String supplier : OrderBySupplier.keySet()) {
            findMinHelper.put(supplier, new Hashtable<>(OrderBySupplier.get(supplier)));
        }
        return findMinHelper;
    }

    private ArrayList<Supplier.OrderForInventory> sendInventoryOrder(Hashtable<String, Integer> minOrder) throws Exception {
        ArrayList<Supplier.OrderForInventory> Result = new ArrayList<>();
        for (String supplierName : minOrder.keySet()) {
            Supplier supplier = supplierDAO.getSupplierByName(supplierName);
            Supplier.OrderForInventory addMe = supplier.sendOrder(minOrder.get(supplierName));
            Result.add(addMe);
        }
        return Result;
    }

    private void removeOnMakeOrders(Hashtable<String, Integer> orders) throws Exception {
        for (String supplierName : orders.keySet()) {
            Supplier supplier = supplierDAO.getSupplierByName(supplierName);
            supplier.removeOnMakeOrder(orders.get(supplierName));
        }
    }


    private Constants.Pair<Double, Hashtable<String, Integer>> CalcPriceForInventoryOrder(Hashtable<String, Hashtable<Integer, Integer>> OrderBySupplier, Contract.Day day) throws Exception {

        Hashtable<String, Integer> orders = new Hashtable<>();
        ArrayList<Supplier> suppliers;
        if(day != null) {
            suppliers = supplierDAO.getSuppliersBySupplyDay(day);
        }
        else {
            suppliers = new ArrayList<>(supplierDAO.getSuppliers().values());
        }
        double TotalPrice = 0;
        for (String supplierName : OrderBySupplier.keySet()) {
            Supplier supplier = supplierDAO.getSupplierByName(supplierName);
            if(!suppliers.contains(supplier)) {
                TotalPrice = Double.POSITIVE_INFINITY;
                continue;
            }
            int orderID = CreateOrder(supplierName, OrderBySupplier.get(supplierName));
            orders.put(supplierName, orderID);
            double priceForOne = supplier.CalcOrderCost(orderID);
            TotalPrice = TotalPrice + priceForOne;
        }
        return new Constants.Pair<>(TotalPrice, orders);
    }

    private int CreateOrder(String supplierName, Hashtable<Integer, Integer> orders) throws Exception {
        Supplier supplier = supplierDAO.getSupplierByName(supplierName);
        Order order = supplier.CreateOrder(orders, supplierDAO);
        return order.getOrderID();
    }

    private static int CreateNeedOrder(String supplierName, Hashtable<Integer, Integer> orders) throws Exception {
        Supplier supplier = supplierDAO.getSupplierByName(supplierName);
        Order order = supplier.CreateOrder(orders, supplierDAO);
        return order.getOrderID();
    }

    private Hashtable<String, Hashtable<Integer, Integer>> ToSuppliers(Hashtable<Integer, Integer> ItemsForOrder, Contract.Day day) throws Exception {
        Hashtable<String, Hashtable<Integer, Integer>> OrderBySupplier = new Hashtable<>();
        ArrayList<Supplier> suppliers = new ArrayList<>(supplierDAO.getSuppliersBySupplyDay(day)); //ONLY DAY GOOD
        for (Integer itemId : ItemsForOrder.keySet()) {
            SupplierItem supplierItem = supplierDAO.getItemID_To_Item().get(itemId);
            int catalogId = supplierItem.getSupplier_catalogID();
            String CurrSupplier = supplierItem.getSupplier();
            if(!suppliers.contains(supplierDAO.getSupplierByName(CurrSupplier)))
            {
                continue;
            }
            if (!OrderBySupplier.containsKey(CurrSupplier)) {
                Hashtable<Integer, Integer> newOrder = new Hashtable<>();
                OrderBySupplier.put(CurrSupplier, newOrder);
            }
            OrderBySupplier.get(CurrSupplier).put(catalogId, ItemsForOrder.get(itemId));
        }
        return OrderBySupplier;
    }

    private Hashtable<Integer, Integer> MoreThenOneSuppForItem(Integer[] SortedItemArr, int order_quantity, Contract.Day day) throws Exception {
        ArrayList<Supplier> suppliers = new ArrayList<>(supplierDAO.getSuppliersBySupplyDay(day));
        SortedMap<Double, Integer>[] PriceForQuantity = new SortedMap[order_quantity];
        for (int i = 0; i < PriceForQuantity.length; i++) {
            PriceForQuantity[i] = new TreeMap<>();
        }
        SortedSet<Integer> quantities;
        SortedMap<Integer, Double> PricePerUnitDiscounted;
        SupplierItem currSupplierItem = supplierDAO.getItemID_To_Item().get(SortedItemArr[SortedItemArr.length - 1]);
        if(suppliers.contains(supplierDAO.getSupplierByName(currSupplierItem.getSupplier()))) {
            PricePerUnitDiscounted = currSupplierItem.GetPriceForOneForEveryQuantityDiscount();
        }
        else
        {
            PricePerUnitDiscounted = new TreeMap<>();
        }
        quantities = new TreeSet<>(PricePerUnitDiscounted.keySet());


        int j = 0;
        int quantity = currSupplierItem.getQuantity();
        int maxQuantity = quantity;
        double cost = currSupplierItem.CalculatePriceWithQuantityDiscount(1);
        while (j < quantity) {
            for (Integer q : quantities) {
                while (j < q) {
                    PriceForQuantity[j].put(cost, SortedItemArr.length - 1);
                    j++;
                }
                cost = PricePerUnitDiscounted.get(q);
            }
            while (j <= quantity) {
                PriceForQuantity[j].put(cost, SortedItemArr.length - 1);
                j++;
            }
        }


        //must have at lease 2 items (so order will be possible)
        for (int i = SortedItemArr.length - 2; i >= 0; i--) {
            currSupplierItem = supplierDAO.getItemID_To_Item().get(SortedItemArr[i]);
            if(suppliers.contains(supplierDAO.getSupplierByName(currSupplierItem.getSupplier()))) {
                PricePerUnitDiscounted = currSupplierItem.GetPriceForOneForEveryQuantityDiscount();
            }
            else
            {
                PricePerUnitDiscounted = new TreeMap<>();
            }
            quantities = new TreeSet<>(PricePerUnitDiscounted.keySet());

            j = 0;
            quantity = currSupplierItem.getQuantity();
            cost = currSupplierItem.CalculatePriceWithQuantityDiscount(1);
            while (j < quantity) {
                for (Integer q : quantities) {
                    while (j < q) {
                        PriceForQuantity[j].put(cost, i);
                        j++;
                    }
                    cost = PricePerUnitDiscounted.get(q);
                }
                while (j <= quantity) {
                    PriceForQuantity[j].put(cost, i);
                    j++;
                }
            }
        }
        //.out.println("MoreThenOneSuppForItem Finished");
        return MakeAlmostBestOrder(PriceForQuantity, SortedItemArr, order_quantity, maxQuantity);
    }

    private Hashtable<Integer, Integer> MakeAlmostBestOrder(SortedMap<Double, Integer>[] PriceForQuantity, Integer[] SortedItemArr, int order_quantity, int maxQuantity) {
        Hashtable<Integer, Integer> itemsToOrder = new Hashtable<>();
        //Quantity
        Set<Integer> chosen = new TreeSet<>();
        while (order_quantity != 0) {
            double prevCostIterSaver = Double.POSITIVE_INFINITY;
            int minIDX = 1;
            double minCost = Double.POSITIVE_INFINITY;
            for (int i = 1; i <= maxQuantity && i <= order_quantity; i++) {
                SortedMap<Double, Integer> price = PriceForQuantity[i];
                double currCost = price.firstKey();
                while (chosen.contains(price.get(price.firstKey()))) {
                    price.remove(currCost);
                    if (price.isEmpty()) {
                        currCost = prevCostIterSaver;
                    } else {
                        currCost = price.firstKey();
                    }
                }
                prevCostIterSaver = currCost;
                currCost = prevCostIterSaver;
                if (currCost <= minCost) {
                    minCost = currCost;
                    minIDX = i;
                }
            }
            //System.out.println("quantityTaken:"+minIDX);
            SortedMap<Double, Integer> finalPrice = PriceForQuantity[minIDX];
            chosen.add(finalPrice.get(minCost));
            order_quantity = order_quantity - minIDX;
            itemsToOrder.put(SortedItemArr[finalPrice.get(minCost)], minIDX);
            //System.out.println(order_quantity);
        }
        //System.out.println("MakeAlmostBestOrder Finished");
        return itemsToOrder;
    }

    public Set<Integer> getAllItemsByProp(String itemName, String itemManufacture) {
        return supplierDAO.getItemCode_To_Items().get(itemName + Constants.Code + itemManufacture);
    }
}



