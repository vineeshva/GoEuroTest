package com.test.goeuro;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;





import au.com.bytecode.opencsv.CSVWriter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GetPlaceInfo {

	public static void main(String[] args) {
		String placeName = args[0];
		
		try {
			URL url = new URL("http://api.goeuro.com/api/v2/position/suggest/en/"+placeName);
			
			
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            
            if (conn.getResponseCode() != 200) 
            {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
 
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String apiOutputStr = br.readLine();
            conn.disconnect();
           
            Gson gson = new Gson();
            
            Type collectionType = new TypeToken<List<PlaceInfoVO>>(){}.getType();
            List<PlaceInfoVO> placeInfo_Lst = (List<PlaceInfoVO>) new Gson().fromJson( apiOutputStr , collectionType);
            
           
            String csvFilePath = "output.csv";
			CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFilePath));
			 List<String[]> csvOutData = new ArrayList<String[]>();
            for (PlaceInfoVO placeInfoVO : placeInfo_Lst) {
            	csvOutData.add(new String[] {placeInfoVO.get_id(), placeInfoVO.getName(),placeInfoVO.getType(),placeInfoVO.getGeo_position().getLatitude(),placeInfoVO.getGeo_position().getLongitude()});
			}
           
            csvWriter.writeAll(csvOutData);
            csvWriter.close();
			 
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}catch (IOException e) {
            e.printStackTrace();
        } 
	}
	

}
