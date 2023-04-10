package sirup.service.diagnostics;

import sirup.service.auth.rpc.client.AuthClient;
import sirup.service.log.rpc.client.LogClient;

public class Main {

    public static void main(String[] args) {
        LogClient.init("localhost",2102, "DiagnosticsService");
        AuthClient.init("localhost", 2101);

        new ConnectionChecker().reformDiagnostics();
    }
}
