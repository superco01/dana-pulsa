package com.debrief2.pulsa.mobile.utils.errors;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Data
public class ErrorList {
    private Map<String, ErrorCodeValue> errorCodeValueMap = new HashMap<String, ErrorCodeValue>();

    public ErrorList() {
        //Order Domain
        errorCodeValueMap.put("unknown phone number", new ErrorCodeValue("unknown phone number", 404, HttpStatus.NOT_FOUND));
        errorCodeValueMap.put("invalid phone number", new ErrorCodeValue("invalid phone number", 400, HttpStatus.BAD_REQUEST));
        errorCodeValueMap.put("you’ve already requested this exact order within the last 30 seconds, please try again later if you actually intended to do that", new ErrorCodeValue("you’ve already requested this exact order within the last 30 seconds, please try again later if you actually intended to do that", 409, HttpStatus.CONFLICT));
        errorCodeValueMap.put("catalog not found", new ErrorCodeValue("catalog not found", 404, HttpStatus.NOT_FOUND));
        errorCodeValueMap.put("selected catalog is not available for this phone’s provider", new ErrorCodeValue("selected catalog is not available for this phone’s provider", 400, HttpStatus.BAD_REQUEST));
        errorCodeValueMap.put("not enough balance", new ErrorCodeValue("not enough balance", 400, HttpStatus.BAD_REQUEST));
        errorCodeValueMap.put("unknown transaction", new ErrorCodeValue("unknown transaction", 404, HttpStatus.NOT_FOUND));
        errorCodeValueMap.put("unknown method", new ErrorCodeValue("unknown method", 404, HttpStatus.NOT_FOUND));
        errorCodeValueMap.put("voucher you want to redeem is either not found, has been used or already expired", new ErrorCodeValue("voucher you want to redeem is either not found, has been used or already expired", 400, HttpStatus.BAD_REQUEST));
        errorCodeValueMap.put("unknown voucher", new ErrorCodeValue("unknown voucher", 404, HttpStatus.NOT_FOUND));
        errorCodeValueMap.put("can't cancel completed transaction", new ErrorCodeValue("can't cancel completed transaction", 400, HttpStatus.BAD_REQUEST));
        //Promotion Domain
        errorCodeValueMap.put("you don’t have any vouchers", new ErrorCodeValue("you don’t have any vouchers", 404, HttpStatus.NOT_FOUND));
        errorCodeValueMap.put("you don’t have any vouchers recommendation", new ErrorCodeValue("you don’t have any vouchers recommendation", 404, HttpStatus.NOT_FOUND));
        errorCodeValueMap.put("There is no recommendation", new ErrorCodeValue("There is no recommendation", 404, HttpStatus.NOT_FOUND));
        errorCodeValueMap.put("voucher not found", new ErrorCodeValue("voucher not found", 404, HttpStatus.NOT_FOUND));
        //Member Domain
        errorCodeValueMap.put("invalid email", new ErrorCodeValue("invalid email", 400, HttpStatus.BAD_REQUEST));
        errorCodeValueMap.put("invalid request format", new ErrorCodeValue("invalid request format", 400, HttpStatus.BAD_REQUEST));
        errorCodeValueMap.put("invalid pin", new ErrorCodeValue("invalid pin", 400, HttpStatus.BAD_REQUEST));
        errorCodeValueMap.put("user already exist", new ErrorCodeValue("user already exist", 409, HttpStatus.CONFLICT));
        errorCodeValueMap.put("incorrect phone number", new ErrorCodeValue("incorrect phone number", 404, HttpStatus.NOT_FOUND));
        errorCodeValueMap.put("incorrect pin", new ErrorCodeValue("incorrect pin", 404, HttpStatus.NOT_FOUND));
        errorCodeValueMap.put("user not found", new ErrorCodeValue("user not found", 404, HttpStatus.NOT_FOUND));
        errorCodeValueMap.put("invalid OTP", new ErrorCodeValue("invalid OTP", 400, HttpStatus.BAD_REQUEST));
        errorCodeValueMap.put("incorrect OTP", new ErrorCodeValue("incorrect OTP", 404, HttpStatus.NOT_FOUND));
        errorCodeValueMap.put("OTP expired", new ErrorCodeValue("OTP expired", 401, HttpStatus.UNAUTHORIZED));
        errorCodeValueMap.put("OTP not found", new ErrorCodeValue("OTP not found", 404, HttpStatus.NOT_FOUND));
        errorCodeValueMap.put("user session not found", new ErrorCodeValue("user session not found", 404, HttpStatus.NOT_FOUND));
        errorCodeValueMap.put("updated", new ErrorCodeValue("updated", 200, HttpStatus.OK));
    }
}
