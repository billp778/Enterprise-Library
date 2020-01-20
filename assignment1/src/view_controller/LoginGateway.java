package view_controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



import model.Publisher;
import model.User;

public class LoginGateway {

	private static LoginGateway instance = null;
	public static LoginGateway getInstance() {
		if(instance == null) {
			instance = new LoginGateway();
		}
		
		return instance;
	}
	
	private LoginGateway() {
		
	}
	private Connection connection;
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	public User getUser(String enterName, String password) throws Exception{
		
		User user = new User();
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			String query = "select login, password, role, name from user where login = ?" ; 
		
			st = this.connection.prepareStatement(query);
			st.setString(1, enterName);
			//execute the query and get the record data in the form of a ResultSet
			rs = st.executeQuery();

			//iterate over the result set using the next() method
			//note: result set is not initially at first row. we have to manually move it to row 1
			rs.next() ;
				user.setName(rs.getString("name"));
				user.setRole(rs.getString("role"));
			
			
		} catch (SQLException e) {
			//should log this exception since something happened during the query execution
		
			throw e;
		} finally {
			//be sure to close things properly if they are open, regardless of exception
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException e) {
				//should probably log this also
				e.printStackTrace();
			}
		}
		
	
		return user;
	}
	
	
	
}