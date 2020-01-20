package book_list;


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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import model.AuthorBook;
import model.Book;
import view_controller.BookTableGateway;
import view_controller.MainViewController;
import view_controller.MyController;
import view_controller.ViewType;

public class bookListController implements MyController {
	public static Logger logger = LogManager.getLogger();

    @FXML
    private Button Delete;
    
    @FXML
    private Button btnFirst;
    @FXML
    private Button btnPrev;
    @FXML
    private Button btnNext;
    @FXML
    private Button btnLast;
    @FXML
    private Button btnSearch;
    @FXML
    private TextField tfBookSearch;
    @FXML
    private Label lblCurrent;
    
    String titleSearched = "";
    int a = 0;
    int b = 0;
    int searchMod = 0;
    int regMod = 0;
	
    @FXML
    private ListView<Book> listView;
    private List<Book> books;
    private ObservableList<Book> booksObsList;
    
    private List<Book> refreshList;

    public bookListController(List<Book> DBlist){
    	this.books = DBlist;
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
		
	List<Book> books = BookTableGateway.getInstance().getBooks();
	booksObsList  = FXCollections.observableArrayList();
	lblCurrent.setText("Fetched records " + (a + 1) + " to " + (b += 50) + " out of " + BookTableGateway.getInstance().getCount());
    regMod = BookTableGateway.getInstance().getCount() % 50;   
	listView.setItems(booksObsList);
	booksObsList.addAll(books);
	   
				
	}
    
	
	@FXML public void handleButtonAction(ActionEvent action) throws Exception {
		Object source = action.getSource();
		if(source == btnFirst){
			try{
				
				if(!tfBookSearch.getText().isEmpty()) {
					titleSearched = tfBookSearch.getText();
					BookTableGateway.getInstance().getBooksSearch(titleSearched);
					a = 0;
					int searchCount = BookTableGateway.getInstance().getSearchCount();
					searchMod = BookTableGateway.getInstance().getSearchCount() % 50;
					if(searchCount < 50) {
						b = searchCount;
					}else {
						b = 50;
					}
					
						try{	        
		                    refreshList = BookTableGateway.getInstance().getBooksSearch(titleSearched);
		                    lblCurrent.setText("Fetched records " + (a+1) + " to " + (b) + " out of " + BookTableGateway.getInstance().getSearchCount());
		                    booksObsList  = FXCollections.observableArrayList();
		                    listView.setItems(booksObsList);
		                    booksObsList.addAll(refreshList);
		                }
		                catch (Exception e) {
		                    logger.error("Error fetching some 50 Books");
		                    e.printStackTrace();
		                }
				}else if(tfBookSearch.getText().isEmpty()) {
					refreshList =    BookTableGateway.getInstance().getBooks();
	                a = 0;
	                b = 50;
	                searchMod = 0;
	                lblCurrent.setText("Fetched records " + (a+1) + " to " + (b) + " out of " + BookTableGateway.getInstance().getCount());
	                booksObsList  = FXCollections.observableArrayList();
	                listView.setItems(booksObsList);
	                booksObsList.addAll(refreshList);
				}

            }
            catch (Exception e) {
                logger.error("Error fetching some 50 Books");
                e.printStackTrace();
            }
		}
		if(source == btnSearch && !tfBookSearch.getText().isEmpty()){
			titleSearched = tfBookSearch.getText();
			BookTableGateway.getInstance().getBooksSearch(titleSearched);
			a = 0;
			int searchCount = BookTableGateway.getInstance().getSearchCount();
			searchMod = BookTableGateway.getInstance().getSearchCount() % 50;
			if(searchCount < 50) {
				b = searchCount;
			}else {
				b = 50;
			}
			
				try{	        
                    refreshList = BookTableGateway.getInstance().getBooksSearch(titleSearched);
                    lblCurrent.setText("Fetched records " + (a+1) + " to " + (b) + " out of " + BookTableGateway.getInstance().getSearchCount());
                    booksObsList  = FXCollections.observableArrayList();
                    listView.setItems(booksObsList);
                    booksObsList.addAll(refreshList);
                }
                catch (Exception e) {
                    logger.error("Error fetching some 50 Books");
                    e.printStackTrace();
                }
			
		}
		if(source == btnNext && b < BookTableGateway.getInstance().getCount()){
			
			
			if(!tfBookSearch.getText().isEmpty() && b < BookTableGateway.getInstance().getSearchCount()) {
				if(b + 50 > BookTableGateway.getInstance().getSearchCount()) {
					a+=50;
					b += searchMod;
					lblCurrent.setText("Fetched records " + (a+1) + " to " + (b) + " out of " + BookTableGateway.getInstance().getSearchCount());
                    refreshList =    BookTableGateway.getInstance().getBooksNextSearch(titleSearched, a);
                    booksObsList  = FXCollections.observableArrayList();
                    listView.setItems(booksObsList);
                    booksObsList.addAll(refreshList);

				}else {
					a+=50;
					b += 50;
					lblCurrent.setText("Fetched records " + (a+1) + " to " + (b) + " out of " + BookTableGateway.getInstance().getSearchCount());
                    refreshList =    BookTableGateway.getInstance().getBooksNextSearch(titleSearched, a);
                    booksObsList  = FXCollections.observableArrayList();
                    listView.setItems(booksObsList);
                    booksObsList.addAll(refreshList);

				}
			}else if(tfBookSearch.getText().isEmpty()) {
				
				
				if(b + 50 > BookTableGateway.getInstance().getCount()) {
					a+=50;
					b += regMod;
					lblCurrent.setText("Fetched records " + (a+1) + " to " + (b) + " out of " + BookTableGateway.getInstance().getCount());
                    refreshList =    BookTableGateway.getInstance().getBooksNext(a);
                    booksObsList  = FXCollections.observableArrayList();
                    listView.setItems(booksObsList);
                    booksObsList.addAll(refreshList);

				}else {
					a+=50;
					b += 50;
					lblCurrent.setText("Fetched records " + (a+1) + " to " + (b) + " out of " + BookTableGateway.getInstance().getCount());
                    refreshList =    BookTableGateway.getInstance().getBooksNext(a);
                    booksObsList  = FXCollections.observableArrayList();
                    listView.setItems(booksObsList);
                    booksObsList.addAll(refreshList);

				}
			}

		}
		if(source == btnPrev && a > 1){
			try{
				if(!tfBookSearch.getText().isEmpty()) {
					
					if(b == BookTableGateway.getInstance().getSearchCount()) {
						b -= searchMod;
						if(a - 50 == 1) {
							a = 0;
							lblCurrent.setText("Fetched records " + (a+1) + " to " + (b) + " out of " + BookTableGateway.getInstance().getSearchCount());
							refreshList =    BookTableGateway.getInstance().getBooksNextSearch(titleSearched, a);
		                    booksObsList  = FXCollections.observableArrayList();
		                    listView.setItems(booksObsList);
		                    booksObsList.addAll(refreshList);
							a = 1;
						}else {
							a -= 50;
							lblCurrent.setText("Fetched records " + (a+1) + " to " + (b) + " out of " + BookTableGateway.getInstance().getSearchCount());
							refreshList =    BookTableGateway.getInstance().getBooksNextSearch(titleSearched, a);
		                    booksObsList  = FXCollections.observableArrayList();
		                    listView.setItems(booksObsList);
		                    booksObsList.addAll(refreshList);
						}

					}else {
						b -= 50;
						if(a - 50 == 1) {
							a = 0;
							lblCurrent.setText("Fetched records " + (a+1) + " to " + (b) + " out of " + BookTableGateway.getInstance().getSearchCount());
							refreshList =    BookTableGateway.getInstance().getBooksNextSearch(titleSearched, a);
		                    booksObsList  = FXCollections.observableArrayList();
		                    listView.setItems(booksObsList);
		                    booksObsList.addAll(refreshList);
							a = 1;
						}else {
							a -= 50;
							lblCurrent.setText("Fetched records " + (a+1) + " to " + (b) + " out of " + BookTableGateway.getInstance().getSearchCount());
							refreshList =    BookTableGateway.getInstance().getBooksNextSearch(titleSearched, a);
		                    booksObsList  = FXCollections.observableArrayList();
		                    listView.setItems(booksObsList);
		                    booksObsList.addAll(refreshList);
		                    
						}
						
					}
					
				}else if(tfBookSearch.getText().isEmpty()) {
					
					if(b == BookTableGateway.getInstance().getCount()) {
						a -= 50;
						b -= regMod;
						lblCurrent.setText("Fetched records " + (a+1) + " to " + (b) + " out of " + BookTableGateway.getInstance().getCount());
	                    refreshList =    BookTableGateway.getInstance().getBooksNext(a);
	                    booksObsList  = FXCollections.observableArrayList();
	                    listView.setItems(booksObsList);
	                    booksObsList.addAll(refreshList);
					}else {
						b -= 50;
						if(a - 50 == 1) {
							a = 0;
							lblCurrent.setText("Fetched records " + (a+1) + " to " + (b) + " out of " + BookTableGateway.getInstance().getCount());
							refreshList =    BookTableGateway.getInstance().getBooksNext(a);
		                    booksObsList  = FXCollections.observableArrayList();
		                    listView.setItems(booksObsList);
		                    booksObsList.addAll(refreshList);
							a = 1;
						}else {
							a -= 50;
							lblCurrent.setText("Fetched records " + (a+1) + " to " + (b) + " out of " + BookTableGateway.getInstance().getCount());
							refreshList =    BookTableGateway.getInstance().getBooksNext(a);
		                    booksObsList  = FXCollections.observableArrayList();
		                    listView.setItems(booksObsList);
		                    booksObsList.addAll(refreshList);
						}
					}
					
				}
			}catch (Exception e) {
                logger.error("Error fetching some 50 Books");
                e.printStackTrace();
            }
		}
		 if(source == btnLast){
			 try{
				 if(!tfBookSearch.getText().isEmpty()) {
					 
					 	a = BookTableGateway.getInstance().getSearchCount() - searchMod;
	                    b = BookTableGateway.getInstance().getSearchCount();
	                    refreshList = BookTableGateway.getInstance().getLastBooksSearch(titleSearched, a, searchMod);
	                    lblCurrent.setText("Fetched records " + (a+1) + " to " + (b) + " out of " + BookTableGateway.getInstance().getSearchCount());
	                    booksObsList  = FXCollections.observableArrayList();
	                    listView.setItems(booksObsList);
	                    booksObsList.addAll(refreshList);
				 
				 }else if (tfBookSearch.getText().isEmpty()) {
		                
	                	a = BookTableGateway.getInstance().getCount() - regMod;
	                    b = BookTableGateway.getInstance().getCount();
	                    refreshList =    BookTableGateway.getInstance().getLastBooks(a, regMod);
	                    lblCurrent.setText("Fetched records " + (a+1) + " to " + (b) + " out of " + BookTableGateway.getInstance().getCount());
	                    booksObsList  = FXCollections.observableArrayList();
	                    listView.setItems(booksObsList);
	                    booksObsList.addAll(refreshList);
				 }
				 	
                }
                catch (Exception e) {
                    logger.error("Error fetching some 50 Books");
                    e.printStackTrace();
                }

            }
		if(source == Delete){

			Book deleteThis = listView.getSelectionModel().getSelectedItem();
	    	
			try {
                // attempt to delete the book
                BookTableGateway.getInstance().deleteBook(deleteThis);
                
                // show modal window confirming deletion
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Book Deleted");
                alert.setContentText("Book has been deleted");
                alert.showAndWait();
                
                //refresh menu list to show updated books
                refreshList = BookTableGateway.getInstance().getBooks();
                bookListView.setItems(refreshList);
                
            } catch (Exception e) {
                logger.error("Error deleting book");
                e.printStackTrace();
            }
            initialize();
        }
	 }
	 
	 
	@FXML    void selectBook(MouseEvent event) { 

	    if(event.getClickCount() == 2) { 
	    	
	    	
	    	
	    	int index = listView.getSelectionModel().getSelectedIndex();
	        
	    	Book selected = booksObsList.get(index);
	    	
	        logger.error("Book Info");
	        
	        MainViewController.getInstance().switchView(ViewType.VIEW2, selected);
	        
	    }   
	}
}


