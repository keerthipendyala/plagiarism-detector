package edu.northeastern.cs5500.testservices;


import edu.northeastern.cs5500.Cs5500Spring2018Team203Application;
import edu.northeastern.cs5500.service.ComparisonReportService;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(PowerMockRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Cs5500Spring2018Team203Application.class)
@DataJpaTest
@PrepareForTest({ComparisonReportService.class,IOUtils.class})

/**
 * Test comparison report service
 * @author anju
 */
public class ComparisonReportServiceTest {

    private MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new ComparisonReportService()).build();

    /**
     * Test response type from api
     * @throws Exception
     */
    @Test
    public void testGetReport() throws Exception {
        mockMvc.perform(get("/api/report/23456/index.html").
                contentType(new MediaType(MediaType.APPLICATION_OCTET_STREAM.getType())).
                content("test".getBytes())).
                andExpect(status().isOk()).andExpect(content().contentType("text/html"));
    }

    /**
     * Test response if api returns html text
     * @throws Exception
     */
    @Test
    public void testGetReportValid() throws Exception {
        File fileMock = mock(File.class);
        FileInputStream inputStreamMock = mock(FileInputStream.class);
        PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(fileMock);
        PowerMockito.whenNew(FileInputStream.class).withAnyArguments().thenReturn(inputStreamMock);
        doReturn(true).when(fileMock).exists();
        PowerMockito.mockStatic(IOUtils.class);
        PowerMockito.when(IOUtils.toByteArray(any(InputStream.class))).
                thenReturn("<html><body></body></html>".getBytes());
        MvcResult result = mockMvc.perform(get("/api/report/23456/index.html").
                contentType(new MediaType(MediaType.APPLICATION_OCTET_STREAM.getType()))).
                andExpect(status().isOk()).andExpect(content().contentType("text/html")).
                andReturn();
        assertEquals(result.getResponse().getContentAsString(),"<html><body></body></html>");
    }

    /**
     * Test empty response on failures
     * @throws Exception
     */
    @Test
    public void testGetReportInvalid() throws Exception {
        File fileMock = mock(File.class);
        FileInputStream inputStreamMock = mock(FileInputStream.class);
        PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(fileMock);
        PowerMockito.whenNew(FileInputStream.class).withAnyArguments().thenReturn(inputStreamMock);
        doReturn(true).when(fileMock).exists();
        PowerMockito.mockStatic(IOUtils.class);
        PowerMockito.when(IOUtils.toByteArray(any(InputStream.class))).thenThrow(new IOException());
        MvcResult result = mockMvc.perform(get("/api/report/23456/index.html").
                contentType(new MediaType(MediaType.APPLICATION_OCTET_STREAM.getType()))).
                andExpect(status().isOk()).andReturn();
        assertArrayEquals(result.getResponse().getContentAsByteArray(),new byte[0]);
    }

    /**
     * Test service response
     * @throws Exception
     */
    @Test
    public void testGetImage() throws Exception {
        mockMvc.perform(get("/api/report/23456/image.gif").
                contentType(new MediaType(MediaType.APPLICATION_OCTET_STREAM.getType()))).
                andExpect(status().isOk()).andExpect(content().contentType("image/gif"));
    }

    /**
     * Test response if file returns byte
     * @throws Exception
     */
    @Test
    public void testGetImageValid() throws Exception {
        File fileMock = mock(File.class);
        FileInputStream inputStreamMock = mock(FileInputStream.class);
        PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(fileMock);
        PowerMockito.whenNew(FileInputStream.class).withAnyArguments().thenReturn(inputStreamMock);
        doReturn(true).when(fileMock).exists();
        PowerMockito.mockStatic(IOUtils.class);
        byte[] testImageByteArray = "test".getBytes();
        PowerMockito.when(IOUtils.toByteArray(any(InputStream.class))).thenReturn(testImageByteArray);
        MvcResult result = mockMvc.perform(get("/api/report/23456/image.gif").
                contentType(new MediaType(MediaType.APPLICATION_OCTET_STREAM.getType()))).
                andExpect(status().isOk()).andExpect(content().contentType("image/gif")).
                andReturn();
        assertArrayEquals(result.getResponse().getContentAsByteArray(),testImageByteArray);
    }

    /**
     * Test empty response on failures
     * @throws Exception
     */
    @Test
    public void testGetImageInvalid() throws Exception {
        File fileMock = mock(File.class);
        FileInputStream inputStreamMock = mock(FileInputStream.class);
        PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(fileMock);
        PowerMockito.whenNew(FileInputStream.class).withAnyArguments().thenReturn(inputStreamMock);
        doReturn(true).when(fileMock).exists();
        PowerMockito.mockStatic(IOUtils.class);
        PowerMockito.when(IOUtils.toByteArray(any(InputStream.class))).thenThrow(new IOException());
        MvcResult result = mockMvc.perform(get("/api/report/23456/image.gif").
                contentType(new MediaType(MediaType.APPLICATION_OCTET_STREAM.getType()))).
                andExpect(status().isOk()).andReturn();
        assertArrayEquals(result.getResponse().getContentAsByteArray(),new byte[0]);
    }


}
