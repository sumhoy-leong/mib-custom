package com.mincom.mib.custom;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import org.apache.camel.Exchange;

public class ExchangeController2 {
	List<Map<String, Object>> globalStationProccessedList = new ArrayList<Map<String, Object>>();
	List<Map<String, Object>> globalStationMap = new ArrayList<Map<String, Object>>();

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> resultsetToCSV(Exchange exchange) throws Exception {

		System.out.println("ExchangeController DEBUG");
	    System.out.println("Processing " + exchange.getIn().getBody());

	    globalStationMap = (List<Map<String, Object>>) exchange.getIn().getBody();
	    globalStationProccessedList.addAll(globalStationMap);

	    return globalStationProccessedList;
	}
}
