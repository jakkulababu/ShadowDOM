package files.xlsx;

import files.csv.CSVVendorItemUtil;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * Utils to work with excels
 */
public class XLSXVendorItemUtils {
    private static final Logger log = LoggerFactory.getLogger(XLSXVendorItemUtils.class);

    private XLSXVendorItemUtils() {
    }


    /**
     * Set the id for a given workbook
     *
     * @param filePath   the path of the template
     * @param id         the id of the item
     * @param partNumber the partNumber to set
     * @return the path of teh newly create book
     */
    public static String setId(String filePath, String id, String partNumber) {
        File fd;
        try {
            fd = File.createTempFile("test", ".xlsx");
            OutputStream out = Files.newOutputStream(fd.toPath());
            File template = new File(CSVVendorItemUtil.class.getClassLoader().getResource(filePath).getFile());
            Workbook book = WorkbookFactory.create(template);
            Sheet sheet = book.getSheet("Entities");
            Row row = sheet.getRow(2);
            Cell cell = row.getCell(3);
            cell.setCellValue(id);

            cell = row.getCell(6);
            cell.setCellValue(partNumber);
            book.write(out);
            book.close();
        } catch (IOException e) {
            log.error("File problems ", e);
            return null;
        }
        return FilenameUtils.normalize(fd.getAbsolutePath());
    }


}
