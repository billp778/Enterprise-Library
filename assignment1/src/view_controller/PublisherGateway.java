package view_controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Publisher;

public class PublisherGateway {

	private static PublisherGateway instance = null;
	public static PublisherGateway getInstance() {
		if(instance == null) {
			instance = new PublisherGateway();
		}
		
		return instance;
	}
	
	private PublisherGateway() {
		
	}
	private Connection connection;
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public List<Publisher> getPublishers() {
		// fetch a mighty list of Books!! mwahahah! from the database... How Evil
		List<Publisher> publisherlist = new ArrayList<Publisher>();
		Statement st = null;
		ResultSet rs = null;
		Publisher publisher;
		try {
			//fetch records 
			//don't need a parameterized query here since no user-provided input
			//is being passed to the db server
			String query = "select * "
					+ " from publisher";
			st = this.connection.createStatement();
			//execute the query and get the record data in the form of a ResultSet
			rs = st.executeQuery(query);

			//iterate over the result set using the next() method
			//note: result set is not initially at first row. we have to manually move it to row 1
			while(rs.next()) {
				publisher = new Publisher();
				publisher.setId(Integer.parseInt(rs.getString("id")));
				publisher.setName(rs.getString("publisher_name"));
				publisher.setAdded(rs.getTimestamp("date_added"));
				
				publisherlist.add(publisher);
			}
			
		} catch (SQLException e) {
			//should log this exception since something happened during the query execution
			e.printStackTrace();
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
		
		
		
		return publisherlist;
	}
	
}
