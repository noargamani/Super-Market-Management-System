package BusinessLayer;

import java.util.Hashtable;

import static DataAcessLayer.DAO.Suppliers.SupplierItemDAO.getItemCode_To_ItemCatalogID;


public abstract class Item {

    protected int CatalogNumber;
    protected String Name;
    protected String Manufacturer;
    protected int TotalAmount;
    protected int MinimumAmount;
    private static int itemId;
    private static Hashtable<String, Integer> itemCode_To_Items;// <ITEM_CODE, id>

    /**
     * Constructs a new Item with the specified catalog number, name, manufacturer, total amount, and minimum amount.
     *
     * @param catalogNumber the catalog number of the item
     * @param name          the name of the item
     * @param manufacturer  the manufacturer of the item
     * @param totalAmount   the total amount of the item
     * @param minimumAmount the minimum amount of the item
     */
    public Item(int catalogNumber, String name, String manufacturer, int totalAmount, int minimumAmount) {
        CatalogNumber = catalogNumber;
        Name = name;
        Manufacturer = manufacturer;
        TotalAmount = totalAmount;
        MinimumAmount = minimumAmount;
        itemId = 0;
        itemCode_To_Items = new Hashtable<>();

    }

    /**
     * Exception indicating that an item is below the minimum amount and needs to be ordered.
     */
    public static class NeedOrderException extends Exception {
        boolean is_valid;
        int amount;
        String itemName;
        String manufacturer;

        /**
         * Constructs a new NeedOrderException with the specified item name, manufacturer, and amount.
         *
         * @param itemName     the name of the item
         * @param manufacturer the manufacturer of the item
         * @param amount       the amount of the item below the minimum amount
         */
        public NeedOrderException(String itemName, String manufacturer, int amount) {
            super("Item " + itemName + " from " + manufacturer + " is below minimum amount. Amount: " + amount);
            this.amount = amount;
            this.itemName = itemName;
            this.manufacturer = manufacturer;
            this.is_valid = true;
        }

        /**
         * Constructs a new NeedOrderException by copying the attributes from the given exception.
         *
         * @param e The NeedOrderException to copy attributes from.
         */
        public NeedOrderException(NeedOrderException e) {
            super(e.getMessage());
            this.amount = e.amount;
            this.itemName = e.itemName;
            this.manufacturer = e.manufacturer;
            this.is_valid = e.is_valid;
        }

        /**
         * Returns the amount of the item below the minimum amount.
         *
         * @return the amount of the item below the minimum amount
         */
        public int getAmount() {
            return amount;
        }

        /**
         * Returns the name of the item.
         *
         * @return the name of the item
         */
        public String getItemName() {
            return itemName;
        }

        /**
         * Returns the manufacturer of the item.
         *
         * @return the manufacturer of the item
         */
        public String getManufacturer() {
            return manufacturer;
        }
    }

    /**
     * Initializes and returns the item code to item catalog ID mapping.
     *
     * @return the item code to item catalog ID mapping as a Hashtable
     */
    public static Hashtable<String, Integer> initItemCode_To_Items() {
        Constants.Pair<Integer, Hashtable<String, Integer>> res = getItemCode_To_ItemCatalogID();
        itemId = res.first;
        itemCode_To_Items = res.second;
        return itemCode_To_Items;
    }

    /**
     * Retrieves the catalog number of the item with the specified name and manufacturer.
     *
     * @param name        the name of the item
     * @param manufacturer the manufacturer of the item
     * @return the catalog number of the item if it exists, or a newly created catalog number
     */
    public static int getItemCatalogNumber(String name, String manufacturer) {
        if(itemExists(name, manufacturer))
        {
            return initItemCode_To_Items().get(name + Constants.Code + manufacturer);
        }
        MadeNewItem(name, manufacturer);
        return itemId;
    }

    /**
     * Checks if an item exists in the itemCode_To_Items Hashtable.
     *
     * @param name        the name of the item.
     * @param manufacturer the manufacturer of the item.
     * @return true if the item exists, false otherwise.
     */
    private static boolean itemExists(String name, String manufacturer) {
        return initItemCode_To_Items().containsKey(name +Constants.Code + manufacturer);
    }

    /**
     * Adds a new item to the itemCode_To_Items Hashtable.
     *
     * @param name        the name of the new item.
     * @param manufacturer the manufacturer of the new item.
     */
    private static void MadeNewItem(String name, String manufacturer) {
        itemCode_To_Items.put(name + Constants.Code + manufacturer, itemId);
        itemId++;
    }

    /**
     * Returns the catalog number of the item.
     * @return the catalog number of the item.
     */
    public int getCatalogNumber() {
        return CatalogNumber;
    }

    /**
     * Sets the total amount of the item.
     * @param totalAmount The new total amount of the item.
     * check if below MINIMUM AMOUNT; if so, make need order
     */
    public void setTotalAmount(int totalAmount) throws NeedOrderException {
        TotalAmount = totalAmount;
        if (TotalAmount < MinimumAmount) {
            make_need_order();
        }
    }

    /**
     * Removes one item from the total amount and checks if the item quantity falls below the minimum amount,
     * triggering a need to order more.
     *
     * @throws NeedOrderException if the total amount falls below the minimum amount.
     */
    public void removeOne() throws NeedOrderException {
        //TotalAmount--;
        if (TotalAmount < MinimumAmount) {
            make_need_order();
        }
    }

    /**
     * Throws a NeedOrderException indicating that the item needs to be reordered.
     *
     * @throws NeedOrderException indicating the need to reorder the item.
     */
    public void make_need_order() throws NeedOrderException {
        String item_name = this.getName();
        String item_manufacturer = this.getManufacturer();
        int amount = (this.getMinimumAmount() - this.getTotalAmount()) * 2;
        NeedOrderException e = new NeedOrderException(item_name, item_manufacturer, amount);
        throw e;
    }

    /**
     * Returns the name of the item.
     * @return the name of the item.
     */
    public String getName() {
        return Name;
    }

    /**
     * Returns the manufacturer of the item.
     * @return the manufacturer of the item.
     */
    public String getManufacturer() {
        return Manufacturer;
    }

    /**
     * Returns the total amount of the item that is available in the store.
     * @return the total amount of the item that is available in the store.
     */
    public int getTotalAmount() {
        return TotalAmount;
    }

    /**
     * Returns the minimum amount of the item that should be kept in the store at all times.
     * @return the minimum amount of the item that should be kept in the store at all times.
     */
    public int getMinimumAmount() {
        return MinimumAmount;
    }

    /**
     * Sets the catalog number for the item.
     *
     * @param catalogNumber the catalog number to set for the item.
     */
    public void setCatalogNumber(int catalogNumber) {
        CatalogNumber = catalogNumber;
    }

    /**
     * Sets the name of the item.
     * @param name: The new name of the item.
     */
    public void setName(String name) {
        Name = name;
    }

    /**
     * Sets the manufacturer of the item.
     * @param manufacturer: The new manufacturer of the item.
     */
    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    /**
     * Sets the minimum amount for the item.
     * @param minimumAmount: The new minimum amount for the item.
     */
    public void setMinimumAmount(int minimumAmount) {
        MinimumAmount = minimumAmount;
    }

    /**
     * Retrieves the item code for the current item.
     *
     * @return the item code in the format "CatalogNumber_Code_Manufacturer"
     */
    public String getItemCode()
    {
        return Name+ Constants.Code+this.Manufacturer;
    }


}