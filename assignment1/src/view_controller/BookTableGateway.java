package view_controller;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.MediaException;
import model.AuditTrailEntry;
import model.Author;
import model.AuthorBook;
import model.Book;


public class BookTableGateway {
private static BookTableGateway instance = null;
	
	private Connection connection;
	
	private static Logger logger = LogManager.getLogger(BookTableGateway.class);
	public int count = 0;
	public int searchCount = 0;

	// private List<AuthorBook> aBooks;

	public static BookTableGateway getInstance() {
		if(instance == null) {
			instance = new BookTableGateway();
		}
		
		return instance;
	}
	
	private BookTableGateway() {
		
	}

	public List<AuthorBook> getAuthorsForBook(Book book){
		
		List<AuthorBook> aBooks = new ArrayList<AuthorBook>();
		Book bk1 = null;
		Author auth1 = null;
		AuthorBook authBk = null;
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			//fetch records 
			//don't need a parameterized query here since no user-provided input
			//is being passed to the db server
			String query = "select * from author_book inner join author as AD on author_book.author_id = AD.id inner join book as BD on author_book.book_id = BD.id where book_id=?;";
				
			st = this.connection.prepareStatement(query);
			st.setString(1, "" + book.getId() );
			rs = st.executeQuery();
			
			while(rs.next()) {
				
				bk1 = new Book();
				bk1.setId(Integer.parseInt(rs.getString("id")));
				bk1.setTitle(rs.getString("title"));
				bk1.setSummary(rs.getString("summary"));
				bk1.setYearPublished(Integer.parseInt(rs.getString("year_published")));
				bk1.setIsbn(rs.getString("isbn"));
				bk1.setPublisherID(rs.getInt("publisher_id"));
				bk1.setTimestamp(rs.getTimestamp("last_modified"));
				
				auth1 = new Author();
				auth1.setId(Integer.parseInt(rs.getString("id")));
				auth1.setFirstName(rs.getString("first_name"));
				auth1.setLastName(rs.getString("last_name"));
				auth1.setDateOfBirth(LocalDate.parse(rs.getString("dob"), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
				auth1.setGender(rs.getString("gender"));
				auth1.setWebSite(rs.getString("web_site"));
				
				authBk = new AuthorBook();
				authBk.setAuthor(auth1);
				authBk.setBook(bk1);
				double royalty = Double.parseDouble(rs.getString("royality")) * 100000;
				authBk.setRoyalty((int) royalty);
				//authBk.
				aBooks.add(authBk);
			
			//execute the query and get the record data in the form of a ResultSet
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return aBooks;
	}
	
	
	public List<AuditTrailEntry> getAuditTrails(Book book) {
		// fetch a mighty list of Books!! mwahahah! from the database... How Evil
		List<AuditTrailEntry> auditlist = new ArrayList<AuditTrailEntry>();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			//fetch records 
			//don't need a parameterized query here since no user-provided input
			//is being passed to the db server
			String query = "select * "
					+ " from book_audit_trail where book_id=(?) order by date_added ASC;";
			st = this.connection.prepareStatement(query);
			st.setString(1, "" + book.getId() );
			rs = st.executeQuery();
			
			while(rs.next())
			{
				AuditTrailEntry temp = new AuditTrailEntry();
				temp.setId(rs.getInt("id"));
				temp.setDate(rs.getTimestamp("date_added"));
				temp.setMessage(rs.getString("entry_msg"));
				auditlist.add(temp);
			}

			//execute the query and get the record data in the form of a ResultSet
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return auditlist;
	}

	
public List<Book> getBooks() {
	// fetch a mighty list of Books!! mwahahah! from the database... How Evil
		List<Book> booklist = new ArrayList<Book>();
		Statement st = null;
		ResultSet rs = null;
		ResultSet rp1 = null;
		Book book;
		try {
			//fetch records 
			//don't need a parameterized query here since no user-provided input
			//is being passed to the db server
			String query = "select * "
					+ " from book LIMIT 0,50";
			st = this.connection.createStatement();
			Statement stat = this.connection.createStatement();
	        rp1 = stat.executeQuery("SELECT COUNT(*) FROM book;");
	        while(rp1.next()){
	     	   this.setCount(rp1.getInt(1));
	     	   
	     	}
			//execute the query and get the record data in the form of a ResultSet
			rs = st.executeQuery(query);

			//iterate over the result set using the next() method
			//note: result set is not initially at first row. we have to manually move it to row 1
			while(rs.next()) {
				book = new Book();
				book.setId(Integer.parseInt(rs.getString("id")));
				book.setTitle(rs.getString("title"));
				book.setSummary(rs.getString("summary"));
				book.setYearPublished(Integer.parseInt(rs.getString("year_published")));
				book.setIsbn(rs.getString("isbn"));
				book.setPublisherID(rs.getInt("publisher_id"));
				book.setTimestamp(rs.getTimestamp("last_modified"));
				booklist.add(book);
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
	
	
	
	return booklist;
}


public void saveBook(Book book) throws Exception {
	// insert or update the book
	try {
		if(book.getId()==0) {
			// if book id=0, insert a new book and save its id back into the book
			int newBookID;
			newBookID= insertBook(book);
			book.setId(newBookID);
		}else {
			// update the current book
			updateBook(book);
			
		}
	} catch (SQLException e) {
		logger.error("SQL Exception in saveBook() method");
		e.printStackTrace();
	}
}

public void updateRoyal(int BookID, int AuthorID, double royal) throws Exception{
	String query = "UPDATE `author_book` SET `royality` = '"+royal+"' WHERE `author_book`.`author_id` = "+AuthorID+" AND `author_book`.`book_id` = "+BookID+";";
	
	PreparedStatement ps = (PreparedStatement) connection.prepareStatement(query);

	// execute prepared statement
	ps.executeUpdate();
	
	// pop up alert modal showing book has been updated
	Alert alert = new Alert(AlertType.INFORMATION);
	alert.setTitle("Information Dialog");
	alert.setHeaderText("Update Royalty Success");
	alert.setContentText("Royalty has been updated");
	alert.showAndWait();
	
	// close prepared statement
	try {
		if(ps != null)
			ps.close();
	} catch (SQLException e) {
		logger.error("SQL Exception while attempting to close prepared statement in updateBook() method");
		e.printStackTrace();
	}
}

public void insertAuthor(int BookID, int AuthorID) throws Exception{
	String query = "INSERT INTO `author_book` (`author_id`, `book_id`, `royality`) VALUES ('"+AuthorID+"', '"+BookID+"', '"+0+"')";
	
	PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);		
	
	// execute prepared statement
	ps.executeUpdate();
	
	// generate result set
	ResultSet rs = ps.getGeneratedKeys();
	rs.next();
	

	
	// close result set and prepared statement
	try {
		if(rs != null)
			rs.close();
		if(ps != null)
			ps.close();		
	} catch (SQLException e) {
		logger.error("SQL Exception while attempting to close prepare statement in insertBook() method");
		e.printStackTrace();
	}
	
	// pop up alert modal showing new book has been added
	Alert alert = new Alert(AlertType.INFORMATION);
	alert.setTitle("Information Dialog");
	alert.setHeaderText("Add Author Success");
	alert.setContentText("New Author has been added");
	alert.showAndWait();

}
	

void updateBook(Book book) throws Exception{
	String query = "UPDATE `book` "
			+ "SET `title`='"+book.getTitle()+"' ,"
			+ "`summary`='"+book.getSummary()+"' "
			+ ",`year_published`="+book.getYearPublished()+" "
			+ ",`publisher_id`="+book.getPublisherID()+" "
			+ ",`isbn`='"+book.getIsbn()+"' "
			+ "WHERE id= "+book.getId();
	PreparedStatement ps = (PreparedStatement) connection.prepareStatement(query);

	// execute prepared statement
	ps.executeUpdate();
	
	// pop up alert modal showing book has been updated
	Alert alert = new Alert(AlertType.INFORMATION);
	alert.setTitle("Information Dialog");
	alert.setHeaderText("Update Book Success");
	alert.setContentText("Book has been updated");
	alert.showAndWait();
	
	// close prepared statement
	try {
		if(ps != null)
			ps.close();
	} catch (SQLException e) {
		logger.error("SQL Exception while attempting to close prepared statement in updateBook() method");
		e.printStackTrace();
	}
}




private int insertBook(Book book) throws Exception{
	String query = "INSERT INTO `book` (`title`, `summary`, `year_published`, `isbn`) VALUES ('"+ book.getTitle()+"', '"+book.getSummary()+"', '"+book.getYearPublished()+"', '"+book.getIsbn()+"')";
	
	PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);		
	
	// execute prepared statement
	ps.executeUpdate();
	
	// generate result set
	ResultSet rs = ps.getGeneratedKeys();
	rs.next();
	
	// set book ID as the result set generated ID from SQL database
	book.setId(rs.getInt(1));
	
	// close result set and prepared statement
	try {
		if(rs != null)
			rs.close();
		if(ps != null)
			ps.close();		
	} catch (SQLException e) {
		logger.error("SQL Exception while attempting to close prepare statement in insertBook() method");
		e.printStackTrace();
	}
	
	// pop up alert modal showing new book has been added
	Alert alert = new Alert(AlertType.INFORMATION);
	alert.setTitle("Information Dialog");
	alert.setHeaderText("Add Book Success");
	alert.setContentText("New book has been added");
	alert.showAndWait();
	
	return book.getId();
}


public void deleteBook(Book book) throws Exception{
	String query = "DELETE FROM `book` WHERE id= "+book.getId();
	
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

public void deleteAuthorBook(AuthorBook authorBook) throws Exception{
	String query = "DELETE FROM `author_book` WHERE author_id= "+authorBook.getAuthor().getId();
	
	PreparedStatement ps = (PreparedStatement) connection.prepareStatement(query);
	
	ps.executeUpdate();
	
	try{
		if(ps != null)
			ps.close();
	}catch (SQLException e) {
		logger.error("SQL Exception while attempting to close prepare statement in deleteAuthorBook() method");
		e.printStackTrace();
	}
	
}
public Timestamp checkCurrentTimestamp(Book book) {
	
	try {
		// do database things
		PreparedStatement st=null;
		ResultSet rs =null;
		try {
			String query="SELECT `last_modified` FROM `book` WHERE id="+book.getId();

			st=(PreparedStatement) connection.prepareStatement(query);

			// generate result set
			rs = st.executeQuery();
			rs.next();

			// return timestamp from database
			return rs.getTimestamp("last_modified");
							
		}catch (SQLException e) {
			logger.error("SQL Exception while attempting to retreive timestamp");
			e.printStackTrace();
		} finally {

			// close result set and prepared statement
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException e) {
				logger.error("SQL Exception while attempting to close prepared statement in checkCurrentTimestamp method");
				e.printStackTrace();
			}
		}
		
	} catch(MediaException e) {
		logger.error("Exception in getBooks() method, exiting system");
		e.printStackTrace();
		System.exit(1);
	}
	logger.error("Could not load timestampe from database, returning null");
	return null;
	
	
}

public void writeAudit(int bookID, String message) {
	//TODO update query, just write to database, don't need return statements
	String query ="INSERT INTO `book_audit_trail` (`id`, `book_id`, `date_added`, `entry_msg`) VALUES (NULL, '"+bookID+"', CURRENT_TIMESTAMP, '"+message+"')";
	
	PreparedStatement ps = null;
	try {
		ps = connection.prepareStatement(query);		
		
		// execute prepared statement
		ps.executeUpdate(query);
	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	
	// close result set and prepared statement
	try {
		if(ps != null)
			ps.close();		
	} catch (SQLException e) {
		logger.error("SQL Exception while attempting to close prepare statement in writeAudit() method");
		e.printStackTrace();
	}		
}

public void insertAuto(String title, String summary, int year, int pub, String isbn) throws Exception{
	String query = "INSERT INTO `book` (`title`, `summary`, `year_published`, `publisher_id`, `isbn`) VALUES ('"+ title+"', '"+summary+"', '"+year+"', '"+pub+"', '"+isbn+"')";
	
	PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);		
	
	// execute prepared statement
	ps.executeUpdate();
	
	// generate result set
	ResultSet rs = ps.getGeneratedKeys();
	rs.next();
	
	
	
	// close result set and prepared statement
	try {
		if(rs != null)
			rs.close();
		if(ps != null)
			ps.close();		
	} catch (SQLException e) {
		
		e.printStackTrace();
	}
	
}

public List<Book> getBooksNext(int a) {
	// fetch a mighty list of Books!! mwahahah! from the database... How Evil
	List<Book> booklist = new ArrayList<Book>();
	Statement st = null;
	ResultSet rs = null;
	Book book;
	try {
		//fetch records 
		//don't need a parameterized query here since no user-provided input
		//is being passed to the db server
		String query = "select * "
				+ " from book LIMIT "+a+",50";
		st = this.connection.createStatement();

		//execute the query and get the record data in the form of a ResultSet
		rs = st.executeQuery(query);

		//iterate over the result set using the next() method
		//note: result set is not initially at first row. we have to manually move it to row 1
		while(rs.next()) {
			book = new Book();
			book.setId(Integer.parseInt(rs.getString("id")));
			book.setTitle(rs.getString("title"));
			book.setSummary(rs.getString("summary"));
			book.setYearPublished(Integer.parseInt(rs.getString("year_published")));
			book.setIsbn(rs.getString("isbn"));
			book.setPublisherID(rs.getInt("publisher_id"));
			book.setTimestamp(rs.getTimestamp("last_modified"));
			booklist.add(book);
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
	
	
	
	return booklist;
}

public List<Book> getBooksNextSearch(String titleSearched, int a) {
	// fetch a mighty list of Books!! mwahahah! from the database... How Evil
	List<Book> booklist = new ArrayList<Book>();
	Statement st = null;
	ResultSet rs = null;
	Book book;
	try {
		//fetch records 
		//don't need a parameterized query here since no user-provided input
		//is being passed to the db server
		String query = "SELECT * FROM `book` WHERE title LIKE \""+titleSearched+"\" LIMIT "+a+",50";
		st = this.connection.createStatement();

		//execute the query and get the record data in the form of a ResultSet
		rs = st.executeQuery(query);

		//iterate over the result set using the next() method
		//note: result set is not initially at first row. we have to manually move it to row 1
		while(rs.next()) {
			book = new Book();
			book.setId(Integer.parseInt(rs.getString("id")));
			book.setTitle(rs.getString("title"));
			book.setSummary(rs.getString("summary"));
			book.setYearPublished(Integer.parseInt(rs.getString("year_published")));
			book.setIsbn(rs.getString("isbn"));
			book.setPublisherID(rs.getInt("publisher_id"));
			book.setTimestamp(rs.getTimestamp("last_modified"));
			booklist.add(book);
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
	
	
	
	return booklist;
}

public List<Book> getLastBooks(int a, int regMod) {
    // fetch a mighty list of Books!! mwahahah! from the database... How Evil
    List<Book> booklist = new ArrayList<Book>();
    Statement st = null;
    ResultSet rs = null;
    
    Book book;
    try {
        //fetch records 
        //don't need a parameterized query here since no user-provided input
        //is being passed to the db server
    	
        //String query = "SELECT * FROM ( SELECT * FROM book ORDER BY id DESC LIMIT 50 ) sub ORDER BY id ASC;";
    	String query = "select * "
				+ " from book LIMIT "+a+","+regMod+"";
        st = this.connection.createStatement();

        //execute the query and get the record data in the form of a ResultSet
        rs = st.executeQuery(query);
        
        
        
        //iterate over the result set using the next() method
        //note: result set is not initially at first row. we have to manually move it to row 1
        while(rs.next()) {
            book = new Book();
            book.setId(Integer.parseInt(rs.getString("id")));
            book.setTitle(rs.getString("title"));
            book.setSummary(rs.getString("summary"));
            book.setYearPublished(Integer.parseInt(rs.getString("year_published")));
            book.setIsbn(rs.getString("isbn"));
            book.setPublisherID(rs.getInt("publisher_id"));
            book.setTimestamp(rs.getTimestamp("last_modified"));
            booklist.add(book);
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
	
	
	
	return booklist;
}

public List<Book> getLastBooksSearch(String titleSearched, int a, int searchMod) {
    // fetch a mighty list of Books!! mwahahah! from the database... How Evil
    List<Book> booklist = new ArrayList<Book>();
    Statement st = null;
    ResultSet rs = null;
    
    Book book;
    try {
        //fetch records 
        //don't need a parameterized query here since no user-provided input
        //is being passed to the db server
    	
        //String query = "SELECT * FROM ( SELECT * FROM book ORDER BY id DESC LIMIT 50 ) sub ORDER BY id ASC;";
    	String query = "SELECT * FROM `book` WHERE title LIKE \""+titleSearched+"\" LIMIT "+a+","+searchMod+"";
        st = this.connection.createStatement();

        //execute the query and get the record data in the form of a ResultSet
        rs = st.executeQuery(query);
        
        
        
        //iterate over the result set using the next() method
        //note: result set is not initially at first row. we have to manually move it to row 1
        while(rs.next()) {
            book = new Book();
            book.setId(Integer.parseInt(rs.getString("id")));
            book.setTitle(rs.getString("title"));
            book.setSummary(rs.getString("summary"));
            book.setYearPublished(Integer.parseInt(rs.getString("year_published")));
            book.setIsbn(rs.getString("isbn"));
            book.setPublisherID(rs.getInt("publisher_id"));
            book.setTimestamp(rs.getTimestamp("last_modified"));
            booklist.add(book);
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
	
	
	
	return booklist;
}

public List<Book> getBooksSearch(String titleSearched) {
	// fetch a mighty list of Books!! mwahahah! from the database... How Evil
	List<Book> booklist = new ArrayList<Book>();
	Statement st = null;
	ResultSet rs = null;
	ResultSet rp1 = null;
	Book book;
	try {
		//fetch records 
		//don't need a parameterized query here since no user-provided input
		//is being passed to the db server
		String query = "SELECT * FROM `book` WHERE title LIKE \""+titleSearched+"\" LIMIT 0,50";
		st = this.connection.createStatement();

		//execute the query and get the record data in the form of a ResultSet
		rs = st.executeQuery(query);
		
		Statement stat = this.connection.createStatement();
        rp1 = stat.executeQuery("SELECT COUNT(*) FROM `book` WHERE title LIKE \""+titleSearched+"\" ");
        while(rp1.next()){
     	   this.setSearchCount(rp1.getInt(1));
        }
		//iterate over the result set using the next() method
		//note: result set is not initially at first row. we have to manually move it to row 1
		while(rs.next()) {
			book = new Book();
			book.setId(Integer.parseInt(rs.getString("id")));
			book.setTitle(rs.getString("title"));
			book.setSummary(rs.getString("summary"));
			book.setYearPublished(Integer.parseInt(rs.getString("year_published")));
			book.setIsbn(rs.getString("isbn"));
			book.setPublisherID(rs.getInt("publisher_id"));
			book.setTimestamp(rs.getTimestamp("last_modified"));
			booklist.add(book);
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
	
	return booklist;
	}

public int getSearchCount() {
	return searchCount;
}
public void setSearchCount(int searchCount) {
	this.searchCount = searchCount;
}
public int getCount() {
	return count;
}
public void setCount(int count) {
	this.count = count;
}

private int parseint(String string) {
	// TODO Auto-generated method stub
	return 0;
}

public Connection getConnection() {
	return connection;
}

public void setConnection(Connection connection) {
	this.connection = connection;
  }
}