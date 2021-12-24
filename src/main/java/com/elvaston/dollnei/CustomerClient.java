package com.elvaston.dollnei;

import com.elvaston.dollnei.grpc.model.customer.Customer;
import com.elvaston.dollnei.grpc.model.customer.CustomerRequest;
import com.elvaston.dollnei.grpc.model.customer.CustomerServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class CustomerClient {

    private CustomerServiceGrpc.CustomerServiceBlockingStub serviceStub;

    @PostConstruct
    private void setup() {
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress("localhost", 6565)
                .usePlaintext()
                .build();

        serviceStub = CustomerServiceGrpc.newBlockingStub(managedChannel);
    }

    public String customerQuery(final String customerName) {
        CustomerRequest request = CustomerRequest.newBuilder().setCustomerName(customerName).build();
        log.info("client sending request: {}", request);

        Customer customer = serviceStub.customerQuery(request);
        log.info("client received customer: {}", customer);

        return customer.toString();
    }
}
