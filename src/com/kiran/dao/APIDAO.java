package com.kiran.dao;

import java.util.List;
import javax.sql.DataSource;

import com.kiran.model.APIBean;

public interface APIDAO 
{
	// This is the method to be used to initialize database resources ie. connection.
	public void setDataSource(DataSource ds);
   
	// This is the method to be used to list down all the records from the api table.
	public List<APIBean> listURLs();
	
	//	This is the method to be used to create a record in api table.
	public boolean create(String url, String shortKey, Integer count);
	
	// This is the method to be used to update a record into the api table.
	public boolean update(String urlStr, String shortkey, Integer count);
	
	// This is the method to be used to list down a row from api table corresponding to url string.
	public List<APIBean> getRecord(String urlStr); 
	
	// This is the method to be used to list down a row from api table corresponding to url string.
	public List<APIBean> listAPI(int size, int offset); 
}