package view_controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import audit.auditTrailController;
import author_detail.authorDetailController;
import author_list.authorListController;
import auto.autoController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import model.Author;
import model.Book;
import model.User;
import view_controller.MyController;
import book_detail.bookDetailController;
import book_list.bookListController;
import view_controller.ViewType;
import view_controller.MainViewController;

public class MainViewController implements Initializable{
	
	private static MainViewController instance = null;
	
	public static Logger logger = LogManager.getLogger();

	public int SessionID;
	
	User user;
	
private BorderPane borderPane;
	
	@FXML
    private MenuItem menuBookList, menuAuthorList, AutoGen;

    @FXML
    private MenuItem menuQuit, login, logout;
    
    @FXML
    private MenuItem menuAddBook, menuAddAuthor;
    
    private bookDetailController bookChanges;
    
    private Main main;
	public static MainViewController getInstance() {
		if(instance == null)
			instance = new MainViewController();
			
	   
		return instance;
		
	}


    public void switchView(ViewType viewType, Book selected) {
    	String viewString = "";
    	MyController controller = null;
    	
    	switch(viewType) {
    		case VIEW1:
    			controller =  new bookListController(BookTableGateway.getInstance().getBooks());
    			viewString = "../book_list/BookListView.fxml";
    			bookChanges = null;
    			break;
    			
    			
    		case VIEW2:
    			controller =  new bookDetailController(selected);
    			viewString = "../book_detail/BookDetailView.fxml";
    			bookChanges = (bookDetailController) controller;
    			break;
    		
    		case VIEW3:
    			controller = new auditTrailController(selected);
    			viewString = "../audit/auditTrailView.fxml";
    			break;
    		
    		case VIEW4:
    			controller = new autoController(selected);
    			viewString = "../auto/AutoGen.fxml";
    			break;
    			
    	
    		
		default:
			break;
    			
    	}
		try {
    		URL url = this.getClass().getResource(viewString);
    		FXMLLoader loader = new FXMLLoader(url);
    		loader.setController(controller);
			Parent viewNode = loader.load();
			
			// plug viewNode into MainView's borderpane center
			borderPane.setCenter(viewNode);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @FXML
  void clickMenuItem(ActionEvent event) throws Exception {
    	
    	if(event.getSource() == menuQuit){
    		if ( bookChanges != null && bookChanges.changesMade()){
    			Alert alert = new Alert(AlertType.CONFIRMATION);
				
				alert.getButtonTypes().clear();
				ButtonType buttonTypeOne = new ButtonType("Yes");
				ButtonType buttonTypeTwo = new ButtonType("No");
				ButtonType buttonTypeThree = new ButtonType("Cancel");
				alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree);

				alert.setTitle("Save Changes?");
				alert.setHeaderText("The current view has unsaved changes.");
				alert.setContentText("If you wish to save hit yes and press save, othwise press no to continue");

				Optional<ButtonType> result = alert.showAndWait();
				if(result.get().getText().equalsIgnoreCase("Yes")) {
					try {
						bookChanges.save();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					logger.error("*** View saved");
					Platform.exit();
					}else if(result.get().getText().equalsIgnoreCase("No")) {
						Platform.exit();
					}else{
						logger.info("Cancelled");
					}
						
    		}
    		else{
    			
    			Platform.exit();
    		}
    	} // End of First Click
    	 
    	
    	if (event.getSource() == menuBookList){
    		if ( bookChanges != null && bookChanges.changesMade()){
    			Alert alert = new Alert(AlertType.CONFIRMATION);
				
				alert.getButtonTypes().clear();
				ButtonType buttonTypeOne = new ButtonType("Yes");
				ButtonType buttonTypeTwo = new ButtonType("No");
				ButtonType buttonTypeThree = new ButtonType("Cancel");
				alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree);

				alert.setTitle("Save Changes?");
				alert.setHeaderText("The current view has unsaved changes.");
				alert.setContentText("If you wish to save hit yes and press save, othwise press no to continue");

				Optional<ButtonType> result = alert.showAndWait();
				if(result.get().getText().equalsIgnoreCase("Yes")) {
					try {
						bookChanges.save();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					logger.error("*** View saved");
					switchView(ViewType.VIEW1, new Book());
					}else if(result.get().getText().equalsIgnoreCase("No")) {
						switchView(ViewType.VIEW1, new Book());
					}else{
						logger.info("Cancelled");
					}
						
					
    		}
    		else{
    			switchView(ViewType.VIEW1, new Book());
    		}
    	} // End of Second Click
    	
    	
    	 if (event.getSource() == menuAddBook) {
    		 if ( bookChanges != null && bookChanges.changesMade()){
    			 Alert alert = new Alert(AlertType.CONFIRMATION);
 				
 				alert.getButtonTypes().clear();
 				ButtonType buttonTypeOne = new ButtonType("Yes");
 				ButtonType buttonTypeTwo = new ButtonType("No");
 				ButtonType buttonTypeThree = new ButtonType("Cancel");
 				alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree);

 				alert.setTitle("Save Changes?");
 				alert.setHeaderText("The current view has unsaved changes.");
 				alert.setContentText("If you wish to save hit yes and press save, othwise press no to continue");

 				Optional<ButtonType> result = alert.showAndWait();
 				if(result.get().getText().equalsIgnoreCase("Yes")) {
 					try {
 						bookChanges.save();
 					} catch (Exception e) {
 						// TODO Auto-generated catch block
 						e.printStackTrace();
 					}
 					logger.error("*** View saved");
 					 switchView(ViewType.VIEW2, new Book());
 					}else if(result.get().getText().equalsIgnoreCase("No")) {
 						 switchView(ViewType.VIEW2, new Book());
 					}else{
 						logger.info("Cancelled");
 					}
    		 }
    		 else{
    			 switchView(ViewType.VIEW2, new Book());
    		 }
 			} //End of 3rd Click
    
    	 if (event.getSource() == menuAuthorList){
     		if ( bookChanges != null && bookChanges.changesMade()){
     			Alert alert = new Alert(AlertType.CONFIRMATION);
 				
 				alert.getButtonTypes().clear();
 				ButtonType buttonTypeOne = new ButtonType("Yes");
 				ButtonType buttonTypeTwo = new ButtonType("No");
 				ButtonType buttonTypeThree = new ButtonType("Cancel");
 				alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree);

 				alert.setTitle("Save Changes?");
 				alert.setHeaderText("The current view has unsaved changes.");
 				alert.setContentText("If you wish to save hit yes and press save, othwise press no to continue");

 				Optional<ButtonType> result = alert.showAndWait();
 				if(result.get().getText().equalsIgnoreCase("Yes")) {
 					try {
 						bookChanges.save();
 					} catch (Exception e) {
 						// TODO Auto-generated catch block
 						e.printStackTrace();
 					}
 					logger.error("*** View saved");
 					switchView2(ViewType.VIEW1, new Author());
 					}else if(result.get().getText().equalsIgnoreCase("No")) {
 						switchView2(ViewType.VIEW1, new Author());
 					}else{
 						logger.info("Cancelled");
 					}
 						
 					
     		}
     		else{
     			switchView2(ViewType.VIEW1, new Author());
     		}
     	} // End of 4th click
    	 
    	 if (event.getSource() == menuAddAuthor){
      		if ( bookChanges != null && bookChanges.changesMade()){
      			Alert alert = new Alert(AlertType.CONFIRMATION);
  				
  				alert.getButtonTypes().clear();
  				ButtonType buttonTypeOne = new ButtonType("Yes");
  				ButtonType buttonTypeTwo = new ButtonType("No");
  				ButtonType buttonTypeThree = new ButtonType("Cancel");
  				alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree);

  				alert.setTitle("Save Changes?");
  				alert.setHeaderText("The current view has unsaved changes.");
  				alert.setContentText("If you wish to save hit yes and press save, othwise press no to continue");

  				Optional<ButtonType> result = alert.showAndWait();
  				if(result.get().getText().equalsIgnoreCase("Yes")) {
  					try {
  						bookChanges.save();
  					} catch (Exception e) {
  						// TODO Auto-generated catch block
  						e.printStackTrace();
  					}
  					logger.error("*** View saved");
  					switchView2(ViewType.VIEW2, new Author());
  					}else if(result.get().getText().equalsIgnoreCase("No")) {
  						switchView2(ViewType.VIEW2, new Author());
  					}else{
  						logger.info("Cancelled");
  					}
  						
  					
      		}
      		else{
      			switchView2(ViewType.VIEW2, new Author());
      		}
      	} // End of 5th click
    	 
