package CRM_APP.Controller.Task;

import CRM_APP.Controller.Home.HomeController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Task.TaskDB;
import CRM_APP.Handler.DateTimePickerHandler;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Task;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class FullCalendarView {
    private ArrayList<DateNode> allCalendarDays = new ArrayList<>(35);
    private VBox view;
    private Text calendarTitle;
    private Text calendarYear;
    static StackPane main_pane = new StackPane();
    public static Button previousMonth;
    Button nextMonth;
    private YearMonth currentYearMonth;
    private boolean isDateInMonth = false;
    private Task task;
    private Thread myThread;
    private String[] monthChar = {"","JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
                            "JULY", "AUGUST", "SEPTEMBER", "OCTOBER","NOVEMBER", "DECEMBER"};
    public static boolean openFromHome = false;
//     yearMonth year month to create the calendar of
    public FullCalendarView(YearMonth yearMonth) {
        currentYearMonth = yearMonth;
        // Create the calendar grid pane

        GridPane calendar = new GridPane();
        calendar.setMaxSize(Region.USE_PREF_SIZE, -1);
        calendar.setGridLinesVisible(true);
        calendar.getStylesheets().add("/CRM_APP/Style/TaskStyle.css");
        calendar.getStyleClass().add("root");
        // Create rows and columns with anchor panes for the calendar
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                DateNode ap = new DateNode();
                ap.setPrefSize(500,500);

                calendar.add(ap,j,i);
                allCalendarDays.add(ap);
            }
        }
        //region DAY OF THE WEEK LABEL
        Text[] dayNames = new Text[]{ new Text("Sunday"), new Text("Monday"), new Text("Tuesday"),
                                        new Text("Wednesday"), new Text("Thursday"), new Text("Friday"),
                                        new Text("Saturday") };
        GridPane dayLabels = new GridPane();
        dayLabels.setPrefWidth(610);

        Integer col = 0;
        for (Text txt : dayNames) {
            AnchorPane ap = new AnchorPane();

            ap.setPrefSize(1000, 10);
            ap.setBottomAnchor(txt, 5.0);
            ap.getChildren().add(txt);
            txt.getStyleClass().add("dayNames");
            txt.setFill(Paint.valueOf("#546e7a"));
            dayLabels.add(ap, col++, 0);
        }
        //endregion
        //region DEFINE HEADER AREA
        calendarTitle = new Text();
        calendarYear = new Text();

        previousMonth = new Button("\uD83E\uDC94");
        nextMonth = new Button("\uD83E\uDC96");

        VBox monthBar= new VBox(calendarTitle, calendarYear);

        previousMonth.setOnAction(e -> {
            previousMonth();
        });
        nextMonth.setOnAction(e -> {
            nextMonth();
        });
        HBox titleBar = new HBox(previousMonth, monthBar, nextMonth);
        //endregion
        // region DEFINE AND MAKE MAIN AVAILABLE
        titleBar.setPadding(new Insets(10, 0, 20, 0));
        titleBar.setAlignment(Pos.CENTER);
        monthBar.setAlignment(Pos.CENTER);
        view = new VBox(titleBar, dayLabels, calendar);
        view.getStylesheets().add("/CRM_APP/Style/TaskStyle.css");
        view.getStylesheets().add(HomeController.styleSheet);
        view.getStyleClass().add("container");
        monthBar.getStyleClass().add("title");
        calendarTitle.getStyleClass().add("month");
        calendarYear.getStyleClass().add("year");
        calendarTitle.setFill(Paint.valueOf("#546e7a"));
        calendarYear.setFill(Paint.valueOf("#546e7a"));
        main_pane.getChildren().add(view);
        //endregion
        // Populate calendar with the appropriate day numbers
        populateCalendar(yearMonth);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        populateCalendar(currentYearMonth);
                        System.out.println("running");
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask,  0,1000);

//        Thread thread = new Thread(myThread){
//            @Override
//            public void run() {
//                super.run();
//                myThread= new Thread(this::handleThread);
//                myThread.start();
//            }
//            private void handleThread(){
//                while(true){
//                    Platform.runLater(() ->{
//
//                    });
//                    try{
//
//                        Thread.sleep(5000);
//                    }catch(InterruptedException e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };


