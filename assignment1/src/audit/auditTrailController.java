package audit;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.AuditTrailEntry;
import model.Book;
import view_controller.BookTableGateway;
import view_controller.MainViewController;
import view_controller.MyController;
import view_controller.ViewType;

public class auditTrailController implements MyController, Initializable{

    @FXML
    private ListView<AuditTrailEntry> AuditList;


    
    @FXML
    private Button Back;

    @FXML
    private Label AuditTitle;
    
    private Book book;
    
    
    public auditTrailController(Book selected) {
    	this.book = selected;
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		AuditTitle.setText("Audit Trail for" + book.getTitle());
	    List<AuditTrailEntry> audits = BookTableGateway.getInstance().getAuditTrails(book);
	    ObservableList<AuditTrailEntry> auditsObsList;
		auditsObsList  = FXCollections.observableArrayList();
		auditsObsList.addAll(audits);
		   
		AuditList.setItems(auditsObsList);
		   
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

    @FXML public void handleButtonAction(ActionEvent action2) throws Exception {
    	Object source = action2.getSource();
    	if(source == Back){
    		MainViewController.getInstance().switchView(ViewType.VIEW2, this.book);
    	}
    }
}


