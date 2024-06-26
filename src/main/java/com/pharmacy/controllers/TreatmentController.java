package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import com.pharmacy.POGO.DetailedTreatment;
import com.pharmacy.POGO.Treatment;
import com.pharmacy.services.TreatmentService;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

public class TreatmentController extends MyController{

	Executor executor;

	private long currentTreatmentId;
	
	@FXML
	private Button backToControlPanelButton;
	@FXML
	private TableView treatmentsTableView;
	@FXML
	private Button editTreatmentButton;
	@FXML
	private Button deleteTreatmentButton;
	@FXML CreateTreatmentController createTreatmentController; //child fxml
	private TreatmentService treatmentService;
	@FXML private TextField searchBox;
	@FXML private Button showSpecificTreatmentButton;
	@FXML private Label treatmentsCount;

	private void initializeThreadPool() {
		this.executor= Executors.newCachedThreadPool((runnable)-> {
			Thread thread= new Thread(runnable);
			thread.setDaemon(true);
			return thread;
		});
	}


	public TreatmentController()
	{
		this.treatmentService= new TreatmentService();
		this.initializeThreadPool();
	}


	private void renderTreatments() {
		Task<List<DetailedTreatment>> task= new Task<List<DetailedTreatment>>() {
			@Override
			protected List<DetailedTreatment> call() throws Exception {
				return TreatmentController.this.treatmentService
						.getAllTreatments();
			}
		};
		task.setOnSucceeded(e-> {
			this.treatmentsTableView.setItems(FXCollections.observableArrayList(
					task.getValue()
			));
		});
		task.setOnFailed(e-> task.getException().printStackTrace());
		this.executor.execute(task);
	}


