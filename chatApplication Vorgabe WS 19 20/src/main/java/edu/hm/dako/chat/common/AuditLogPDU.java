package edu.hm.dako.chat.common;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;


/**
 * <p/>
 * Nachrichtenaufbau fuer das AuditLog-Protokoll
 *
 * @author Mandl
 */
public class AuditLogPDU implements Serializable {

	private static final long serialVersionUID = -6172619032079227589L;
	private static final Logger log = Logger.getLogger(AuditLogPDU.class);

	// Kommandos bzw. PDU-Typen
	private AuditLogPduType pduType;

	// Login-Name des Clients
	private String userName;

	// Name des Chat-Client-Threads, der den Request absendet
	private String clientThreadName;

	// Name des Worker-Threads, der den Request im Chat-Server verarbeitet
	private String serverThreadName;

	// Zeitstempel zum Zeitpunkt des Audit-Logs im Chat-Server
	private long auditTime;

	// Nutzdaten (eigentliche Chat-Nachricht in Textform)
	private String message;

	public AuditLogPDU() {

		this.pduType = AuditLogPduType.UNDEFINED;
		this.clientThreadName = null;
		this.serverThreadName = null;
		this.userName = null;
		this.auditTime = 0;
	}

	public String toString() {

		Date dateAndTime = new Date(this.auditTime);

		return "\n"
				+ "AuditLogPdu ****************************************************************************************************"
				+ "\n" + "AuditLogType: " + pduType + "\n" + "userName: " + this.userName + ", "
				+ "\n" + "clientThreadName: " + this.clientThreadName + "\n"
				+ "serverThreadName: " + this.serverThreadName + "\n" + "auditTime: "
				+ dateAndTime.toString() + "\n" + "message: " + this.message + "\n"
				+ "**************************************************************************************************** SimplePdu"
				+ "\n";
	}

	//HABEN WIR HINZUGEFUEGT.....

	public String auditLogInfo(String connectionType) {
		StringBuilder sb = new StringBuilder();
		sb.append("Connection Type: ");
		sb.append(connectionType);
		sb.append(" | ");
		sb.append(new Date(this.auditTime));
		sb.append(" | ");
		sb.append(pduType);
		sb.append(" | ");
		sb.append(serverThreadName);
		sb.append(" | ");
		sb.append(clientThreadName);
		sb.append(" | ");
		sb.append(userName);
		sb.append(" | ");
		sb.append(message + "\n");
		return sb.toString();
	}

	private String convertTimeWithTimeZone(long time){
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("CET"));
		cal.setTimeInMillis(time);
		return (cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-"
				+ cal.get(Calendar.DAY_OF_MONTH) + "___" + cal.get(Calendar.HOUR_OF_DAY) + ":"
				+ cal.get(Calendar.MINUTE));
	}

	public void setPduType(AuditLogPduType pduType) {
		this.pduType = pduType;
	}

	public AuditLogPduType getPduType() {
		return pduType;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return (this.userName);
	}

	public void setClientThreadName(String threadName) {
		this.clientThreadName = threadName;
	}

	public String getClientThreadName() {
		return (this.clientThreadName);
	}

	public void setServerThreadName(String threadName) {
		this.serverThreadName = threadName;
	}

	public String getServerThreadName() {
		return (this.serverThreadName);
	}

	public void setMessage(String msg) {
		this.message = msg;
	}

	public void setAuditTime(long auditTime) {
		this.auditTime = auditTime;
	}

	public long getAuditTime() {
		return (auditTime);
	}

	public String getMessage() {
		return (message);
	}

	public static AuditLogPDU convertChatPDUtoAuditLogPDU(ChatPDU chatPDUtoConvert) {
		AuditLogPDU resultAuditLogPDU = new AuditLogPDU();
		resultAuditLogPDU.pduType = convertChatPDUTypeToAuditLogPDUType(chatPDUtoConvert.getPduType());
		resultAuditLogPDU.userName = chatPDUtoConvert.getUserName();
		resultAuditLogPDU.clientThreadName = chatPDUtoConvert.getClientThreadName();
		resultAuditLogPDU.serverThreadName = chatPDUtoConvert.getServerThreadName();
		resultAuditLogPDU.auditTime = chatPDUtoConvert.getServerTime();
		resultAuditLogPDU.message = chatPDUtoConvert.getMessage();
		return resultAuditLogPDU;
	}

	private static AuditLogPduType convertChatPDUTypeToAuditLogPDUType(PduType pduType) {
		for (AuditLogPduType auditLogPDUTypeItem : AuditLogPduType.values()) {
			if (auditLogPDUTypeItem.getDescription().equals(pduType.getDescription())) {
				return auditLogPDUTypeItem;
			}
			else {
				return null;
			}
		}
		return null;
	}
}