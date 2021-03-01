package cz.cvut.fel.ear.tm.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectRestTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldGetAllProjects() throws Exception {
        this.mockMvc.perform(get("/rest/projects")).andDo(print()).andExpect(status().isOk());
    }
}
