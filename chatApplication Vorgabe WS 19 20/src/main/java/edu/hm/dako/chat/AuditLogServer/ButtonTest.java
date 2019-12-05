package edu.hm.dako.chat.AuditLogServer;


import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import edu.hm.dako.chat.common.AuditLogPDU;
import edu.hm.dako.chat.connection.ServerSocketInterface;
import edu.hm.dako.chat.tcp.TcpConnection;
import edu.hm.dako.chat.tcp.TcpServerSocket;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class ButtonTest extends JFrame implements ActionListener {

    private JButton button;
    private JButton button2;

    ServerSocketInterface socket;
    TcpConnection connection;

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

    FileWriter writer;
    AuditLogPDU pdu;

    public ButtonTest() throws Exception {
        button = new JButton("Beenden");
        button2 = new JButton("Starten");
        button.addActionListener(this);
        button2.addActionListener(this);
        this.getContentPane().add(button2);
        this.getContentPane().add(button);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            try {
                writer.close();
                connection.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == button2) {
            try {
                init();
                startServer();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void init() throws Exception{
        socket = new TcpServerSocket(AUDIT_LOG_SERVER_PORT, DEFAULT_SENDBUFFER_SIZE, DEFAULT_RECEIVEBUFFER_SIZE);
        connection = (TcpConnection) socket.accept();
        writer = new FileWriter(auditLogFile);
    }

    public void startServer() throws Exception {
        while (true) {
            pdu = (AuditLogPDU) connection.receive();
            System.out.println("Nachricht erhalten: " + pdu.auditLogInfo("TCP"));
            writer.write(pdu.auditLogInfo("TCP"));
            counter++;
            writer.flush();
        }
    }

    public static void main(String[] args) throws Exception {
        ButtonTest test = new ButtonTest();
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.setSize(400,400);
        test.setVisible(true);
    }
}
