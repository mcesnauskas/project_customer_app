package lt.mindaugas.customer_app;

public class Main {
    public static void main(String[] args) {
        CustomerService customerService = new CustomerService();
        customerService.getAllCustomers().forEach(System.out::println);
    }
}
