package com.elvaston.dollnei;

import com.elvaston.dollnei.grpc.model.customer.Customer;
import com.elvaston.dollnei.grpc.model.customer.CustomerRequest;
import com.elvaston.dollnei.grpc.model.customer.CustomerServiceGrpc;
import com.elvaston.dollnei.grpc.model.customer.CustomerStatus;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.extern.log4j.Log4j2;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
@Log4j2
public class CustomerServiceImpl extends CustomerServiceGrpc.CustomerServiceImplBase {

    ConcurrentMap<String, Customer> cache = new ConcurrentHashMap<>();

    @Override
    public void customerQuery(CustomerRequest request, StreamObserver<Customer> responseObserver) {
        log.info("server received query request: {}", request);
        Customer customer = cache.getOrDefault(request.getCustomerName(), Customer.newBuilder()
                .setCustomerName(request.getCustomerName())
                .setCustomerStatus(CustomerStatus.Unknown)
                .build());
        log.info("server returning customer: {}", customer);

        responseObserver.onNext(customer);
        responseObserver.onCompleted();
    }

    @Override
    public void customerAdd(CustomerRequest request, StreamObserver<Customer> responseObserver) {
        log.info("server received add request: {}", request);
        Customer customer;
        if (cache.containsKey(request.getCustomerName())) {
            customer = cache.get(request.getCustomerName());
        } else {
            customer = Customer.newBuilder()
                    .setCustomerName(request.getCustomerName())
                    .setBrId(12345L)
                    .addAddressLines("Elvaston")
                    .addAddressLines("Gomshall Lane")
                    .setCustomerStatus(CustomerStatus.Added)
                    .setPartyAccountID(678910L)
                    .build();
            cache.put(request.getCustomerName(), customer);
        }
        log.info("server returning customer: {}", customer);

        responseObserver.onNext(customer);
        responseObserver.onCompleted();
    }


    @Override
    public void customerStatus(CustomerRequest request, StreamObserver<Customer> responseObserver) {
        log.info("server received status request: {}", request);
        CustomerStatus customerStatus;
        if (cache.containsKey(request.getCustomerName())) {
            customerStatus = cache.get(request.getCustomerName()).getCustomerStatus();
        } else {
            customerStatus = CustomerStatus.Unknown;
        }

        Customer customer = Customer.newBuilder()
                .setCustomerName(request.getCustomerName())
                .setCustomerStatus(customerStatus)
                .build();

        log.info("server returning customer: {}", customer);

        responseObserver.onNext(customer);
        responseObserver.onCompleted();
    }
}
