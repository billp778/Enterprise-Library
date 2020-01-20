package author_detail;

import java.net.URL;
import java.time.LocalDate;

import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Author;
import view_controller.AuthorGateway;
import view_controller.MainViewController;
import view_controller.MyController;

public class authorDetailController implements MyController, Initializable  {

	public static Logger logger = LogManager.getLogger();
	
	
	
 
	
    @FXML
    private TextField lName;

    @FXML
    private TextField website;

    @FXML
    private TextField fName;

    @FXML
    private TextField gender;

    @FXML
    private TextField dob;

    @FXML
    private Button save;

    // 	tempAuthor.setDateOfBirth((LocalDate.parse(dob.getText())));
    //author.setDateOfBirth(tempAuthor.getDateOfBirth());
    
    @FXML
    void handleButtonAction(ActionEvent event) throws Exception {
    	Object source = event.getSource();
    	if(source == save){
    		 if (!fName.getText().isEmpty() && 
    				 !lName.getText().isEmpty() && 
    				 !dob.getText().isEmpty() && 
    				 !gender.getText().isEmpty()){
    	            
    			 	Author tempAuthor = new Author();
    	             tempAuthor.setFirstName(fName.getText());
    	             tempAuthor.setLastName(lName.getText());
    	             tempAuthor.setDateOfBirth((LocalDate.parse(dob.getText())));
    	             tempAuthor.setGender(gender.getText());
    	             tempAuthor.setWebSite(website.getText());
    	            tempAuthor.setId(author.getId());
    	            
   
    	            
    	             AuthorGateway.getInstance().saveAuthor(tempAuthor);
    	             
    	             author.setFirstName(tempAuthor.getFirstName());
    	            author.setLastName(tempAuthor.getLastName());
    	            author.setDateOfBirth(tempAuthor.getDateOfBirth());
    	            author.setGender(tempAuthor.getGender());
    	            author.setWebSite(tempAuthor.getWebSite());
    	            
    	             logger.info("saved");
    	            
    }
    else{
    	
    	Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error Dialog");
		alert.setHeaderText("All Fields Required Except Website");
		alert.showAndWait();
    }

    	}
    }
       
    private Author author;
    
    public authorDetailController(Author selected) {
    	this.author = selected;
    }
    
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		//dob.setText(author.getDateOfBirth().toString().replaceAll(".*", ""));
		
		fName.setText(author.getFirstName());
		lName.setText(author.getLastName());
		dob.setText(author.getDateOfBirth().toString());
		gender.setText(author.getGender());
		website.setText(author.getWebSite());
		
		 if(MainViewController.getInstance().SessionID == 3){
			 save.setDisable(true);
			 fName.setDisable(true);
			 lName.setDisable(true);
			 dob.setDisable(true);
			 gender.setDisable(true);
			 website.setDisable(true);
			
			 }
		
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
}
