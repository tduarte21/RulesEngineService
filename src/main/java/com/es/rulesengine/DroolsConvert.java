package com.es.rulesengine;

import java.io.*;

import org.drools.compiler.compiler.*;
import org.drools.compiler.compiler.xml.*;
import org.drools.compiler.lang.*;
import org.drools.compiler.lang.descr.*;
import org.drools.core.xml.*;
import org.xml.sax.SAXException;

@SuppressWarnings("restriction")
public class DroolsConvert {

	public static String drlFileToXml(String file) {
		try {
			InputStream in = new FileInputStream(file);
			Reader source = new InputStreamReader(in);
			return drlToXml(source);
		} catch (DroolsParserException | IOException e) {
			e.printStackTrace();
			return "ERROR";
		}
	}

	public static String drlStringToXml(String drl) {
		try {
			InputStream stream = new ByteArrayInputStream(drl.getBytes());
			Reader source = new InputStreamReader(stream);
			return drlToXml(source);
		} catch (DroolsParserException e) {
			e.printStackTrace();
			return "ERROR";
		}
	}

	private static String drlToXml(Reader source) throws DroolsParserException {
		DrlParser drlParser = new DrlParser();
		PackageDescr pkgDesc = drlParser.parse(source);
		XmlDumper xmlDumper = new XmlDumper();
		String xml = xmlDumper.dump(pkgDesc);
		return xml;

	}

	public static String xmlFileToDrl(String file) {
		try {
			InputStream in = new FileInputStream(file);
			Reader source = new InputStreamReader(in);
			return xmlToDrl(source);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
			return "ERROR";
		}
	}

	public static String xmlStringToDrl(String xml) {
		try {
			InputStream stream = new ByteArrayInputStream(xml.getBytes());
			Reader source = new InputStreamReader(stream);
			return xmlToDrl(source);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
			return "ERROR";
		}
	}
	
	
	private static String xmlToDrl(Reader source) throws SAXException, IOException {		
		SemanticModules sm = new SemanticModules();
		XmlPackageReader xmlPackageReader = new XmlPackageReader(sm);
		
		
		/*int intValueOfChar;
	    String targetString = "";
	    while ((intValueOfChar = source.read()) != -1) {
	        targetString += (char) intValueOfChar;
	    }
	    source.close();*/
		
		PackageDescr pkgDesc = xmlPackageReader.read(source);
		if(pkgDesc == null)
			return "Parse Error!";
		DrlDumper drlDumper = new DrlDumper();
		String drl = drlDumper.dump(pkgDesc);
		return drl;

	}
	
	public static Boolean writeFile(String fileName, String path, String content)
			throws FileNotFoundException, UnsupportedEncodingException {
		String filePath = path + fileName;
		File file = new File(filePath);

		PrintWriter writer = new PrintWriter(file, "UTF-8");
		writer.write(content);
		writer.close();

		// TODO: Check

		return true;
	}

}
