// Name: Ruomei(Della) Wang
// AndrewID: ruomeiw

package hw3;

import java.io.File;
import java.net.URL;
import java.time.format.DateTimeFormatter;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class CyberCop extends Application{

	public static final String DEFAULT_PATH = "data"; //folder name where data files are stored
	public static final String DEFAULT_HTML = "/CyberCop.html"; //local HTML
	public static final String APP_TITLE = "Cyber Cop"; //displayed on top of app

	CCView ccView = new CCView();
	CCModel ccModel = new CCModel();
	FileChooser fileChooser = new FileChooser();
	CaseView caseView; //UI for Add/Modify/Delete menu option

	GridPane cyberCopRoot;
	Stage stage;

	static Case currentCase; //points to the case selected in TableView.
	static File file = null; // initiate the file in order to select a file in the Select handler.

	public static void main(String[] args) {
		launch(args);
	}

	/** start the application and show the opening scene */
	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		primaryStage.setTitle("Cyber Cop");
		cyberCopRoot = ccView.setupScreen();  
		setupBindings();
		Scene scene = new Scene(cyberCopRoot, ccView.ccWidth, ccView.ccHeight);
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		ccView.webEngine.load(getClass().getResource(DEFAULT_HTML).toExternalForm());
		primaryStage.show();
	}

	/** setupBindings() binds all GUI components to their handlers.
	 * It also binds disableProperty of menu items and text-fields 
	 * with ccView.isFileOpen so that they are enabled as needed
	 */
	void setupBindings() {
		// Set up the bindings between the boolean value isFileOpen with Menu Items
		// to disable certain Menu Items, TextFields, label, and buttons when the file is open or not
		ccView.openFileMenuItem.disableProperty().bind(ccView.isFileOpen);
		ccView.saveFileMenuItem.disableProperty().bind(ccView.isFileOpen.not());
		ccView.closeFileMenuItem.disableProperty().bind(ccView.isFileOpen.not());
		ccView.addCaseMenuItem.disableProperty().bind(ccView.isFileOpen.not());
		ccView.modifyCaseMenuItem.disableProperty().bind(ccView.isFileOpen.not());
		ccView.deleteCaseMenuItem.disableProperty().bind(ccView.isFileOpen.not());
		ccView.yearComboBox.disableProperty().bind(ccView.isFileOpen.not());
		ccView.titleTextField.disableProperty().bind(ccView.isFileOpen.not());
		ccView.caseTypeTextField.disableProperty().bind(ccView.isFileOpen.not());
		ccView.caseNumberTextField.disableProperty().bind(ccView.isFileOpen.not());
		ccView.caseNotesLabel.disableProperty().bind(ccView.isFileOpen.not());
		ccView.searchButton.disableProperty().bind(ccView.isFileOpen.not());
		ccView.clearButton.disableProperty().bind(ccView.isFileOpen.not());
		ccView.caseCountChartMenuItem.disableProperty().bind(ccView.isFileOpen.not());

		// Set up event handlers with Menu Items and buttons
		ccView.openFileMenuItem.setOnAction(new OpenFileMenuItemHandler());
		ccView.closeFileMenuItem.setOnAction(new CloseFileMenuItemHandler());
		ccView.exitMenuItem.setOnAction(new ExitMenuItemHandler());
		ccView.searchButton.setOnAction(new SearchButtonHandler());
		ccView.clearButton.setOnAction(new ClearButtonHandler());
		ccView.addCaseMenuItem.setOnAction(new CaseMenuItemHandler());
		ccView.modifyCaseMenuItem.setOnAction(new CaseMenuItemHandler());
		ccView.deleteCaseMenuItem.setOnAction(new CaseMenuItemHandler());
		ccView.saveFileMenuItem.setOnAction(new SaveFileMenuItemHandler());
		ccView.caseCountChartMenuItem.setOnAction(new CaseCountChartMenuItemHandler());

		// Created a lambda event handler that focuses on clicking on each entry in the TableView columns.
		// When clicking on an entry, the TextFields will display the current case's information.
		// Also, the corresponding web page will show up on the right side.
		ccView.caseTableView.setRowFactory(caseClickedOn -> {
			TableRow<Case> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty()) {
					currentCase = row.getItem();
					ccView.titleTextField.setText(currentCase.getCaseTitle());
					ccView.caseTypeTextField.setText(currentCase.getCaseType());
					ccView.caseNumberTextField.setText(currentCase.getCaseNumber());
					ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());
					ccView.yearComboBox.setValue(currentCase.getCaseDate().substring(0, currentCase.getCaseDate().indexOf("-")));
					if (currentCase.getCaseLink() == null || currentCase.getCaseLink().isBlank()) {  //if no link in data
						URL url = getClass().getResource(DEFAULT_HTML);  //default html
						if (url != null) ccView.webEngine.load(url.toExternalForm());
					} else if (currentCase.getCaseLink().toLowerCase().startsWith("http")){  //if external link
						ccView.webEngine.load(currentCase.getCaseLink());
					} else {
						URL url = getClass().getClassLoader().getResource(currentCase.getCaseLink().trim());  //local link
						if (url != null) ccView.webEngine.load(url.toExternalForm());
					}
				}
			});
			return row;
		});
	}

	// OpenFileMenuItemHandler handles events of opening a file, choosing a file, and updates all the GUI components.
	class OpenFileMenuItemHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			fileChooser.setTitle("Select File");
			fileChooser.setInitialDirectory(new File(DEFAULT_PATH));
			file = fileChooser.showOpenDialog(stage);
			if (file != null) {
				ccView.isFileOpen.set(true);
				try {
					ccModel.readCases(file.getAbsolutePath());
				}
				catch (DataException d) {
					System.out.println("The file must have cases with tab seperated date, title, type, and case number!");
				}
				ccModel.buildYearMapAndList();
				ccView.caseTableView.setItems(ccModel.caseList);
				ccView.yearComboBox.setItems(ccModel.yearList);
				stage.setTitle("Cyber Cop: " + file.getName());
				ccView.messageLabel.setText(ccModel.caseList.size() + " cases.");  
				currentCase = ccModel.caseList.get(0);
				ccView.titleTextField.setText(ccModel.caseList.get(0).getCaseTitle());
				ccView.caseTypeTextField.setText(ccModel.caseList.get(0).getCaseType());
				ccView.caseNumberTextField.setText(ccModel.caseList.get(0).getCaseNumber());
				ccView.caseNotesTextArea.setText(ccModel.caseList.get(0).getCaseNotes());
				ccView.yearComboBox.setValue(ccModel.caseList.get(0).getCaseDate().substring(0, ccModel.caseList.get(0).getCaseDate().indexOf("-")));	
			}
		}
	}

	// SaveFileMenuItemHandler handles events of writing into a new file, choosing a file directory, and updates the GUI components.
	class SaveFileMenuItemHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			fileChooser.setTitle("Save File");
			fileChooser.setInitialDirectory(new File(DEFAULT_PATH));
			file = fileChooser.showSaveDialog(stage);
			if (file != null) {
				ccView.isFileOpen.set(true);
				if(ccModel.writeCases(file.getAbsolutePath())) 		ccView.messageLabel.setText(file.getName() + " saved.");  
				else 	ccView.messageLabel.setText("File save failed"); 
			}
		}
	}

	// CloseFileMenuItemHandler handles events of closing a file. 
	// The currentCase will be set to null, the isFileOpen boolean will be set to false.
	// All GUI components are cleared. File is set to null. YearList and CaseList are cleared.
	class CloseFileMenuItemHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			ccView.isFileOpen.set(false);
			stage.setTitle("Cyber Cop");
			currentCase = null;
			ccView.titleTextField.clear();
			ccView.caseTypeTextField.clear();
			ccView.caseNumberTextField.clear();
			ccView.caseNotesTextArea.clear();
			ccView.messageLabel.setText(null);
			ccModel.caseList.clear();
			ccModel.yearList.clear();
			file = null;
		}

	}

	// ExitMenuItemHandler handles the event of user exiting CyberCop 2.0.
	class ExitMenuItemHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			System.exit(0);
		}
	}

	// SearchButtonHandler handles the event of searching for a certain case.
	// Extract the information from the TextFields and invoke the searchCases method.
	// Update the GUI components once the search is complete.
	class SearchButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			ccView.isFileOpen.set(true);
			String caseTitleInput = ccView.titleTextField.getText();
			String caseTypeInput = ccView.caseTypeTextField.getText();
			String caseDateInput = ccView.yearComboBox.getValue();
			String caseNumberInput = ccView.caseNumberTextField.getText();
			if (caseTitleInput == null && caseTypeInput == null && caseDateInput == null && caseNumberInput == null) {
				ccView.caseTableView.setItems(ccModel.caseList);
			} else {
				ObservableList<Case> searchResult = FXCollections.observableArrayList(); 
				searchResult = (ObservableList<Case>) ccModel.searchCases(caseTitleInput, caseTypeInput, caseDateInput, caseNumberInput);
				ccView.caseTableView.setItems(searchResult);
				ccView.titleTextField.setText(searchResult.get(0).getCaseTitle());
				ccView.caseTypeTextField.setText(searchResult.get(0).getCaseType());
				ccView.yearComboBox.setValue(searchResult.get(0).getCaseDate().substring(0, searchResult.get(0).getCaseDate().indexOf("-")));
				ccView.caseNumberTextField.setText(searchResult.get(0).getCaseNumber());
				ccView.caseNotesTextArea.setText(searchResult.get(0).getCaseNotes());
				ccView.messageLabel.setText(searchResult.size() + " cases.");
			}
		}
	}

	// ClearButtonHandler handles the clear button and clears the text in TextFields and ComboBox.
	class ClearButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			ccView.isFileOpen.set(true);
			ccView.titleTextField.clear();
			ccView.caseTypeTextField.clear();
			ccView.caseNumberTextField.clear();
			ccView.caseNotesTextArea.clear();
			ccView.yearComboBox.setValue(null);
		}
	}

	// CaseMenuItemHandler tells which Menu Item the user clicked on, and initializes different case views accordingly
	// Show the stage and set up the button handlers with the buttons of the Case View
	class CaseMenuItemHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			ccView.isFileOpen.set(true);
			MenuItem itemSelected = (MenuItem)event.getSource();
			String buttonClicked = itemSelected.getText();
			if (buttonClicked.equals("Add case")) {
				caseView = new AddCaseView("Add case");
				Stage addCaseStage = caseView.buildView();
				addCaseStage.show();
				caseView.updateButton.setOnAction(new AddButtonHandler());

			} else if (buttonClicked.equals("Modify case")) {
				caseView = new ModifyCaseView("Modify case");
				Stage modifyCaseStage = caseView.buildView();
				modifyCaseStage.show();
				caseView.updateButton.setOnAction(new ModifyButtonHandler());

			} else if (buttonClicked.equals("Delete case")) {
				caseView = new DeleteCaseView("Delete case");
				Stage deleteCaseStage = caseView.buildView();
				deleteCaseStage.show();
				caseView.updateButton.setOnAction(new DeleteButtonHandler());	
			}
		}
	}

	// AddButtonHandler handles the event of adding a case.
	// Updates the GUI components with the new case's information as well as the case count.
	// Add the new case into the caseList and update the yearList.
	class AddButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			ccView.isFileOpen.set(true);
			caseView.caseDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			String caseAddedTitle = caseView.titleTextField.getText();
			String caseAddedType = caseView.caseTypeTextField.getText();
			String caseAddedDate = caseView.caseDatePicker.getValue().toString();
			String caseAddedNumber = caseView.caseNumberTextField.getText();
			String caseAddedCategory = caseView.categoryTextField.getText();
			String caseAddedLink = caseView.caseLinkTextField.getText();
			String caseAddedNotes = caseView.caseNotesTextArea.getText();
			try {
				// See if the added case's number already exists
				// If so, throw the DataException with corresponding error message
				if (ccModel.caseMap.containsKey(caseAddedNumber)) {
					String message = "Duplicate case number";
					throw new DataException(message);
				}
				// Use a IF statement to see if the case has missing data in first four columns
				// If there is data missing, do not add this case to the caseList and throw DataException with corresponding error message
				if (caseAddedDate != null && caseAddedTitle != null && caseAddedType != null && caseAddedNumber != null) {
					if (caseAddedCategory == null) caseAddedCategory = " ";
					if (caseAddedLink == null) caseAddedLink = " ";
					if (caseAddedNotes == null) caseAddedNotes = " ";
					ccModel.caseList.add(new Case(caseAddedDate.trim(), caseAddedTitle.trim(), caseAddedType.trim(), caseAddedNumber.trim(), caseAddedLink,
							caseAddedCategory, caseAddedNotes));
				}
				else {
					String message1 = "Case must have date, title, type, and number";
					throw new DataException(message1);
				}
			} catch(DataException d) {
				System.out.println("Case must have date, title, type, and number");
			}
			
			ccModel.buildYearMapAndList();
			ccView.titleTextField.setText(currentCase.getCaseTitle());
			ccView.caseTypeTextField.setText(currentCase.getCaseType());
			ccView.caseNumberTextField.setText(currentCase.getCaseNumber());
			ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());
			ccView.messageLabel.setText(ccModel.caseList.size() + " cases.");
			ccView.caseTableView.setItems(ccModel.caseList);
			ccView.yearComboBox.setItems(ccModel.yearList);
			ccView.caseTableView.refresh();
		}
	}

	// ModifyButtonHandler handles the event of modifying the content of a case
	// Updates the GUI components with the newly modified case's information as well as the case count.
	// Updates the caseList, yearMap, and yearList
	class ModifyButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			ccView.isFileOpen.set(true);
			caseView.caseDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			String caseModifiedTitle = caseView.titleTextField.getText(); 
			String caseModifiedType = caseView.caseTypeTextField.getText();
			String caseModifiedDate = caseView.caseDatePicker.getValue().toString();
			String caseModifiedNumber = caseView.caseNumberTextField.getText();
			String caseModifiedCategory = caseView.categoryTextField.getText();
			String caseModifiedLink = caseView.caseLinkTextField.getText();
			String caseModifiedNotes = caseView.caseNotesTextArea.getText();
			try {
				// Traverse through the caseList to see if the added case's number equals to another case' number
				// (because the case number can be itself's number)
				// If so, throw the DataException with corresponding error message
				if (ccModel.caseMap.containsKey(caseModifiedNumber)) {
					String message = "Duplicate case number";
					throw new DataException(message);
				}
				// Use a IF statement to see if the case has missing data in first four columns
				// If there is data missing, do not modify this case and throw DataException with corresponding error message
				if (!caseModifiedDate.isBlank() && !caseModifiedTitle.isBlank() && !caseModifiedType.isBlank() && !caseModifiedNumber.isBlank())
					currentCase = new Case(caseModifiedDate, caseModifiedTitle, caseModifiedType, caseModifiedNumber, caseModifiedLink,
							caseModifiedCategory, caseModifiedNotes);
				else {
					String message3 = "Case must have date, title, type, and number";
					throw new DataException(message3);
				}
			} catch(DataException d) {
				System.out.println("Case must have date, title, type, and number");
			}
			ccModel.caseList.set(ccModel.caseList.size() - 1, currentCase);
			ccModel.buildYearMapAndList();
			ccView.caseTableView.setItems(ccModel.caseList);
			ccView.yearComboBox.setItems(ccModel.yearList);
			ccView.titleTextField.setText(currentCase.getCaseTitle());
			ccView.caseTypeTextField.setText(currentCase.getCaseType());
			ccView.caseNumberTextField.setText(currentCase.getCaseNumber());
			ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());
			ccView.yearComboBox.setValue(currentCase.getCaseDate().substring(0, currentCase.getCaseDate().indexOf("-")));
		}

	}

	// DeleteButtonHandler handles the event of deleting a case
	// Updates the GUI components to be the next current case
	// Updates the caseList, yearMap, and yearList
	class DeleteButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			ccView.isFileOpen.set(true);
			caseView.caseDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			ccModel.caseList.remove(currentCase);
			ccModel.buildYearMapAndList();
			ccView.caseTableView.setItems(ccModel.caseList);
			ccView.yearComboBox.setItems(ccModel.yearList);
			ccView.messageLabel.setText(ccModel.caseList.size() + " cases.");
			ccView.caseNotesTextArea.clear();
			ccView.caseTableView.refresh();
		}
	}

	// Invokes ccView's showChartView() method and passes yearMap to show the CaseCountChart
	class CaseCountChartMenuItemHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			ccView.showChartView(ccModel.yearMap);
		}

	}
}