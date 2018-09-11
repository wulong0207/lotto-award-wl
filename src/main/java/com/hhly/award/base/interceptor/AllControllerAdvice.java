package com.hhly.award.base.interceptor;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hhly.award.base.common.ResultBO;
import com.hhly.award.base.exception.ServiceRuntimeException;
/**
 * 
 * @author jiangwei
 * @Version 1.0
 * @CreatDate 2016-12-15 下午3:19:12
 * @Desc 异常处理
 */
@ControllerAdvice
public class AllControllerAdvice {
	
	public static  Logger logger = Logger.getLogger(AllControllerAdvice.class);
	/**
	 * 用于解析日期格式参数
	 * @param binder
	 */
	@InitBinder    
	public void initBinder(WebDataBinder binder) {    
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
	        dateFormat.setLenient(false);    
	        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));    
	}
	
	@ExceptionHandler(ServiceRuntimeException.class)
	@ResponseBody
	public Object exp(HttpServletRequest request, ServiceRuntimeException ex) {
		handleException(ex);
		return ResultBO.getFail(ex.getMessage(), null);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	public Object exp(HttpServletRequest request, IllegalArgumentException ex) {
		handleException(ex);
		return ResultBO.getFail(ex.getMessage(), null);
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Object exp(HttpServletRequest request, Exception ex) {
		handleException(ex);
		return ResultBO.getFail("系统异常", null);
	}
	
	
	private void handleException(Exception ex){
		logger.error(ex);
		ex.printStackTrace();
	}
		
}
