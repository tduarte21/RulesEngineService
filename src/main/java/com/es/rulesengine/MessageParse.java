package com.es.rulesengine;

public class MessageParse {

	public static void parseMsg(String message) {

		String drl = DroolsConvert.xmlStringToDrl(message);
		
		System.out.println(drl);
		
		//DroolsConvert.writeFile(fileName, path, content)

	}

}
