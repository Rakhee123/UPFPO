package com.brightleaf.ruleservice.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class AddressFinder {
	private static Logger log = Logger.getLogger(AddressFinder.class);
	
	public AddressFinder() {
			  
	}

	public boolean findAddress(ParameterList params, List<ExtractedEntityForSingleAttribute> listExtraction) {
		boolean success = false;
		String regExStreet = "(?i)\\d+[ ](?:[A-Za-z0-9.-]+[ ]*)+(?:,|;|\\.|Avenue|Lane|Road|Boulevard|Drive|Street|Ave|Dr|Rd|Blvd|Ln|St)\\.?";
		String regExZip = "\\b\\d{5}(?:-\\d{4})?\\b";
		String regExCity = "(?:[A-Z][a-z.-]+[ ]?)+";
		String regExState = "(?i)(Alabama|Alaska|Arizona|Arkansas|California|Colorado|Connecticut|Delaware|Florida|Georgia|Hawaii|"
				+ "Idaho|Illinois|Indiana|Iowa|Kansas|Kentucky|Louisiana|Maine|Maryland|Massachusetts|Michigan|"
				+ "Minnesota|Mississippi|Missouri|Montana|Nebraska|Nevada|New[ ]Hampshire|New[ ]Jersey|New[ ]Mexico"
				+ "|New[ ]York|North[ ]Carolina|North[ ]Dakota|Ohio|Oklahoma|Oregon|Pennsylvania|Rhode[ ]Island"
				+ "|South[ ]Carolina|South[ ]Dakota|Tennessee|Texas|Utah|Vermont|Virginia|Washington|West[ ]Virginia"
				+ "|Wisconsin|Wyoming|AL|AK|AS|AZ|AR|CA|CO|CT|DE|DC|FM|FL|GA|GU|HI|ID|IL|IN|IA|KS|KY|LA|ME|MH|MD|MA|MI|MN|MS|MO|MT"
				+ "|NE|NV|NH|NJ|NM|NY|NC|ND|MP|OH|OK|OR|PW|PA|PR|RI|SC|SD|TN|TX|UT|VT|VI|VA|WA|WV|WI|WY)";
		String regExCountry = "(?i)(USA|United States of America|United States|US)";

		String text = params.getChunk();
		// Street
		Pattern p11 = Pattern.compile(regExStreet);
		Matcher m11 = p11.matcher(text);
		String street = "";
		if (m11.find()) {
			street = m11.group();
			text = text.substring(text.indexOf(street) + street.length());
		}

		// City
		p11 = Pattern.compile(regExCity);
		String city = "";
		boolean done = false;
		while (!done && text != "") {
			m11 = p11.matcher(text);
			if (m11.find()) {
				city = m11.group();
				if (!findCity(city)) {
					text = text.substring(text.indexOf(city) + city.length());
					text = text.trim();
				} else {
					done = true;
					text = text.substring(text.indexOf(city) + city.length());
				}
			} else {
				city = "";
				done = true;
			}
		}

		// State
		p11 = Pattern.compile(regExState);
		m11 = p11.matcher(text);
		String state = "";
		if (m11.find()) {
			state = m11.group();
			text = text.substring(text.indexOf(state) + state.length());
		}

		// ZIP
		p11 = Pattern.compile(regExZip);
		m11 = p11.matcher(text);
		String zip = "";
		if (m11.find()) {
			zip = m11.group();
			text = text.substring(text.indexOf(zip) + zip.length());
		}

		// Country
		p11 = Pattern.compile(regExCountry);
		m11 = p11.matcher(text);
		String country = "";
		if (m11.find()) {
			country = m11.group();
		}

		if (street.contentEquals("") && city.contentEquals("") && state.contentEquals("") && zip.contentEquals("")
				&& country.contentEquals("")) {
			return success;
		}

		ExtractedEntityForSingleAttribute e = new ExtractedEntityForSingleAttribute();
		e.setCompanyId(params.getCompanyId());
		e.setDocumentName(params.getDocumentName());
		e.setExtractedAttributeId(params.getRule().getAttributeId());
		e.setExtractedSentence(params.getSentence());
		e.setExtractedChunk(params.getChunk());
		String adr = "";
		country = country.trim();
		adr += (!street.contentEquals("")) ? street + " " : "";
		adr += (!city.contentEquals("")) ? city + " " : "";
		adr += (!state.contentEquals("")) ? state + " " : "";
		adr += (!zip.contentEquals("")) ? zip + " " : "";
		adr += (!country.contentEquals("")) ? country + " " : "";
		adr = adr.trim();
		e.setExtractedEntity(adr);
		e.setAppliedRule(params.getRule().getRuleId());
		e.setPageNumber(params.getPageNumber());
		listExtraction.add(e);
		success = true;

		return success;
	}

	private static boolean findCity(String city) {
		String baseFolderLocation = FolderStructure.AE_HOME.getLocation();
		File file = new File(baseFolderLocation + "/Digester_Home/Digester_Functions/Ruta_Functions/AddressFinder/resources/city.txt");
		String s = city.replaceAll(" ", "");
		try(Scanner scanner =new Scanner(file);) {

			// now read the file line by line...
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.trim().contentEquals(city) || line.trim().contentEquals(s)) {
					return true;
				}
			}
		} catch (FileNotFoundException e) {
			log.error("Error in findCity", e);
		} 
		return false;
	}
}
