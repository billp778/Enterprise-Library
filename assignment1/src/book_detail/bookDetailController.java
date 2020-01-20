package book_detail;


import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import view_controller.AuthorGateway;
import view_controller.BookTableGateway;
import view_controller.MainViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import model.Book;
import model.Author;
import model.AuthorBook;
import model.Publisher;
import view_controller.MyController;
import view_controller.PublisherGateway;
import view_controller.ViewType;

public class bookDetailController implements MyController, Initializable {
	public static Logger logger = LogManager.getLogger();
	
    @FXML
    private TextField tfTitle, tfYear, tfISBN;

    @FXML
    private Button save, addAuth, deleteAuth, AuditRecord;

    @FXML
    private ListView<AuthorBook> AuthorList;
    ObservableList<AuthorBook> authorObsList;
    
    // List view in the modal dialog box ----------------------
    @FXML
    private ListView<Author> AuthorAddList;
   private List<Author> selection;
    @FXML
    private TextField AddRoyalty;
    @FXML
    private Button SaveAuthorModal;  // Does not have a on action handle yet

    //----------------------------------------------------------
    
     @FXML
    public TextArea taSummary;
    
    @FXML
    private ComboBox<Publisher> PublisherCombo;

    private Book book;
    
	private Timestamp originalTimestamp;
	
	public String myString;

	
    public bookDetailController(Book selected) {
    	this.book = selected;
    }
    

    @Override
	public void initialize() {		
    	 List<AuthorBook> authors = book.getAuthors();
		  authorObsList = FXCollections.observableArrayList();
		AuthorList.setItems(authorObsList);
		 authorObsList.addAll(authors);
		 
		
	}
    
    


    
    
    @FXML public void handleButtonAction2(ActionEvent action2) throws Exception {
    	Object source = action2.getSource();
    	if(source == AuditRecord){
    		//int index = listView.getSelectionModel().getSelectedIndex();
	        
	    	//Book selected = booksObsList.get(index);
	    	
	        logger.error("Audit Info");
	        
	        MainViewController.getInstance().switchView(ViewType.VIEW3, this.book);
    	}
    }
    
  
    
