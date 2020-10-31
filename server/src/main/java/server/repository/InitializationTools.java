package server.repository;

import org.apache.ibatis.jdbc.ScriptRunner;
import server.util.Config;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

public class InitializationTools {



        public static void Init() throws SQLException {
            Connection con = Config.getHikariDataSource().getConnection();
            //Initialize the script runner
            ScriptRunner sr = new ScriptRunner(con); //Creating a reader object
            Reader reader = new
                    InputStreamReader(Config.class.getResourceAsStream("/init.sql"));
            //Running the script
            sr.runScript(reader);
        }
}
