package com.kuryshee.safehome.requestdataretriever;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;

import org.apache.commons.io.IOUtils;

/**
 * Implements extraction of data from HTTP GET response input stream.
 * @author Ekaterina Kurysheva
 *
 */
public class GetDataRetriever {
	
	private String charset = "UTF-8";
	
	/**
	 * Extracts short data in string format.
	 * @param input
	 * @return string.
	 */
	public String getStringData(InputStream input) {
		String result = "";
		
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(input, charset))){     
        
			int c;
			StringBuilder strb = new StringBuilder();
			
			while((c = reader.read()) != -1) {
				strb.append((char) c);
			}
			result = strb.toString();	
			
        } catch (IOException e) {
			Logger.getLogger(GetDataRetriever.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		
		return result;
	}
	
	/**
	 * Extracts JSON array from input stream.
	 * @param input
	 * @return {@link JsonArray}
	 */
	public JsonArray getJSONarray(InputStream input) {
		JsonReader jsonReader = Json.createReader(input);
		JsonArray array = jsonReader.readArray();
		jsonReader.close();
		
		return array;
	}
	
	/**
	 * Extracts data from input stream as byte array.
	 * @param input
	 * @return byte array or null.
	 */
	public byte[] getByteArray(InputStream input) {
		byte[] bytes = null;
		
		try {
			bytes = IOUtils.toByteArray(input);
		} catch (IOException e) {
			Logger.getLogger(GetDataRetriever.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		
		return bytes;
	}
}
