package CRM_APP.Database.Survey;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Model.QuestionType;
import CRM_APP.Model.SurveyType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionTypeDB {
    private Database db;

    //when user login
    public ResultSet getQuestionType() throws SQLException, ClassNotFoundException {
        db = new Database();
        ResultSet resultSet = null;

        String query = "SELECT * FROM " + Const.QUESTION_TYPE_TABLE;
        PreparedStatement preparedStatement = db.getDbConnection().prepareStatement(query);
        resultSet = preparedStatement.executeQuery();

        return resultSet;
    }


    public void save(QuestionType questionType) {
        try {
            db = new Database();
            String query = "INSERT INTO " + Const.QUESTION_TYPE_TABLE + "( "
                    + Const.QUESTIONTYPE_ID + ", "
                    + Const.QUESTIONTYPE_NAME + ", "
                    + Const.QUESTIONTYPE_DES + " ) VALUES  (?,?,?)";
            PreparedStatement preparedStatement = db.getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, questionType.getqTypeID());
            preparedStatement.setString(2, questionType.getqTypeName());
            preparedStatement.setString(3, questionType.getDes());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void update(QuestionType questionType) {
        db = new Database();
        String query = " UPDATE " + Const.QUESTION_TYPE_TABLE + " SET "
                + Const.QUESTIONTYPE_NAME + " =?, "
                + Const.QUESTIONTYPE_DES + "=? WHERE " + Const.QUESTIONTYPE_ID + "=?";
        try {
            PreparedStatement preparedStatement = db.getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, questionType.getqTypeName());
            preparedStatement.setString(2, questionType.getDes());
            preparedStatement.setString(3, questionType.getqTypeID());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }
}
