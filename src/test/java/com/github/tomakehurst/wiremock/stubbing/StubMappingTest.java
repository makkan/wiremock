package com.github.tomakehurst.wiremock.stubbing;

import org.junit.Assert;
import org.junit.Test;

public class StubMappingTest {

    @Test
    public void shouldCreateAStubWithCapture() {
        StubMapping stubMapping = StubMapping
                .buildFrom("{ \"request\": { \"urlCapture\": \"/nagios/(.*)\", \"method\": \"GET\" }, \"response\": { \"status\": 200, \"body\": \"Wiremock is alive\"}}");
        Assert.assertNotNull(stubMapping.getRequest().getUrlCapture());
    }
}
