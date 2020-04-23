import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.time.LocalTime;
import java.util.*;

public class ProcessE {

    public static void main(String[] args) {

        Process process = new Process("E", true, "C", "D", "F");
        process.run();
    }


}

