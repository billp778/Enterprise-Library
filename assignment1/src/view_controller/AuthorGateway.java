package view_controller;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


import model.Author;




public class AuthorGateway {

	private static AuthorGateway instance = null;
	public static AuthorGateway getInstance() {
		if(instance == null) {
			instance = new AuthorGateway();
		}
		
		return instance;
	}
	
	private AuthorGateway() {
		
	}
	private Connection connection;
	
	private static Logger logger = LogManager.getLogger(AuthorGateway.class);
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public List<Author> getAuthors() {
		// fetch a mighty list of Books!! mwahahah! from the database... How Evil
		List<Author> authorlist = new ArrayList<Author>();
		Statement st = null;
		ResultSet rs = null;
		Author author;
		try {
			//fetch records 
			//don't need a parameterized query here since no user-provided input
			//is being passed to the db server
			String query = "select * "
					+ " from author";
			st = this.connection.createStatement();
			//execute the query and get the record data in the form of a ResultSet
			rs = st.executeQuery(query);

			//iterate over the result set using the next() method
			//note: result set is not initially at first row. we have to manually move it to row 1
			while(rs.next()) {
				author = new Author();
				author.setId(Integer.parseInt(rs.getString("id")));
				author.setFirstName(rs.getString("first_name"));
				author.setLastName(rs.getString("last_name"));
				author.setDateOfBirth(LocalDate.parse(rs.getString("dob"), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
				author.setGender(rs.getString("gender"));
				author.setWebSite(rs.getString("web_site"));
				
				authorlist.add(author);
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
		
		
		
		return authorlist;
	}
	
	
	public void deleteAuthor(Author author) throws Exception{
		String query = "DELETE FROM `author` WHERE id= "+author.getId();
		
		PreparedStatement ps = (PreparedStatement) connection.prepareStatement(query);

		// execute prepared statement
		ps.executeUpdate();
		
		// close prepared statement
		try {
			if(ps != null)
				ps.close();
		} catch (SQLException e) {
			logger.error("SQL Exception while attempting to close prepare statement in deleteBook() method");
			e.printStackTrace();
		}
	}
	
	public void saveAuthor(Author author) throws Exception {
		// insert or update the book
		try {
			if(author.getId()==0) {
				// if book id=0, insert a new book and save its id back into the book
				int newAuthorID;
				newAuthorID= insertAuthor(author);
				author.setId(newAuthorID);
			}else {
				// update the current book
				updateAuthor(author);
				
			}
		} catch (SQLException e) {
			logger.error("SQL Exception in saveAuthor() method");
			e.printStackTrace();
		}
	}
	
	private int insertAuthor(Author author) throws Exception{
		String query = "INSERT INTO `author` (`first_name`, `last_name`, `dob`, `gender`, `web_site`) VALUES ('"+ author.getFirstName()+"', '"+author.getLastName()+"', '"+author.getDateOfBirth()+"', '"+author.getGender()+"', '"+author.getWebSite()+"')";
		
		PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);		
		
		// execute prepared statement
		ps.executeUpdate();
		
		// generate result set
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		
		// set book ID as the result set generated ID from SQL database
		author.setId(rs.getInt(1));
		
		// close result set and prepared statement
		try {
			if(rs != null)
				rs.close();
			if(ps != null)
				ps.close();		
		} catch (SQLException e) {
			logger.error("SQL Exception while attempting to close prepare statement in insertAuthor() method");
			e.printStackTrace();
		}
		
		// pop up alert modal showing new book has been added
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText("Add Author Success");
		alert.setContentText("New Author has been added");
		alert.showAndWait();
		
		return author.getId();
	}
	
	void updateAuthor(Author author) throws Exception{
		String query = "UPDATE `author` "
				+ "SET `first_name`='"+author.getFirstName()+"' ,"
				+ "`last_name`='"+author.getLastName()+"' "
				+ ",`dob`='"+author.getDateOfBirth()+"' "
				+ ",`gender`='"+author.getGender()+"' "
				+ ",`web_site`='"+author.getWebSite()+"' "
				+ "WHERE id= "+author.getId();
		PreparedStatement ps = (PreparedStatement) connection.prepareStatement(query);

		// execute prepared statement
		ps.executeUpdate();
		
		// pop up alert modal showing book has been updated
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText("Update Author Success");
		alert.setContentText("Author has been updated");
		alert.showAndWait();
		
		// close prepared statement
		try {
			if(ps != null)
				ps.close();
		} catch (SQLException e) {
			logger.error("SQL Exception while attempting to close prepared statement in updateAuthor() method");
			e.printStackTrace();
		}
	}
	
}
