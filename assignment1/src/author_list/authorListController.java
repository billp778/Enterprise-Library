package author_list;


import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import model.Author;
import view_controller.AuthorGateway;
import view_controller.MainViewController;
import view_controller.MyController;
import view_controller.ViewType;

public class authorListController implements MyController {
	public static Logger logger = LogManager.getLogger();

    @FXML
    private Button Delete;
	
    @FXML
    private ListView<Author> listView;
    private List<Author> author;
    private ObservableList<Author> authObsList;
    
    private List<Author> refreshList;

    public authorListController(List<Author> authorList){
    	this.author = authorList;
    }
    
	
	@Override
	public void initialize() 
	{
		if(MainViewController.getInstance().SessionID == 2){
			Delete.setDisable(true);
		}
		
		if(MainViewController.getInstance().SessionID == 3){
			Delete.setDisable(true);
			
		}
		
		authObsList  = FXCollections.observableArrayList();
		authObsList.addAll(author);
	   
	listView.setItems(authObsList);
	   
	   
				
	}
    
	
	 @FXML public void handleButtonAction(ActionEvent action) throws Exception {
			Object source = action.getSource();
			if(source == Delete){
				Author deleteThis = listView.getSelectionModel().getSelectedItem();
		    	
		    	try {
		    		// attempt to delete the book
					AuthorGateway.getInstance().deleteAuthor(deleteThis);
					
					// show modal window confirming deletion
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information Dialog");
					alert.setHeaderText("Author Deleted");
					alert.setContentText("Author has been deleted");
					alert.showAndWait();
					
					//refresh menu list to show updated books
					refreshList = AuthorGateway.getInstance().getAuthors();
					authorListView.setItems(refreshList);
					
				} catch (Exception e) {
					logger.error("Error deleting Author");
					e.printStackTrace();
				}
		    }
		}
	 
	 
	@FXML    void selectAuthor(MouseEvent event) { 

	    if(event.getClickCount() == 2) { 
	    	
	    	
	    	
	    	int index = listView.getSelectionModel().getSelectedIndex();
	        
	    	Author selected = authObsList.get(index);
	    	
	        logger.error("Author Info");
	        
	        MainViewController.getInstance().switchView2(ViewType.VIEW2, selected);
	        
	    }   
	}
}


