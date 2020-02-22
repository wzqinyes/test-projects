package wu.util;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    public static final String OFFICE_EXCEL_XLS = "xls";
    public static final String OFFICE_EXCEL_XLSX = "xlsx";

    public static Sheet getSheet(Workbook workbook, int nubmer) {
        int numberOfSheets = workbook.getNumberOfSheets();
        if (nubmer <= numberOfSheets) {
            return workbook.getSheetAt(nubmer - 1);
        }
        throw new RuntimeException("下标大于页数,总页数" + numberOfSheets + "，下标" + nubmer);
    }

    public static List<String> getColumnData(Sheet sheet, int columnIndex) {
        int num = sheet.getLastRowNum();
        List<String> result = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            Row row = sheet.getRow(i+1);
            if (row == null) {
                continue;
            }
            Cell cell = row.getCell(columnIndex);
            if (cell == null) {
                continue;
            }
            result.add(i, cell.getStringCellValue());
        }
        return result;
    }



    /**
     * 读取指定Sheet也的内容
     * @param filepath filepath 文件全路径
     * @param sheetNo sheet序号,从0开始,如果读取全文sheetNo设置null
     */
    public static String readExcel(String filepath, Integer sheetNo) throws EncryptedDocumentException,
        InvalidFormatException, IOException {
        StringBuilder sb = new StringBuilder();
        Workbook workbook = getWorkbook(filepath);
        if (workbook != null) {
            if (sheetNo == null) {
                int numberOfSheets = workbook.getNumberOfSheets();
                for (int i = 0; i < numberOfSheets; i++) {
                    Sheet sheet = workbook.getSheetAt(i);
                    if (sheet == null) {
                        continue;
                    }
                    sb.append(readExcelSheet(sheet));
                }
            } else {
                Sheet sheet = workbook.getSheetAt(sheetNo);
                if (sheet != null) {
                    sb.append(readExcelSheet(sheet));
                }
            }
        }
        return sb.toString();
    }

    /**
     * 根据文件路径获取Workbook对象
     * @param filepath 文件全路径
     */
    public static Workbook getWorkbook(String filepath)
        throws EncryptedDocumentException, InvalidFormatException, IOException {
        InputStream is = null;
        Workbook wb = null;
        if (StringUtils.isEmpty(filepath)) {
            throw new IllegalArgumentException("文件路径不能为空");
        } else {
            String suffiex = getSuffiex(filepath);
            if (StringUtils.isEmpty(suffiex)) {
                throw new IllegalArgumentException("文件后缀不能为空");
            }
            if (OFFICE_EXCEL_XLS.equals(suffiex) || OFFICE_EXCEL_XLSX.equals(suffiex)) {
                try {
                    is = new FileInputStream(filepath);
                    wb = WorkbookFactory.create(is);
                } finally {
                    if (is != null) {
                        is.close();
                    }
                }
            } else {
                throw new IllegalArgumentException("该文件非Excel文件");
            }
        }
        return wb;
    }


    /**
     * 获取后缀
     * @param filepath filepath 文件全路径
     */
    private static String getSuffiex(String filepath) {
        if (StringUtils.isEmpty(filepath)) {
            return "";
        }
        int index = filepath.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        return filepath.substring(index + 1, filepath.length());
    }

    public static String readExcelSheet(Sheet sheet) {
        StringBuilder sb = new StringBuilder();

        if(sheet != null){
            int rowNos = sheet.getLastRowNum();// 得到excel的总记录条数（+1，因为从0开始算）
            for (int i = 0; i <= rowNos; i++) {// 遍历行
                Row row = sheet.getRow(i);
                if(row != null){
                    int columNos = row.getLastCellNum();// 表头总共的列数
                    for (int j = 0; j < columNos; j++) {
                        Cell cell = row.getCell(j);
                        if(cell != null){
                            cell.setCellType(CellType.STRING);
                            sb.append(cell.getStringCellValue()).append("\t");
                        }
                    }
                }
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    public static void copyWorkbook(Workbook workbook, String sourceFile) {

        String suffiex = getSuffiex(sourceFile);

        String desFileName;
        if (suffiex.equals("")){
            desFileName = sourceFile + "-New";
        } else {
            desFileName = sourceFile.substring(0, sourceFile.length() - suffiex.length() -1) + "-New." + suffiex;
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(desFileName);
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
