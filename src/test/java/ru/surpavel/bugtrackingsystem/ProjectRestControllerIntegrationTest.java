package ru.surpavel.bugtrackingsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import ru.surpavel.bugtrackingsystem.controller.ProjectController;
import ru.surpavel.bugtrackingsystem.entity.Project;
import ru.surpavel.bugtrackingsystem.repository.ProjectRepository;
import ru.surpavel.bugtrackingsystem.repository.TaskRepository;
import ru.surpavel.bugtrackingsystem.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { BugTrackingSystemSpringBootApplication.class, ProjectRepository.class, UserRepository.class,
        TaskRepository.class, ProjectController.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProjectRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectController projectController;

    @Test
    public void contexLoads() throws Exception {
        assertThat(projectRepository).isNotNull();
        assertThat(userRepository).isNotNull();
        assertThat(taskRepository).isNotNull();
        assertThat(projectController).isNotNull();
    }

    @Test
    public void getAllProjects() throws Exception {
        String title = "first";
        Project project = new Project(title);
        projectRepository.save(project);
        this.mockMvc.perform(get("/projects")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(title)));
    }

    @Test
    public void getProjectById() throws Exception {
        String title = "second";
        Project project = new Project(title);
        projectRepository.save(project);
        Long id = projectRepository.findByTitle(title).get().getId();
        this.mockMvc.perform(get("/projects/" + id)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(title)));
    }
    @Test
    public void createProject() throws Exception {
        String title = "third";
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(new Project(title));
        mockMvc.perform(post("/projects").content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath(title).exists());
    }
}
