package BusinessLayer.Suppliers.Classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class Contract {



    /**
     *
     */
    public enum Day{
        Sunday,Monday, Tuesday,Wednesday,Thursday,Friday,Saturday,None;

        public static Day getDay(int dayNumber) {
            switch (dayNumber) {
                case 1:
                    return Sunday;
                case 2:
                    return Monday;
                case 3:
                    return Tuesday;
                case 4:
                    return Wednesday;
                case 5:
                    return Thursday;
                case 6:
                    return Friday;
                case 7:
                    return Saturday;
                default:
                    return None;
            }
        }
    }

    /**
     *
     * @param doesShipment
     * @param ship_days
     * @param date_sign
     */
    public Contract(boolean doesShipment, ArrayList<Day> ship_days, Date date_sign) {
        this.doesShipment = doesShipment;
        this.ship_days = ship_days;
        this.date_sign = date_sign;
    }

    /**
     * Getter for doesShipment field.
     *
     * @return a boolean indicating if shipment is included in the contract
     */
    public boolean isDoesShipment() {
        return doesShipment;
    }

    /**
     * Setter for doesShipment field.
     *
     * @param doesShipment a boolean indicating if shipment is included in the contract
     */
    public void setDoesShipment(boolean doesShipment) {
        this.doesShipment = doesShipment;
    }

    /**
     * Getter for ship_days field.
     *
     * @return an ArrayList of Day enum representing the days of the week for shipment
     */
    public ArrayList<Day> getShip_days() {
        return ship_days;
    }

    /**
     * Setter for ship_days field.
     *
     * @param ship_days an ArrayList of Day enum representing the days of the week for shipment
     */
    public void setShip_days(ArrayList<Day> ship_days) {
        this.ship_days = ship_days;
    }

    /**
     * Getter for date_sign field.
     *
     * @return a Date object representing the signing date of the contract
     */
    public Date getDate_sign() {
        return date_sign;
    }

    boolean doesShipment;
    ArrayList<Day> ship_days;
    final Date date_sign;

    /**
     * Overrides toString() method to provide a string representation of the Contract object.
     *
     * @return a string representation of the Contract object
     */
    @Override
    public String toString() {
        return "Contract{" +
                "doesShipment=" + doesShipment +
                ", ship_days=" + ship_days +
                ", date_sign=" + date_sign +
                '}';
    }

}