package ru.surpavel.bugtrackingsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import ru.surpavel.bugtrackingsystem.controller.ProjectController;
import ru.surpavel.bugtrackingsystem.entity.Project;
import ru.surpavel.bugtrackingsystem.repository.ProjectRepository;
import ru.surpavel.bugtrackingsystem.repository.TaskRepository;
import ru.surpavel.bugtrackingsystem.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ProjectRepository.class, UserRepository.class, TaskRepository.class,
        ProjectController.class, }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

    @Before
    public void createProjectInRepo() {
        Project project = new Project("first");
        projectRepository.save(project);

        Mockito.when(projectRepository.findByTitle(project.getTitle())).thenReturn(project);
    }

    @Test
    public void contexLoads() throws Exception {

        assertThat(projectRepository).isNotNull();
        assertThat(userRepository).isNotNull();
        assertThat(taskRepository).isNotNull();
        assertThat(projectController).isNotNull();
    }

    @Test
    public void getAllProjectsAPI() throws Exception {

        this.mockMvc.perform(get("/projects")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("first")));
    }

//    @Test
//    public void createProjectAPI() throws Exception {
//        Project project = new Project("first");
//        mockMvc.perform(MockMvcRequestBuilders.post("/projects").content(asJsonString(new Project("first")))
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated()).andExpect(MockMvcResultMatchers.jsonPath("$.projectId").exists());
//    }
}
