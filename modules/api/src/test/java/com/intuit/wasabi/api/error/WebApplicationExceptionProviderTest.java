/*******************************************************************************
 * Copyright 2016 Intuit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.intuit.wasabi.api.error;

import com.intuit.wasabi.api.HttpHeader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WebApplicationExceptionProviderTest {

    @Mock
    private HttpHeader httpHeader;
    @Mock
    private ExceptionJsonifier exceptionJsonifier;
    @Mock
    private WebApplicationException webApplicationException;
    @Mock
    private ResponseBuilder responseBuilder;
    @Mock
    private Response response;
    @Captor
    private ArgumentCaptor<Object> entity;
    private WebApplicationExceptionProvider webApplicationExceptionProvider;

    @Before
    public void before() {
        webApplicationExceptionProvider = new WebApplicationExceptionProvider(httpHeader, exceptionJsonifier);
    }

    @Test
    public void toResponse() throws Exception {
        when(httpHeader.headers(INTERNAL_SERVER_ERROR)).thenReturn(responseBuilder);
        when(responseBuilder.type(APPLICATION_JSON_TYPE)).thenReturn(responseBuilder);
        when(webApplicationException.getMessage()).thenReturn("error");
        when(exceptionJsonifier.serialize(INTERNAL_SERVER_ERROR, "error")).thenReturn("json");
        when(responseBuilder.entity("json")).thenReturn(responseBuilder);
        when(responseBuilder.build()).thenReturn(response);

        webApplicationExceptionProvider.toResponse(webApplicationException);

        verify(httpHeader).headers(INTERNAL_SERVER_ERROR);
        verify(exceptionJsonifier).serialize(INTERNAL_SERVER_ERROR, "error");
        verify(responseBuilder).type(APPLICATION_JSON_TYPE);
        verify(responseBuilder).entity("json");
        verify(responseBuilder).build();
    }
}
