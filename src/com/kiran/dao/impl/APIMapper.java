package com.kiran.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import com.kiran.model.APIBean;

public class APIMapper implements RowMapper<APIBean> 
{
   public APIBean mapRow(ResultSet rs, int rowNum) throws SQLException 
   {
      APIBean apiBean = new APIBean();
      apiBean.setUrl(rs.getString("url"));
      apiBean.setShortKey(rs.getString("shortkey"));
      apiBean.setCount(rs.getInt("count"));
      return apiBean;
   }
}