package com.codeup.capstone3dprinting;

import com.codeup.capstone3dprinting.models.*;
import com.codeup.capstone3dprinting.repos.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Capstone3dPrintingApplication.class)
@AutoConfigureMockMvc
public class IntegrationTests {

    private User testUser;
    private HttpSession httpSession;
    private File testFile;

    @Autowired
    private MockMvc mvc;

    @Autowired
    UserRepository userDao;

    @Autowired
    FileRepository fileDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Before
    public void setup() throws Exception {

        testUser = userDao.findByUsername("testUser");

        // Creates the test user if not exists
        if (testUser == null) {
            User newUser = new User();
            newUser.setUsername("testUser");
            newUser.setId(20L);
            newUser.setPassword(passwordEncoder.encode("pass"));
            newUser.setEmail("testUser@codeup.com");
            newUser.setAvatarUrl("");
            newUser.setFirstName("Testy");
            newUser.setLastName("Testerson");
            newUser.setActive(true);
            newUser.setAdmin(false);
            newUser.setVerified(true);
            newUser.setJoinedAt(new Timestamp(0));
            newUser.setFlagged(false);
            newUser.setPrivate(true);

            testUser = userDao.save(newUser);

            testFile = fileDao.findByTitle("TestFile");
            if(testFile == null){
                File newFile = new File();
                newFile.setId(30L);
                newFile.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                newFile.setDescription("testing file");
                newFile.setFileUrl("something");
                newFile.setFlagged(false);
                newFile.setPrivate(false);
                newFile.setTitle("TestFile");
                newFile.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                newFile.setOwner(testUser);

                testFile = fileDao.save(newFile);
            }
        }

        // Throws a Post request to /login and expect a redirection to the files index page after being logged in
        httpSession = this.mvc.perform(
                post("/login").with(csrf())
                .param("username", "testUser")
                .param("password", "pass"))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("/"))
                .andReturn()
                .getRequest()
                .getSession();



    }


    @After
    public void postTest() {
        fileDao.delete(fileDao.findByTitle("TestFile"));
        userDao.delete(userDao.findByUsername("testUser"));
    }

    @Test
    public void contextLoads() {
        // Sanity Test, just to make sure the MVC bean is working
        assertNotNull(mvc);
    }

    @Test
    public void testIfUserSessionIsActive() throws Exception{
        // It makes sure the returned session is not null
        assertNotNull(httpSession);
    }

    @Test
    public void testShowFileWhenPrivateLoggedOut() throws Exception {
        File existingFile = fileDao.getOne(14L);
        this.mvc.perform(get("/privateFile/" + existingFile.getId()).with(csrf()))
                .andExpect(content().string(containsString("private")));
    }
    @Test
    public void showFileEditFormTestWhileLoggedIn()throws Exception{
        File existingFile = fileDao.getOne(30L);
        this.mvc.perform(get("/files/" + existingFile.getId() +"/edit").with(csrf())
                .session((MockHttpSession) httpSession)
                .contentType("application/x-www-form-urlencoded")
                .param("file",testFile.getTitle()))
                .andExpect(content().string(containsString("edit")));
    }


}

