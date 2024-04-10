package lt.mindaugas.customer_app;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.List;

@WebServlet("/customers")
public class CustomerListServlet extends HttpServlet {

    CustomerService customerService;

    public CustomerListServlet() {
        this.customerService = new CustomerService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();

        List<Customer> list = customerService.getAllCustomers();
        for (Customer customer : list) {
            writer.write("<p>" + customer.toString() + "</p>");
            System.out.println(customer);
        }


    }
}
