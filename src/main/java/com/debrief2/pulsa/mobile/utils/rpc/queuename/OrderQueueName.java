package com.debrief2.pulsa.mobile.utils.rpc.queuename;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Data
public class OrderQueueName {
    private final String getAllCatalog = "getAllCatalog";
    private final String getRecentNumber = "getRecentNumber";
    private final String cancel = "cancel";
    private final String getProviderById = "getProviderById";
    private final String getPaymentMethodNameById = "getPaymentMethodNameById";
    private final String getTransactionById = "getTransactionById";
    private final String getTransactionByIdByUserId = "getTransactionByIdByUserId";
    private final String getHistoryInProgress = "getHistoryInProgress";
    private final String getHistoryCompleted = "getHistoryCompleted";
    private final String createTransaction = "createTransaction";
    private final String pay = "pay";
}
