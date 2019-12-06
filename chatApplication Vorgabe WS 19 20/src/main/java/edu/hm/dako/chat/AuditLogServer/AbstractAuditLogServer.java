package edu.hm.dako.chat.AuditLogServer;

import org.apache.log4j.Logger;

import java.io.FileWriter;

public abstract class AbstractAuditLogServer {


    Logger log = Logger.getLogger(AuditLogTcpServer.class);

    // Serverport fuer AuditLog-Service
    final int AUDIT_LOG_SERVER_PORT = 40001;

    // Standard-Puffergroessen fuer Serverport in Bytes
    final int DEFAULT_SENDBUFFER_SIZE = 30000;
    final int DEFAULT_RECEIVEBUFFER_SIZE = 800000;

    // Name der AuditLog-Datei
    String auditLogFile;

    // Zaehler fuer ankommende AuditLog-PDUs
    long counter = 0;

    Boolean keepGoing;
    FileWriter writer;

    AbstractAuditLogServer(String connectionType) {
        auditLogFile = String.format("ChatAuditlog%s.dat", connectionType);
    }

    abstract void start();

    void stop() {
        keepGoing = false;
        System.exit(0);
    }
}
