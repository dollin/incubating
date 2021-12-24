package com.elvaston.dollnei;

import com.elvaston.dollnei.grpc.model.customer.Customer;
import com.elvaston.dollnei.grpc.model.customer.CustomerRequest;
import com.elvaston.dollnei.grpc.model.customer.CustomerServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.TimeUnit;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CustomerClientService {

    public void queryByCustomerName(final String customerName) throws InterruptedException {
        log.info("Query by customerName: {}", customerName);

        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:6565").usePlaintext().build();

        try {
            CustomerServiceGrpc.CustomerServiceBlockingStub customerServiceStub = CustomerServiceGrpc.newBlockingStub(channel);
            CustomerRequest request = CustomerRequest.newBuilder().setCustomerName(customerName).build();

            log.info("client query with request: {}", request);
            Customer customer = customerServiceStub.customerQuery(request);
            log.info("client received customer: {}", customer);
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    public static void main(String[] args) throws Exception {
        new CustomerClientService().queryByCustomerName("NEV_LDN");
    }
}