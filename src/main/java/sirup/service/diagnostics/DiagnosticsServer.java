package sirup.service.diagnostics;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import sirup.service.log.rpc.client.LogClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class DiagnosticsServer {

    private int port = 2105;
    private Server server;
    private LogClient logger = LogClient.getInstance();

    public void start() throws IOException {
        server = ServerBuilder.forPort(2105).addService(new DiagnosticsImplementation()).build();
        server.start();
        logger.info("Service Running, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            try {
                DiagnosticsServer.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("*** server shut down");
        }));
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}
