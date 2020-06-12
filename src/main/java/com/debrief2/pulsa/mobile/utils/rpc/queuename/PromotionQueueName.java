package com.debrief2.pulsa.mobile.utils.rpc.queuename;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class PromotionQueueName {
    private final String getMyVoucher = "getMyVoucher";
    private final String getVoucherRecommendation = "getVoucherRecommendation";
    private final String getVoucherPromotion = "getVoucherPromotion";
    private final String getVoucherDetail = "getVoucherDetail";
}
