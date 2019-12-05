package edu.hm.dako.chat.AuditLogServer;

import java.io.*;

import edu.hm.dako.chat.common.AuditLogPDU;
import edu.hm.dako.chat.tcp.TcpConnection;
import edu.hm.dako.chat.tcp.TcpServerSocket;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


/**
 * AuditLog Server fuer die Protokollierung von Chat-Nachrichten eines Chat-Servers.
 * Implementierung auf Basis von TCP.
 *
 * @author mandl
 *
 */
public class AuditLogTcpServer {
	private static Logger log = Logger.getLogger(AuditLogTcpServer.class);

	// Serverport fuer AuditLog-Service
	private static final int AUDIT_LOG_SERVER_PORT = 40001;

	// Standard-Puffergroessen fuer Serverport in Bytes
	private static final int DEFAULT_SENDBUFFER_SIZE = 30000;
	private static final int DEFAULT_RECEIVEBUFFER_SIZE = 800000;

	// Name der AuditLog-Datei
	private static final String auditLogFile = new String("ChatAuditLogTCP.dat");

	// Zaehler fuer ankommende AuditLog-PDUs
	protected static long counter = 0;


	public static void main(String[] args) throws Exception {

		PropertyConfigurator.configureAndWatch("log4j.auditLogServer_tcp.properties", 60 * 1000);
		System.out.println("AuditLog-TcpServer gestartet, Port: " + AUDIT_LOG_SERVER_PORT);
		log.info("AuditLog-TcpServer gestartet, Port: " + AUDIT_LOG_SERVER_PORT);

		TcpServerSocket socket = new TcpServerSocket(AUDIT_LOG_SERVER_PORT, DEFAULT_SENDBUFFER_SIZE, DEFAULT_RECEIVEBUFFER_SIZE);
		TcpConnection connection = (TcpConnection) socket.accept();

		FileWriter writer =  new FileWriter(auditLogFile);

		AuditLogPDU pdu;

		while (true) {
			pdu = (AuditLogPDU) connection.receive();
			System.out.println("Nachricht erhalten: " + pdu.auditLogInfo("TCP"));
			writer.write(pdu.auditLogInfo("TCP"));
			counter++;
			writer.flush();
			if (false) {
				connection.close();
				socket.close();
			}
		}
	}
}