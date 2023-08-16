package DataAcessLayer.DTO.Suppliers;



public class SupplierCardDTO {
    private int bnNumber;
    private String payCondition;
    private String bankAccount;

    public SupplierCardDTO(int bnNumber, String payCondition, String bankAccount) {
        this.bnNumber = bnNumber;
        this.payCondition = payCondition;
        this.bankAccount = bankAccount;
    }

    public int getBnNumber() {
        return bnNumber;
    }

    public void setBnNumber(int bnNumber) {
        this.bnNumber = bnNumber;
    }

    public String getPayCondition() {
        return payCondition;
    }

    public void setPayCondition(String payCondition) {
        this.payCondition = payCondition;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }
}
