// Name: Ruomei(Della) Wang
// AndrewID: ruomeiw

package hw3;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Case implements Comparable<Case> {

	// Creates private StringProperty for each variables of Case.
	private StringProperty caseDate = new SimpleStringProperty();
	private StringProperty caseTitle = new SimpleStringProperty();
	private StringProperty caseType = new SimpleStringProperty();
	private StringProperty caseNumber = new SimpleStringProperty();
	private StringProperty caseLink = new SimpleStringProperty();
	private StringProperty caseCategory = new SimpleStringProperty();
	private StringProperty caseNotes = new SimpleStringProperty();

	// Default Constructor 
	Case() {
		caseDate.set("");
		caseTitle.set("");
		caseType.set("");
		caseNumber.set("");
		caseLink.set("");
		caseCategory.set("");
		caseNotes.set("");
	}

	// Parameterized Constructor
	Case(String caseDate, String caseTitle, String caseType, String caseNumber, String caseLink, String caseCategory, String caseNotes) {
		this.caseDate.set(caseDate);
		this.caseTitle.set(caseTitle);
		this.caseType.set(caseType);
		this.caseNumber.set(caseNumber);
		this.caseLink.set(caseLink);
		this.caseCategory.set(caseCategory);
		this.caseNotes.set(caseNotes);
	}

	// Create public setters and getters.
	public final String getCaseDate() { return caseDate.get(); }
	public final String getCaseTitle() { return caseTitle.get(); }
	public final String getCaseType() { return caseType.get(); }
	public final String getCaseNumber() { return caseNumber.get(); }
	public final String getCaseLink() { return caseLink.get(); }
	public final String getCaseCategory() { return caseCategory.get(); }
	public final String getCaseNotes() { return caseNotes.get(); }

	public final void setCaseDate(String caseDate) { this.caseDate.set(caseDate); }
	public final void setCaseTitle(String caseTitle) { this.caseTitle.set(caseTitle); }
	public final void setCaseType(String caseType) { this.caseType.set(caseType); }
	public final void setCaseNumber(String caseNumber) { this.caseNumber.set(caseNumber); }
	public final void setCaseLink(String caseLink) { this.caseLink.set(caseLink); }
	public final void setCaseCategory(String caseCategory) { this.caseCategory.set(caseCategory); }
	public final void setCaseNotes(String caseNotes) { this.caseNotes.set(caseNotes); }

	// Define the StringProperty of each variable.
	public final StringProperty caseDateProperty() { return caseDate; }
	public final StringProperty caseTitleProperty() { return caseTitle; }
	public final StringProperty caseDateTypeProperty() { return caseType; }
	public final StringProperty caseNumberProperty() { return caseNumber; }
	public final StringProperty caseLinkProperty() { return caseLink; }
	public final StringProperty caseCategoryProperty() { return caseCategory; }
	public final StringProperty caseNotesProperty() { return caseNotes; }

	// Overrides toString() method to return caseNumber.
	@Override
	public String toString() {
		return caseNumber.get();
	}

	// Sort on caseDate to start from the most recent case.
	@Override
	public int compareTo(Case c) {
		return c.getCaseDate().compareTo(caseDate.get());
	}
}
