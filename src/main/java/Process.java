import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.*;

public class Process {
    final String myName;
    boolean initial;
    String M;
    List<String> somsiedzi;
    Map<String, SignalStatus> status;
    Session session;
    MessageConsumer myConsumer;
    Map<String, MessageProducer> producers = new HashMap<>();


    public Process(String name, boolean initial, String... somsiads) {
        myName = name;
        somsiedzi = new ArrayList<>(Arrays.asList(somsiads));
        status = new HashMap<>();
        for (String s : somsiedzi) {
            status.put(s, new SignalStatus());
        }
        this.initial = initial;
        M = null;

        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_BROKER_URL);
            Connection con = connectionFactory.createConnection();
            con.start();
            session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    protected boolean gotQUorENfromAllSomsiads(Map<String, SignalStatus> status) {
        return status.entrySet().stream().allMatch(e -> e.getValue().QU || e.getValue().EN);
    }

    protected void sendTo(String to, String status) {
        try {

            MessageProducer producer;
            if (!producers.containsKey(to)) {
                producer = session.createProducer(session.createQueue(to));
                producers.put(to, producer);
            } else {
                producer = producers.get(to);
            }
            TextMessage message = session.createTextMessage(status);
            message.setStringProperty("from", myName);
            inform("Send " + message.getText() + " to " + to);
            producer.send(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    protected void inform(String text) {
        try {
            MessageProducer producer = session.createProducer(session.createTopic("INFO"));
            TextMessage message = session.createTextMessage(text);
            message.setStringProperty("from", myName);
            producer.send(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    protected TextMessage read(String name) {
        try {
            if (myConsumer == null) {
                myConsumer = session.createConsumer(session.createQueue(name));
            }
            TextMessage receive = (TextMessage) myConsumer.receive();
            inform("Got " + receive.getText() + " from " + receive.getStringProperty("from"));
            return receive;
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void run() {
        if (initial) {
            M = "initial";
            for (String s : somsiedzi) {
                sendTo(s, "EN");
            }
        }

        while (M == null || !gotQUorENfromAllSomsiads(status)) {
            TextMessage message = read(myName);
            try {
                String from = message.getStringProperty("from");
                if (message.getText().equals("EN") && !from.equals(M)) {
                    if (M == null) {
                        M = from;
                        inform(from + " is my M");
                        somsiedzi.remove(M);
                        status.remove(M);
                        for (String s : somsiedzi) {
                            sendTo(s, "EN");
                        }
                    } else {
                        status.get(from).EN = true;
                    }
                }
                if (message.getText().equals("QU")) {
                    status.get(from).QU = true;
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }

        if (!initial) {
            inform("Job is done, sending QU to my M(" + M + ")");
            sendTo(M, "QU");
        }
        System.out.println("KONIEC");
    }
}
