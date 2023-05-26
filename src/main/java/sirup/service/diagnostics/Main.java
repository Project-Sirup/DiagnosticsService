package sirup.service.diagnostics;

import sirup.service.auth.rpc.client.AuthClient;
import sirup.service.log.rpc.client.LogClient;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        LogClient.init(Env.LOG_ADDRESS,Env.LOG_PORT, "DiagnosticsService");
        AuthClient.init(Env.AUTH_ADDRESS,Env.AUTH_PORT);

        //new ConnectionChecker().reformDiagnostics();
        final DiagnosticsServer server = new DiagnosticsServer();
        server.start();
        server.blockUntilShutdown();
    }
}
