package com.codeup.capstone3dprinting;

import com.codeup.capstone3dprinting.models.*;
import com.codeup.capstone3dprinting.repos.CategoryRepository;
import com.codeup.capstone3dprinting.repos.FileRepository;
import com.codeup.capstone3dprinting.repos.MessageRepository;
import com.codeup.capstone3dprinting.repos.UserRepository;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Capstone3dPrintingApplication.class)
@AutoConfigureMockMvc
public class CapstoneIntegrationTests {

    private User testUser;
    private HttpSession httpSession;
    private File testFile;

    @Autowired
    private MockMvc mvc;

    @Autowired
    UserRepository userDao;

    @Autowired
    MessageRepository messageDao;

    @Autowired
    FileRepository fileDao;

    @Autowired
    CategoryRepository categoryDao;

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
        userDao.delete(userDao.findByUsername("testUser"));
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
    public void testSendAndReadMessage() throws Exception {
        String randStr = UUID.randomUUID().toString();

        //send a message to testUser from testUser
        this.mvc.perform(post("/messages/send").with(csrf())
                .session((MockHttpSession) httpSession)
                .param("recipient", "testUser")
                .param("message", "integration test" + randStr))
                .andExpect(status().is3xxRedirection());

        Message message = messageDao.findByMessage("integration test" + randStr);

        assertNotNull(message);
        assertEquals(message.getRecipient().getId(), userDao.findByUsernameIgnoreCase("testUser").getId());
        assertEquals(message.getSender().getId(), testUser.getId());
        assertTrue(message.isUnread());

        //simulates clicking on message to read
        this.mvc.perform(get("/ajax/read/" + message.getId()).with(csrf())
                .session((MockHttpSession) httpSession));

        //should not be marked unread now
        assertFalse(messageDao.findMessageById(message.getId()).isUnread());

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
                .session((MockHttpSession) httpSession)
                .param("username", "testUser")
                .param("password", "pass"))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("/login?error"))
                .andReturn()
                .getRequest()
                .getSession();

        //user, while logged out, tries to re-post or directly post to change password
        this.mvc.perform(post("/change-password")
                .param("newPassword", "newpass1")
                .param("confirmPassword", "newpass2")
                .param("currentPassword", "pass"))
                .andExpect(status().is4xxClientError());

        //user signs in correctly with the newly changed password
        this.mvc.perform(post("/login").with(csrf())
                .session((MockHttpSession) httpSession)
                .param("username", "testUser")
                .param("password", "newpass1"))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("/"))
                .andReturn()
                .getRequest()
                .getSession();

    }

    @Test
    public void testPasswordRecovery() throws Exception {

        //if logged in, don't need to access the page
        this.mvc.perform(get("/password-recovery").with(csrf())
                .session((MockHttpSession) httpSession))
                .andExpect(redirectedUrl("/"));


        //if you try to post directly while still logged in
        this.mvc.perform(post("/password-recovery").with(csrf())
                .session((MockHttpSession) httpSession)
                .param("email", testUser.getEmail()))
                .andExpect(redirectedUrl("/"));

        //if you aren't logged in, load the correct page
        this.mvc.perform(get("/password-recovery").with(csrf()))
                .andExpect(content().string(containsString("Please enter the email associated with your account")));

        this.mvc.perform(post("/logout").with(csrf()).session((MockHttpSession) httpSession))
                .andExpect(redirectedUrl("/login?logout"));

        assertEquals(testUser.getEmail(), "testUser@codeup.com");

        String oldPassword = testUser.getPassword();

        this.mvc.perform(post("/password-recovery").with(csrf())
                .session((MockHttpSession) httpSession)
                .param("email", testUser.getEmail()))
                .andExpect(redirectedUrl("/password-recovery"));

        String token = userDao.findByUsernameIgnoreCase("testUser").getPassword();
        assertNotEquals(token, oldPassword);

        //incorrect token
        this.mvc.perform(post("/reset").with(csrf())
                .param("token", token + UUID.randomUUID())
                .param("resetNew", "codeup2")
                .param("resetConfirm", "codeup2"))
                .andExpect(redirectedUrl("/users/password-recovery"));

        //correct token, passwords don't match
        this.mvc.perform(post("/reset").with(csrf())
                .param("token", token)
                .param("resetNew", "codeup1")
                .param("resetConfirm", "codeup2"))
                .andExpect(redirectedUrl("/reset?token=" + token));

        //correct token, passwords match
        this.mvc.perform(post("/reset").with(csrf())
                .param("token", token)
                .param("resetNew", "codeup2")
                .param("resetConfirm", "codeup2"))
                .andExpect(redirectedUrl("/login"));

        assertNotEquals(userDao.findByUsernameIgnoreCase("testUser").getPassword(), token);
    }

    //TODO: email verification

    //TODO: signing up process

    //TODO: file tests
    @Test
    public void testFiles() throws Exception {
        testFile = new File();
        testFile.setFileUrl("test");
        testFile.setCreatedAt(new Timestamp(new Date().getTime()));
        testFile.setUpdatedAt(new Timestamp(new Date().getTime()));
        testFile.setTitle("test file title");
        testFile.setDescription("integration test file" + UUID.randomUUID());
        testFile.setFlagged(false);
        testFile.setPrivate(false);
        testFile.setOwner(testUser);

        fileDao.save(testFile);
        //TODO: make tests more atomic
        List<Category> categories = new ArrayList<>();
        categories.add(categoryDao.getOne(1L));
        categories.add(categoryDao.getOne(2L));
        testFile.setCategories(categories);
        testFile.setComments(new ArrayList<>());
        testFile.setImages(new ArrayList<>());
        fileDao.save(testFile);

        //test show file, when public
        this.mvc.perform(get("/files/" + testFile.getId()))
                .andExpect(content().string(containsString(testFile.getDescription())));

        //TODO: test show file when private while not logged in as well as logged in

        //show all files
        this.mvc.perform(get("/files"))
                .andExpect(content().string(containsString("All Files")));

        assertEquals(testFile.getCategories().size(), categories.size());





        //TODO: delete file programmatically

        //clear categories, then delete file
        testFile.setCategories(new ArrayList<>());
        fileDao.save(testFile);
        fileDao.delete(fileDao.findByTitle("test file title"));
    }

    //TODO: notification tests

    //TODO: user tests



}
