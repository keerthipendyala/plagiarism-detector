package edu.northeastern.cs5500.testservices;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.cs5500.Cs5500Spring2018Team203Application;
import edu.northeastern.cs5500.models.Submission;
import edu.northeastern.cs5500.scanner.JPlagServiceScanner;
import edu.northeastern.cs5500.scanner.JplagProgram;
import edu.northeastern.cs5500.service.ComparisionService;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit test for comparison service.
 *
 * @author anju
 */
@RunWith(PowerMockRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Cs5500Spring2018Team203Application.class)
@PrepareForTest({ComparisionService.class, JPlagServiceScanner.class})
public class ComparisonServiceTest {
    private MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new ComparisionService()).build();

    /**
     * Test submission api
     * @throws Exception
     */
    @Test
    public void testScanSubmissionInJplagAssignment() throws Exception {
        List<Submission> sublist = new ArrayList<>();
        Submission s1 = new Submission();
        s1.setid(1);
        Submission s2 = new Submission();
        s1.setid(2);
        sublist.add(s1);
        sublist.add(s2);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        byte[] jsonBytes = mapper.writeValueAsBytes(sublist);
        JPlagServiceScanner scannerMock = mock(JPlagServiceScanner.class);
        PowerMockito.whenNew(JPlagServiceScanner.class).
                withNoArguments().thenReturn(scannerMock);

        MvcResult mvcResult = mockMvc.perform(post("/api/scanassignment").
                with(request -> {
                    request.addHeader("host", "www.test.com");
                    return request;
                }).contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8")))
                .content(jsonBytes)).andExpect(status().isOk()).andReturn();
        assertNotNull(mvcResult);
        assertEquals("1",mvcResult.getResponse().getContentAsString());
    }
}
