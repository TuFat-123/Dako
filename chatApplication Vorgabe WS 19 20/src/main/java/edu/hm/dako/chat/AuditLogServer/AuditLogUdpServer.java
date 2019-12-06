package edu.hm.dako.chat.AuditLogServer;

import edu.hm.dako.chat.common.AuditLogPDU;
import edu.hm.dako.chat.udp.UdpSocket;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileWriter;

public class AuditLogUdpServer extends AbstractAuditLogServer {

	AuditLogUdpServer() {
		super("UDP");
	}

	@Override
	public void start() {
		PropertyConfigurator.configureAndWatch("log4j.auditLogServer_udp.properties", 60 * 1000);
		log.info("AuditLog-UdpServer gestartet, Port" + AUDIT_LOG_SERVER_PORT);

		UdpSocket socket;

		try {
			socket = new UdpSocket(AUDIT_LOG_SERVER_PORT, DEFAULT_SENDBUFFER_SIZE, DEFAULT_RECEIVEBUFFER_SIZE);
			writer = new FileWriter(auditLogFile);

			keepGoing = true;
			while (keepGoing) {
				AuditLogPDU pdu = (AuditLogPDU) socket.receive(0);
				log.info("Nachricht erhalten: " + pdu.auditLogInfo("UDP"));
				writer.write(pdu.auditLogInfo("UDP"));
				writer.flush();
				counter++;
			}
			writer.write(String.format("Es wurden %d Nachrichten empfangen", counter));
			log.info("TCP Socket wird geschlossen und Prozess wird beendet");
			socket.close();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}