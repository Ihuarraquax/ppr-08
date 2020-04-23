import javax.jms.*;

public class ProcessA{

    public static void main(String[] args) {

        Process process = new Process("A", false, "B", "C");
        process.run();
    }
}
