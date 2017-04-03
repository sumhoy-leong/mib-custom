package com.mincom.mib.custom;

import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Date;

import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.Body;
import org.apache.camel.Message;

public class PropertyConfig   {
	String lastModate = null;
	static Logger log = LoggerFactory.getLogger(PropertyConfig.class);
	
	public Map<String, Object> getLastmodate(String assoc_rec) {    
    	String lastModate = null;
    	
    	try {
    		String[] array = assoc_rec.split("=");
    		lastModate = array[1].substring(0, 7);
    	} catch (Exception e) {
    		e.printStackTrace();
    		log.error("Please check table setup, table type 'VIP'");
    	}
    	    	
    	Map<String, Object> m = new HashMap<String, Object>();
    	m.put("lastmodate", lastModate);
    	return m;

    }
	
}
