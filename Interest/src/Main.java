import java.io.IOException;
import java.sql.Timestamp;

public class Main {
    public static synchronized void main(String[] args) throws IOException, InterruptedException {

        NetworkUtil util = new NetworkUtil();
        while (true){

            util.httpRequest("http://localhost:8080/exam/accounts/interest", "GET");

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            System.out.println("Interest has ben added  at" + timestamp);

            Thread.sleep(86400000);
        }
    }
}
