package CRM_APP.Model;

import com.jfoenix.controls.JFXCheckBox;

public class Team {
    private String teamID;
    private String teamName;
    private String status;
    private String emID;
    private JFXCheckBox remark;

    public Team() {
        remark = new JFXCheckBox();
    }

    public JFXCheckBox getRemark() {
        return remark;
    }

    public void setRemark(JFXCheckBox remark) {
        this.remark = remark;
    }

    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmID() {
        return emID;
    }

    public void setEmID(String emID) {
        this.emID = emID;
    }
}
