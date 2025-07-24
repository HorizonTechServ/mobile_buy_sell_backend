package com.one.arpitInstituteAPI.logger;

import com.one.arpitInstituteAPI.logger.vo.AuditVO;

public class AuditUtility {

	private static IAuditLogger iAuditLogger = AuditProviderFactory.getProviderInstance();
	
	private AuditUtility(){
		//Implementation
	}
	
	public static void sendAuditRequest(AuditVO auditVO) {
		iAuditLogger.sendAuditRequest(auditVO);
	}
	
}