//        view.sceneProperty().addListener((obs, oldScene, newScene) -> {
//            if (newScene == null) {
//                // not showing...
//                System.out.println("thread interrupted");
//                thread.interrupt();
//            } else {
//                // showing ...
//                System.out.println("thread started");
//                thread.start();
//            }
//        });
        //endregion
    }

    //Set the days of the calendar to correspond to the appropriate date
    //yearMonth year and month of month to render

    //function Check available month
    public boolean checkDateInMonth( LocalDate check, String base){
        String[] newCheck = check.toString().split("-");
        int c = Integer.parseInt(newCheck[1]);
        if(base.equals(monthChar[c])){
            return true;
        }else {
            return false;
        }
    }

    public void populateCalendar(YearMonth yearMonth) {
        // Get the date i want to start with on the calendar
        LocalDate calendarDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
        task = new Task();
        // Dial back the day until it is SUNDAY (unless the month starts on a sunday)
        while (!calendarDate.getDayOfWeek().toString().equals("SUNDAY") ) {
            calendarDate = calendarDate.minusDays(1);
        }
        //create vbox inside every single grid
        for (DateNode ap : allCalendarDays) {
            if (ap.getChildren().size() != 0) {
                ap.getChildren().remove(0);
            }
            //region DEFINE COMPONENTS FOR GRID
            Text txt = new Text(String.valueOf(calendarDate.getDayOfMonth()));
            ap.setDate(calendarDate);
            VBox containerView = new VBox(); //this is container
            HBox todoView = new HBox(); //this view todoLabel container
            Label lbl_task = new Label(); //this todoTitle
            //endregion
            todoView.getChildren().add(lbl_task);
            boolean checkDate = checkDateInMonth(calendarDate,yearMonth.getMonth().toString());
            //style & add date in month or not
            LocalDate  match = calendarDate;

//          region ADD INFORMATION TO EVERY SINGLE GRID
            if(checkDate) {
                Task task = new Task();
                task.setTaskName(lbl_task.getText());
                //add date to grid
                TaskDB taskDB = new TaskDB();
                containerView.getChildren().addAll(txt);
                //GET TASKS LIST BELONG TO CURRENT EMP
                try {
                    ResultSet row = taskDB.getSomeID(HomeController.userId, Const.TASK_TABLE, "EmpID");
                    while(row.next()){
                        LocalDate dateStart = DateTimePickerHandler.formatDate(row.getString("StartDate"));;
                        LocalDate dateEnd = DateTimePickerHandler.formatDate(row.getString("EndDate"));
                        String color = row.getString("Color");
                        String taskColor = OtherHandler.toRGBCode(Color.web(color));
                        if(OtherHandler.dateList(dateStart, dateEnd).contains(match)){
                            //region DEFINE FOR NEW EACH TASK INSIDE
                            lbl_task = new Label();
                            todoView = new HBox();
                            String taskName = "";
                            String  taskID = "";
                            taskName = row.getString("TaskName");
                            lbl_task.setText(taskName);
                            todoView.getChildren().add(lbl_task);
                            VBox vBoxView = new VBox(todoView);
                            containerView.getChildren().add(vBoxView);
                            //endregion
                            //region OPEN SEND INFO TO TASK DETAILS
                            String finalTaskName = taskName;
                            LocalDate finalCalendarDate = calendarDate;
                            String finalTaskID = row.getString(Const.TASK_ID);
                            todoView.setOnMouseClicked(e -> {
                                SceneHandler sceneHandler = new SceneHandler();
                                TaskDetailController.dates = finalCalendarDate;
                                TaskDetailController.taskId = finalTaskID;
                                sceneHandler.newScene("/CRM_APP/View/Task/taskDetail.fxml");
                            });
                            //endregion
                            //region STYLE
                            todoView.setStyle("-fx-background-color: " + taskColor);
                            todoView.setAlignment(Pos.CENTER);
                            ap.getStyleClass().add("datePane");
                            lbl_task.getStyleClass().add("taskText");
                            todoView.getStyleClass().add("subTask");
                            //endregion
                        }
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                ap.getChildren().addAll(containerView);
                //reset style
                ap.getStyleClass().removeIf(style -> style.equals("notMonth"));
            }else{
                ap.getStyleClass().add("notMonth");
            }

            calendarDate = calendarDate.plusDays(1);
            //style again
            todoView.setAlignment(Pos.CENTER);
            ap.getStyleClass().add("datePane");
            lbl_task.getStyleClass().add("taskText");
            todoView.getStyleClass().add("subTask");
        //endregion
        }

        // Change the title of the calendar
        calendarTitle.setText(yearMonth.getMonth().toString() );
        calendarYear.setText(String.valueOf(yearMonth.getYear()));
    }


    //refresh listview


//     Move the month back by one. Repopulate the calendar with the correct dates.
    private void previousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        populateCalendar(currentYearMonth);
    }

//     Move the month forward by one. Repopulate the calendar with the correct dates.
    private void nextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        populateCalendar(currentYearMonth);
    }

    public VBox getView() {
        return view;
    }

    public ArrayList<DateNode> getAllCalendarDays() {
        return allCalendarDays;
    }

    public void setAllCalendarDays(ArrayList<DateNode> allCalendarDays) {
        this.allCalendarDays = allCalendarDays;
    }
}
