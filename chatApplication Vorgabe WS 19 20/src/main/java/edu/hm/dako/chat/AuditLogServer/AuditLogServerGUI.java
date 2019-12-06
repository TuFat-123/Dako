package edu.hm.dako.chat.AuditLogServer;

import edu.hm.dako.chat.common.SystemConstants;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class AuditLogServerGUI extends Application {
   private Button stopStart;
   private AbstractAuditLogServer auditLogServer;
   private ComboBox<String> auditLogImplType;
   private ObservableList<String> implTypeOptions = FXCollections.observableArrayList(
           SystemConstants.AUDIT_LOG_SERVER_TCP_IMPL, SystemConstants.AUDIT_LOG_SERVER_UDP_IMPL);


   public void start(Stage primary) {
       GridPane panel = new GridPane();
       stopStart = new Button("Start");
       auditLogImplType = new ComboBox<>(implTypeOptions);
       auditLogImplType.setValue(implTypeOptions.get(0));

       stopStart.setOnAction(event -> {
           if (auditLogServer != null) {
               auditLogServer.stop();
               auditLogServer = null;
               stopStart.setText("Stop");
               return;
           }

           String implType = auditLogImplType.getValue();
           if (implType.equals("TCP")) {
               auditLogServer = new AuditLogTcpServer();
           } else if (implType.equals("UDP")) {
               auditLogServer = new AuditLogUdpServer();
           }
           new ServerThread().start();
           stopStart.setText("Stop");
       });


       panel.add(stopStart, 2, 2);
       panel.add(auditLogImplType, 2, 1);

       Scene scene = new Scene(panel);
       primary.setScene(scene);
       primary.show();

   }

    public static void main(String[] args) {
        launch(args);
    }

   class ServerThread extends Thread {
       public void run() {
           auditLogServer.start();
           stopStart.setText("Start");
           auditLogServer = null;
       }

   }
}
