package server.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import server.repository.InitializationTools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

/**
 * Konfigurerering og oppsett av databasen.
 */
public final class Config {

    //Hashing o.l burde ligge i config liste ettersom den aldri blir lastet opp.
    //Burde lage en package hvor vi legger en cryptering og dekrypteringsklasse
    //Lager man da noen metoder i disse klassene kan man bare kalle på disse i de metodene hvor det er nødvendig

    private static String DATABASE_HOST;
    private static int DATABASE_PORT;
    private static String DATABASE_USERNAME;
    private static String DATABASE_PASSWORD;
    private static String SCHEMA_NAME;

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    public static HikariDataSource getHikariDataSource()
    {
        return ds;
    }

    public static void initializeConnectionPool()
    {

        config = new HikariConfig();
        String url = "jdbc:mysql://"
                + DATABASE_HOST
                + ":"
                + DATABASE_PORT
                + "/"
                + SCHEMA_NAME
                + "?serverTimezone=CET";


        config.setJdbcUrl(url);
        config.setUsername(DATABASE_USERNAME);
        config.setPassword(DATABASE_PASSWORD);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");


        ds = new HikariDataSource(config);
    }



    public static void initializeConfig() throws IOException {


        JsonObject json;
        JsonParser parser = new JsonParser();


        InputStream input = Config.class.getResourceAsStream("/config.json");

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));


        StringBuffer stringBuffer = new StringBuffer();
        String str;

        while ((str = reader.readLine()) != null) {
            stringBuffer.append(str);
        }

        json = (JsonObject) parser.parse(stringBuffer.toString());

        DATABASE_HOST = json.get("DATABASE_HOST").toString().replace("\"", "");
        DATABASE_PORT = Integer.parseInt(json.get("DATABASE_PORT").toString().replace("\"", ""));
        DATABASE_USERNAME = json.get("DATABASE_USERNAME").toString().replace("\"", "");
        DATABASE_PASSWORD = json.get("DATABASE_PASSWORD").toString().replace("\"", "");
        SCHEMA_NAME = json.get("SCHEMA_NAME").toString().replace("\"", "");

        initializeConnectionPool();

        try { InitializationTools.Init();
        } catch (SQLException e) { e.printStackTrace();
        }
    }
}
