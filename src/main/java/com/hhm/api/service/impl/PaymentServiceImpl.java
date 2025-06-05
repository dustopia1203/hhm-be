package com.hhm.api.service.impl;

import com.hhm.api.config.properties.VNPayProperties;
import com.hhm.api.model.dto.request.CreateVNPayPaymentURLRequest;
import com.hhm.api.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final VNPayProperties vnPayProperties;

    @Override
    public String createVNPayPaymentURL(CreateVNPayPaymentURLRequest request) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        ZoneId zoneVN = ZoneId.of("Asia/Ho_Chi_Minh");

        String vnp_Url = vnPayProperties.getUrl();
        String vnp_HashSecret = vnPayProperties.getHashSecret();

        Map<String, String> vnp_Params = new HashMap<>();

        vnp_Params.put("vnp_Version", vnPayProperties.getVersion());
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnPayProperties.getTmnCode());
        vnp_Params.put("vnp_Amount", String.valueOf(request.getAmount() * 100));
        vnp_Params.put("vnp_CreateDate", ZonedDateTime.now(zoneVN).format(dateFormatter));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_IpAddr", "::1");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_OrderInfo", request.getOrderInfo());
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_ReturnUrl", vnPayProperties.getReturnUrl());
        vnp_Params.put("vnp_ExpireDate", ZonedDateTime.now(zoneVN).plus(vnPayProperties.getExpiredTime()).format(dateFormatter));
        vnp_Params.put("vnp_TxnRef", RandomStringUtils.randomAlphabetic(3) + ZonedDateTime.now(zoneVN).format(dateFormatter) + RandomStringUtils.randomAlphabetic(3));

        List<String> keys = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(keys);

        // key=value&... -> hashing
        StringBuilder hashData = new StringBuilder();

        Iterator<String> keyIterator = keys.iterator();

        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            String value = vnp_Params.get(key);

            if (!value.isBlank()) {
                hashData.append(key);
                hashData.append("=");
                hashData.append(URLEncoder.encode(value, StandardCharsets.US_ASCII));

                if (keyIterator.hasNext()) {
                    hashData.append("&");
                }
            }
        }

        String vnp_SecureHash = vnPayHmacSHA512(vnp_HashSecret, hashData.toString());

        return String.format("%s?%s&vnp_SecureHash=%s", vnp_Url, hashData, vnp_SecureHash);
    }

    private String vnPayHmacSHA512(final String key, final String data) {
        try {

            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }
}
