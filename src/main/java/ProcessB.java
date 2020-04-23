import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.net.Socket;
import java.util.*;

public class ProcessB {
    public static void main(String[] args) {

        Process process = new Process("B", false, "A", "D");
        process.run();
    }

}
