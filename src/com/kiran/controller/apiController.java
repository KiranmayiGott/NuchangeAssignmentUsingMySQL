package com.kiran.controller;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kiran.dao.impl.APIDAOImpl;
import com.kiran.model.APIBean;
import com.kiran.model.baseResponse;
import com.kiran.model.countResponse;
import com.kiran.model.getResponse;
import com.kiran.model.listResponse;

@RestController
@RequestMapping("/api")
public class apiController 
{
	private static final String successStatus = "Success";
	private static final String errorStatus = "Error";
	private static final int code_success = 200;
	private static final int auth_failure = 404;
	
	@RequestMapping(value="/storeurl", method = RequestMethod.GET)
	public baseResponse storeurl(@RequestParam(value = "url") String url)
	{
		baseResponse response = new baseResponse();
		
		String urlStr = url;
		String shortKey = generateShortKey(urlStr);
		
		if(!checkDBurlStr(urlStr))
		{
			if(writeToDB(urlStr, shortKey, 0))
			{
				response.setStatus(successStatus);
				response.setCode(code_success);
				response.setMessage("URL is successfully added");
			}
			else
			{
				response.setStatus(errorStatus);
				response.setCode(auth_failure);
				response.setMessage("URL is not added successfully");
			}
		}
		else
		{
			response.setStatus(successStatus);
			response.setCode(code_success);
			response.setMessage("URL already exists");
		}
		
		return response;
	}
	
	public boolean checkDBurlStr(String urlStr)
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		APIDAOImpl adi = (APIDAOImpl)context.getBean("APIDAOImpl");
		
		System.out.println("------Checking Records for url string--------" );
	    List<APIBean> urls = adi.listURLs();
	    for (APIBean record : urls) 
	    {
	    	System.out.print("URL : " + record.getUrl() );
	    	//System.out.print(", Short Key : " + record.getShortKey() );
	    	//System.out.println(", Count : " + record.getCount());
	    	if(urlStr.equalsIgnoreCase(record.getUrl()))
	    	{
	    		return true;
	    	}
	    }
		return false;
	}
	
	public boolean writeToDB(String urlStr, String shortKey, int count)
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		APIDAOImpl adi = (APIDAOImpl)context.getBean("APIDAOImpl");
	    
		System.out.println("------Record Creation--------" );
		if(adi.create(urlStr, shortKey, count))
		{
			return true;
		}
		return false;
	}
	
	public List<APIBean> getRecord(String urlStr)
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		APIDAOImpl adi = (APIDAOImpl)context.getBean("APIDAOImpl");
		
		System.out.println("------Get a row for url string--------" );
	    List<APIBean> urls = adi.getRecord(urlStr);
	    for (APIBean record : urls) 
	    {
	    	System.out.print("Short Key : " + record.getShortKey() );
	    	System.out.println(", Count : " + record.getCount());
	    }
	    return urls;
	}
	
	public boolean updateDBget(String urlStr, String shortKey, int count)
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		APIDAOImpl adi = (APIDAOImpl)context.getBean("APIDAOImpl");
		
		System.out.println("------Get Record Update--------" );
		if(adi.update(urlStr, shortKey, count))
		{
			return true;
		}
		return false;
	}
	
	@RequestMapping(value="/get", method = RequestMethod.GET)
	public getResponse get(@RequestParam(value = "url") String url)
	{
		getResponse response = new getResponse();
		
		String urlStr = url;
		
		if(checkDBurlStr(urlStr))
		{
			List<APIBean> lt = getRecord(urlStr);
			String sk = lt.get(0).getShortKey();
			int cnt = lt.get(0).getCount();
			cnt++;
			if(updateDBget(urlStr, sk, cnt))
			{
				response.setStatus(successStatus);
				response.setCode(code_success);
				response.setKey(sk);
				response.setMessage("Count is successfully updated");
			}
			else
			{
				response.setStatus(errorStatus);
				response.setCode(auth_failure);
				response.setKey(sk);
				response.setMessage("Count could not update successfully");
			}
		}
		else
		{
			response.setStatus(successStatus);
			response.setCode(code_success);
			response.setMessage("URL does not exists");
		}
		return response;
	}
	
	@RequestMapping(value="/count", method = RequestMethod.GET)
	public countResponse count(@RequestParam(value = "url") String url)
	{
		countResponse response = new countResponse();
		
		String urlStr = url;
		
		if(checkDBurlStr(urlStr))
		{
			List<APIBean> lt = getRecord(urlStr);
			int cnt = lt.get(0).getCount();
			
			response.setStatus(successStatus);
			response.setCode(code_success);
			response.setCount(cnt);
			response.setMessage("Latest count");
		}
		else
		{
			response.setStatus(successStatus);
			response.setCode(code_success);
			response.setMessage("URL does not exists");
		}
		return response;
	}
	
	public List<APIBean> listAPI(int page, int size)
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		APIDAOImpl adi = (APIDAOImpl)context.getBean("APIDAOImpl");
		
		System.out.println("------Getting all Records--------" );
	    List<APIBean> urls = adi.listAPI(page, size);
	    for (APIBean record : urls) 
	    {
	    	System.out.print("URL : " + record.getUrl() );
	    	//System.out.print(", Short Key : " + record.getShortKey());
	    	System.out.println(", Count : " + record.getCount());
	    }
		return urls;
	}
	
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public listResponse list(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size)
	{
		listResponse response = new listResponse();
		
		int offset = size * (page - 1);
		List<APIBean> lt = listAPI(size, offset);
		
		response.setStatus(successStatus);
		response.setCode(code_success);
		response.setLt(lt);
		return response;
	}
	
	private String generateShortKey(String urlStr) 
	{
		String shortKey = urlStr.substring(0, 4);
		return shortKey;
	}
}
