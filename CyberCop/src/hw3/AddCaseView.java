// Name: Ruomei(Della) Wang
// AndrewID: ruomeiw

package hw3;

import java.time.LocalDate;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class AddCaseView extends CaseView {

	// Extends super class Case View
	AddCaseView(String header) {
		super(header);
	}

	// Create a case view and scene for "Add Case"
	@Override
	Stage buildView() {
		updateButton.setText("Add Case");
		titleTextField.setText(null);
		caseTypeTextField.setText(null);
		caseDatePicker.setValue(LocalDate.now());
		caseNumberTextField.setText(null);
		categoryTextField.setText(null);
		caseLinkTextField.setText(null);
		caseNotesTextArea.setText(null);
		Scene addCaseScene = new Scene(updateCaseGridPane, CASE_WIDTH, CASE_HEIGHT);
		stage.setScene(addCaseScene);

		// Lambda event handler that handles the clear button
		// Clears all the GUI components
		clearButton.setOnAction(clear -> {
			titleTextField.clear();
			caseTypeTextField.clear();
			caseDatePicker.setValue(null);
			caseNumberTextField.clear();
			categoryTextField.clear();
			caseLinkTextField.clear();
			caseNotesTextArea.clear();
		});

		// Lambda event handler that closes the Case View dialogue
		closeButton.setOnAction(close -> {
			stage.close();
		});

		return stage;
	}

}
