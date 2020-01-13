package com.finlabs.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class StateUploader {
	
	public static void uploadStateData() throws Exception {
		
		Set<Entry<String, String>> stateMasterSet = getStateSet(Constants.stateMasterDataFilePath);
		
		for(Entry<String, String> sortedMapEnSet : stateMasterSet){
			Map<String, Object> postDataMap = new HashMap<String, Object>();
			postDataMap.put("stateName", sortedMapEnSet.getKey());
			postDataMap.put("stateNameHindi", sortedMapEnSet.getValue());
			MahindraUtils.performPostCallJson(Constants.saveStateURL, postDataMap);
			System.out.println(sortedMapEnSet.getKey() + " ---- " + sortedMapEnSet.getValue() + " is uploaded.");
		}
		
	}
	
	public static Set<Entry<String, String>> getStateSet(String filePath) throws Exception {
		
		Workbook workbook = WorkbookFactory.create(new File(filePath));
		
		System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets: ");
		
		Sheet sheet = workbook.getSheetAt(0);
		
		Map<String, String> stateMasterMap = MahindraUtils.getDataMap(sheet, Constants.stateMasterDataName).get(Constants.stateMasterDataName);
		
		TreeMap<String, String> sortedStateMasterTreeMap = new TreeMap<>(stateMasterMap);
        Set<Entry<String, String>> stateMasterSet = sortedStateMasterTreeMap.entrySet();
        
        workbook.close();
		
		return stateMasterSet;
	}
}


