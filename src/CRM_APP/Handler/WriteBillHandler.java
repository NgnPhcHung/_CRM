package CRM_APP.Handler;

import CRM_APP.Controller.Bill.BillController;
import CRM_APP.Model.Bill;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;


public class WriteBillHandler {
    NotificationHandler notification;

    private static HSSFCellStyle createStyleForTitle(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }
    public void createBill(File f){
        HSSFWorkbook workbook = new HSSFWorkbook();
        notification = new NotificationHandler();
        HSSFSheet sheet = workbook.createSheet("Bill sheet");

        List<HashMap> list = BillController.getBillList();

        int rownum = 0;
        Cell cell;
        Row row;
        //
        HSSFCellStyle style = createStyleForTitle(workbook);

        row = sheet.createRow(rownum);

        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Bill ID");
        cell.setCellStyle(style);

        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Customer");
        cell.setCellStyle(style);

        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Project");
        cell.setCellStyle(style);

        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Bill Date");
        cell.setCellStyle(style);

        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("Amount");
        cell.setCellStyle(style);

        cell = row.createCell(5, CellType.STRING);
        cell.setCellValue("Percent");
        cell.setCellStyle(style);

        cell = row.createCell(6, CellType.STRING);
        cell.setCellValue("Total Amount");
        cell.setCellStyle(style);

        cell = row.createCell(7, CellType.STRING);
        cell.setCellValue("Status");
        cell.setCellStyle(style);

        cell = row.createCell(8, CellType.STRING);
        cell.setCellValue("Employee");
        cell.setCellStyle(style);

        // Data
        for (HashMap item : list) {
            rownum++;
            row = sheet.createRow(rownum);

            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(item.get("Bill ID").toString());

            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(item.get("Customer").toString());

            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue(item.get("Project").toString());

            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue(item.get("Bill Date").toString());

            cell = row.createCell(4, CellType.STRING);
            cell.setCellValue(item.get("Amount").toString());

            cell = row.createCell(5, CellType.STRING);
            cell.setCellValue(item.get("Percent").toString());

            cell = row.createCell(6, CellType.STRING);
            cell.setCellValue(item.get("Total Amount").toString());

            cell = row.createCell(7, CellType.STRING);
            cell.setCellValue(item.get("Status").toString());

            cell = row.createCell(8, CellType.STRING);
            cell.setCellValue(item.get("Employee").toString());


        }
        File file = new File(f+"\\"+generateBill()+".xls");
        file.getParentFile().mkdirs();

        FileOutputStream outFile = null;

        try {
            outFile = new FileOutputStream(file);
            workbook.write(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            notification.popup(notification.error, "File not found");
        } catch (IOException e) {
            notification.popup(notification.error, "File not input error");
            e.printStackTrace();
        }
        notification.popup(notification.success, file.getAbsolutePath());
    }
    private String generateBill(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String logTime = dtf.format(now);
        String id = logTime;
        return id = id.replaceAll("[^\\d.]", "");
    }
}
