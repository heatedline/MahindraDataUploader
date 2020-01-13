package com.finlabs.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.detectlanguage.DetectLanguage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finlabs.dto.DistrictDTO;
import com.finlabs.dto.StateDTO;

public class DistrictUploader {
	
	
	public static void uploadDistrictData(String filePath) {
		HttpURLConnection getStateByStateNameConn = null;
		DetectLanguage.apiKey = "02f20478454ab563eac7a65c5295ed09"; //1000 requests per day, 1 MB per day, Free, Language Detection API
		try {
			Workbook workbook = WorkbookFactory.create(new File(filePath));
			System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets: ");
			Sheet sheet = workbook.getSheetAt(1);
			
			Row row = null;
			StateDTO stateDTO = null;
			DistrictDTO districtDTO = null;
			StringBuilder response = null;
			String cookieName = "";
			String cookieValue = "";
			
			Map<String, String> cookieMap = MahindraUtils.getCookieMap();
			
			for(Map.Entry<String, String> cookieMapEntry : cookieMap.entrySet()) {
				cookieName = cookieMapEntry.getKey();
				cookieValue = cookieMapEntry.getValue();
			}
			
			for (int i = 0; i < sheet.getLastRowNum(); i++) {
				response = new StringBuilder();
				row = sheet.getRow(i);
				String s = row.getCell(0).getStringCellValue();
				
				if(!s.isEmpty()) {
					String stateName = URLEncoder.encode(s, "UTF-8").replace("+", "%20");
					
					URL getStateByStateNameURL = new URL(Constants.getStateByStateNameURL + stateName);
					getStateByStateNameConn = (HttpURLConnection) getStateByStateNameURL.openConnection();
					getStateByStateNameConn.setReadTimeout(Constants.TIMEOUT);
					getStateByStateNameConn.setConnectTimeout(Constants.TIMEOUT);
					getStateByStateNameConn.setRequestMethod("GET");
					getStateByStateNameConn.setRequestProperty("Content-Type", "application/json");
					getStateByStateNameConn.setRequestProperty("Accept", "application/json");
					getStateByStateNameConn.setRequestProperty("Cookie", cookieName + "=" + cookieValue);
					getStateByStateNameConn.setDoInput(true);
					getStateByStateNameConn.setDoOutput(true);
					
					int responseCode = getStateByStateNameConn.getResponseCode();
					
					if (responseCode == HttpURLConnection.HTTP_OK) {
						String line;
						BufferedReader br = new BufferedReader(new InputStreamReader(getStateByStateNameConn.getInputStream()));
						while ((line = br.readLine()) != null) {
							response.append(line);
						}
					} else {
						response = new StringBuilder();
					}
					
					stateDTO = new ObjectMapper().readValue(response.toString(), StateDTO.class);
					
					Map<String, Object> postDataMap = new HashMap<String, Object>();
					districtDTO = new DistrictDTO();
					
					int colNum = row.getLastCellNum();
					for (int j = 1; j < colNum; j += 2) {
						if(row.getCell(j) != null) {
							String englishValue = row.getCell(j).getStringCellValue();
							String hindiValue = row.getCell(j+1).getStringCellValue();
							if(!englishValue.isEmpty() && !hindiValue.isEmpty()) {
								districtDTO.setDistrictName(englishValue);
								districtDTO.setDistrictNameHindi(hindiValue);
							}
						}
					}
					
					postDataMap.put("state", stateDTO.getId());
					postDataMap.put("districtName", districtDTO.getDistrictName());
					postDataMap.put("districtNameHindi", districtDTO.getDistrictNameHindi());
					MahindraUtils.performPostCallJson(Constants.saveDistrictURL, postDataMap);
				}
			}
			
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getStateByStateNameConn.disconnect();
		}
		
	}

}
