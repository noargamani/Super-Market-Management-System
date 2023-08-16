package BusinessLayer.Suppliers.Classes;

import BusinessLayer.Constants;

public class ItemForOrder implements Comparable<ItemForOrder> {
    private  Supplier mySupplier;

    private final int itemCatalogID;
    private final int orderAmount;
    //private double totalPriceWithItemDisc;
    private double TotalDiscount;
    private double totalPriceWithoutItemDisc;

    public ItemForOrder(int orderId, int catalogId, double totalPriceWithoutDiscount, double totalDiscount, double finalPrice, int orderAmount) {
        this.itemCatalogID=catalogId;
        this.totalPriceWithoutItemDisc=totalPriceWithoutDiscount;
        this.orderAmount=orderAmount;
    }

    private SupplierItem getMyItem()
    {
        return mySupplier.getItemByCatalogID(itemCatalogID);
    }

    public String getItemName()
    {
        return getMyItem().getName();
    }

    public String getItemManufacture()
    {
        return getMyItem().getManufacturer();
    }
    public void CalcDiscount(){
        this.TotalDiscount = getMyItem().CalcDiscountForQuantity(this.orderAmount);
    }
    public void CalcNormalPrice(){
        this.totalPriceWithoutItemDisc = getMyItem().CalcPriceForQuantity(this.orderAmount);
    }
    public ItemForOrder(int orderAmount , int itemCatalogID , Supplier mySupplier){
        this.itemCatalogID = itemCatalogID;
        this.orderAmount = orderAmount;
        this.mySupplier = mySupplier;
        CalcDiscount();
        CalcNormalPrice();
    }


    public ItemForOrder(int orderAmount , int itemCatalogID ){
        this.itemCatalogID = itemCatalogID;
        this.orderAmount = orderAmount;
try {


    CalcDiscount();
    CalcNormalPrice();
}
catch (Exception e){

}
    }

    public Constants.Pair<Double, Double> getTotalPriceWithItemDisc(double TotalOrderDisc){
        double finalDiscForItem = (10000 - (100 - TotalDiscount)*(100 - TotalOrderDisc) )/100;
        double finalPriceForItem = (100 - finalDiscForItem) * this.totalPriceWithoutItemDisc/ 100;
        return new Constants.Pair<>(finalPriceForItem , finalDiscForItem);
    }



// --Commented out by Inspection START (11/04/2023 03:43):
//    public int getItemCatalogId() {
//        return itemCatalogID;
//    }
// --Commented out by Inspection STOP (11/04/2023 03:43)



    public int getOrderAmount() {
        return orderAmount;
    }

    public double getTotalPriceWithoutItemDisc() {
        return totalPriceWithoutItemDisc;
    }

    @Override
    public String toString() {
        return "ItemForOrder{" +
                "mySupplier=" + mySupplier +
                ", itemCatalogID=" + itemCatalogID +
                ", orderAmount=" + orderAmount +
                ", TotalDiscount=" + TotalDiscount +
                ", totalPriceWithoutItemDisc=" + totalPriceWithoutItemDisc +
                '}';
    }

    public int getItemCatalogID() {
        return itemCatalogID;
    }

    public double getTotalDiscount() {
        return TotalDiscount;
    }


    @Override
    public int compareTo(ItemForOrder o) {
        return Integer.compare(this.itemCatalogID, (o.itemCatalogID));
    }
}
