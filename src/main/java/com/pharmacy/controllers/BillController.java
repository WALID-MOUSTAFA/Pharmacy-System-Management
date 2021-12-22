package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import com.pharmacy.POGO.Sale;
import com.pharmacy.POGO.SaleDetails;
import com.pharmacy.POGO.Supplier;
import com.pharmacy.POGO.Treatment;
import com.pharmacy.services.SalesDetailsService;
import com.pharmacy.services.SalesService;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

public class BillController extends MyController{

    Executor executor;

    private SalesService salesService;
    private SalesDetailsService salesDetailsService;
    private long currentSelectedBillId;
    @FXML private TableView billsTableView;
    @FXML private Button deleteBillButton;
    @FXML private Button editBillButton;
    @FXML private TextField searchBox;
    @FXML private Label total;

    private void initializeThreadPool() {
        this.executor= Executors.newCachedThreadPool((runnable)-> {
            Thread thread= new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        });
    }

    public BillController() throws SQLException {
        this.salesService= new SalesService();
        this.salesDetailsService= new SalesDetailsService();
        this.initializeThreadPool();
    }

    @FXML
    public void initialize() throws SQLException{
        this.inititalizeBillTableView();
        this.addBillsTableViewDoubleClickListener();
        this.addBillsTableViewFocusListener();
      	this.initializeMonthFilter();
      	this.total.visibleProperty().bind(Bindings.isNotNull(this.monthFilter.valueProperty()));
        this.total.visibleProperty().bind(Bindings.isNotEmpty(this.monthFilter.valueProperty().asString()));
    }

    private void addBillsTableViewFocusListener() {

        this.deleteBillButton.disableProperty().bind(Bindings.isEmpty(this.billsTableView.getSelectionModel().getSelectedItems()));
        // this.billsTableView.focusedProperty()
                // .addListener((observableVal, oldval, newval) -> {
                    // if (newval) {
                 //      this.editBillButton.setDisable(false);
                        // this.deleteBillButton.setDisable(false);
                    // } else {
                   //     this.editBillButton.setDisable(true);
                        // this.deleteBillButton.setDisable(true);
                    // }
                // });
			
    }

    private void addBillsTableViewDoubleClickListener() {
        this.billsTableView.setRowFactory
                ( tv->  {
                    TableRow<Treatment> row= new TableRow<>();
                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                            try {
				    this.showBillDetails();
                            } catch (IOException|SQLException e){}
                        }
                    });
                    return row ;
                });
    }

    private void calculateTotal() {
        double total= this.billsTableView.getItems().stream().mapToDouble(b -> ((Sale)b).getNetTotal()).sum();
        this.total.setText(String.valueOf(total));
    }

    private void showBillDetails() throws IOException, SQLException{
        Stage stage= new Stage();
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation
                (getClass().getResource("/fxml/BillDetails.fxml"));
        BillDetailsController billDetailsController=
                new BillDetailsController();
        billDetailsController.setStage(stage);
        billDetailsController.setId(this.currentSelectedBillId);
        loader.setController(billDetailsController);
        Parent root= loader.load();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }


    private void renderBills() {
        Task<List<Sale>> task= new Task<List<Sale>>() {
            @Override
            protected List<Sale> call() throws Exception {
                return BillController.this.salesService
                        .getAllSales();
            }
        };
        task.setOnSucceeded(e-> {
            this.billsTableView.setItems(FXCollections.observableArrayList(
                    task.getValue()
            ));
        });
        task.setOnFailed(e-> task.getException().printStackTrace());
        this.executor.execute(task);
    }

    private void inititalizeBillTableView() throws SQLException{

        this.billsTableView.getColumns().clear();
        this.renderBills();

        TableColumn<Sale, String> customerName= new TableColumn<>("العميل");
        customerName.setCellValueFactory(tv-> {
            return new SimpleStringProperty(tv.getValue().getCustomer().getName());
        });

        TableColumn<Sale, Double> netTotalColumn= new TableColumn<>("الإجمالي");
        netTotalColumn.setCellValueFactory(new PropertyValueFactory<>("netTotal"));

        TableColumn<Sale, Double> discountColumn= new TableColumn<>("الخصم");
        discountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));

        TableColumn<Sale, Double> dateColumn= new TableColumn<>("التاريخ");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateIn"));


        TableColumn<Sale, Double> nameColumn= new TableColumn<>("اسم المشتري");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        this.billsTableView.getColumns().addAll(customerName, netTotalColumn, dateColumn, nameColumn);



        //set currentTreatmentId
        this.billsTableView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldvalue, newvalue)->{
                    this.currentSelectedBillId=((Sale)newvalue).getId();
                });

        this.calculateTotal();

    }


	@FXML
	public void deleteSale() throws SQLException{
		List<SaleDetails> relatedSaleDetails= this.salesDetailsService.getRelatedSaleDetails(this.currentSelectedBillId);
		if(relatedSaleDetails != null) {
	    	for (SaleDetails sd: relatedSaleDetails) {
		    this.salesDetailsService.restoreBalance(sd);
	    	}
		}
		if(this.salesService.deleteSale(this.currentSelectedBillId)){
			this.billsTableView.getItems().remove(this.billsTableView.getSelectionModel().getSelectedItem());
		} else {
			MyUtils.ALERT_ERROR("حدث خطأ أثناء الحذف");
		}
		
	}

    @FXML
    private void doSearch() throws SQLException {
        this.billsTableView
                .setItems(FXCollections.observableArrayList(this.salesService.getAllSales()));
        String q= this.searchBox.getText();
        if(q.isEmpty()) {
            this.inititalizeBillTableView();
            return;
        }
        ObservableList<Sale> items= this.billsTableView.getItems();
        FilteredList<Sale> filteredList= new FilteredList<>(items);
        filteredList.setPredicate(new Predicate<Sale>() {
			@Override
			public boolean test(Sale sale) {
				return sale.getName().contains(q)
					|| sale.getCustomer().getName().contains(q)
					|| sale.getDateIn().contains(q);
			}
		});
            this.billsTableView.setItems(filteredList);
            this.calculateTotal();
    }


	private void initializeMonthFilter() {
		String[] months= {
			"يناير",
			"فبراير"
			, "مارس"
			, "إبريل"
			, "مايو",
			"يونيو",
			"يوليو",
			"أغسطس",
			"سبتمبر",
			"أكتوبر",
			"نوفمبر",
			"ديسمبر"
		};

		for(String m : months) {
			this.monthFilter.getItems().add(m);
		}
	}

	
	//NOTE(walid): we will only filter based on month of this year;
	@Override
	protected void onFilterChange(Filter filter) {

		try {
			this.billsTableView
				.setItems(FXCollections.observableArrayList(this.salesService.getAllSales()));
		} catch(Exception e) {

		}
			
		switch(filter){
		case MONTH: {
			String  month= this.monthFilter.getValue();
			String monthNum= MyUtils.getMonthNumric(month);

			ObservableList<Sale> items=
				this.billsTableView.getItems();
			FilteredList<Sale> filteredList=
				new FilteredList<>(items);

			filteredList.setPredicate
				(new Predicate<Sale>()
				 {
					 @Override
					 public boolean test(Sale s) {
						 return s.getDateIn().split(" ")
							 [0].split("-")[1]
							 .equals(monthNum);
					 }
					});
                        this.billsTableView.setItems(filteredList);


                        break;
		}
		default: return;
		}
		this.calculateTotal();
	}

	@Override
	protected void deleteFilter() throws SQLException{
		this.inititalizeBillTableView();
		this.monthFilter.setValue("");
	}

}
