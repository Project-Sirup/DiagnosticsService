package sirup.service.diagnostics;

import sirup.service.auth.rpc.client.AuthClient;
import sirup.service.log.rpc.client.LogClient;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        LogClient.init("localhost",2102, "DiagnosticsService");
        AuthClient.init("localhost", 2101);

        //new ConnectionChecker().reformDiagnostics();
        final DiagnosticsServer server = new DiagnosticsServer();
        server.start();
        server.blockUntilShutdown();
    }
}
