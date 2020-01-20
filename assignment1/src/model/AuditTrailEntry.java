package model;

import java.sql.Timestamp;

public class AuditTrailEntry {

	private Timestamp Date;
	private int id;
	private String message;
	
	public AuditTrailEntry(){
		
	}
	
	AuditTrailEntry(int id, String message, Timestamp Date){
		this.id = id;
		this.message = message;
		this.Date = Date;
		
	}
	
	public String toString()
	{
		return this.Date + "\t\t" + this.message;
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getDate() {
		return Date;
	}

	public void setDate(Timestamp date) {
		Date = date;
	}


	

}
