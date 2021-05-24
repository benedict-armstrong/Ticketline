package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataCart;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartItemDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NewsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CartItemMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.CartItem;
import at.ac.tuwien.sepm.groupphase.backend.repository.CartRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CartEndpointTest implements TestDataCart {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemMapper cartItemMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private CartItem cartItem;

    private ApplicationUser user;
    private ApplicationUser user2;

    @BeforeEach
    public void beforeEach() {
        user = userRepository.save(CART_USER);
        user2 = userRepository.save(CART_USER2);
        cartItem = CartItem.builder()
            .user(user)
            .ticketId(CART_TICKET_ID)
            .amount(CART_AMOUNT)
            .creationDate(LocalDateTime.now())
            .build();
    }

    @AfterEach
    public void afterEach() {
        cartRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void whenPostCartItem_ThenCartItemWithId() throws Exception {

        String body = objectMapper.writeValueAsString(cartItemMapper.cartItemToCartItemDto(cartItem));

        MvcResult mvcResult = this.mockMvc.perform(post(CART_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        CartItemDto cartItemResponse = objectMapper.readValue(response.getContentAsString(),
            CartItemDto.class);

        assertNotNull(cartItemResponse.getId());

    }

    @Test
    public void whenPostCartItem_WithMissingUser_ThenValidationException() throws Exception {

        CartItemDto cartItemDto = cartItemMapper.cartItemToCartItemDto(cartItem);
        cartItemDto.setUserId(null);

        String body = objectMapper.writeValueAsString(cartItemDto);

        MvcResult mvcResult = this.mockMvc.perform(post(CART_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void whenPostCartItem_WithNegativeAmount_ThenValidationException() throws Exception {

        CartItemDto cartItemDto = cartItemMapper.cartItemToCartItemDto(cartItem);
        cartItemDto.setAmount(-5);

        String body = objectMapper.writeValueAsString(cartItemDto);

        MvcResult mvcResult = this.mockMvc.perform(post(CART_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void AfterInsertingTwoCartItems_whenGetCart_ThenCartWithLengthOfTwo() throws Exception {

        String body = objectMapper.writeValueAsString(cartItemMapper.cartItemToCartItemDto(cartItem));

        MvcResult mvcResult = this.mockMvc.perform(post(CART_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        cartItem.setTicketId(CART_TICKET_ID + 1);
        body = objectMapper.writeValueAsString(cartItemMapper.cartItemToCartItemDto(cartItem));

        mvcResult = this.mockMvc.perform(post(CART_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andReturn();
        response = mvcResult.getResponse();

        body = objectMapper.writeValueAsString(user.getId());

        mvcResult = this.mockMvc.perform(get(CART_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andReturn();
        response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<CartItemDto> cartItemResponse = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), CartItemDto[].class)
        );
        assertAll(
            () -> assertEquals(2, cartItemResponse.size()),
            () -> assertNotNull(cartItemResponse.get(0).getId())
        );
    }

    @Test
    public void AfterInsertingTwoCartItemsWithSameTicketId_whenGetCart_ThenCartWithLengthOfOne() throws Exception {

        String body = objectMapper.writeValueAsString(cartItemMapper.cartItemToCartItemDto(cartItem));

        MvcResult mvcResult = this.mockMvc.perform(post(CART_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        mvcResult = this.mockMvc.perform(post(CART_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andReturn();
        response = mvcResult.getResponse();

        body = objectMapper.writeValueAsString(user.getId());

        mvcResult = this.mockMvc.perform(get(CART_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andReturn();
        response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<CartItemDto> cartItemResponse = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), CartItemDto[].class)
        );
        assertAll(
            () -> assertEquals(1, cartItemResponse.size()),
            () -> assertNotNull(cartItemResponse.get(0).getId())
        );
    }

    @Test
    public void AfterInsertingTwoCartItems_whenGetCartButWrongUser_ThenCartEmpty() throws Exception {

        String body = objectMapper.writeValueAsString(cartItemMapper.cartItemToCartItemDto(cartItem));

        MvcResult mvcResult = this.mockMvc.perform(post(CART_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        mvcResult = this.mockMvc.perform(post(CART_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andReturn();
        response = mvcResult.getResponse();

        body = objectMapper.writeValueAsString(user2.getId());

        mvcResult = this.mockMvc.perform(get(CART_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andReturn();
        response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<CartItemDto> cartItemResponse = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), CartItemDto[].class)
        );
        assertAll(
            () -> assertEquals(0, cartItemResponse.size())
        );
    }

    @Test
    public void GivenACartItem_whenUpdateAmount_ThenNewCartItemWithAmount() throws Exception {
        cartRepository.save(cartItem);
        cartItem.setAmount(CART_AMOUNT + 10);
        String body = objectMapper.writeValueAsString(cartItemMapper.cartItemToCartItemDto(cartItem));

        MvcResult mvcResult = this.mockMvc.perform(put(CART_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        CartItemDto cartItemResponse = objectMapper.readValue(response.getContentAsString(), CartItemDto.class);
        assertAll(
            () -> assertEquals(CART_AMOUNT + 10, cartItemResponse.getAmount())
        );
    }
}
