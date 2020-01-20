package model;

import java.sql.Timestamp;

import java.util.List;


import view_controller.BookTableGateway;


public class Book 
{
	private int id;
	private String title;
	private String summary;
	private int yearPublished;
	private String isbn;
	private int publisherID;
	private Timestamp timestamp;

	
	public Book(int id, String title, String summary, int yearPublished, String isbn, int publisherID) 
	{
		this.id = id;
		this.title = title;
		this.summary = summary;
		this.yearPublished = yearPublished;
		this.isbn = isbn;
		this.publisherID = publisherID;
	}

	public Book(){
		title="";
		summary="";
		isbn="";
		publisherID=0;
		timestamp=null;
	}
	@Override
	public String toString() {
		return title;
	}

	public List<AuthorBook> getAuthors(){
		return BookTableGateway.getInstance().getAuthorsForBook(this);
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**	
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * @return the yearPublished
	 */
	public int getYearPublished() {
		return yearPublished;
	}

	/**
	 * @param yearPublished the yearPublished to set
	 */
	public void setYearPublished(int yearPublished) {
		this.yearPublished = yearPublished;
	}

	/**
	 * @return the isbn
	 */
	public String getIsbn() {
		return isbn;
	}

	/**
	 * @param isbn the isbn to set
	 */
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	/**
	 * @return the timestamp
	 */
	public Timestamp getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the publisherID
	 */
	public int getPublisherID() {
		return publisherID;
	}

	/**
	 * @param publisherID the publisherID to set
	 */
	public void setPublisherID(int publisherID) {
		this.publisherID = publisherID;
	}
	



}

/*

public void save() {
    if(!isValidId())
        throw new BookException("Taco Id must be > 0");
    if(!isValidTile()) {
        // show popup saying taco type is invalid 
        // displayAwesomeGUIPopup("Taco Type must be between 1 and 20 characters");

        throw new BookException("Taco Type must be between 1 and 20 characters");
    }

    if(!isValidSummary()) {
        throw new BookException("Summary must be less than 65536 characters.");
    }

    if(!isValidYear()) {
        throw new BookException("Summary must be less than 65536 characters.");
    }

    if(!isValidIsbn()) {
        throw new BookException("ISBN cannot be more than 13 characters.");
    }

    // save to gateway
    try {
        BookTableGateway.getInstance().updateBook(this);
    } catch (GatewayException e) {
        throw new BookException("BookTableGateway Update book: " + e.toString());
    }
}

public boolean isValidId() {
    if(id < 0)
        return false;
    return true;
}

public boolean isValidTile() {
    return(title.length() > 1 && title.length() < 50);

}

public boolean isValidSummary() {
    if (summary.length() > 65536) {
        return(false);
    }
    return true;
}

public boolean isValidYear() {
    if (yearPublished < 1455 && yearPublished > 2019) {
        return(false);
    }
    return true;
}

public boolean isValidIsbn() {
    if (isbn.length() > 13) {
        return(false);
    }
    return true;
}*/