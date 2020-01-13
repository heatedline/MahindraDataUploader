package com.finlabs.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class MahindraUtils {

	/**
	 * Method to get the Cookie from respective API
	 * @return Cookie Name and Cookie Value in HashMap format
	 */
	public static Map<String, String> getCookieMap() {
		Map<String, String> cookieMap = null;

		try {
			Connection.Response res = Jsoup.connect(Constants.tokenURL).data("username", Constants.username)
					.data("password", Constants.password).method(Connection.Method.POST).execute();

			cookieMap = res.cookies();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SSLException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cookieMap;
	}
	
	/**
	 * Method to create a HashMap containing the master data name and the HashMap of master data with English text as key and translated text as value.
	 * @param sheet The Sheet of the workbook (excel) containing the master data.
	 * @param masterDataName The name of the Master Data provided.
	 * @return HashMap as stated in the description of this method.
	 * @throws Exception
	 */
	public static Map<String, Map<String, String>> getDataMap(Sheet sheet, String masterDataName) throws Exception {
		Row row = null;
		Map<String, Map<String, String>> superMap = new HashMap<String, Map<String, String>>();
		Map<String, String> myMap = new HashMap<String, String>();
		
		for (int i = 0; i < sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i);
			String keyCell = row.getCell(0).getStringCellValue();
			
			int colNum = row.getLastCellNum();
			for (int j = 1; j < colNum; j++) {
				String value = row.getCell(j).getStringCellValue();
				if(!value.isEmpty()) {
					myMap.put(keyCell, value);
				}
			}
			superMap.put(masterDataName, myMap);
		}
		return superMap;
	}
	
	/**
	 * Universal method to perform 'POST' call using JSON Data.
	 * @param requestURL The API for the POST call
	 * @param postDataParamsArgs Parameters in the form of HashMap which is converted to JSON Object.
	 * @return The response object as a String.
	 */
	public static synchronized String performPostCallJson(String requestURL, Map<String, Object> postDataParamsArgs) {

		String cookieName = "", cookieValue = "";

		Map<String, String> cookieMap = MahindraUtils.getCookieMap();

		for (Map.Entry<String, String> cookieMapEntry : cookieMap.entrySet()) {
			cookieName = cookieMapEntry.getKey();
			cookieValue = cookieMapEntry.getValue();
		}
		
	    URL url;
		StringBuilder response = new StringBuilder();
		try {
			url = new URL(requestURL);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(Constants.TIMEOUT);
			conn.setConnectTimeout(Constants.TIMEOUT);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Cookie", cookieName + "=" + cookieValue);
			conn.setDoInput(true);
			conn.setDoOutput(true);

			JSONObject postDataParams = new JSONObject();

			for (Map.Entry<String, Object> entry : postDataParamsArgs.entrySet()) {
				postDataParams.put(entry.getKey(), entry.getValue());
			}

			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

			writer.write(postDataParams.toString());

			writer.flush();
			writer.close();
			os.close();
			int responseCode = conn.getResponseCode();
			System.out.println("responseCode: " + responseCode);
			if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
				String line;
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line = br.readLine()) != null) {
					response.append(line);
				}
			} else {
				response = new StringBuilder();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SSLException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response.toString().trim();
	}

}
