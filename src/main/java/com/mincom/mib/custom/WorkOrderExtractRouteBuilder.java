package com.mincom.mib.custom;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.component.sql.SqlComponent;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.dataformat.csv.CsvDataFormat;
import org.apache.camel.Exchange;
import org.apache.camel.model.OptionalIdentifiedDefinition;
import org.apache.camel.component.sql.SqlComponent;

import org.springframework.stereotype.Component;
import org.springframework.context.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;
import javax.naming.InitialContext;

import org.quartz.SchedulerException;

import com.mincom.mib.camel.OptionalRouteBuilder;

@Component
public class WorkOrderExtractRouteBuilder extends RouteBuilder implements OptionalRouteBuilder {

	@Autowired
	@Qualifier("ellipseDS")
	DataSource dataSource;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	protected String getSource() { return null; }
	
	protected String getTarget() {	return null; }
	
	public String[] getRouteNames() { return new String[] { "CustomWOTaskEvent" }; }

	public String getPropertyName() { return "mib.custom.wo.enabled"; }	
	
//	@DependsOn("ellipseDS")
    public void configure() throws Exception {
    	    	    	
   	 	CsvDataFormat csv = new CsvDataFormat();
   	 	csv.setDelimiter(',');
   	 	String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
    	 
   	 	RouteDefinition route_wo = (RouteDefinition)    from("quartz://myGroup/woTimer?trigger.repeatInterval=60") 				 
			.routeId("CustomWorkOrderExtract")
			.setHeader(Exchange.FILE_NAME, constant("HP_FIN_WO_"+today+".csv"))
			.to( "properties:{{dv.sql.selectWorkOrder}}" )
			.bean(ExchangeController.class, "resultsetToCSV")
			.marshal(csv)			
			.marshal().zipFile()
			.to("properties:{{target.dir}}{{parameters}}{{filename.wo}}")
			.end();		 
    }
   
}
