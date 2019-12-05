package edu.hm.dako.chat.AuditLogServer;

import java.io.*;

import edu.hm.dako.chat.common.AuditLogPDU;
import edu.hm.dako.chat.udp.UdpSocket;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class AuditLogUdpServer {

	private static Logger log = Logger.getLogger(AuditLogUdpServer.class);

	// UDP-Serverport fuer AuditLog-Service
	private static final int AUDIT_LOG_SERVER_PORT = 40001;

	// Standard-Puffergroessen fuer Serverport in Bytes
	private static final int DEFAULT_SENDBUFFER_SIZE = 30000;
	private static final int DEFAULT_RECEIVEBUFFER_SIZE = 800000;

	// Name der AuditLog-Datei
	private static final String auditLogFile = new String("ChatAuditLogUDP.dat");

	// Zaehler fuer ankommende AuditLog-PDUs
	protected static long counter = 0;

	public static void main(String[] args) throws Exception {
		PropertyConfigurator.configureAndWatch("log4j.auditLogServer_udp.properties", 60 * 1000);
		System.out.println("AuditLog-UdpServer gestartet, Port: " + AUDIT_LOG_SERVER_PORT);
		log.info("AuditLog-UdpServer gestartet, Port" + AUDIT_LOG_SERVER_PORT);



		// AB HIER HABEN WIR GEMACHT......
		UdpSocket socket = new UdpSocket(AUDIT_LOG_SERVER_PORT, DEFAULT_SENDBUFFER_SIZE, DEFAULT_RECEIVEBUFFER_SIZE);
		FileWriter writer = new FileWriter(auditLogFile);


		while (true)
		{
			AuditLogPDU pdu = (AuditLogPDU) socket.receive(0);
			log.info("Nachricht erhalten: " + pdu.auditLogInfo("UDP"));
			writer.write(pdu.auditLogInfo("UDP"));
			writer.flush();
			counter++;
			if (false) {
				log.info("AuditLog-UpdServer wird beendet....");
				socket.close();
				writer.close();
				break;
			}
		}
	}
}