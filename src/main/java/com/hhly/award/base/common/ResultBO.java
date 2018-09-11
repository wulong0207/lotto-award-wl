package com.hhly.award.base.common;

/**
 * @author huangb
 *
 * @Date 2016年12月1日
 *
 * @Desc 结果对象(用于校验，判断等公共结果的输出)
 */

@SuppressWarnings("serial")
public class ResultBO<T> extends BaseBO {
	/**
	 * 成功
	 */
	private static final int OK = 1;
	/**
	 * 失败
	 */
	private static final int ERR = 0;
	/**
	 * 成功返回code
	 */
	public static final String SUCCESS_CODE = "10001";
	
	public static final String FAIL_CODE = "10002";
	

	/**
	 * 输出状态 1(成功)/0(失败)
	 */
	private int success;
	/**
	 * 错误信息代码
	 */
	private String errorCode;
	/**
	 * 输出消息
	 */
	private String message;
	/**
	 * 输出数据（附件信息）
	 */
	private T data;

	public ResultBO() {
		
	}
	public ResultBO(int success, String errorCode, String message, T data) {
		this.success = success;
		this.errorCode = errorCode;
		this.message = message;
		this.data = data;
	}


	public static <T> ResultBO<T> getSuccess(T data){
		return new ResultBO<T>(OK, SUCCESS_CODE, "成功", data);
	}
	
	public static <T> ResultBO<T> getFail(String message,T data){
		return new ResultBO<T>(ERR, FAIL_CODE, message, data);
	}
	
	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	
}
