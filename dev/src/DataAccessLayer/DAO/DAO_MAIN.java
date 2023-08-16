package DataAcessLayer.DAO;

import DataAcessLayer.DAO.Suppliers.SupplierDAO;
import DataAcessLayer.Repo;

import java.sql.Statement;

public class DAO_MAIN {
    public static void deleteAllItems( String tableName)  {
        try {
            String query = "DELETE FROM " + tableName;
            try (Statement stmt = Repo.connect().createStatement()) {
                stmt.executeUpdate(query);
            }
        }
        catch (Exception e){
        }
    }
    public static void  createTables(){
        try {
            Repo.createTables(Repo.connect());
        }
        catch (Exception e){

        }
    }

    public static SupplierDAO initControllers(){
        try {
            SupplierDAO sC = new SupplierDAO();

            /*
            Hashtable<String, Supplier> suppliers = sC.getSuppliers();
            ContactDAO contactDAO=new ContactDAO();
            ArrayList<ContactDTO> contactDTOS=contactDAO.getAllContacts();

            SupplierCardDAO supplierCardDAO=new SupplierCardDAO();
             ManufactureDAO manufactureDAO=new ManufactureDAO();

            ContractDAO contractDAO=new ContractDAO();
            ArrayList<ContractDTO> contractDTOS=contractDAO.getAllContracts();

            TotalOrderDiscountByPriceDAO totalOrderDiscountByPriceDAO =new TotalOrderDiscountByPriceDAO();
            int max=0;
            for(Supplier supplier: suppliers.values()){

                for(ContractDTO dto:contractDTOS){
                    if(dto.getSupplierName()==supplier.getSupplier_name()){
                        Contract contract=new Contract(dto.isDoesShipment(),dto.getShipDays(),dto.getDateSign());
                        supplier.setContract(contract);
                    }
                }
                for(ContactDTO contactDTO:contactDTOS){
                    if(contactDTO.getSupplierName() == (supplier.getSupplier_name())){
                        supplier.getContact_List().put(contactDTO.getName(),contactDTO.getPhone());
                    }
                }
                SupplierCardDTO supplierCardDTOS=supplierCardDAO.getSupplierCardByBnNumber(supplier.getBn_number());
                Supplier_Card supplier_card=new Supplier_Card(supplier.getBn_number(),supplierCardDTOS.getBankAccount(),supplierCardDTOS.getPayCondition());
                supplier.setSupplier_card(supplier_card);

                 ArrayList<TotalOrderDiscountByPriceDTO> totalOrderDiscountByPriceDTOS = totalOrderDiscountByPriceDAO.getAllTotalOrderDiscounts();
                 for(TotalOrderDiscountByPriceDTO dto: totalOrderDiscountByPriceDTOS){
                     if(dto.getSupplier().equals(supplier.getSupplier_name())){
                         supplier.getDiscountByTotalPrice().put(dto.getPrice(),dto.getDiscount());
                     }
                 }
                try {


                    ArrayList<ManufactureDTO> manufactureDTOS = manufactureDAO.getAllManufacturesFromSupplier(supplier.getSupplier_name());
                    for (ManufactureDTO m : manufactureDTOS) {
                        supplier.getManufactures().add(m.getName());
                    }
                }
                catch (Exception e){

                }
                sC.getSuppliers().put(supplier.getSupplier_name(),supplier);

            }
             */
            return sC;
        }
        catch (Exception e){
            return  null;
        }

    }

}
