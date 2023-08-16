import BusinessLayer.Suppliers.Classes.Contract;
import Presentaion_layer.CLI.Actions.Order.Make_Auto_Order;
import Service_layer.InventoryService;
import Service_layer.Suppliers_Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


class DayNotifier {
    private Suppliers_Service suppliers_service;
    private InventoryService inventoryService;
    private LocalDate lastNotificationDate;

    /**
     * Constructs a DayNotifier object with the specified Suppliers_Service and InventoryService.
     *
     * @param suppliers_service The Suppliers_Service object to use for periodic orders.
     * @param inventoryService  The InventoryService object for inventory management.
     */
    public DayNotifier(Suppliers_Service suppliers_service, InventoryService inventoryService) {
        this.suppliers_service = suppliers_service;
        this.inventoryService = inventoryService;
        this.lastNotificationDate = null;
    }

    /**
     * Checks the current day and triggers periodic orders if a day has passed since the last notification.
     * The periodic orders are based on the current day of the week.
     */
    public void checkDay() throws Exception {
        LocalDate currentDate = LocalDate.now();
        LocalDate tomorrow = currentDate.plusDays(1);
        Contract.Day currentDay = Make_Auto_Order.getDayOfWeek(tomorrow.getDayOfWeek().toString());
        System.out.println("Today is " + currentDay);

        // Check if a day has passed since the last notification
        if (lastNotificationDate != null) {
            int daysSinceLastNotification = (int) ChronoUnit.DAYS.between(lastNotificationDate, currentDate);
            if (daysSinceLastNotification >= 1) {
                suppliers_service.PeriodicOrder(currentDay);
            }
        }

        // Update the last notification date
        lastNotificationDate = currentDate;
    }
}
