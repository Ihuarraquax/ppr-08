import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ProcessInfo {

    public static void main(String[] args) {

        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_BROKER_URL);
            Connection con = connectionFactory.createConnection();
            con.start();
            Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer info = session.createConsumer(session.createTopic("INFO"));

            while (true){
                TextMessage message = (TextMessage) info.receive();
                String from = message.getStringProperty("from");
                System.out.println(from+": " +message.getText());
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
