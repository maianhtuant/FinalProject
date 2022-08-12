package edu.sdccd.cisc191.c;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.management.Notification;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;

import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.event.KeyEvent;
import java.io.*;



public class ClientWindow extends Application {
	Socket socket;
	ObjectOutputStream objToServer;
	ObjectInputStream objFromServer;
	DataOutputStream dataToServer;
	ArrayList<MaterialDetail> list =  new ArrayList<MaterialDetail>();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
	public void connectServer() throws UnknownHostException, IOException {
		socket = new Socket("localhost", 4446);
		objToServer = new ObjectOutputStream(socket.getOutputStream());
	    objFromServer = new ObjectInputStream(socket.getInputStream());
		dataToServer = new DataOutputStream(socket.getOutputStream());
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Client client = new Client();
		BorderPane bPane = new BorderPane();
		Scene scene= new Scene(bPane, 800, 600);
		
		MenuBar menuBar = new MenuBar();

		
		Menu file = new Menu("File");
		menuBar.getMenus().add(file);
		VBox menuBox = new VBox(menuBar);
		bPane.setTop(menuBox);
		
		
        
        VBox main = new VBox();
        main.setAlignment(Pos.TOP_CENTER);
        main.setPadding(new Insets(20, 30, 70, 20));
        HBox id = new HBox();
        Label lID = new Label("ID");
        lID.setPrefWidth(70);
        TextField tID = new TextField();
        id.getChildren().add(lID);
        id.getChildren().add(tID);
        
        HBox name = new HBox();
        Label lName = new Label("Name");
        lName.setPrefWidth(70);
        TextField tName = new TextField();
        name.getChildren().add(lName);
        name.getChildren().add(tName);
        
        HBox price = new HBox();
        Label lPrice = new Label("Price");
        lPrice.setPrefWidth(70);
        TextField tPrice = new TextField();
        tPrice.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				// TODO Auto-generated method stub
				if(!arg2.matches("\\d{0,7}([\\.]\\d{0,2})?")) {
					Alert alert = new Alert(AlertType.ERROR);
	        		alert.setTitle("Error Input");
	        		alert.setHeaderText("Price error input");
	        		alert.setContentText("Price should be a number");

	        		alert.showAndWait();
	        		tPrice.setText("");
				}
			}

        	
		});
        price.getChildren().add(lPrice);
        price.getChildren().add(tPrice);
        
        HBox color = new HBox();
        Label lColor = new Label("Color");
        lColor.setPrefWidth(70);
        TextField tColor = new TextField();
        ColorPicker cPicker = new ColorPicker();
        cPicker.setPrefHeight(25);
        cPicker.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Color c= cPicker.getValue();
				tColor.setText(c.toString());
			}
		});
        color.getChildren().add(lColor);
        color.getChildren().add(tColor);
        color.getChildren().add(cPicker);
        
        HBox quality = new HBox();
        Label lQuality = new Label("Quality");
        lQuality.setPrefWidth(70);
        TextField tQuality = new TextField();
        tQuality.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				// TODO Auto-generated method stub
				if(!arg2.matches("\\d*")) {
					Alert alert = new Alert(AlertType.ERROR);
	        		alert.setTitle("Error Input");
	        		alert.setHeaderText("Quality error input");
	        		alert.setContentText("Price should be a number");

	        		alert.showAndWait();
	        		tQuality.setText("");
				}
			}

        	
		});
        
        
        
        quality.getChildren().add(lQuality);
        quality.getChildren().add(tQuality);
        
        
        //create table and read data from server
        VBox tableBox = new VBox();
        TableView<MaterialDetail> table = new TableView<MaterialDetail>();
        TableColumn<MaterialDetail, String> columnID = new TableColumn<MaterialDetail, String>("ID");
        TableColumn<MaterialDetail, String> columnName = new TableColumn<MaterialDetail, String>("Name");        
        TableColumn<MaterialDetail, Double> columnPrice = new TableColumn<MaterialDetail, Double>("Price");
        TableColumn<MaterialDetail, String> columnColor = new TableColumn<MaterialDetail, String>("Color");
        TableColumn<MaterialDetail, Integer> columnQuality = new TableColumn<MaterialDetail, Integer>("Quality");
        table.getColumns().addAll(columnID,columnName,columnPrice,columnColor,columnQuality);
        columnID.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MaterialDetail,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<MaterialDetail, String> data) {
				// TODO Auto-generated method stub
				return new ReadOnlyStringWrapper(data.getValue().getId());
			}
		});
        columnName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MaterialDetail,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<MaterialDetail, String> data) {
				// TODO Auto-generated method stub
				return new ReadOnlyStringWrapper(data.getValue().getName());
			}
		});
        
        columnPrice.setCellValueFactory(new PropertyValueFactory<MaterialDetail, Double>("Price"));
        columnColor.setCellValueFactory(new PropertyValueFactory<MaterialDetail, String>("Color"));
        columnQuality.setCellValueFactory(new PropertyValueFactory<MaterialDetail, Integer>("Quality"));
        
        
        ObservableList<MaterialDetail> obsList = FXCollections.observableArrayList();
        FilteredList<MaterialDetail> filtered = new FilteredList<>(obsList, p -> true);
        
    	
        //button submit & acction
        HBox btnBox = new HBox();
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setSpacing(40);
        Button btnSubmit = new Button("Submit");
        
        Button btnCancel = new Button("Cancel");
        btnBox.getChildren().add(btnSubmit);
        btnBox.getChildren().add(btnCancel);
        btnSubmit.setOnAction(e -> {
        	if(tID.getText().equals("")) {
        		Alert alert = new Alert(AlertType.ERROR);
        		alert.setTitle("Error Input");
        		alert.setContentText("Please input ID before submit");

        		alert.show();
        		e.consume();
        	}
        	
        	if(tID.isEditable() ==true) {
        	String getID = tID.getText();
        	String getName = tName.getText();
        	String getColor = tColor.getText();
        	String getPriceString = tPrice.getText().trim();
        	double getPrice = Double.parseDouble(tPrice.getText().trim());;
        	
        		/*Alert alert = new Alert(AlertType.ERROR);
        		alert.setTitle("Information Dialog");
        		alert.setHeaderText("Look, an Information Dialog");
        		alert.setContentText("I have a great message for you!");

        		alert.showAndWait();
        		*/
        	
        	
        	int getQuality = Integer.parseInt(tQuality.getText());
        	Material m = new Material(getID, getName);
        	MaterialDetail mD = new MaterialDetail(getID, getName, getPrice, getColor, getQuality);
        	
        	try {
        		client.insertMaterial(mD);
        		list = client.getAllMaterial();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        	//getData back from server fill to table
        	table.getItems().clear();
        	
				for(MaterialDetail c:list) {
	        		obsList.add(c);
	        	}
        	table.setItems(obsList);
        	tID.setText("");
        	tName.setText("");
        	tPrice.setText("");
        	tColor.setText("");
        	tQuality.setText("");
        	
        	}else if(tID.isEditable() == false) {
        		String getID = tID.getText();
            	String getName = tName.getText();
            	double getPrice = Double.parseDouble(tPrice.getText().trim());
            	String getColor = tColor.getText();
            	int getQuality = Integer.parseInt(tQuality.getText());
            	Material m = new Material(getID, getName);
            	MaterialDetail mD = new MaterialDetail(getID, getName, getPrice, getColor, getQuality);
            	
            	try {
            		client.modifiedItem(mD);
            		list = client.getAllMaterial();
            	}catch (Exception e1) {
    				e1.printStackTrace();
    			}
            	table.getItems().clear();
            	
				for(MaterialDetail c:list) {
	        		obsList.add(c);
	        	}
        	}
        });
        	btnCancel.setOnAction(e->{
        		tID.setText("");
        		tID.setEditable(true);
        		tName.setText("");
        		tPrice.setText("");
        		tColor.setText("");
        		tQuality.setText("");
        		
        	});
        
        table.setItems(obsList);
        tableBox.getChildren().add(table);
        
        VBox bottomBox = new VBox();
        TextField searchTF = new TextField();
        Button delete = new Button("Delete");
        Label labelSuccess = new Label();
        bottomBox.getChildren().add(searchTF);
        bottomBox.getChildren().add(delete);
        bottomBox.getChildren().add(labelSuccess);
        tableBox.getChildren().add(bottomBox);
        	//set delete button action
        	delete.setOnAction(e ->{
        		int index = table.getSelectionModel().getSelectedIndex();
        		
        		MaterialDetail m = table.getItems().get(index);
        		
        		String getID = m.getId();
        		try {
					int success = client.deleteItem(getID);
					if(success == 1) {
						labelSuccess.setText("Delete Success");
						table.getItems().clear();
						ObservableList<MaterialDetail> afterDelete = FXCollections.observableArrayList();
						ArrayList<MaterialDetail> listAD = new ArrayList<MaterialDetail>();
						listAD = client.getAllMaterial();
						for(MaterialDetail c:listAD) {
							afterDelete.add(c);
			        	}
						table.setItems(afterDelete);
					}else if(success == 0) {
						labelSuccess.setText("Please select row");
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	});
        	//set action for search textbox
	        searchTF.setOnKeyPressed(e ->{
	        	if(e.getCode()== KeyCode.ENTER) {
	        		
	        		String text = searchTF.getText();
	        		if(!text.equals("")) {
		        		
		        			List<MaterialDetail> searchReturn = new ArrayList<MaterialDetail>();
		        			try {
								searchReturn = client.getAllMaterial();
							} catch (UnknownHostException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
		        			ObservableList<MaterialDetail> obsReturn = FXCollections.observableArrayList();
		        			searchReturn.stream().filter(m -> m.getId().contains(text) || m.getName().contains(text)).forEach(m -> obsReturn.add(m));
		        			
		        			/*try {
								searchReturn = (ArrayList<MaterialDetail>) client.findMaterial(text);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
					        ObservableList<MaterialDetail> obsReturn = FXCollections.observableArrayList();
					        if(searchReturn!=null) {
					        	for(MaterialDetail c:searchReturn) {
					        		obsReturn.add(c);
					        	}
					        	
					        	table.getItems().clear();
					        	table.setItems(obsReturn);
					        }*/
					        
		        			table.getItems().clear();
				        	table.setItems(obsReturn);
						
	        		}else if(text.equals("")){
	        			System.out.println(obsList.size());
	        			table.getItems().clear();
	        			try {
							list = client.getAllMaterial();
						} catch (UnknownHostException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	        			for(MaterialDetail c:list) {
	    	        		obsList.add(c);
	    	        	}
	        			table.setItems(obsList);
	        		}
	        	}
	        });
	        table.setRowFactory(tv ->{
	        	
	        	//set action double click to table row
	        	TableRow<MaterialDetail> r = new TableRow<>();
	        	r.setOnMouseClicked(event ->{
	        		if(event.getClickCount() == 2 && !r.isEmpty()) {
	        			MaterialDetail getItem = r.getItem();
	        			tID.setText(getItem.getId());
	        			tID.setEditable(false);
	        			tName.setText(getItem.getName());
	                	tPrice.setText(getItem.getPriceS());
	                	tColor.setText(getItem.getColor());
	                	cPicker.setValue(Color.valueOf(getItem.getColor()));
	                	tQuality.setText(getItem.getQualityS());
	                	
	        		}
	        	});
	        	return r;
	        });
	        
        
        
        main.getChildren().add(id);
        main.getChildren().add(name);
        main.getChildren().add(price);
        main.getChildren().add(color);
        main.getChildren().add(quality);
        main.getChildren().add(btnBox);
        
        
        
        bPane.setRight(tableBox);
        bPane.setLeft(main);
        primaryStage.setScene(scene);
        primaryStage.show();
        
		
	}
	
	
	
	
	
}
