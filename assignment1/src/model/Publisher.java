package model;

import java.sql.Timestamp;


public class Publisher {

	
	private int id;
	private String name;
	private Timestamp added;
	
	public Publisher (int id, String name, Timestamp added){
		this.id = id;
		this.name = name;
		this.added = added;
	}
	
	public Publisher(){
		
	}
	
	public String toString(){
		return this.name;
	}
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getAdded() {
		return added;
	}
	public void setAdded(Timestamp added) {
		this.added = added;
	}
}
