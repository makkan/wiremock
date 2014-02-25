package com.github.tomakehurst.wiremock.matching;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import junit.framework.Assert;

import org.junit.Test;

import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.servlet.HttpServletRequestAdapter;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

public class RequestPatternTest {

    @Test
    public void shouldMatchUrl() {
        StubMapping stubMapping = StubMapping
                .buildFrom("{ \"request\": { \"url\": \"/nagios\", \"method\": \"GET\" }, \"response\": { \"status\": 200, \"body\": \"Wiremock is alive\"}}");
        RequestPattern requestPattern = stubMapping.getRequest();

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getRequestURI()).thenReturn("/nagios");
        when(httpServletRequest.getMethod()).thenReturn("GET");

        Request request = new HttpServletRequestAdapter(httpServletRequest);
        Assert.assertTrue(requestPattern.isMatchedBy(request));
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionIfAllUrlIsSet() {
        StubMapping
                .buildFrom("{ \"request\": { \"url\": \"/nagios\", \"urlPattern\": \"/nagios\", \"urlCapture\": \"/nagios\", \"method\": \"GET\" }, \"response\": { \"status\": 200, \"body\": \"Wiremock is alive\"}}");
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionIfUrlAndUrlPatternIsSet() {
        StubMapping
                .buildFrom("{ \"request\": { \"url\": \"/nagios\", \"urlPattern\": \"/nagios\", \"method\": \"GET\" }, \"response\": { \"status\": 200, \"body\": \"Wiremock is alive\"}}");
    }

    @Test
    public void shouldMatchUrlPattern() {
        StubMapping stubMapping = StubMapping
                .buildFrom("{ \"request\": { \"urlPattern\": \"/nagios/.*\", \"method\": \"GET\" }, \"response\": { \"status\": 200, \"body\": \"Wiremock is alive\"}}");
        RequestPattern requestPattern = stubMapping.getRequest();

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getRequestURI()).thenReturn("/nagios/test");
        when(httpServletRequest.getMethod()).thenReturn("GET");

        Request request = new HttpServletRequestAdapter(httpServletRequest);
        Assert.assertTrue(requestPattern.isMatchedBy(request));
    }

    @Test
    public void shouldMatchUrlCapture() {
        StubMapping stubMapping = StubMapping
                .buildFrom("{ \"request\": { \"urlCapture\": \"/nagios/(.*)\", \"method\": \"GET\" }, \"response\": { \"status\": 200, \"body\": \"Wiremock is alive\"}}");
        RequestPattern requestPattern = stubMapping.getRequest();

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getRequestURI()).thenReturn("/nagios/test");
        when(httpServletRequest.getMethod()).thenReturn("GET");

        Request request = new HttpServletRequestAdapter(httpServletRequest);
        Assert.assertTrue(requestPattern.isMatchedBy(request));
    }

    @Test
    public void shouldNotMatchUrlCatpure() {
        StubMapping stubMapping = StubMapping
                .buildFrom("{ \"request\": { \"urlCapture\": \"/nagios/(.*)\", \"method\": \"GET\" }, \"response\": { \"status\": 200, \"body\": \"Wiremock is alive\"}}");
        RequestPattern requestPattern = stubMapping.getRequest();

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getRequestURI()).thenReturn("/nagios");
        when(httpServletRequest.getMethod()).thenReturn("GET");

        Request request = new HttpServletRequestAdapter(httpServletRequest);
        Assert.assertFalse(requestPattern.isMatchedBy(request));
    }
}
