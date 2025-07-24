package com.one.arpitInstituteAPI.logger;

import com.one.arpitInstituteAPI.logger.vo.AuditVO;

public interface IAuditLogger {
	
	public void sendAuditRequest(AuditVO auditVO);
}
