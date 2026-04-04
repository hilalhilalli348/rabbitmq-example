package ourcorp.rabbitmqexample.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum RetryHeaderUtils {

    RETRY_HEADER_UTILS;

    private static final String RETRY_COUNT_HEADER = "x-retry-count";

    public int getRetryCount(Map<String, Object> headers) {
        if (headers == null || !headers.containsKey(RETRY_COUNT_HEADER)) {
            return 0;
        }
        return (Integer) headers.get(RETRY_COUNT_HEADER);
    }


    public Map<String, Object> incrementRetryCount(Map<String, Object> headers) {
        int current = getRetryCount(headers);

        Map<String, Object> newHeaders = new HashMap<>();
        if (headers != null) {
            headers.forEach((key, value) -> {
                if (!key.startsWith("amqp_") && !key.equals("id") && !key.equals("timestamp")) {
                    newHeaders.put(key, value);
                }
            });
        }

        newHeaders.put(RETRY_COUNT_HEADER, current + 1);
        return newHeaders;
    }
}