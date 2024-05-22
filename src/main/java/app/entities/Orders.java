package app.entities;

import java.time.LocalDate;


public class Orders {
    private String carport;
    private int orderId;
    private String status;
    private LocalDate date;
    private int userId;
    private int customerWidth;
    private int customerLength;
    private int price;

    private ContactInformation contactInformation;




    public Orders(String carport, String status, LocalDate date, int userId) {
        this.carport = carport;
        this.status = status;
        this.date = date;
        this.userId = userId;
    }

    public Orders(int customerWidth, int customerLength, String status, ContactInformation contactInformation) {
        this.customerWidth = customerWidth;
        this.customerLength = customerLength;
        this.status = status;
        this.contactInformation = contactInformation;
    }

    public Orders(int orderId, int customerWidth, int customerLength, String status, ContactInformation contactInformation)
    {
        this.orderId=orderId;
        this.customerWidth = customerWidth;
        this.customerLength = customerLength;
        this.status=status;
        this.contactInformation = contactInformation;
    }

    public Orders(String status, int customerWidth, int customerLength, int price) {
        this.status=status;
        this.customerWidth = customerWidth;
        this.customerLength = customerLength;
        this.price=price;
    }

    public Orders(int orderId, String status, int customerWidth, int customerLength, int price) {
        this.orderId=orderId;
        this.status=status;
        this.customerWidth = customerWidth;
        this.customerLength = customerLength;
        this.price=price;
    }

    public Orders(int customerWidth, int customerLength, String status) {
        this.customerLength =customerLength;
        this.customerWidth =customerWidth;
        this.status = status;
    }


    public Orders(int orderId, String carport, String status, LocalDate date, int userId, int customerLength, int customerWidth, int price) {
        this.orderId = orderId;
        this.carport = carport;
        this.status = status;
        this.date = date;
        this.userId = userId;
        this.customerLength = customerLength;
        this.customerWidth=customerWidth;
        this.price=price;

    }



    public Orders(int orderId, String carport, String status, int userId, int customerWidth, int customerLength, int price) {
        this.orderId = orderId;
        this.carport = carport;
        this.status = status;
        this.userId = userId;
        this.customerWidth = customerWidth;
        this.customerLength = customerLength;
        this.price = price;
    }


    public String getCarport() {
        return carport;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getUserId() {
        return userId;
    }

    public int getCustomerWidth() {
        return customerWidth;
    }

    public int getCustomerLength() {
        return customerLength;
    }

    public ContactInformation getContactInformation() {
        return contactInformation;
    }

    public void setCarport(String carport) {
        this.carport = carport;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCustomerWidth(int customerWidth) {
        this.customerWidth = customerWidth;
    }

    public void setCustomerLength(int customerLength) {
        this.customerLength = customerLength;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setContactInformation(ContactInformation contactInformation) {
        this.contactInformation = contactInformation;
    }
}
