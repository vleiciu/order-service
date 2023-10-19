package com.org.os.orchestration.api;

import org.apache.camel.Exchange;

public interface ResponseOrchestration {
    void handleResponse(Exchange e);
}
