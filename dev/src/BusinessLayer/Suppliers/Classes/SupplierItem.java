package BusinessLayer.Suppliers.Classes;



import BusinessLayer.Constants;
import BusinessLayer.Item;

import java.util.*;

public class SupplierItem extends Item {

    private final int itemId;
    private final String mySupplier;
    double price;
    Date expiration;
    SortedMap<Integer,Integer> discountByQuantity;//Quantity->discount(only on the items)
   int supplier_catalogID;
    int quantity;

    /**
     * Constructor for creating an instance of the Item class.
     * @param itemId The unique identifier for the item.
     * @param itemName The name of the item.
     * @param itemManufacture The manufacturer of the item.
     * @param supplier_catalogID The catalog ID of the item in the supplier's catalog.
     * @param item_list_price The price of the item.
     * @param discountByQuantity Quantity-based discount for the item.
     * @param quantity The quantity of the item.
     * @param mySupplier The supplier associated with the item.
     */
    public SupplierItem(int CatalogID, String itemName, String itemManufacture , int supplier_catalogID, double item_list_price, SortedMap<Integer,
            Integer> discountByQuantity, int quantity , String mySupplier, Date expiration, int itemId) {
        super(CatalogID, itemName, itemManufacture,0,0);
        // TODO: insert minimum amount & total item

        this.supplier_catalogID = CatalogID;
        this.price = item_list_price;
        this.discountByQuantity = new TreeMap<>();
        if(discountByQuantity != null)
        {
            this.discountByQuantity.putAll(discountByQuantity);
        }
        this.quantity = quantity;
        this.mySupplier = mySupplier;
        this.expiration = expiration;
        this.itemId = itemId;
    }

    public Date getExpiration() {
        return expiration;
    }

    public int getItemId() {
        return itemId;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    /**
     * Get the supplier's catalog ID for the item.
     * @return The supplier's catalog ID for the item.
     */
    public int getSupplier_catalogID() {
        return supplier_catalogID;
    }

    /**
     * Get the list price of the item.
     * @return The list price of the item.
     */
    public double getItem_list_price() {
        return price;
    }

    /**
     * Get the quantity-based discount for the item.
     * @return The quantity-based discount for the item.
     */
    public SortedMap<Integer, Integer> getDiscountByQuantity() {
        return discountByQuantity;
    }


    /**
     * Get the supplier associated with the item.
     * @return The supplier associated with the item.
     */
    public String getSupplier()
    {
        return this.mySupplier;
    }

    /**
     * Get the quantity of the item.
     * @return The quantity of the item.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Set the quantity of the item.
     * @param quantity The quantity of the item.
     */
    public void removeQuantity(int quantity) {
        this.quantity =  this.quantity-quantity;
        if( this.quantity < 0)
        {
            try {
                throw new Exception("cantBuy Quantity");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Get the item code, which combines the item name and manufacturer.
     * @return The item code.
     */
    public String getItemManufacture() {
        return Manufacturer;
    }
    /**
     * Get Price For One For Every Quantity Discount
     * @return
     */
    public SortedMap<Integer , Double> GetPriceForOneForEveryQuantityDiscount()
    {
        SortedMap<Integer , Double> PricePerUnitDiscounted = new TreeMap<>();
        int[] quantityArray = discountByQuantity.keySet().stream().mapToInt(i->i).toArray();
        for (int quantity : quantityArray) {
            if (quantity > this.quantity) {
                break;
            }
            //else
            double cost = CalculatePriceWithQuantityDiscountForOne(quantity).first;
            PricePerUnitDiscounted.put(quantity, cost);
        }
        return PricePerUnitDiscounted;
    }

    private Constants.Pair<Double , Double> CalculatePriceWithQuantityDiscountForOne(int quantity)
    {
        double discount = CalculateDiscount(quantity);
        double Price = getPrice(discount);
        return new Constants.Pair<>(Price, discount);
    }
    private double CalculateDiscount(int quantity)
    {
        if(quantity > this.quantity)
        {
            return -1;
        }
        //else
        int[] quantityArray = discountByQuantity.keySet().stream().mapToInt(i->i).toArray();
        int discQuantityIdx = -1;
        if(quantityArray.length != 0) {
            discQuantityIdx = Constants.binarySearch(quantityArray, 0, quantityArray.length, quantity);
        }
        if(discQuantityIdx == -1)
        {
            return 0;
        }
        return (double) discountByQuantity.get(quantityArray[discQuantityIdx]);
    }

    private double getPrice(double discount) {
        return ((100 - discount) * this.price) / 100;
    }

    public double CalculatePriceWithQuantityDiscount(int quantity)
    {
        return CalculatePriceWithQuantityDiscountForOne(quantity).first*quantity;
    }
    public double CalcDiscountForQuantity(int quantity)
    {
        return CalculatePriceWithQuantityDiscountForOne(quantity).second;
    }
    public double CalcPriceForQuantity(int quantity)
    {
        return price * quantity;
    }


    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + CatalogNumber +
                ", itemName='" + Name + '\'' +
                ", supplier_catalogID=" + supplier_catalogID +
                ", price=" + price +
                ", discountByQuantity=" + discountByQuantity +
                ", quantity=" + quantity +
                '}';
    }

    public void setQuantity(int i) {
        this.quantity = i;
    }
}
