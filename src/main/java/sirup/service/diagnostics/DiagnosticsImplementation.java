package sirup.service.diagnostics;

import io.grpc.stub.StreamObserver;
import sirup.service.diag.rpc.proto.DiagnosticsRequest;
import sirup.service.diag.rpc.proto.DiagnosticsResponse;
import sirup.service.diag.rpc.proto.Report;
import sirup.service.diag.rpc.proto.SirupDiagnosticsServiceGrpc;

public class DiagnosticsImplementation extends SirupDiagnosticsServiceGrpc.SirupDiagnosticsServiceImplBase {

    private final ConnectionChecker connectionChecker = new ConnectionChecker();

    @Override
    public void runDiagnostics(DiagnosticsRequest request, StreamObserver<DiagnosticsResponse> responseObserver) {
        Report report = connectionChecker.reformDiagnostics();
        DiagnosticsResponse response = DiagnosticsResponse.newBuilder().setReport(report).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
