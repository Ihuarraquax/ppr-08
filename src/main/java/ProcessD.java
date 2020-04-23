import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.net.Socket;
import java.util.*;

public class ProcessD {
    public static void main(String[] args) {

        Process process = new Process("D", false, "B", "C", "E");
        process.run();
    }

}
