package CRM_APP.Database;

public class Const {
    //Table
    public static final String EMPLOYEE_TABLE = "employee";
    public static final String TEAM_TABLE = "team";
    public static final String TEAM_DETAILS_TABLE = "team_details";
    public static final String TASK_TABLE = "task";
    public static final String AUTHEN_TABLE = "authen";
    public static final String QUESTION_TABLE = "question";
    public static final String QUESTION_DETAIL_TABLE = "question_details";
    public static final String SURVEY_TYPE_TABLE = "survey_type";
    public static final String PROJECT_TABLE = "project";
    public static final String MODULE_TABLE = "module";


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
    public static final String AUTHEN_AUTHENID = "AuthenID";
    public static final String AUTHEN_LOGTIME = "LoginTime";
    public static final String AUTHEN_OUTTIME = "LogOutTime";
    public static final String AUTHEN_DEVICE = "Device";
    //question
    public static final String QUESTION_ID = "QuestionID";
    public static final String QUESTION_NAME = "QuestionName";
    public static final String QUESTION_DATE_ADD = "DateAdd";
    //survey_type
    public static final String SURVEYTYPE_ID = "SurID";
    public static final String SURVEYTYPE_NAME = "SurName";
    //Question detail
    public static final String QUESTIONDETAIL_ID = "ChoiceID";
    public static final String QUESTIONDETAIL_CHOICE = "Choice";

    //Project
    public static final String PROJECT_ID = "ProjectID";
    public static final String PROJECT_NAME = "ProjectName";
    public static final String PROJECT_BEGIN_TIME = "BeginTime";
    public static final String PROJECT_END_TIME = "EndTime";
    public static final String PROJECT_MANAGER = "Manager";
    public static final String PROJECT_TOTAL_EMP = "TotalEmployee";
    public static final String PROJECT_TOTAL_AMOUNT = "TotalAmount";

    //module
    public static final String MODULE_ID = "ModID";
    public static final String MODULE_NAME = "ModName";
    public static final String MODULE_STATUS = "Status";
    public static final String MODULE_PROJECT_ID = "ProjectID";

}
