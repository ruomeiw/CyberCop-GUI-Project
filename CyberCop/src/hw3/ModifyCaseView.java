// Name: Ruomei(Della) Wang
// AndrewID: ruomeiw

package hw3;

import java.time.LocalDate;

import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

public class ModifyCaseView extends CaseView {

	// Extends super class Case View
	ModifyCaseView(String header) {
		super(header);
	}

	// Create a case view and scene for "Add Case"
	@Override
	Stage buildView() {
		updateButton.setText("Modify Case");
		titleTextField.setText(CyberCop.currentCase.getCaseTitle());
		caseTypeTextField.setText(CyberCop.currentCase.getCaseType());
		caseDatePicker = new DatePicker(LocalDate.now());
		caseNumberTextField.setText(CyberCop.currentCase.getCaseNumber());
		categoryTextField.setText(CyberCop.currentCase.getCaseCategory());
		caseLinkTextField.setText(CyberCop.currentCase.getCaseLink());
		caseNotesTextArea.setText(CyberCop.currentCase.getCaseNotes());
		Scene modifyCaseScene = new Scene(updateCaseGridPane, CASE_WIDTH, CASE_HEIGHT);
		stage.setScene(modifyCaseScene);

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
