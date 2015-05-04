package com.enablix.refdata.xls.loader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.services.util.DatastoreUtil;
import com.enablix.util.data.loader.DataFileProcessor;

public class XLSRefdataProcessor implements DataFileProcessor {

	@Autowired
	private ContentCrudService crud;
	
	@Override
	public void process(File dataFile) {

		XSSFWorkbook workbook = null;
		
		try {
			
			if (dataFile.isHidden()) {
				return;
			}
			
			workbook = new XSSFWorkbook(dataFile);
			
			for (XSSFSheet sheet : workbook) {
				
				String collectionName = getCollectionName(dataFile, sheet);
				System.out.println("Collection name: " + collectionName);
				
				List<String> contentItems = new ArrayList<>();
				
				boolean headerRow = true;
				
				for (Row row : sheet) {
					
					if (headerRow) {
						
						for (Cell cell : row) {
							contentItems.add(cell.getStringCellValue());
						}
						System.out.println(contentItems);
						headerRow = false;
						
					} else {
						
						Map<String, Object> record = new HashMap<>();
						
						int i = 0;
						for (Cell cell : row) {
							
							if (i >= contentItems.size()) {
								break;
							}
							
							record.put(contentItems.get(i), getCellValueAsString(cell));
							i++;
						}
				
						String recordIdentity = getRecordIdentity(record);
						
						if (!StringUtil.isEmpty(recordIdentity)) {
							crud.upsert(collectionName, recordIdentity, record);
						}
						
					}
				}
			}
			
		} catch (InvalidFormatException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {

			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					//e.printStackTrace();
				}
			}
		}
	}
	
	private String getRecordIdentity(Map<String, Object> record) {
		return (String) record.get(ContentDataConstants.IDENTITY_KEY);
	}
	
	private String getTemplateIdFromFile(File dataFile) {
		String fileName = dataFile.getName();
		int indxOfDot = fileName.indexOf('.');
		return fileName.substring(0, indxOfDot);
	}
	
	private String getRefdataContainerId(XSSFSheet sheet) {
		return sheet.getSheetName();
	}
	
	private String getCollectionName(File dataFile, XSSFSheet sheet) {
		return DatastoreUtil.getCollectionName(getTemplateIdFromFile(dataFile), getRefdataContainerId(sheet));
	}
	
	private String getCellValueAsString(Cell cell) {
		
		String cellValue = "";

		switch (cell.getCellType()) {
	        
			case Cell.CELL_TYPE_STRING:
	            cellValue = cell.getStringCellValue();
	            break;
	            
	        case Cell.CELL_TYPE_NUMERIC:
	            cellValue = String.valueOf(cell.getNumericCellValue());
	            break;
	            
	        case Cell.CELL_TYPE_BOOLEAN:
	            cellValue = String.valueOf(cell.getBooleanCellValue());
	            break;
	            
	        default :
     
        }
		
		return cellValue;

	}
	
	public static void main(String[] args) {
		String fileName = "C:\\Dikshit\\Enablix\\amlSalesTemplate.xlsx";
		File file = new File(fileName);
		XLSRefdataProcessor processor = new XLSRefdataProcessor();
		processor.process(file);
	}
}