    	 if(event.getSource() == AutoGen){
    		 switchView(ViewType.VIEW4, new Book());
    	 } // end of 6th click
    	 
    	 
    	 if(event.getSource() == login){
    		 Dialog<Pair<String, String>> dialog = new Dialog<>();
    			dialog.setTitle("Login Dialog");
    			dialog.setHeaderText("Enter user name and password");

    			// Set the button types.
    			ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
    			dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

    			// Create the username and password labels and fields.
    			GridPane grid = new GridPane();
    			grid.setHgap(10);
    			grid.setVgap(10);
    			grid.setPadding(new Insets(20, 150, 10, 10));

    			TextField username = new TextField();
    			username.setPromptText("Username");
    			PasswordField password = new PasswordField();
    			password.setPromptText("Password");

    			grid.add(new Label("Username:"), 0, 0);
    			grid.add(username, 1, 0);
    			grid.add(new Label("Password:"), 0, 1);
    			grid.add(password, 1, 1);

    			// Enable/Disable login button depending on whether a username was entered.
    			Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
    			loginButton.setDisable(true);

    			// Do some validation (using the Java 8 lambda syntax).
    			username.textProperty().addListener((observable, oldValue, newValue) -> {
    			    loginButton.setDisable(newValue.trim().isEmpty());
    			});

    			dialog.getDialogPane().setContent(grid);

    			// Request focus on the username field by default.
    			Platform.runLater(() -> username.requestFocus());

    			dialog.setResultConverter(dialogButton -> {
    			    if (dialogButton == loginButtonType) {
    			        return new Pair<>(username.getText(), password.getText());
    			    }
    			    return null;
    			});

    			Optional<Pair<String, String>> result = dialog.showAndWait();
    			
    			if(result.isPresent()){
  
    			try {
    				
    				user = LoginGateway.getInstance().getUser(result.get().getKey(), result.get().getValue());
    				
    				
    				if(user.getName() != null && user.getRole().equals("Administrator")){
    					 Alert alert = new Alert(AlertType.CONFIRMATION);
    						
    						alert.getButtonTypes().clear();
    						ButtonType buttonTypeOne = new ButtonType("Start");
    						alert.getButtonTypes().setAll(buttonTypeOne);

    						alert.setTitle("Hello");
    						alert.setHeaderText("Hello " + user.getName() + ".");
    						
    						alert.showAndWait();
    					
    					SessionID = 1;
    					
    					login.setDisable(true);
    					logout.setDisable(false);
    					menuAddBook.setDisable(false); 
    					menuAddAuthor.setDisable(false);
    					menuBookList.setDisable(false); 
    					menuAuthorList.setDisable(false); 
    					AutoGen.setDisable(false);
    				}
    				
    				if(user.getName() != null && user.getRole().equals("Intern")){
    					 Alert alert = new Alert(AlertType.CONFIRMATION);
 						
 						alert.getButtonTypes().clear();
 						ButtonType buttonTypeOne = new ButtonType("Start");
 						alert.getButtonTypes().setAll(buttonTypeOne);

 						alert.setTitle("Hello");
 						alert.setHeaderText("Hello " + user.getName() + ".");
 						
 						alert.showAndWait();
    					
    					SessionID = 3;
    					logout.setDisable(false);
    					login.setDisable(true);
    				
    					menuBookList.setDisable(false); 
    					menuAuthorList.setDisable(false); 
    					
    				}
    				
    				if(user.getName() != null && user.getRole().equals("Data Entry")){
    					 Alert alert = new Alert(AlertType.CONFIRMATION);
 						
 						alert.getButtonTypes().clear();
 						ButtonType buttonTypeOne = new ButtonType("Start");
 						alert.getButtonTypes().setAll(buttonTypeOne);

 						alert.setTitle("Hello");
 						alert.setHeaderText("Hello " + user.getName() + ".");
 						
 						alert.showAndWait();
    					
    					SessionID = 2;
    					login.setDisable(true);
    					logout.setDisable(false);
    					menuAddBook.setDisable(false); 
    					menuAddAuthor.setDisable(false);
    					menuBookList.setDisable(false); 
    					menuAuthorList.setDisable(false); 
    					AutoGen.setDisable(false);
    				}
    				
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					Alert alert = new Alert(AlertType.CONFIRMATION);
					
					alert.getButtonTypes().clear();
					ButtonType buttonTypeOne = new ButtonType("Ok");
					alert.getButtonTypes().setAll(buttonTypeOne);

					alert.setTitle("Error");
					alert.setHeaderText("Login Failed");
					alert.setContentText("The information you have entered does not match anything in our database, please try again.");
					
					alert.showAndWait();
				} 
    				
    		}
    	 }
    	 
