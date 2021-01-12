package com.brightleaf.ruleservice.serviceimpl;

import java.io.File;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.RecursiveParserWrapper;
import org.apache.tika.sax.BasicContentHandlerFactory;
import org.apache.tika.sax.RecursiveParserWrapperHandler;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.brightleaf.ruleservice.model.ExtractedEntityForSingleAttribute;
import com.brightleaf.ruleservice.model.Rule;
import com.brightleaf.ruleservice.service.FileTypeFactoryService;


@Service
public class PstFileTypeServiceImpl implements FileTypeFactoryService {
	
	protected final static Logger logger = Logger.getLogger(PstFileTypeServiceImpl.class);

	@Override
	public void extractAttributes(File file, Integer companyId, List<ExtractedEntityForSingleAttribute> listExtraction, List<Rule> listRule, HttpServletRequest request, String outputDateFormat) {
		Parser p = new AutoDetectParser();
		RecursiveParserWrapper wrapper = new RecursiveParserWrapper(p);
		RecursiveParserWrapperHandler handler = new RecursiveParserWrapperHandler(
				new BasicContentHandlerFactory(BasicContentHandlerFactory.HANDLER_TYPE.TEXT, -1));
		String fileName = "";
		try(FileWriter fw = new FileWriter("C:\\shama\\testout.txt");) {
			fileName = file.getCanonicalPath();

			InputStream stream = new FileInputStream(fileName);

			wrapper.parse(stream, handler, new Metadata(), new ParseContext());

			int i = 0;
			
			for (Metadata metadata : handler.getMetadataList()) {
				for (String name : metadata.names()) {
					for (String value : metadata.getValues(name)) {
						String s = value.toLowerCase();
						if (s.contains("date") || s.contains("from-email")
								|| s.contains("to-email")|| s.contains("cc-email")
								|| s.contains("title")) {
						logger.info(i + " " + name + ": " + value);
						fw.write(i + " " + name + ": " + value);
						fw.write("\n");
						}
					}
					fw.write("******************************************************************************");
					fw.write("\n");
				}
				i++;
			}//date, to-email, from-email, cc-email, title, content

		} catch (IOException | SAXException | TikaException e) {
			logger.error("Error in ExtractAttributes", e);
		} 
	}

}
