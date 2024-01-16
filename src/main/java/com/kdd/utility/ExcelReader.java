package com.kdd.utility;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;


import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public class ExcelReader {
	private static HSSFWorkbook excelWorkbook;
	private static final Logger log = Logger.getLogger(ExcelReader.class.getName());
	private static final String RUN_MODE_YES = "YES";

	//Baban Added
	 private static org.apache.poi.ss.usermodel.Cell Cell;
	 private static DataFormatter fmt= new DataFormatter();

	public synchronized static void setExcelFile(String sheetPath) {
		try{
			System.out.println("Excel Path inside setExcelFile :"+sheetPath);
			
			FileInputStream excelFile = new FileInputStream(sheetPath);
			
			excelWorkbook = new HSSFWorkbook(excelFile);
		} catch(Exception exp){
			log.error("Exception occured in setExcelFile: ", exp);
		}		
	}

	public static synchronized int getNumberOfRows(String sheetName) {
		
		HSSFSheet excelSheet = excelWorkbook.getSheet(sheetName);
		
		//int numberOfRows = excelSheet.getPhysicalNumberOfRows();
		
		int numberOfRows=excelSheet.getLastRowNum()+1;
		
		System.out.println("Excel numberOfRows :"+numberOfRows);
		
		log.debug("Number Of Rows: "+numberOfRows);
		return numberOfRows;
	}

	public synchronized String getCellData(int rowNumb, int colNumb, String sheetName) throws Exception{
		try{
			
			/*
			 * XSSFSheet excelSheet = excelWorkbook.getSheet(sheetName); XSSFCell cell =
			 * excelSheet.getRow(rowNumb).getCell(colNumb);
			 * //log.debug("Getting cell data."); if(cell.getCellType() ==
			 * XSSFCell.CELL_TYPE_NUMERIC) { cell.setCellType(XSSFCell.CELL_TYPE_STRING); }
			 * String cellData = cell.getStringCellValue(); return cellData;
			 */
			
			DataFormatter formatter=new DataFormatter();
			
			HSSFSheet excelSheet = excelWorkbook.getSheet(sheetName);
			Cell = excelSheet.getRow(rowNumb).getCell(colNumb);
	       	
	       // String CellData = Cell.getStringCellValue();
	       // return CellData;
	       	
	       	if(Cell.getCellType()==CellType.STRING)
	       	{
	       		String cellData=formatter.formatCellValue(Cell);
	       		System.out.println("getCellData :"+cellData);
	       		return cellData;
	       	}
	       	else if(Cell.getCellType()==CellType.NUMERIC)
	       	{
	       		FormulaEvaluator evaluator=excelWorkbook.getCreationHelper().createFormulaEvaluator();
	       		String cellData=formatter.formatCellValue(Cell,evaluator);
	       		System.out.println("getCellData :"+cellData);
	       		return cellData;
	       	}
	       	else if(Cell.getCellType()==CellType.FORMULA)
	       	{
	       		FormulaEvaluator evaluator=excelWorkbook.getCreationHelper().createFormulaEvaluator();
	       		String cellData=formatter.formatCellValue(Cell,evaluator);
	       		System.out.println("getCellData :"+cellData);
	       		return cellData;
	       	}
			
			/*
			 * else if(Cell.getCellType()==CellType.BLANK)
			 * 
			 * { return ""; }
			 */
			  else 
			  {
				  System.out.println("getCellData :"+String.valueOf(Cell.getBooleanCellValue()));
				  return String.valueOf(Cell.getBooleanCellValue());
			  }
			 
						
		}
		catch(Exception exp){
			return "";
		}
	}

	public static synchronized void clearColumnData(String sheetName, int colNumb, String excelFilePath) {
		int rowCount = getNumberOfRows(sheetName);
		HSSFRow row;
		HSSFSheet excelSheet = excelWorkbook.getSheet(sheetName);
		for(int i=1; i< rowCount; i++) {
			HSSFCell cell = excelSheet.getRow(i).getCell(colNumb);
			if(cell==null){
				row = excelSheet.getRow(i);
				cell = row.createCell(colNumb);
			}
			cell.setCellValue("");			
		}
		log.debug("Clearing column "+colNumb+" of Sheet: "+sheetName);
		writingDataIntoFile(excelFilePath);
	}

	public synchronized void setCellData(String result, int rowNumb, int colNumb, String excelFilePath, String sheetName) {	
		HSSFSheet excelSheet = excelWorkbook.getSheet(sheetName);
		HSSFRow row = excelSheet.getRow(rowNumb);
		HSSFCell cell = row.getCell(colNumb);
		log.debug("Setting results into the excel sheet.");
		if(cell==null){
			cell = row.createCell(colNumb);
		}
		cell.setCellValue(result);
		log.debug("Setting value into cell["+rowNumb+"]["+colNumb+"]: "+result);
		writingDataIntoFile(excelFilePath);		
	}

	private static synchronized void writingDataIntoFile(String excelFilePath) {
		try{
			FileOutputStream fileOut = new FileOutputStream(excelFilePath);
			excelWorkbook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		}catch(Exception exp){
			log.error("Exception occured in setCellData: ",exp);
		}
	}

	public synchronized Map<Integer, String> getTestCasesToRun(String sheetName, int runModeColumn, int testCaseColumn) {
		Map<Integer, String> testListMap = new HashMap<Integer, String>();
		System.out.println("sheetName runModeColumn testCaseColumn :"+sheetName+"_"+runModeColumn+"_"+testCaseColumn);
		try {
			int rowCount = getNumberOfRows(sheetName);
			System.out.println("Excel rowCount :"+rowCount);
			String testCase;
			for(int i=1; i< rowCount; i++) {
				testCase = getTestCaseToRun(i, runModeColumn, testCaseColumn, sheetName);
				if(testCase != null) {
					testListMap.put(i,testCase);
				}
			}
		}catch (Exception e) {
			log.error("Exeception Occured while adding data to List:\n", e);
		}
		return testListMap;
	}

	private synchronized String getTestCaseToRun(int row, int runModeColumn, int testCaseColumn, String sheetName) {
		String testCaseName = null;
		try{
			if(getCellData(row, runModeColumn, sheetName).equalsIgnoreCase(RUN_MODE_YES)){
				testCaseName = getCellData(row, testCaseColumn, sheetName).trim();
				log.debug("Test Case to Run: "+testCaseName);
			} 
		} catch(Exception exp){
			log.error("Exception occured in getTestCaseRow: ", exp);
		}
		return testCaseName;
	}
}
