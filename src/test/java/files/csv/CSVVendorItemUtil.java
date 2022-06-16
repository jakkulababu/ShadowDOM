package files.csv;

import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Utils to work with CSV
 */
public class CSVVendorItemUtil {
    private static final Logger log = LoggerFactory.getLogger(CSVVendorItemUtil.class);

    private CSVVendorItemUtil() {

    }

    /**
     * Set the part number for an csv arrow
     *
     * @param filePath   path to csv file
     * @param row        row to change
     * @param partNumber part number
     * @return path to the generated file
     */
    public static String setPartNumber(String filePath, int row, String partNumber) {
        File fd;
        try {
            fd = File.createTempFile("test", ".csv");
            BufferedWriter bf = Files.newBufferedWriter(fd.toPath());
            CSVPrinter csvPrinter = new CSVPrinter(bf, CSVFormat.DEFAULT);
            FileReader fr = new FileReader(CSVVendorItemUtil.class.getClassLoader().getResource(filePath).getFile());
            CSVParser parser = CSVParser.parse(fr, CSVFormat.DEFAULT);
            List<CSVRecord> records = parser.getRecords();
            for (int x = 0; x < records.size(); x++) {
                List<String> newRecord = new ArrayList<String>();
                for (int y = 0; y < records.get(x).size(); y++) {
                    String val = records.get(x).get(y);
                    if (x == row && y == 5) {
                        val = partNumber;
                    }
                    newRecord.add(val);
                }
                csvPrinter.printRecord(newRecord);
            }
            csvPrinter.flush();

        } catch (IOException e) {
            log.error("File problems ", e);
            return null;
        }
        return FilenameUtils.normalize(fd.getAbsolutePath());
    }
}
