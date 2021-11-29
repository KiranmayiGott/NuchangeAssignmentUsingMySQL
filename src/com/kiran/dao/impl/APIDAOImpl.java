package com.kiran.dao.impl;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.kiran.dao.APIDAO;
import com.kiran.model.APIBean;

public class APIDAOImpl implements APIDAO 
{
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;
   
	public void setDataSource(DataSource dataSource) 
	{
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}
   
	public boolean create(String url, String shortKey, Integer count) 
	{
		String insertQuery = "insert into apitbl (url, shortkey, count) values (?, ?, ?)";
		jdbcTemplateObject.update( insertQuery, url, shortKey, count);
		System.out.println("Created Record = " + url + " short key = " + shortKey + " count = " + count);
		return true;
	}
	
	public List<APIBean> listURLs() 
	{
		String SQL = "select * from apitbl";
		List <APIBean> apis = jdbcTemplateObject.query(SQL, new APIMapper());
		return apis;
	}

	public boolean update(String urlStr, String shortKey, Integer count) 
	{
		String SQL = "update apitbl set count = ? where shortkey = ?";
		jdbcTemplateObject.update(SQL, count, shortKey);
		System.out.println("Updated Record with short key = " + shortKey);
		return true;
	}

	public List<APIBean> getRecord(String urlStr) 
	{
		
		String SQL = "select * from apitbl where url = '" + urlStr + "';";
		List <APIBean> apis = jdbcTemplateObject.query(SQL, new APIMapper());
		return apis;
	}
	
	public List<APIBean> listAPI(int size, int offset) 
	{
		String SQL = "select * from apitbl limit " +size + " offset " + offset + ";";
		List <APIBean> apis = jdbcTemplateObject.query(SQL, new APIMapper());
		return apis;
	}
}