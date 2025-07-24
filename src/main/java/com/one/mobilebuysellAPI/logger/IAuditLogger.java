package com.one.mobilebuysellAPI.logger;

import com.one.mobilebuysellAPI.logger.vo.AuditVO;

public interface IAuditLogger {
	
	public void sendAuditRequest(AuditVO auditVO);
}
