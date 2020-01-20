package model;

public class AuthorBook {
	private Author author;
	private Book book;
	private int royalty;
	private Boolean newRecord;
	
	public AuthorBook() {
		// TODO Auto-generated constructor stub
	}
	
	public AuthorBook(Author author, Book book, int royalty, Boolean newRecord) {
		super();
		this.setAuthor(author);
		this.setBook(book);
		this.royalty = royalty;
		this.newRecord = newRecord;
	}

	
	
	public String toString() {
		double decimal = ((float)royalty)/100000.0;
		return this.author.getFirstName() + " " + this.author.getLastName() + " " + decimal+"%";
	}
	
	public int getRoyalty() {
		return royalty;
	}
	public void setRoyalty(int royalty) {
		this.royalty = royalty;
	}
	public Boolean getNewRecord() {
		return newRecord;
	}
	public void setNewRecord(Boolean newRecord) {
		this.newRecord = newRecord;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
}
