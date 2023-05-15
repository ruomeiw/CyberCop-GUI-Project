// Name: Ruomei(Della) Wang
// AndrewID: ruomeiw

package hw3;

public class CaseReaderFactory {
	CaseReader createReader(String filename) {				// Initialize a new CaseReader
		if (filename.endsWith("csv")) {						// Tells if the file is csv or tsv, then returns corresponding case reader.
			CaseReader csvCaseReader = new CSVCaseReader(filename);
			return csvCaseReader;
		} else {
			CaseReader tsvCaseReader = new TSVCaseReader(filename);
			return tsvCaseReader;
		}
	}
}
