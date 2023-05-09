package sirup.service.diagnostics;

import sirup.service.auth.rpc.client.AuthClient;
import sirup.service.auth.rpc.client.AuthServiceUnavailableException;
import sirup.service.diag.rpc.proto.Report;
import sirup.service.diag.rpc.proto.Vitals;
import sirup.service.log.rpc.client.LogClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class ConnectionChecker {

    private final String regService = "http://127.0.0.1:2100/api/v1/health";
    private final String userService = "http://127.0.0.1:2103/api/v1/health";
    private final String notifyService = "http://127.0.0.1:2104/api/v1/health";

    private final LogClient logClient = LogClient.getInstance();

    private int failed = 0;
    private final int total = 5;

    public Report reformDiagnostics() {
        Report.Builder reportBuilder = Report.newBuilder();
        long start = System.currentTimeMillis();
        LogClient logger = LogClient.getInstance();
        logger.info("Performing diagnostics...");
        logger.info("Checking gRPC services");
        checkGrpc(reportBuilder);
        logger.info("Checking REST services");
        checkREST(reportBuilder);
        logger.info("Diagnostics performed in " + (System.currentTimeMillis() - start) + "ms");
        if (failed > 0) {
            logger.warn(failed + " / " + total + " service(s) did not respond");
            return reportBuilder.build();
        }
        logger.info("All services responded, system running fine");
        return reportBuilder.build();
    }

    public void checkREST(Report.Builder reportBuilder) {
        HttpClient client = HttpClient.newBuilder().build();
        time(reportBuilder, "RegisterService", () -> {
            try {
                return _checkREST(client, HttpRequest.newBuilder()
                        .uri(new URI(regService))
                        .GET()
                        .build());
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return false;
            }
        });
        time(reportBuilder, "UserService", () -> {
            try {
                return _checkREST(client, HttpRequest.newBuilder()
                        .uri(new URI(userService))
                        .GET()
                        .build());
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return false;
            }
        });
        time(reportBuilder, "NotificationService", () -> {
            try {
                return _checkREST(client, HttpRequest.newBuilder()
                        .uri(new URI(notifyService))
                        .GET()
                        .build());
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    public void checkGrpc(Report.Builder reportBuilder) {
        time(reportBuilder,"LogService", () -> logClient.health() == 200);
        AuthClient authClient = AuthClient.getInstance();
        time(reportBuilder, "AuthService", () -> {
            try {
                return authClient.health() == 200;
            } catch (AuthServiceUnavailableException e) {
                return false;
            }
        });
    }

    private void time(Report.Builder reportBuilder, String serviceName, Timed timed) {
        logClient.info("Checking " + serviceName);
        long start = System.currentTimeMillis();
        boolean responded = timed.check();
        long time = System.currentTimeMillis() - start;
        if (!responded) {
            failed++;
            logClient.warn("Did not respond");
            time = -1;
        }
        logClient.info("Responded in " + time + "ms");
        reportBuilder.addVitals(Vitals.newBuilder()
                .setServiceName(serviceName)
                .setRunning(responded)
                .setResponseTime(time)
                .build());
    }

    private interface Timed {
        boolean check();
    }

    private boolean _checkREST(HttpClient client, HttpRequest request) {
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
}
