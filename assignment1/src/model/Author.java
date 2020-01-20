package model;

import java.time.LocalDate;

public class Author {

	
	private int id;
	private String firstName;
	private String lastName;
	private LocalDate dateOfBirth;
	private String gender;
	private String webSite;
	
	
	
	public Author(int id, String firstName, String lastName, LocalDate dateOfBirth, String gender, String webSite) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.webSite = webSite;
		
		
	}

	public Author() {
		id = 0;
		firstName = "";
		lastName = "";
		dateOfBirth = LocalDate.now();
		gender = "";
		webSite = "";
	}
	
	public String toString(){
		return this.firstName + " " + this.lastName;
	}
	
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}


	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public String getWebSite() {
		return webSite;
	}


	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}



}
