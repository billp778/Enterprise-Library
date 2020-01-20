package view_controller;
	
import java.net.URL;
import java.sql.Connection;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import view_controller.MainViewController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

//CS 4743 Assignment 5 <Seth Greco>  <William Page>

public class Main extends Application {
	public static Logger logger = LogManager.getLogger();
	
	@Override
	public void start(Stage stage) throws Exception {
		URL url = this.getClass().getResource("MainView.fxml");
		FXMLLoader loader = new FXMLLoader(url);
		
		MainViewController controller = MainViewController.getInstance();
		loader.setController(controller);
		
		Parent rootNode = loader.load();
		controller.setBorderPane((BorderPane) rootNode);
		
		stage.setScene(new Scene(rootNode));
		
	
		
		stage.setTitle("A Library of Books");
		stage.setWidth(1280);
		stage.setHeight(960);
		stage.show();
		
	}

	
	@Override
	public void init() throws Exception {
		super.init();
		
		logger.info("creating connection...");
		
		// create db connection
		MysqlDataSource ds = new MysqlDataSource();
		ds.setURL("jdbc:mysql://easel2.fulgentcorp.com/eug119");
		ds.setUser("eug119");
		ds.setPassword("QcMCvtWDFeJo9GIiUATH");
		Connection connection = ds.getConnection();
		
		// assign connection to gateways
		BookTableGateway.getInstance().setConnection(connection);
		PublisherGateway.getInstance().setConnection(connection);
		AuthorGateway.getInstance().setConnection(connection);
		LoginGateway.getInstance().setConnection(connection);
	}
	
	@Override
	public void stop() throws Exception {
		super.stop();
		
		logger.info("closing connection...");
		
		BookTableGateway.getInstance().getConnection().close();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
