package CRM_APP.Model;

import com.jfoenix.controls.JFXCheckBox;

public class Employee {
    private String id;
    private String name;
    private String address;
    private String phone;
    private String position;
    private String password;
    private JFXCheckBox remark;

    public Employee() {
        remark = new JFXCheckBox();
    }

    public JFXCheckBox getRemark() {
        return remark;
    }

    public void setRemark(JFXCheckBox remark) {
        this.remark = remark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
