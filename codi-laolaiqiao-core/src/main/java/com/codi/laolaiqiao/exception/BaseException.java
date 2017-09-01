package com.codi.laolaiqiao.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class BaseException extends RuntimeException {

	private static final long serialVersionUID = -802376082358572333L;

	private int code;
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	public BaseException(int code, String msg) {		
		super(msg);
		this.code=code;
	}

	public BaseException(String msg) {
		super(msg);
	}

	public BaseException(Throwable ex) {
		super(ex);

	}

	public BaseException(String msg, Throwable ex) {
		super(msg, ex);
	}
	
	@Override
	public Throwable getCause() {
		return super.getCause();
	}

	@Override
	public String getMessage() {
		if (super.getCause() == null) {
			return super.getMessage();
		} else {
			return super.getMessage() + "; nested exception is " + super.getCause().getClass().getName() + ": "
					+ super.getCause().getMessage();
		}
	}

	@Override
	public void printStackTrace(PrintStream ps) {
		if (super.getCause() == null) {
			super.printStackTrace(ps);
		} else {
			ps.println(this);
			super.getCause().printStackTrace(ps);
		}
	}

	@Override
	public void printStackTrace(PrintWriter pw) {
		if (super.getCause() == null) {
			super.printStackTrace(pw);
		} else {
			pw.println(this);
			super.getCause().printStackTrace(pw);
		}
	}
}