package com.patient.manager.patient_service.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillingServiceGRPCClient {
    private static final Logger log = LoggerFactory.getLogger(BillingServiceGRPCClient.class);
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    public BillingServiceGRPCClient(
            @Value("${billing.service.address:localhost}") String serverAddress,
            @Value("${billing.service.grpc.port:9001}") int port
    ) {
            log.info("Connecting to Billing Service via GRPC at {}:{}", serverAddress, port);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, port).usePlaintext().build();

        blockingStub = BillingServiceGrpc.newBlockingStub(channel);
    }

    public BillingResponse createBillingAccount(String patientId, String name, String email) {
        BillingRequest req = BillingRequest.newBuilder().setPatientId(patientId)
                .setName(name).setEmail(email).build();

        BillingResponse res = blockingStub.createBillingAccount(req);
        log.info("Received response from billing request via GRPC: {}", res);

        return res;
    }
}