	private void initializeTableView() throws SQLException{

		this.treatmentsTableView.getColumns().clear();
		this.renderTreatments();

		TableColumn<DetailedTreatment, String> nameColumn=
			new TableColumn<>("الاسم");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));


		TableColumn<DetailedTreatment, String> typeColumn =
				new TableColumn<>();
		typeColumn.setCellValueFactory(new PropertyValueFactory<>("typeTreatName"));

		TableColumn<DetailedTreatment, String> dateAtColumn=
			new TableColumn<>("تاريخ الإضافة");
		dateAtColumn.setCellValueFactory
			(val-> {
				return new SimpleStringProperty(val.getValue().getDateAt().split(" ")[0]);
			});

		TableColumn<DetailedTreatment, String> statusColumn=
			new TableColumn<>("الحالة");
		statusColumn.setCellValueFactory
			(new PropertyValueFactory<>("status"));

		TableColumn<DetailedTreatment, String> placeColumn=
			new TableColumn<>("المكان");
		placeColumn.setCellValueFactory
			(new PropertyValueFactory<>("place"));

		TableColumn<DetailedTreatment, String> lowCountColumn=
			new TableColumn<>("العدد الحرج");
		lowCountColumn.setCellValueFactory
			(new PropertyValueFactory<>("lowcount"));

		TableColumn<DetailedTreatment, String> companyColumn=
			new TableColumn<>("الشركة");
		companyColumn.setCellValueFactory
			(new PropertyValueFactory<>("company"));

		TableColumn<DetailedTreatment, String> quantityColumn=
			new TableColumn<>("الكمية المتوفرة حالياً");
		quantityColumn.setCellValueFactory
			(new PropertyValueFactory<>("quantity"));

	
		this.treatmentsTableView.getColumns().addAll(nameColumn,
								 typeColumn,
							     dateAtColumn,
							     placeColumn,
							     companyColumn,
							     quantityColumn);

		this.treatmentsTableView.getSelectionModel()
			.selectedItemProperty()
			.addListener((obsvaal, oldval, newval)-> {
					this.setCurrentTreatmentId
						(((Treatment)newval).getId());
				});

		this.renderTreatmentsCount();
	}

	public void addTableViewFocusListeners() {

		this.treatmentsTableView.focusedProperty()
			.addListener((observableVal,oldval,newval)-> {
		if(newval) {
		    this.editTreatmentButton.setDisable(false);
		    this.deleteTreatmentButton.setDisable(false);
		} else {
		    this.editTreatmentButton.setDisable(true);
		    this.deleteTreatmentButton.setDisable(true);
		}
	    });
	}


       private void addTableviewRowDoubleClickListener() {
	   this.treatmentsTableView.setRowFactory
		   ( tv->  {
		   TableRow<Treatment> row= new TableRow<>();
		   row.setOnMouseClicked(event -> {
			   if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
			       try {
				       this.showSpecificTreatment();
			       } catch (IOException|SQLException e){}
			   }
		       });
		   return row ;
	       });
       }


	public void showSpecificTreatment() throws IOException, SQLException{
		Stage stage= new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation
			(getClass().getResource("/fxml/SpecificTreatment.fxml"));
		SpecificTreatmentController specificTreatmentController=
			new SpecificTreatmentController();
		specificTreatmentController.setStage(stage);
		specificTreatmentController.setId(this.currentTreatmentId);
		loader.setController(specificTreatmentController);
		Parent root= loader.load();
		stage.setScene(new Scene(root));
		stage.showAndWait();
	}

	private void setCurrentTreatmentId(long id){
		this.currentTreatmentId= id;
	}
	
	@FXML
	private void initialize() throws SQLException {
		this.initializeTableView();
		//this.addTableViewFocusListeners();
		this.editTreatmentButton.disableProperty().bind(Bindings.isEmpty(this.treatmentsTableView.getSelectionModel().getSelectedItems()));
		this.deleteTreatmentButton.disableProperty().bind(Bindings.isEmpty(this.treatmentsTableView.getSelectionModel().getSelectedItems()));
		this.showSpecificTreatmentButton.disableProperty().bind(Bindings.isEmpty(this.treatmentsTableView.getSelectionModel().getSelectedItems()));
		this.addTableviewRowDoubleClickListener();
		this.createTreatmentController.setTreatmentController(this);
	}

	@FXML
	protected  void backToControlPanel() throws IOException {
		this.swapWithControlPanelScene();
	}


	@FXML
	private void editTreatment() throws IOException, SQLException{
		Stage stage= new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation
			(getClass().getResource("/fxml/editTreatment.fxml"));
		EditTreatmentController editTreatmentController=
			new EditTreatmentController();
		editTreatmentController.setStage(stage);
		editTreatmentController.setId(this.currentTreatmentId);
		loader.setController(editTreatmentController);
		Parent root= loader.load();
		stage.setScene(new Scene(root));

		stage.showAndWait();
		this.treatmentsTableView.getColumns().clear();
		this.initializeTableView();

	}

	@FXML
	private void deleteTreatment() throws SQLException {
		if(!MyUtils.ALERT_CONFIRM("هل تريد حذف هذا المنتج؟")) {
			return;
		}
		try {
			if (this.treatmentService.deleteTreatment(this.currentTreatmentId)) {
				this.treatmentsTableView
						.getItems()
						.remove(this.treatmentsTableView
								.getSelectionModel().getSelectedItem());

			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("لا يمكن حذف المنتج لأنه مرتبط بفواتير موجودة!");
				alert.show();

			}
		} catch (SQLException e){
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("لا يمكن حذف المنتج لأنه مرتبط بفواتير موجودة!");
			alert.show();
		}
		this.renderTreatmentsCount();
	}

	
	


	@FXML
	private void showAsddNewTreatment() throws IOException, SQLException {
		Stage stage= new Stage();
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation
			(getClass().getResource("/fxml/CreateTreatment.fxml"));
		CreateTreatmentController createTreatmentController=
			new CreateTreatmentController();
		createTreatmentController.setStage(stage);
		loader.setController(createTreatmentController);
		Parent root= loader.load();
		stage.setScene(new Scene(root));
		stage.showAndWait();
		this.initializeTableView();
	}


	public void addTreatmentItemToTheTreatmentTableView(DetailedTreatment treatment) throws SQLException {
		this.treatmentsTableView.getItems().add(treatment);
		this.renderTreatmentsCount();
	}

	private void renderTreatmentsCount() throws SQLException {
		int count= this.treatmentService.getTreatmentsCount();
		this.treatmentsCount.setText(String.valueOf(count));
	}

	@FXML
	private void doSearch() throws SQLException {

		this.treatmentsTableView.setItems(FXCollections.observableArrayList(this.treatmentService.getAllTreatments()));
		String q= this.searchBox.getText();
		if(q.isEmpty()) {
			this.initializeTableView();
			return;
		}
		ObservableList<DetailedTreatment> items= this.treatmentsTableView.getItems();
		FilteredList<DetailedTreatment> filteredList= new FilteredList<>(items);
		this.treatmentsTableView.setItems(filteredList);
		filteredList.setPredicate(new Predicate<DetailedTreatment>() {
			@Override
			public boolean test(DetailedTreatment dt) {

				return dt.getName().toLowerCase().contains(q.toLowerCase())
					|| dt.getTypeTreatName().toLowerCase().contains(q.toLowerCase())
						|| dt.getDateAt().toLowerCase().contains(q.toLowerCase())
						|| dt.getCompany().toLowerCase().contains(q.toLowerCase())
						|| dt.getParcode().toLowerCase().contains(q.toLowerCase())
						|| dt.getPlace().toLowerCase().contains(q.toLowerCase());
			}
		});
	}
}
