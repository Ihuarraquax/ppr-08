import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.net.Socket;
import java.util.*;

public class ProcessC {
    public static void main(String[] args) {

        Process process = new Process("C", false, "A", "D", "E");
        process.run();
    }

}
