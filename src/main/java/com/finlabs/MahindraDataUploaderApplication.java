package com.finlabs;

import com.finlabs.utils.Constants;
import com.finlabs.utils.DistrictUploader;
import com.finlabs.utils.StateUploader;

public class MahindraDataUploaderApplication {

	public static void main(String[] args) {
		
		(new Thread(() -> {
			try {
				//StateUploader.uploadStateData();
				DistrictUploader.uploadDistrictData(Constants.stateMasterDataFilePath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		})).start();
		
	}
	
}
