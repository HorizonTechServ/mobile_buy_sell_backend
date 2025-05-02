package com.one.societyAPI.logger;

import com.one.societyAPI.logger.vo.AuditVO;

public interface IAuditLogger {
	
	public void sendAuditRequest(AuditVO auditVO);
}
