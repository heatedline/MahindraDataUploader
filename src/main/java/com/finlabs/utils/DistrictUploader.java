package com.finlabs.utils;

import java.io.File;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class DistrictUploader {
	
	
	public static void getDistrictsSet(String filePath) throws Exception {
		
		Workbook workbook = WorkbookFactory.create(new File(filePath));
		System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets: ");
		Sheet sheet = workbook.getSheetAt(1);
		
		
	}
	

}
