package matrix.model;

import graph.model2.MyNode2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

public class ExcelExport
{
  public void createXMLExport(String[][] values, LinkedList<MyNode2> nodes, File saveLocation)
  {
    LinkedList<LinkedList<String>> res = createLegend(nodes);
    

    int counter = 1;
    for (String[] line : values)
    {
      for (String s : line) {
        ((LinkedList<String>)res.get(counter)).add(s);
      }
      counter++;
    }
    writeToFile(res, saveLocation);
  }
  
  private LinkedList<LinkedList<String>> createLegend(LinkedList<MyNode2> nodes)
  {
    LinkedList<LinkedList<String>> res = new LinkedList<LinkedList<String>>();
    
    LinkedList<String> legend = new LinkedList<String>();
    legend.add("");
    for (MyNode2 n : nodes) {
      legend.add(n.getName());
    }
    res.add(legend);
    for (int i = 1; i < legend.size(); i++)
    {
    	LinkedList<String> temp = new LinkedList<String>();
    	temp.add(legend.get(i));
    	
    	res.add(temp);
    }
    return res;
  }
  
  private void writeToFile(LinkedList<LinkedList<String>> values, File location)
  {
    HSSFWorkbook workbook = new HSSFWorkbook();
    HSSFSheet sheet = workbook.createSheet("Matrix Viz");
    
    CellStyle myStyle = workbook.createCellStyle();
    myStyle.setRotation((short)90);
    myStyle.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
    myStyle.setFillPattern((short)9);
    

    CellStyle style = workbook.createCellStyle();
    style.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
    style.setFillPattern((short)9);
    

    int rownum = 0;
    for (LinkedList<String> tmp : values)
    {
      Row row = sheet.createRow(rownum);
      
      int cellnum = 0;
      for (String s : tmp)
      {
        Cell cell = row.createCell(cellnum);
        if ((rownum == 0) && (cellnum > 0)) {
          cell.setCellStyle(myStyle);
        }
        cell.setCellValue(s);
        if (cellnum == 0) {
          cell.setCellStyle(style);
        }
        CellStyle style2 = cell.getCellStyle();
        style2.setWrapText(true);
        cell.setCellStyle(style2);
        

        cellnum++;
      }
      rownum++;
    }
    for (int i = 0; i < values.size(); i++) {
      sheet.autoSizeColumn(i);
    }
    try
    {
      if (location.exists()) {
        location.delete();
      }
      FileOutputStream out = new FileOutputStream(location);
      workbook.write(out);
      out.close();
      System.out.println("Excel written successfully..");
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}