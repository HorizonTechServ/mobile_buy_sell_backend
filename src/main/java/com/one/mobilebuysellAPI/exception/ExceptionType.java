package com.one.mobilebuysellAPI.exception;

public interface ExceptionType {

	String getMessageMsgId();
	long getErrorCode();
	String getErrorCategory();
}