    @FXML
    void selectRoyalty(MouseEvent event) {
    	
    	if (event.getClickCount() == 2){
 
    		
    		int index = AuthorList.getSelectionModel().getSelectedIndex();
    		int selected = authorObsList.get(index).getRoyalty()/100000;
    		double dbl = selected;
    		int authID = authorObsList.get(index).getAuthor().getId();
    		int bookID = book.getId();
    		String str1 = Double.toString(selected);
    			
			TextInputDialog dialog = new TextInputDialog(str1);
			dialog.setTitle("Royalty Adjustment");
			dialog.setHeaderText("Change the Royalty Amount:");
			dialog.setContentText("Royalty:");	
			
				Optional<String> result = dialog.showAndWait();
				String StringRoyal = result.get();
				double DoubleRoyal = Double.parseDouble(StringRoyal); 
				System.out.println(DoubleRoyal);
			if(result.isPresent()){
				
				
				try {
					BookTableGateway.getInstance().updateRoyal(bookID, authID, DoubleRoyal);
					BookTableGateway.getInstance().writeAudit(book.getId(), "Author Royalty Changed: " + selected+"%" + " to " + DoubleRoyal+"%");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			initialize();
				}
					
    	}
    	
    }

    
    
    @FXML public void handleButtonAction3(ActionEvent action3) throws Exception {
    	Object source = action3.getSource();
    	if(source == addAuth){
    		
    	     selection = AuthorGateway.getInstance().getAuthors();
    	     List<Author> choices = new ArrayList<Author>(selection);
    	
    	     ChoiceDialog<Author> dialog = new ChoiceDialog<Author>(null, choices);
    		dialog.setTitle("Choice Dialog");
    		dialog.setHeaderText("Look, a Choice Dialog");
    		dialog.setContentText("Choose your letter:");
    		
    		Optional<Author> result = dialog.showAndWait();
    		if(result.isPresent()){
    			
				
				try {
					BookTableGateway.getInstance().insertAuthor(book.getId(), result.get().getId());
					BookTableGateway.getInstance().writeAudit(book.getId(), "Author Added: " + result.get());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			initialize();
				}
    		
    	 }
    	
    	
    	if(source == deleteAuth){
    		
			AuthorBook deleteThis = AuthorList.getSelectionModel().getSelectedItem();
	    	
	    	try {
	    		// attempt to delete the book
	    		BookTableGateway.getInstance().writeAudit(book.getId(), "Author deleted: " + deleteThis);
				BookTableGateway.getInstance().deleteAuthorBook(deleteThis);
				
				
				// show modal window confirming deletion
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText("Author Deleted");
				alert.setContentText("Author has been deleted");
				alert.showAndWait();
			
				
			} catch (Exception e) {
				logger.error("Error deleting Author");
				e.printStackTrace();
			}
    		
    		}
    	}
    
    



	public void save() throws Exception{
    	int year = Integer.parseInt(tfYear.getText());
    	if(year >= 1455 && year <=2019){
    		Book tempBook = new Book();
		 	tempBook.setTitle(tfTitle.getText());
		 	tempBook.setSummary(taSummary.getText());
		 	tempBook.setYearPublished(Integer.parseInt(tfYear.getText()));
		 	tempBook.setIsbn(tfISBN.getText());
		 	tempBook.setId(book.getId());
		 	tempBook.setPublisherID(PublisherCombo.getSelectionModel().getSelectedItem().getId());
		 	
		 	if(book.getId() != 0){
		 	if(!tempBook.getTitle().equals(book.getTitle())){
		 		BookTableGateway.getInstance().writeAudit(book.getId(),"Title changed from: " + book.getTitle() + " to " + tempBook.getTitle());
		 	}
		 	if(!tempBook.getSummary().equals(book.getSummary())){
		 		BookTableGateway.getInstance().writeAudit(book.getId(), "Summary changed from: " + book.getSummary() + " to " + tempBook.getSummary());
		 	}
		 	if(tempBook.getYearPublished() != book.getYearPublished()){
		 		BookTableGateway.getInstance().writeAudit(book.getId(), "Year Published changed from: " + book.getYearPublished() + " to " + Integer.toString(tempBook.getYearPublished()));
		 	}
		 	if(!tempBook.getIsbn().equals(book.getIsbn())){
		 		BookTableGateway.getInstance().writeAudit(book.getId(), "Isbn changed from: " + book.getIsbn() + " to " + tempBook.getIsbn());
		 	}
		 	if(tempBook.getPublisherID() != book.getPublisherID()){
		 		BookTableGateway.getInstance().writeAudit(book.getId(), "PublisherID changed from: " + myString + " to " + PublisherCombo.getValue());
		 	}
		 }
	    	if (originalTimestamp!=null) {
		    	// if originalTimestamp does not match up with currentTimestamp, popup error window and do not save. else, proceed.
		    	if(!originalTimestamp.equals(BookTableGateway.getInstance().checkCurrentTimestamp(book))) {
		    		logger.error("Timestamps do not match, saving has been aborted");
		    		
		    		Alert alert = new Alert(AlertType.ERROR);
		    		alert.setTitle("Error Dialog");
		    		alert.setHeaderText("Data has been modified by another user");
		    		alert.setContentText("Press OK to refresh book information");
		    		alert.showAndWait();
		    	
		    	}
		    	else{
		    		BookTableGateway.getInstance().saveBook(tempBook);
		    		
		    		// rewrite temporary book values back into the book being used by the model
		    		book.setId(tempBook.getId());
		    		book.setTitle(tempBook.getTitle());
		    		book.setSummary(tempBook.getSummary());
		    		book.setYearPublished(tempBook.getYearPublished());
		    		book.setIsbn(tempBook.getIsbn());
		    		book.setPublisherID(tempBook.getPublisherID());
				 	logger.info("saved");
				 	
				 	
		    	}
		    }
	    	else{
	    		BookTableGateway.getInstance().saveBook(tempBook);
	    		
	    		// rewrite temporary book values back into the book being used by the model
	    		book.setId(tempBook.getId());
	    		book.setTitle(tempBook.getTitle());
	    		book.setSummary(tempBook.getSummary());
	    		book.setYearPublished(tempBook.getYearPublished());
	    		book.setIsbn(tempBook.getIsbn());
	    		book.setPublisherID(tempBook.getPublisherID());
	    		
			 	logger.info("saved");
	    	}
		}
		else{
			System.out.println("Enter Valid Date...1455-current");
		}
	
   }
    	
    
    
    @FXML public void handleButtonAction(ActionEvent action) throws Exception {
    	
		Object source = action.getSource();
		int year = Integer.parseInt(tfYear.getText());
		if(source == save && year >= 1455 && year <=2019) {
			Book tempBook = new Book();
		 	tempBook.setTitle(tfTitle.getText());
		 	tempBook.setSummary(taSummary.getText());
		 	tempBook.setYearPublished(Integer.parseInt(tfYear.getText()));
		 	tempBook.setIsbn(tfISBN.getText());
		 	tempBook.setId(book.getId());
		 	tempBook.setPublisherID(PublisherCombo.getSelectionModel().getSelectedItem().getId());
		 	

	    	if (originalTimestamp!=null) {
		    	// if originalTimestamp does not match up with currentTimestamp, popup error window and do not save. else, proceed.
		    	if(!originalTimestamp.equals(BookTableGateway.getInstance().checkCurrentTimestamp(book))) {
		    		logger.error("Timestamps do not match, saving has been aborted");
		    		
		    		Alert alert = new Alert(AlertType.ERROR);
		    		alert.setTitle("Error Dialog");
		    		alert.setHeaderText("Data has been modified by another user");
		    		alert.setContentText("Press OK to refresh book information");
		    		alert.showAndWait();
		    	
		    	}
		    	else{
		    		BookTableGateway.getInstance().saveBook(tempBook);
				 	if(book.getId() != 0){
					 	if(!tempBook.getTitle().equals(book.getTitle())){
					 		BookTableGateway.getInstance().writeAudit(book.getId(),"Title changed from: " + book.getTitle() + " to " + tempBook.getTitle());
					 	}
					 	if(!tempBook.getSummary().equals(book.getSummary())){
					 		BookTableGateway.getInstance().writeAudit(book.getId(), "Summary changed from: " + book.getSummary() + " to " + tempBook.getSummary());
					 	}
					 	if(tempBook.getYearPublished() != book.getYearPublished()){
					 		BookTableGateway.getInstance().writeAudit(book.getId(), "Year Published changed from: " + book.getYearPublished() + " to " + Integer.toString(tempBook.getYearPublished()));
					 	}
					 	if(!tempBook.getIsbn().equals(book.getIsbn())){
					 		BookTableGateway.getInstance().writeAudit(book.getId(), "Isbn changed from: " + book.getIsbn() + " to " + tempBook.getIsbn());
					 	}
					 	if(tempBook.getPublisherID() != book.getPublisherID()){
					 		BookTableGateway.getInstance().writeAudit(book.getId(), "PublisherID changed from: " + myString + " to " + PublisherCombo.getValue());
					 	}
					 }
		    		// rewrite temporary book values back into the book being used by the model
		    		book.setId(tempBook.getId());
		    		book.setTitle(tempBook.getTitle());
		    		book.setSummary(tempBook.getSummary());
		    		book.setYearPublished(tempBook.getYearPublished());
		    		book.setIsbn(tempBook.getIsbn());
		    		book.setPublisherID(tempBook.getPublisherID());
				 	logger.info("saved");
				 	
				 	
		    	}
		    }
	    	else{
	    		BookTableGateway.getInstance().saveBook(tempBook);
	    		
	    		// rewrite temporary book values back into the book being used by the model
	    		book.setId(tempBook.getId());
	    		book.setTitle(tempBook.getTitle());
	    		book.setSummary(tempBook.getSummary());
	    		book.setYearPublished(tempBook.getYearPublished());
	    		book.setIsbn(tempBook.getIsbn());
	    		book.setPublisherID(tempBook.getPublisherID());
	    		BookTableGateway.getInstance().writeAudit(tempBook.getId(),"Book added: " + tempBook.getTitle());
			 	logger.info("saved");
	    	}
		}
		else{
			System.out.println("Enter Valid Date...1455-current");
		}
	
   }
 
   
    public boolean changesMade(){
    
    	if (!(book.getTitle().equals(tfTitle.getText()))){
    		return true;
    	}else if (!(book.getSummary().equals(taSummary.getText()))) {
    		return true;
    	}else if (!(book.getPublisherID() == PublisherCombo.getValue().getId())){	
    		return true;
    	}else if (!(Integer.toString(book.getYearPublished()).equals(tfYear.getText()))){
    		return true;
    	}else if (!(book.getIsbn().equals(tfISBN.getText()))) {
    		return true;
    	}
    	else
    		return false;
    }
    

    	
    	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		tfTitle.setText(book.getTitle());
		taSummary.setText(book.getSummary());
		tfYear.setText("" + book.getYearPublished());
		tfISBN.setText(book.getIsbn());
		
		
		if(book.getTimestamp()!=null) {
			originalTimestamp=book.getTimestamp();
		}
		 
		 if(MainViewController.getInstance().SessionID == 3){
			 tfTitle.setDisable(true);
			 taSummary.setDisable(true);
			 tfYear.setDisable(true);
			 tfISBN.setDisable(true);
			 PublisherCombo.setDisable(true);
			 AuthorList.setDisable(true);
			 save.setDisable(true);
			 addAuth.setDisable(true);
			 deleteAuth.setDisable(true);
			 }
		
		 List<Publisher> publisher = PublisherGateway.getInstance().getPublishers();
		 ObservableList<Publisher> publisherObservableList = FXCollections.observableArrayList();

		 publisherObservableList.addAll(publisher);
		
		
    	 List<AuthorBook> authors = book.getAuthors();
		  authorObsList = FXCollections.observableArrayList();
		AuthorList.setItems(authorObsList);
		 authorObsList.addAll(authors);
		 
		
	
 		 
		 
		PublisherCombo.setItems(publisherObservableList);
		PublisherCombo.getSelectionModel().select(book.getPublisherID());
		
		 myString = PublisherCombo.getSelectionModel().getSelectedItem().toString();
		
		if(book.getYearPublished() == 0){
		AuditRecord.setVisible(false);
		}
		
		
		
	}

    
    }
    
    
    
    

  

