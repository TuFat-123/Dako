package edu.hm.dako.chat.AuditLogServer;

import java.io.*;

import edu.hm.dako.chat.common.AuditLogPDU;
import edu.hm.dako.chat.common.AuditLogPduType;
import edu.hm.dako.chat.tcp.TcpConnection;
import edu.hm.dako.chat.tcp.TcpServerSocket;
import org.apache.log4j.PropertyConfigurator;


/**
 * AuditLog Server fuer die Protokollierung von Chat-Nachrichten eines Chat-Servers.
 * Implementierung auf Basis von TCP.
 *
 * @author mandl
 *
 */
public class AuditLogTcpServer extends AbstractAuditLogServer {

	AuditLogTcpServer() {
		super("TCP");
	}

	private TcpServerSocket socket;
	private TcpConnection connection;

	@Override
	public void start() {
		PropertyConfigurator.configureAndWatch("log4j.auditLogServer_tcp.properties", 60 * 1000);
		log.info("AuditLog-TcpServer gestartet, Port: " + AUDIT_LOG_SERVER_PORT);

		keepGoing = true;
		try {
			socket = new TcpServerSocket(AUDIT_LOG_SERVER_PORT, DEFAULT_SENDBUFFER_SIZE, DEFAULT_RECEIVEBUFFER_SIZE);
			connection = (TcpConnection) socket.accept();
			writer = new FileWriter(auditLogFile);

			while (keepGoing) {
				AuditLogPDU pdu = (AuditLogPDU) connection.receive();
				writer.write(pdu.auditLogInfo("TCP"));
				counter++;
				writer.flush();
				if (pdu.getPduType().equals(AuditLogPduType.FINISH_AUDIT_REQUEST)) {
					connection.close();
					connection = (TcpConnection) socket.accept();
				}
			}
			socket.close();
			connection.close();
			writer.write(String.format("Es wurden %d Nachrichten empfangen", counter));
			log.info("TCP Socket wird geschlossen und Prozess wird beendet");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}