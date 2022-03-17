package CRM_APP.Database;

import java.sql.Date;

public class Const {
    //Table
    public static final String EMPLOYEE_TABLE = "employee";
    public static final String TEAM_TABLE = "team";
    public static final String TEAM_DETAILS_TABLE = "team_details";
    public static final String TASK_TABLE = "task";
    public static final String AUTHEN_TABLE = "authen";
    //columns name
    //employee
    public static final String EMPLOYEE_ID = "EmpID";
    public static final String EMPLOYEE_NAME = "EmpName";
    public static final String EMPLOYEE_ADDRESS = "Address";
    public static final String EMPLOYEE_PHONE = "Phone";
    public static final String EMPLOYEE_POSITION = "Position";
    public static final String EMPLOYEE_PASSWORD = "Password";
    //team
    public static final String TEAM_ID = "TeamID";
    public static final String TEAM_NAME = "TeamName";
    //team details
    public static final String TEAM_EM_ID = "EmpID";
    public static final String TEAM_TEAM_ID = "TeamID";
    public static final String TEAM_STATUS = "Status";
    //task
    public static final String TASK_ID = "TaskID";
    public static final String TASK_NAME = "TaskName";
    public static final String TASK_STATUS = "Status";
    public static final String TASK_COLOR = "Color";
    public static final String TASK_START = "start_date";
    public static final String TASK_END = "end_date";
    //authen
    public static final String AUTHEN_AUTHENID = "auID";
    public static final String AUTHEN_LOGTIME = "LoginTime";
    public static final String AUTHEN_OUTTIME = "LogOutTime";
    public static final String AUTHEN_DEVICE = "device";
    public static final String AUTHEN_LOCATION = "location";
}
