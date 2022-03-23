package CRM_APP.Model;

public class Customer {
    private String id;
    private String cusName;

    public Customer(){}
    public Customer(String cusName){
        this.cusName = cusName;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }
}
