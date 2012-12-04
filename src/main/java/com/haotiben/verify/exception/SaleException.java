package com.haotiben.verify.exception;


import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;


/**
 *
 * @author zp
 */
public class SaleException extends WebApplicationException {

	private static final long serialVersionUID = 1L;

	public SaleException(ErrorBean error) {
		super(Response.serverError().entity(error).status(200).build());
	}
	
	public static void error(ErrorBean eb){
		throw new SaleException(eb);
	}

	public static void error(String errorCode) {
		ErrorBean errorBean = new ErrorBean();
		errorBean.setErrorCode(errorCode);
		String errorValue = ErrorUtil.readValue(errorCode);
		errorBean.setError(errorValue);
		throw new SaleException(errorBean);
	}

	public static void error(String errorCode, String request) {
		ErrorBean errorBean = new ErrorBean();
		errorBean.setErrorCode(errorCode);
		String errorValue = ErrorUtil.readValue(errorCode);
		errorBean.setError(errorValue);
		throw new SaleException(errorBean);
	}


}