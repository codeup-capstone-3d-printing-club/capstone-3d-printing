package com.codeup.capstone3dprinting;

import com.codeup.capstone3dprinting.models.Message;
import com.codeup.capstone3dprinting.models.User;
import com.codeup.capstone3dprinting.repos.MessageRepository;
import com.codeup.capstone3dprinting.repos.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Capstone3dPrintingApplication.class)
@AutoConfigureMockMvc
public class CapstoneIntegrationTests {

    private User testUser;
    private HttpSession httpSession;

    @Autowired
    private MockMvc mvc;

    @Autowired
    UserRepository userDao;

    @Autowired
    MessageRepository messageDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Before
    public void setup() throws Exception {

        testUser = userDao.findByUsernameIgnoreCase("testUser");

        // Creates the test user if not exists
        if(testUser == null){
            User newUser = new User();
            newUser.setUsername("testUser");
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

            testUser = userDao.save(newUser);
        }

        // Throws a Post request to /login and expect a redirection to the Ads index page after being logged in
        httpSession = this.mvc.perform(post("/login").with(csrf())
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
        userDao.delete(userDao.findByUsernameIgnoreCase("testUser"));
    }

    @Test
    public void contextLoads() {
        // Sanity Test, just to make sure the MVC bean is working
        assertNotNull(mvc);
    }

    @Test
    public void testIfUserSessionIsActive() throws Exception {
        // It makes sure the returned session is not null
        assertNotNull(httpSession);
    }

    @Test
    public void testSendMessage() throws Exception {
        String randStr = UUID.randomUUID().toString();

        this.mvc.perform(post("/messages/send").with(csrf())
                .session((MockHttpSession) httpSession)
                .param("recipient", "admin")
                .param("message", "integration test" + randStr))
                .andExpect(status().is3xxRedirection());

        Message message = messageDao.findByMessage("integration test" + randStr);
        assertNotNull(message);
        assertEquals(message.getRecipient().getId(), userDao.findByUsernameIgnoreCase("admin").getId());
        assertEquals(message.getSender().getId(), testUser.getId());

        messageDao.delete(message);
    }

    @Test
    public void testChangePassword() throws Exception {

        //when current password is entered in wrong
        this.mvc.perform(post("/change-password").with(csrf())
                .session((MockHttpSession) httpSession)
                .param("newPassword", "newpass1")
                .param("confirmPassword", "newpass1")
                .param("currentPassword", "password"))
                .andExpect(redirectedUrl("/messages"));

        //when new password doesn't match with confirm
        this.mvc.perform(post("/change-password").with(csrf())
                .session((MockHttpSession) httpSession)
                .param("newPassword", "newpass1")
                .param("confirmPassword", "newpass2")
                .param("currentPassword", "pass"))
                .andExpect(redirectedUrl("/messages"));

        //should result in successful password change, then log the user out
        this.mvc.perform(post("/change-password").with(csrf())
                .session((MockHttpSession) httpSession)
                .param("newPassword", "newpass1")
                .param("confirmPassword", "newpass1")
                .param("currentPassword", "pass"))
                .andExpect(redirectedUrl("/logout-change"));

        //the user tries to log in with the wrong password
        this.mvc.perform(post("/login").with(csrf())
                .param("username", "testUser")
                .param("password", "pass"))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("/login?error"))
                .andReturn()
                .getRequest()
                .getSession();

        //user signs in correctly with the newly changed password
        this.mvc.perform(post("/login").with(csrf())
                .param("username", "testUser")
                .param("password", "newpass1"))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("/"))
                .andReturn()
                .getRequest()
                .getSession();

    }



//    @Test
//    public void testSigningUpWhenUsernameOrEmailAlreadyExists() throws Exception {
//
//    }


}
