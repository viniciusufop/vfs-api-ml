package br.com.vfs.api.ml.mvc;

import br.com.vfs.api.ml.shared.errors.ErrorMessage;
import br.com.vfs.api.ml.testcontainer.TestContainerMysqlTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class ControllerTest extends TestContainerMysqlTest {

    private static final String HEADER_STRING = "Authorization";
    private static final String URL_USER = "/api/user";
    private static final String URL_LOGIN = "/api/login";
    private static final String URL_CATEGORY = "/api/category";
    private static final String URL_PRODUCT = "/api/product";

    private static String token;
    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @Order(1)
    @DisplayName("create a new user")
    void testUserOne() throws Exception {
        final var builder = post(URL_USER)
                        .content("{ \"login\":\"test@mock.com\", \"password\":\"123456\" }")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON);
        final var resultCreated = mvc.perform(builder).andExpect(status().isOk()).andReturn();
        assertNotNull(resultCreated.getResponse(),"Invalid message return");
    }

    @Test
    @Order(2)
    @DisplayName("not create a new user with duplicate login")
    void testUserTwo() throws Exception {
        final var builder = post(URL_USER)
                .content("{ \"login\":\"test@mock.com\", \"password\":\"123456\" }")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
        final var resultFail = mvc.perform(builder).andExpect(status().isBadRequest()).andReturn();
        final var errorMessage = objectMapper.readValue(resultFail.getResponse().getContentAsString(), ErrorMessage.class);
        assertFalse(errorMessage.getErrors().isEmpty(), "Return fail");
    }

    @Test
    @Order(3)
    @DisplayName("login a user")
    void testLoginOne() throws Exception {
        final var builder = post(URL_LOGIN)
                .content("{ \"username\":\"test@mock.com\", \"password\":\"123456\" }")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
        final var resultCreated = mvc.perform(builder).andExpect(status().isOk()).andReturn();
        assertNotNull(resultCreated.getResponse().getHeader(HEADER_STRING),"Invalid message return");
        token = resultCreated.getResponse().getHeader(HEADER_STRING);
    }

    @Test
    @Order(4)
    @DisplayName("invalid authentication")
    void testCategoryOne() throws Exception {
        final var builder = post(URL_CATEGORY)
                .content("{ \"name\":\"Phone\" }")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
        final var resultCreated = mvc.perform(builder).andExpect(status().isForbidden()).andReturn();
        assertNotNull(resultCreated.getResponse(),"Invalid message return");
    }

    @Test
    @Order(5)
    @DisplayName("create a new category")
    void testCategoryTwo() throws Exception {
        final var builder = post(URL_CATEGORY)
                .content("{ \"name\":\"Phone\" }")
                .header(HEADER_STRING, token)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
        final var resultCreated = mvc.perform(builder).andExpect(status().isOk()).andReturn();
        assertNotNull(resultCreated.getResponse(),"Invalid message return");
    }

    @Test
    @Order(6)
    @DisplayName("not create a new category with duplicate name")
    void testCategoryThree() throws Exception {
        final var builder = post(URL_CATEGORY)
                .content("{ \"name\":\"Phone\" }")
                .header(HEADER_STRING, token)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
        final var resultFail = mvc.perform(builder).andExpect(status().isBadRequest()).andReturn();
        final var errorMessage = objectMapper.readValue(resultFail.getResponse().getContentAsString(), ErrorMessage.class);
        assertFalse(errorMessage.getErrors().isEmpty(), "Return fail");
    }

    @Test
    @Order(7)
    @DisplayName("create a new product")
    void testProductOne() throws Exception {
        final var builder = post(URL_PRODUCT)
                .content("{\n" +
                        "    \"name\": \"Xiaomi MI 9\",\n" +
                        "    \"description\": \"# Celular Top\",\n" +
                        "    \"features\": [\n" +
                        "        {\"name\": \"Marca\", \"value\": \"Xiaomi\"},\n" +
                        "        {\"name\": \"Modelo\", \"value\": \"Mi 9\"},\n" +
                        "        {\"name\": \"Memoria RAM\", \"value\": \"128Gb\"}\n" +
                        "    ],\n" +
                        "    \"price\": 2300.00,\n" +
                        "    \"quantity\": 140,\n" +
                        "    \"idCategory\": 1\n" +
                        "}")
                .header(HEADER_STRING, token)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
        final var resultCreated = mvc.perform(builder).andExpect(status().isOk()).andReturn();
        assertNotNull(resultCreated.getResponse(),"Invalid message return");
    }
}
