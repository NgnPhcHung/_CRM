package CRM_APP.Controller.Task;

import CRM_APP.Model.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;


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

            Label testString = new Label();
            testString.setText("this is task holder");
            todoView.getChildren().add(testString);
            boolean checkDate = checkDateInMonth(calendarDate,yearMonth.getMonth().toString());
            //style & add date in month or not

            if(checkDate) {
                Task task = new Task();
                task.setTask(testString.getText());
                task.setColor("#ff8f00");
                //add date to grid
                containerView.getChildren().add(txt);
                containerView.getChildren().addAll(todoView);
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
            testString.getStyleClass().add("taskText");
            todoView.getStyleClass().add("subTask");
        }
        // Change the title of the calendar
        calendarTitle.setText(yearMonth.getMonth().toString() );
        calendarYear.setText(String.valueOf(yearMonth.getYear()));
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
}
