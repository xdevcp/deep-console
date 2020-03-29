package cc.devcp.project.console.controller;

import cc.devcp.project.console.module.def.controller.HealthController;
import cc.devcp.project.provider.service.PersistService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;

/**
 * @author <a href="mailto:huangxiaoyu1018@gmail.com">hxy1991</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class HealthControllerTest {

    @InjectMocks
    private HealthController healthController;

    @Mock
    private PersistService persistService;

    private MockMvc mockmvc;

    @Before
    public void setUp() {
        mockmvc = MockMvcBuilders.standaloneSetup(healthController).build();
    }

    @Test
    public void testLiveness() throws Exception {
        String url = "/health";
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url);
        Assert.assertEquals(200, mockmvc.perform(builder).andReturn().getResponse().getStatus());
    }

    @Test
    public void testReady() throws Exception {
        String url = "/health/ready";

        Mockito.when(persistService.configInfoCount(any(String.class))).thenReturn(0);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url);
        Assert.assertEquals(200, mockmvc.perform(builder).andReturn().getResponse().getStatus());

        // Config and Naming are not in ready
        Mockito.when(persistService.configInfoCount(any(String.class))).thenThrow(
                new RuntimeException("HealthControllerTest.testReady"));

        builder = MockMvcRequestBuilders.get(url);
        MockHttpServletResponse response = mockmvc.perform(builder).andReturn().getResponse();
        Assert.assertEquals(500, response.getStatus());
        Assert.assertEquals("Config and Naming are not in ready", response.getContentAsString());

        // Config is not in ready
        Mockito.when(persistService.configInfoCount(any(String.class))).thenThrow(
                new RuntimeException("HealthControllerTest.testReady"));

        response = mockmvc.perform(builder).andReturn().getResponse();
        Assert.assertEquals(500, response.getStatus());
        Assert.assertEquals("Config is not in ready", response.getContentAsString());

        // Naming is not in ready
        Mockito.when(persistService.configInfoCount(any(String.class))).thenReturn(0);

        builder = MockMvcRequestBuilders.get(url);
        response = mockmvc.perform(builder).andReturn().getResponse();
        Assert.assertEquals(500, response.getStatus());
        Assert.assertEquals("Naming is not in ready", response.getContentAsString());
    }
}
