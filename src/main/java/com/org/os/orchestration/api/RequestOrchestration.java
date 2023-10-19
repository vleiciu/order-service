package com.org.os.orchestration.api;

import org.apache.camel.Exchange;

public interface RequestOrchestration {
    void handleRequest(Exchange e);
}
