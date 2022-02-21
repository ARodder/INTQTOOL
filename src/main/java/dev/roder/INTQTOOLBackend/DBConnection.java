package dev.roder.INTQTOOLBackend;

public class DBConnection {

    private static DBConnection dbConnection = null;

    private DBConnection() {

    }

    public static DBConnection getConnection() {
        if (dbConnection == null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }
}
