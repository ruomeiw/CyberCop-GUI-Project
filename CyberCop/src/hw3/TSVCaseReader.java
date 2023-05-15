// Name: Ruomei(Della) Wang
// AndrewID: ruomeiw

package hw3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TSVCaseReader extends CaseReader {

	// Extends super class CaseReader
	TSVCaseReader(String filename) {
		super(filename);
	}

	// Use Scanner, and StringBuilder to read the tsv file
	@Override
	List<Case> readCases() {
		List<Case> caseList = new ArrayList<>();
		StringBuilder fileContent = new StringBuilder();
		String[] fileData;
		Scanner input = null;
		try {
			File tsvFile = new File(filename);
			input = new Scanner(tsvFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (input.hasNext()) {
			fileContent.append(input.nextLine() + "\n");
		}

		fileData = fileContent.toString().split("\n");

		input.close();

		// Loop through fileData to extract each variable
		try {
			int rejectedCaseCounter = 0;		// To keep track of how many rejected cases there are
			for (int i = 0; i < fileData.length; i++) {
				String caseDate = fileData[i].split("\t")[0];
				String caseTitle = fileData[i].split("\t")[1];
				String caseType = fileData[i].split("\t")[2];
				String caseNumber = fileData[i].split("\t")[3];
				String caseLink = fileData[i].split("\t")[4];
				String caseCategory = fileData[i].split("\t")[5];
				String caseNotes = fileData[i].split("\t")[6];

				// Create new caseList element
				// Use a IF statement to see if the case has missing data in first four columns
				// If there is data missing, do not add this case to the caseList and increment the rejectedCaseCounter
				if (!caseDate.isBlank() && !caseTitle.isBlank() && !caseType.isBlank() && !caseNumber.isBlank())
					caseList.add(new Case(caseDate.trim(), caseTitle.trim(), caseType.trim(), caseNumber.trim(), caseLink, caseCategory, caseNotes));
				else 
					rejectedCaseCounter = rejectedCaseCounter + 1;
			}

			// If there are cases that are rejected, show the count in the DataException alert.
			if (rejectedCaseCounter != 0) {
				String message = String.valueOf(rejectedCaseCounter).concat(" rejected cases. \nThe file must have cases with \ntab seperated date, title, type, and case number!");
				throw new DataException(message);
			}
		}
		catch (DataException d) {
			System.out.println("The file must have cases with tab seperated date, title, type, and case number!");
		}
		return caseList;
	}
}
