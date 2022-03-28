package CRM_APP.Controller.Task;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Task.TaskDB;
import CRM_APP.Model.Task;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class FullCalendarView {
    private ArrayList<DateNode> allCalendarDays = new ArrayList<>(35);
    private VBox view;
    private Text calendarTitle;
    private Text calendarYear;
    private YearMonth currentYearMonth;
    private boolean isDateInMonth = false;
    private Task task;
    private String[] monthChar = {"","JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
                            "JULY", "AUGUST", "SEPTEMBER", "OCTOBER","NOVEMBER", "DECEMBER"};

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
        // Days of the week labels
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

        // Create & style calendar title and buttons to change current month
        calendarTitle = new Text();
        calendarYear = new Text();

        Button previousMonth = new Button("\uD83E\uDC94");
        Button nextMonth = new Button("\uD83E\uDC96");

        VBox monthBar= new VBox(calendarTitle, calendarYear);

        previousMonth.setOnAction(e -> {
            previousMonth();
        });
        nextMonth.setOnAction(e -> {
            nextMonth();
        });
        HBox titleBar = new HBox(previousMonth, monthBar, nextMonth);

        // Populate calendar with the appropriate day numbers
        populateCalendar(yearMonth);
        // Create the calendar view
        titleBar.setPadding(new Insets(10, 0, 20, 0));
        titleBar.setAlignment(Pos.CENTER);
        monthBar.setAlignment(Pos.CENTER);
        view = new VBox(titleBar, dayLabels, calendar);
        view.getStylesheets().add("/CRM_APP/Style/TaskStyle.css");
        view.getStyleClass().add("container");
        monthBar.getStyleClass().add("title");
        calendarTitle.getStyleClass().add("month");
        calendarYear.getStyleClass().add("year");
        calendarTitle.setFill(Paint.valueOf("#546e7a"));
        calendarYear.setFill(Paint.valueOf("#546e7a"));

    }

    //Set the days of the calendar to correspond to the appropriate date
    //yearMonth year and month of month to render

    public void generateDate(){

    }

    //function check current month
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
        // Get the date we want to start with on the calendar
        LocalDate calendarDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
        task = new Task();
        // Dial back the day until it is SUNDAY (unless the month starts on a sunday)
        while (!calendarDate.getDayOfWeek().toString().equals("SUNDAY") ) {
            calendarDate = calendarDate.minusDays(1);
        }
        // Populate the calendar with day numbers
        //create vbox inside every single grid
        for (DateNode ap : allCalendarDays) {
            if (ap.getChildren().size() != 0) {
                ap.getChildren().remove(0);
            }
            Text txt = new Text(String.valueOf(calendarDate.getDayOfMonth()));
            ap.setDate(calendarDate);
            VBox containerView = new VBox();
            HBox todoView = new HBox(); //this view for every todo
            Label lbl_task = new Label();

            todoView.getChildren().add(lbl_task);
            boolean checkDate = checkDateInMonth(calendarDate,yearMonth.getMonth().toString());
            //style & add date in month or not
            LocalDate  match = calendarDate;
//            System.out.println(match);
            if(checkDate) {
                Task task = new Task();
                task.setTaskName(lbl_task.getText());
                task.setColor("#8d6e63");
                //add date to grid
                TaskDB taskDB = new TaskDB();
                containerView.getChildren().addAll(txt);
                try {
                    ResultSet row = taskDB.getSomeID("2IhZxMp0c", Const.TASK_TABLE, "ModID");
                    while(row.next()){
                        LocalDate dateStart = formatDate(row.getString("StartDate"));;
                        LocalDate dateEnd = formatDate(row.getString("EndDate"));
                        String color = row.getString("Color");
                        String taskColor = toRGBCode(Color.web(color));
//                        if(match.equals(dateStart) ){
                        if(dateList(dateStart, dateEnd).contains(match)){//
                            lbl_task = new Label();
                            todoView = new HBox();
                            lbl_task.setText(row.getString("TaskName"));
                            todoView.getChildren().add(lbl_task);
                            VBox vBoxView = new VBox(todoView);
                            containerView.getChildren().add(vBoxView);
                            todoView.setStyle("-fx-background-color: " + taskColor);
                            todoView.setAlignment(Pos.CENTER);
                            ap.getStyleClass().add("datePane");
                            lbl_task.getStyleClass().add("taskText");
                            todoView.getStyleClass().add("subTask");
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
            //style

            todoView.setAlignment(Pos.CENTER);
            ap.getStyleClass().add("datePane");
            lbl_task.getStyleClass().add("taskText");
            todoView.getStyleClass().add("subTask");

        }

        // Change the title of the calendar
        calendarTitle.setText(yearMonth.getMonth().toString() );
        calendarYear.setText(String.valueOf(yearMonth.getYear()));
    }

    private List<LocalDate> dateList(LocalDate dateStart, LocalDate dateEnd){
        List<LocalDate> dates = Stream.iterate(dateStart, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(dateStart, dateEnd)+1)
                .collect(Collectors.toList());
        if(dates.size()>0);
        return dates;
    }

    private LocalDate formatDate(String i){
        String start =i;
        String[] d = start.split(" ");
        LocalDate date = LocalDate.parse(d[0]);
        return date;
    }
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

    //to color hex code
    public static String toRGBCode( Color color )
    {
        return String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) );
    }
}