    	 if(event.getSource() == logout){
    		 Alert alert = new Alert(AlertType.CONFIRMATION);
				
				alert.getButtonTypes().clear();
				ButtonType buttonTypeOne = new ButtonType("Yes");
				ButtonType buttonTypeTwo = new ButtonType("No");
				alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

				alert.setTitle("Logout?");
				alert.setHeaderText("Logout");
				alert.setContentText("Do you really want to log out " + user.getName() + "?");
				
				Optional<ButtonType> result = alert.showAndWait();
				
				if(result.get().getText().equalsIgnoreCase("Yes")) {
					borderPane.setCenter(null);
					SessionID = 0;
					user = null;
					logout.setDisable(true);
					login.setDisable(false);
					menuAddBook.setDisable(true); 
					menuAddAuthor.setDisable(true);
					menuBookList.setDisable(true); 
					menuAuthorList.setDisable(true); 
					AutoGen.setDisable(true);
				}
    	 }
    }
    
    
   
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		bookChanges = null;
		logout.setDisable(true);
	
		menuAddBook.setDisable(true); 
		menuAddAuthor.setDisable(true);
		menuBookList.setDisable(true); 
		menuAuthorList.setDisable(true); 
		AutoGen.setDisable(true);
		
	}

	public BorderPane getBorderPane() {
		return borderPane;
	}

	public void setBorderPane(BorderPane borderPane) {
		this.borderPane = borderPane;
	}


	public void switchView2(ViewType viewType, Author selected) {
		String viewString = "";
    	MyController controller = null;
    	switch(viewType) {
		
    	case VIEW1:
			controller =  new authorListController(AuthorGateway.getInstance().getAuthors());
			viewString = "../author_list/AuthorListView.fxml";
			bookChanges = null;
			break;
			
			
		case VIEW2:
			controller =  new authorDetailController(selected);
			viewString = "../author_detail/AuthorDetailView.fxml";
			//bookChanges = (bookDetailController) controller;
			break;
		
	
	default:
		break;
			
	}
	try {
		URL url = this.getClass().getResource(viewString);
		FXMLLoader loader = new FXMLLoader(url);
		loader.setController(controller);
		Parent viewNode = loader.load();
		
		// plug viewNode into MainView's borderpane center
		borderPane.setCenter(viewNode);
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
    	
}

