package auto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import model.Book;
import view_controller.BookTableGateway;
import view_controller.MyController;

public class autoController implements MyController {


    @FXML
    private Button generate;
	private Book book;
	private Connection connection;
    
    
    public autoController(Book selected) {
		// TODO Auto-generated constructor stub
    	this.book = selected;
	}

Random rand = new Random();

	@FXML
    void handleAction(ActionEvent event) throws Exception {
    	Object source = event.getSource();
    	if(source == generate){
    		
    		String isbn = "111111";
    		String summary = "";
    		int low = 1455;
    		int high = 2015;
    		int year = 0;
    		String title = "book";
    		int pub = 0;
    		/*
    		for(int i = 89732; i <= 99999; i++){
    			year = rand.nextInt(high-low)+low;
    			pub = rand.nextInt(3);
    			BookTableGateway.getInstance().insertAuto(title+i, summary, year, pub, isbn);
    			System.out.println("book "+ title+i + "entered" );
    		}*/
    	}
    }


	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

}
