package com.mincom.mib.custom;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.dataformat.csv.CsvDataFormat;
import org.apache.camel.Exchange;
import org.apache.camel.model.OptionalIdentifiedDefinition;
import org.apache.camel.component.properties.*;
import org.apache.camel.component.sql.SqlComponent;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.sql.DataSource;
import javax.naming.InitialContext;

import com.mincom.mib.camel.OptionalRouteBuilder;

@Component
public class AccountCodeExtractRouteBuilder extends RouteBuilder implements OptionalRouteBuilder {	

	@Autowired
	@Qualifier("ellipseDS")
	DataSource dataSource;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	protected String getSource() { return null; }
	
	protected String getTarget() {	return null; }
	
	public String[] getRouteNames() {
		return new String[] { "CustomAMTaskEvent" };
	}

	public String getPropertyName() {
		return "mib.custom.am.enabled";
	}	
	
// 	@DependsOn("ellipseDS")
    public void configure() throws Exception {    	
        	
    	PropertiesComponent pc = new PropertiesComponent();
    	pc.setLocation("classpath:META-INF/spring/settings.properties,classpath:META-INF/spring/sql.properties");
    	getContext().addComponent("properties", pc);
    	
    	CsvDataFormat csv = new CsvDataFormat();
    	csv.setDelimiter(',');
    	String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
    	 
    	try {		 
    		RouteDefinition route_am = (RouteDefinition)    from("quartz://myGroup/amTimer?trigger.repeatInterval=60")
	                .routeId("CustomAccountCodeExtract")
					.setHeader(Exchange.FILE_NAME, constant("HP_FIN_AM_"+today+".csv"))
					.to( "properties:{{dv.sql.selectAccount}}" )
					.bean(ExchangeController2.class, "resultsetToCSV")
					.marshal(csv)
					.marshal().zipFile()					
     				.to("properties:{{target.dir}}{{parameters}}{{filename.am}}")
					.end();
    	} catch(Exception e) { System.out.println(e.toString()); }
    	 
    }
    
}
