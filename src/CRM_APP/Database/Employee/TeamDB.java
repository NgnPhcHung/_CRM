package CRM_APP.Database.Employee;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Model.Team;
import sun.plugin2.main.server.ResultID;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeamDB {
    private Database database;

    public void save(Team team){
        try {
            String query = "INSERT INTO " + Const.TEAM_TABLE + " ( "
                            + Const.TEAM_ID + ", "
                            + Const.TEAM_NAME+") "
                            + " VALUES (?,?)";
            PreparedStatement preparedStatement = Database.dbConnection.prepareStatement(query);
            preparedStatement.setString(1, team.getTeamID());
            preparedStatement.setString(2, team.getTeamName());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addMember(Team team){
        try {
            String query = "INSERT INTO " + Const.TEAM_DETAIL_TABLE + " ( "
                    + Const.TEAM_ID + ", "
                    + Const.EMPLOYEE_ID+") "
                    + " VALUES (?,?)";
            PreparedStatement preparedStatement = Database.dbConnection.prepareStatement(query);
            preparedStatement.setString(1, team.getTeamID());
            preparedStatement.setString(2, team.getEmID());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ResultSet countMember(Team team) throws SQLException {
        ResultSet resultSet = null;
        String  query= "SELECT COUNT(*) AS MEM FROM " + Const.TEAM_DETAIL_TABLE + " WHERE "+ Const.TEAM_ID + "=?";
        PreparedStatement preparedStatement = Database.dbConnection.prepareStatement(query);
        preparedStatement.setString(1, team.getTeamID());
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
    public ResultSet countTask(Team team) throws SQLException{
        ResultSet resultSet = null;
        String  query= "SELECT COUNT(*) AS countTask FROM " + Const.TASK_TABLE + " WHERE "+ Const.TASK_EMP_ID + "=?";
        PreparedStatement preparedStatement = Database.dbConnection.prepareStatement(query);
        preparedStatement.setString(1, team.getEmID());
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
    public ResultSet getTeamMember(Team team) throws SQLException {
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + Const.TEAM_DETAIL_TABLE +" WHERE " + Const.TEAM_ID + " =? AND " + Const.TEAM_EM_ID + " =?";
        PreparedStatement preparedStatement = Database.dbConnection.prepareStatement(query);
        preparedStatement.setString(1, team.getTeamID());
        preparedStatement.setString(2, team.getEmID());
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
}
