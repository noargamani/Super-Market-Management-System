package BusinessLayer.Suppliers.Classes;

public class Supplier_Card {


    int BN_number;
    String bank_account;
    String pay_condition;

    /**
     *
     * @param BN_number
     * @param bank_account
     * @param pay_condition
     */
    public Supplier_Card(int BN_number, String bank_account, String pay_condition) {
        this.BN_number = BN_number;
        this.bank_account = bank_account;
        this.pay_condition = pay_condition;
    }

    public int getBN_number() {
        return BN_number;
    }

    public void setBN_number(int BN_number) {
        this.BN_number = BN_number;
    }

    public String getBank_account() {
        return bank_account;
    }

    public void setBank_account(String bank_account) {
        this.bank_account = bank_account;
    }

    public String getPay_condition() {
        return pay_condition;
    }

    public void setPay_condition(String pay_condition) {
        this.pay_condition = pay_condition;
    }
}
