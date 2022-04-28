package CRM_APP.Model;

import com.jfoenix.controls.JFXCheckBox;

public class Project {
    private String id;
    private String CusId;
    private String name;
    private String beginTime;
    private String endTime;
    private String manager;
    private int amount;

    private JFXCheckBox remark;
    private String projectTeamID;
    private String projectTeamName;

    public Project() {
        this.remark = new JFXCheckBox();
    }

    public String getProjectTeamID() {
        return projectTeamID;
    }

    public void setProjectTeamID(String projectTeamID) {
        this.projectTeamID = projectTeamID;
    }

    public String getProjectTeamName() {
        return projectTeamName;
    }

    public void setProjectTeamName(String projectTeamName) {
        this.projectTeamName = projectTeamName;
    }

    public JFXCheckBox getRemark() {
        return remark;
    }

    public void setRemark(JFXCheckBox remark) {
        this.remark = remark;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getCusId() {
        return CusId;
    }

    public void setCusId(String cusId) {
        CusId = cusId;
    }
}
