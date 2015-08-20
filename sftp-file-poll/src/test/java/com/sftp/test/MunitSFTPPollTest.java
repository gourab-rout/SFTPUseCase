 package com.sftp.test;

import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.SerializationUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.mule.DefaultMuleMessage;
import org.mule.api.*;
import org.mule.api.transport.PropertyScope;
import org.mule.munit.runner.functional.FunctionalMunitSuite;
import org.mule.transport.sftp.SftpConnector;
import org.mule.transport.sftp.StorePolledFiles;



public class MunitSFTPPollTest extends FunctionalMunitSuite{
	
	final Logger logger = Logger.getRootLogger();
	StorePolledFiles spFile = new StorePolledFiles(); 
	
	protected String getConfigResources() {
		// TODO Auto-generated method stub
		return "sftp-file-poll.xml";
	}
	
	/**
	 * Tests duplicate files are not read again
	 * @throws MuleException
	 * @throws Exception
	 */
	@Test 
	public void testSFTPFileProcess() throws MuleException, Exception 
	{
		spFile.initDB();
		MuleMessage mmsg;
		
		//Test First File
		String sInputFile1 = "./src/test/resources/Test1.txt";
		mmsg = getSftpMessage( sInputFile1 );		
		callMainFlow( mmsg );
		
		
		//Test again with First File
		String sInputFile3 = "./src/test/resources/Test1.txt";
		mmsg = getSftpMessage( sInputFile3 );		
		callMainFlow( mmsg );
		
		
	}	
	
	public void callMainFlow( MuleMessage mmsg ) throws MuleException, Exception
	{
		String fileName = (String) mmsg.getProperty("originalFilename", PropertyScope.OUTBOUND);
		if( !spFile.isFileAlreadyProcessed(  fileName )){
			logger.info("Processing started for the file " + fileName );
			spFile.insertPolledFileToDB( fileName  );
			logger.info("Event run flow triggerred");
			MuleEvent getResponse = runFlow("sftp-file-pollFlow", testEvent( mmsg ));
			assertNotNull( getResponse.getMessage().getPayload());
			logger.info("Processing completed for the file " + fileName );			
		}
		else{
			logger.info("File is " + fileName + " already processed");
		}		
	}
	
	public MuleMessage getSftpMessage( String sInputFile ) throws IOException {
		
		MuleMessage mmsg = new DefaultMuleMessage(null, muleContext);
		File fInputFile1 = new File( sInputFile );	

		byte[] filecontent = SerializationUtils.serialize( fInputFile1 );				
		Map fileData = new HashMap();
		fileData.put("fileName", sInputFile);
		fileData.put("fileData", filecontent);
		mmsg.setPayload( fileData );
		mmsg.setOutboundProperty(SftpConnector.PROPERTY_FILENAME, sInputFile);
	    mmsg.setOutboundProperty(SftpConnector.PROPERTY_ORIGINAL_FILENAME, sInputFile);
	    return mmsg;
	}
}