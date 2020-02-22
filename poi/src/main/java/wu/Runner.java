package wu;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import wu.util.ExcelUtil;

import java.io.IOException;
import java.util.List;

public class Runner {

    public static void main(String[] args) throws IOException, InvalidFormatException {

        String templateFile = args[0];
        String sourceFile = args[1];

        Workbook workbook1 = ExcelUtil.getWorkbook(templateFile);
        Sheet sheet = ExcelUtil.getSheet(workbook1, 1);
        List<String> usefulData = ExcelUtil.getColumnData(sheet, 1);
//        System.out.println(usefulData.contains("2016YA0049"));
        workbook1.close();

        Workbook workbook2 = ExcelUtil.getWorkbook(sourceFile);

        Sheet sheet2 = ExcelUtil.getSheet(workbook2, 1);
        int rowNum = sheet2.getLastRowNum();
        for (int i = 1; i <= rowNum; i++) {
            Row sheet2Row = sheet2.getRow(i);
            if (sheet2Row == null) {
                continue;
            }
            Cell cell = sheet2Row.getCell(5);
            if (cell == null) {
                continue;
            }
            String value = cell.getStringCellValue();
            if (!usefulData.contains(value)) {
                sheet2.removeRow(sheet2Row);
            }
//            System.out.println(value);
        }

        ExcelUtil.copyWorkbook(workbook2, sourceFile);

        workbook2.close();

//        String s = ExcelUtil.readExcel("F:\\2016年.xlsx", 0);
//        String s1 = ExcelUtil.readExcel("F:\\驯养系列许可证列表_20200219001933.xls", 0);
//
//        System.out.println(s1);

    }
}
