package com.one.mobilebuysellAPI.logger;

import com.one.mobilebuysellAPI.logger.vo.AuditVO;

public class AuditUtility {

	private static IAuditLogger iAuditLogger = AuditProviderFactory.getProviderInstance();
	
	private AuditUtility(){
		//Implementation
	}
	
	public static void sendAuditRequest(AuditVO auditVO) {
		iAuditLogger.sendAuditRequest(auditVO);
	}
	
}
