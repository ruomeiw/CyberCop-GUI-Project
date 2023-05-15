// Name: Ruomei(Della) Wang
// AndrewID: ruomeiw

package hw3;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class CCModel {
	ObservableList<Case> caseList = FXCollections.observableArrayList(); 			//a list of case objects
	ObservableMap<String, Case> caseMap = FXCollections.observableHashMap();		//map with caseNumber as key and Case as value
	ObservableMap<String, List<Case>> yearMap = FXCollections.observableHashMap();	//map with each year as a key and a list of all cases dated in that year as value. 
	ObservableList<String> yearList = FXCollections.observableArrayList();			//list of years to populate the yearComboBox in ccView

	/** readCases() performs the following functions:
	 * It creates an instance of CaseReaderFactory, 
	 * invokes its createReader() method by passing the filename to it, 
	 * and invokes the caseReader's readCases() method. 
	 * The caseList returned by readCases() is sorted 
	 * in the order of caseDate for initial display in caseTableView. 
	 * Finally, it loads caseMap with cases in caseList. 
	 * This caseMap will be used to make sure that no duplicate cases are added to data
	 * @param filename
	 */
	void readCases(String filename) {
		CaseReaderFactory caseReader = new CaseReaderFactory();						// Create a new instance of CaseReaderFactory
		List<Case> caseListCopy = caseReader.createReader(filename).readCases();    // Create a new ArrayList caseListCopy to store the cases read from the file
		// because CaseReader's readCases() method returns an ArrayList

		caseList.clear();
		for (Case c1 : caseListCopy) {			// Loop through the caseListCopy and add each case to ObservableList caseList
			caseList.add(c1);
		}

		caseMap.clear();
		for (Case c2 : caseList) {				// Loop through the caseList and add each case to caseMap with case numbers being the key
			caseMap.put(c2.getCaseNumber(), c2);
		}

		Collections.sort(caseList);				// Sort the caseList to make sure the most recent case comes first
	}

	boolean writeCases(String filename) {
		boolean isFileSaved = false;	        // Create a boolean variable to show if the file is saved
		int counter = 0;			    		// Create a counter to see if the whole caseList was traversed through
		
		// Use a FileWriter and BufferedWriter to write all the cases in caseList to the new file
		try {
			FileWriter writer = new FileWriter(filename);
			BufferedWriter bw = new BufferedWriter(writer);
			
			// Separate each element with a tab and a space to prevent null values
			for (Case c : caseList) {
				bw.write(c.getCaseDate() + "\t " + c.getCaseTitle() + "\t " + c.getCaseType() + "\t " + c.getCaseNumber() + "\t " 
						  + c.getCaseLink() + "\t " + c.getCaseCategory() + "\t " + c.getCaseNotes() + "\n");
				
				counter++;						// Increment the counter as the loop goes on
			}
			bw.flush(); 						// Flush the BufferedReader
			
			// Set isFileSaved to true and close the BufferedReader once the whole list is traversed
			if (counter == caseList.size()) {
				isFileSaved = true;
				bw.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isFileSaved;
	}

	/** buildYearMapAndList() performs the following functions:
	 * 1. It builds yearMap that will be used for analysis purposes in Cyber Cop 3.0
	 * 2. It creates yearList which will be used to populate yearComboBox in ccView
	 * Note that yearList can be created simply by using the keySet of yearMap.
	 */
	void buildYearMapAndList() {
		//write your code here
					    		
		String caseYear;												
		for (Case c3 : caseList) {										// Loops through the caseList to get the case date and extract the year
			String caseDate = c3.getCaseDate();							// Also evaluates if the yearList already has the year, then add the year to yearList
			caseYear = caseDate.substring(0, caseDate.indexOf("-"));
			if (!yearList.contains(caseYear))
				yearList.add(caseYear);
		}

		for (String year : yearList) {									// Loop through each year in the yearList and caseList to find which cases are in each year
			List<Case> caseOfYear = new ArrayList<>();					// Create a new List of Case that stores cases of a certain year
			for (int i = 0; i < caseList.size(); i++) {					
				int x = caseList.get(i).getCaseDate().indexOf("-");
				if (caseList.get(i).getCaseDate().substring(0, x).equals(year)) {
					caseOfYear.add(caseList.get(i));
				} else {
					yearMap.put(year, caseOfYear);						// Store the cases that meet the year requirement into the caseOfYear list 											// And then add the year and caseOfYear into yearMap
				}
			}
		}
		Collections.sort(yearList);										// Sort the yearList to ensure the list goes in ascending order
	}

	/**searchCases() takes search criteria and 
	 * iterates through the caseList to find the matching cases. 
	 * It returns a list of matching cases.
	 */
	List<Case> searchCases(String title, String caseType, String year, String caseNumber) {	
		ObservableList<Case> searchResult = FXCollections.observableArrayList();   			// Create a new ObservableList to store the searchResults
		if (title == null && caseType == null && year == null && caseNumber == null)		// If all search requirements are null, then show the entire caseList
			return caseList;
		else {																				// Otherwise, evaluates if each variable is null or equals to the variable into the caseList
			for (Case c5 : caseList) {														// If search keywords meet the requirement, add it to the searchResult ObservableList
				if ((title == null || c5.getCaseTitle().toLowerCase().contains(title.toLowerCase())) && (caseType == null || c5.getCaseType().toLowerCase().contains(caseType.toLowerCase())) 
						&& (year == null || c5.getCaseDate().contains(year)) && (caseNumber == null || c5.getCaseNumber().contains(caseNumber)))
					searchResult.add(c5);
			}
			return searchResult;
		}
	}

}
