package sirup.service.diagnostics;

public class Env {
    public static final int DIAG_PORT;
    public static final String LOG_ADDRESS;
    public static final int LOG_PORT;
    public static final String AUTH_ADDRESS;
    public static final int AUTH_PORT;
    public static final String USER_ADDRESS;
    public static final int USER_PORT;
    public static final String REG_ADDRESS;
    public static final int REG_PORT;
    public static final String NOTI_ADDRESS;
    public static final int NOTI_PORT;
    static {
        DIAG_PORT = Integer.parseInt(System.getenv("DIAG_PORT"));
        LOG_ADDRESS = System.getenv("LOG_ADDRESS");
        LOG_PORT = Integer.parseInt(System.getenv("LOG_PORT"));
        AUTH_ADDRESS = System.getenv("AUTH_ADDRESS");
        AUTH_PORT = Integer.parseInt(System.getenv("AUTH_PORT"));
        USER_ADDRESS = System.getenv("USER_ADDRESS");
        USER_PORT = Integer.parseInt(System.getenv("USER_PORT"));
        REG_ADDRESS = System.getenv("REG_ADDRESS");
        REG_PORT = Integer.parseInt(System.getenv("REG_PORT"));
        NOTI_ADDRESS = System.getenv("NOTI_ADDRESS");
        NOTI_PORT = Integer.parseInt(System.getenv("NOTI_PORT"));
    }
    
}
