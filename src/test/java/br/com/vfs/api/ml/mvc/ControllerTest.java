package br.com.vfs.api.ml.mvc;

import br.com.vfs.api.ml.shared.errors.ErrorMessage;
import br.com.vfs.api.ml.testcontainer.TestContainerTest;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class ControllerTest extends TestContainerTest {

    private static final String HEADER_STRING = "Authorization";
    private static final String URL_USER = "/api/user";
    private static final String URL_LOGIN = "/api/login";
    private static final String URL_CATEGORY = "/api/category";
    private static final String URL_PRODUCT = "/api/product";
    private static final String URL_PRODUCT_IMAGE = "/api/product/1/image";
    private static final String URL_OPINION = "/api/opinion";
    private static final String URL_QUESTION = "/api/question";
    private static final String URL_PURCHASE = "/api/purchase";
    private static final String URL_PAYMENT_PAYPAL = "/api/payment-paypal";
    private static final String URL_PAYMENT_PAGSEGURO = "/api/payment-pagseguro";

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

    @Test
    @Order(8)
    @DisplayName("add image a product")
    void testProductImageOne() throws Exception {
        final var resource = new ClassPathResource("image.jpeg").getInputStream();
        final var file = new MockMultipartFile("file", "image.jpeg", MediaType.IMAGE_JPEG_VALUE, resource.readAllBytes());
        final var builder = multipart(URL_PRODUCT_IMAGE)
                .file(file)
                .header(HEADER_STRING, token)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(APPLICATION_JSON);

        final var resultCreated = mvc.perform(builder).andExpect(status().isOk()).andReturn();
        assertNotNull(resultCreated.getResponse(),"Invalid message return");
    }

    @Test
    @Order(9)
    @DisplayName("create a new opinion associate a product")
    void testOpinionOne() throws Exception {

        final var builder = post(URL_OPINION)
                .content("{\n" +
                        "  \"description\": \"Muito bom\",\n" +
                        "  \"evaluation\": 5,\n" +
                        "  \"idProduct\": 1,\n" +
                        "  \"title\": \"TOPZERA\"\n" +
                        "}")
                .header(HEADER_STRING, token)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
        final var resultCreated = mvc.perform(builder).andExpect(status().isOk()).andReturn();
        assertNotNull(resultCreated.getResponse(),"Invalid message return");
    }

    @Test
    @Order(10)
    @DisplayName("create a new question associate a product")
    void testQuestionOne() throws Exception {
        final var builder = post(URL_QUESTION)
                .content("{\n" +
                        "  \"idProduct\": 1,\n" +
                        "  \"title\": \"TOPZERA\"\n" +
                        "}")
                .header(HEADER_STRING, token)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
        final var resultCreated = mvc.perform(builder).andExpect(status().isOk()).andReturn();
        assertNotNull(resultCreated.getResponse(),"Invalid message return");
    }

    @Test
    @Order(11)
    @DisplayName("find product details by id")
    void testProductDetailOne() throws Exception {
        final var builder = get(URL_PRODUCT + "/1")
                .header(HEADER_STRING, token)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
        final var resultCreated = mvc.perform(builder).andExpect(status().isOk()).andReturn();
        assertNotNull(resultCreated.getResponse().getContentAsString(),"Invalid message return");
    }

    @Test
    @Order(12)
    @DisplayName("purchase from product")
    void testPurchaseOne() throws Exception {
        final var builder = post(URL_PURCHASE)
                .content("{\n" +
                        "  \"gateway\": \"PAYPAL\",\n" +
                        "  \"idProduct\": 1,\n" +
                        "  \"quantity\": 20\n" +
                        "}")
                .header(HEADER_STRING, token)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
        final var resultCreated = mvc.perform(builder).andExpect(status().isFound()).andReturn();
        assertNotNull(resultCreated.getResponse().getContentAsString(),"Invalid message return");
    }

    @Test
    @Order(13)
    @DisplayName("invalid purchase from product by quantity no in stoke")
    void testPurchaseTwo() throws Exception {
        final var builder = post(URL_PURCHASE)
                .content("{\n" +
                        "  \"gateway\": \"PAYPAL\",\n" +
                        "  \"idProduct\": 1,\n" +
                        "  \"quantity\": 2000\n" +
                        "}")
                .header(HEADER_STRING, token)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
        final var resultCreated = mvc.perform(builder).andExpect(status().isBadRequest()).andReturn();
        assertNotNull(resultCreated.getResponse().getContentAsString(),"Invalid message return");
    }

    @Test
    @Order(14)
    @DisplayName("paypal fail payment from purchase")
    void testPaymentPaypalOne() throws Exception {
        final var builder = post(URL_PAYMENT_PAYPAL)
                .content("{\n" +
                        "  \"idPurchase\": 1,\n" +
                        "  \"codePayment\": \"11234567\",\n" +
                        "  \"status\": 0\n" +
                        "}")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
        final var resultCreated = mvc.perform(builder).andExpect(status().isOk()).andReturn();
        assertNotNull(resultCreated.getResponse().getContentAsString(),"Invalid message return");
    }

    @Test
    @Order(15)
    @DisplayName("paypal sucess payment from purchase")
    void testPaymentPaypalTwo() throws Exception {
        final var builder = post(URL_PAYMENT_PAYPAL)
                .content("{\n" +
                        "  \"idPurchase\": 1,\n" +
                        "  \"codePayment\": \"112345671231\",\n" +
                        "  \"status\": 1\n" +
                        "}")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
        final var resultCreated = mvc.perform(builder).andExpect(status().isOk()).andReturn();
        assertNotNull(resultCreated.getResponse().getContentAsString(),"Invalid message return");
    }

    @Test
    @Order(16)
    @DisplayName("fail payment by purchase is finally")
    void testPaymentPaypalThree() throws Exception {
        final var builder = post(URL_PAYMENT_PAYPAL)
                .content("{\n" +
                        "  \"idPurchase\": 1,\n" +
                        "  \"codePayment\": \"1123456712312\",\n" +
                        "  \"status\": 1\n" +
                        "}")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
        final var resultCreated = mvc.perform(builder).andExpect(status().isBadRequest()).andReturn();
        assertNotNull(resultCreated.getResponse().getContentAsString(),"Invalid message return");
    }

    @Test
    @Order(17)
    @DisplayName("purchase from product")
    void testPurchaseThree() throws Exception {
        final var builder = post(URL_PURCHASE)
                .content("{\n" +
                        "  \"gateway\": \"PAGSEGURO\",\n" +
                        "  \"idProduct\": 1,\n" +
                        "  \"quantity\": 20\n" +
                        "}")
                .header(HEADER_STRING, token)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
        final var resultCreated = mvc.perform(builder).andExpect(status().isFound()).andReturn();
        assertNotNull(resultCreated.getResponse().getContentAsString(),"Invalid message return");
    }

    @Test
    @Order(18)
    @DisplayName("payment error by purchase")
    void testPaymentPagSeguroOne() throws Exception {
        final var builder = post(URL_PAYMENT_PAGSEGURO)
                .content("{\n" +
                        "  \"idPurchase\": 2,\n" +
                        "  \"codePayment\": \"432asd23423\",\n" +
                        "  \"status\": \"ERRO\"\n" +
                        "}")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
        final var resultCreated = mvc.perform(builder).andExpect(status().isOk()).andReturn();
        assertNotNull(resultCreated.getResponse().getContentAsString(),"Invalid message return");
    }

    @Test
    @Order(19)
    @DisplayName("fail payment exit process")
    void testPaymentPagSeguroTwo() throws Exception {
        final var builder = post(URL_PAYMENT_PAGSEGURO)
                .content("{\n" +
                        "  \"idPurchase\": 2,\n" +
                        "  \"codePayment\": \"432asd23423\",\n" +
                        "  \"status\": \"ERRO\"\n" +
                        "}")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
        final var resultCreated = mvc.perform(builder).andExpect(status().isBadRequest()).andReturn();
        assertNotNull(resultCreated.getResponse().getContentAsString(),"Invalid message return");
    }

    @Test
    @Order(20)
    @DisplayName("payment success by purchase")
    void testPaymentPagSeguroThree() throws Exception {
        final var builder = post(URL_PAYMENT_PAGSEGURO)
                .content("{\n" +
                        "  \"idPurchase\": 2,\n" +
                        "  \"codePayment\": \"432asd23423asda\",\n" +
                        "  \"status\": \"SUCESSO\"\n" +
                        "}")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
        final var resultCreated = mvc.perform(builder).andExpect(status().isOk()).andReturn();
        assertNotNull(resultCreated.getResponse().getContentAsString(),"Invalid message return");
    }

    @Test
    @Order(21)
    @DisplayName("mock system invoice")
    void testOtherSystemMockOne() throws Exception {
        final var builder = post("/mock/invoice")
                .content("{\n" +
                        "  \"idPurchase\": \"2\",\n" +
                        "  \"idBuyer\": 1\n" +
                        "}")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
        final var resultCreated = mvc.perform(builder).andExpect(status().isOk()).andReturn();
        assertNotNull(resultCreated.getResponse().getContentAsString(),"Invalid message return");
    }

    @Test
    @Order(22)
    @DisplayName("mock system ranking vendor")
    void testOtherSystemMockTwo() throws Exception {
        final var builder = post("/mock/ranking-vendor")
                .content("{\n" +
                        "  \"idPurchase\": \"2\",\n" +
                        "  \"idVendor\": 1\n" +
                        "}")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
        final var resultCreated = mvc.perform(builder).andExpect(status().isOk()).andReturn();
        assertNotNull(resultCreated.getResponse().getContentAsString(),"Invalid message return");
    }

}
