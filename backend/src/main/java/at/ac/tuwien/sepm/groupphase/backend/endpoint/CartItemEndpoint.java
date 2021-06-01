package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartItemDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CartItemMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.CartItem;
import at.ac.tuwien.sepm.groupphase.backend.service.CartItemService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/cartItems")
public class CartItemEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final CartItemService cartItemService;
    private final CartItemMapper cartItemMapper;

    @Autowired
    public CartItemEndpoint(CartItemService cartItemService, CartItemMapper cartItemMapper) {
        this.cartItemService = cartItemService;
        this.cartItemMapper = cartItemMapper;
    }

    @GetMapping
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all cartItems in cart from user")
    public List<CartItemDto> getCartTickets() {
        LOGGER.info("GET /api/v1/cartItems");
        return cartItemMapper.cartItemListToCartItemDtoList(cartItemService.getCartItems(CartItem.Status.IN_CART));
    }

    @PostMapping(path = "/{amount}")
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a cartItem in cart of user")
    public CartItemDto createCartTicket(@RequestBody CartItemDto cartItemDto, @PathVariable int amount) {
        LOGGER.info("POST /api/v1/cartItems {}", cartItemDto);
        return cartItemMapper.cartItemToCartItemDto(cartItemService.save(
            cartItemMapper.cartItemDtoToCartItem(cartItemDto), CartItem.Status.IN_CART, amount
        ));
    }

    @PostMapping(path = "/{id}/addTicket")
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a ticket to a cartItem in cart of user")
    public CartItemDto addTicket(@PathVariable Long id) {
        LOGGER.info("POST /api/v1/{}/addTicket", id);
        return cartItemMapper.cartItemToCartItemDto(cartItemService.addTicket(
            id
        ));
    }

    @PutMapping("/checkout")
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buys tickets in cartItems from cart")
    public boolean checkout() {
        LOGGER.info("PUT /api/v1/cartItems/checkout");
        return cartItemService.checkout();
    }

    /*
    @PutMapping(path = "/{id}/cancel")
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Cancel a cartItem")
    public OrderDto cancelOrder(@PathVariable Long id) {
        LOGGER.info("POST /api/v1/cartItems/{}/cancel", id);
        return orderMapper.orderToOrderDto(orderService.cancel(id));
    }*/

    @DeleteMapping(path = "/{id}")
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete cartItem")
    public boolean delete(@PathVariable Long id) {
        LOGGER.info("DELETE /api/v1/cartItems/{}", id);
        return cartItemService.delete(id);
    }

    @DeleteMapping(path = "/{id}/{ticketId}")
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete ticket from cartItem")
    public boolean delete(@PathVariable Long id, @PathVariable Long ticketId) {
        LOGGER.info("DELETE /api/v1/cartItems/{}/{}", id, ticketId);
        return cartItemService.deleteTicket(id, ticketId);
    }

    @GetMapping("/paid")
    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get cartItems from user that have been paid for")
    public List<CartItemDto> getPaidCartItems() {
        LOGGER.info("GET /api/v1/cartItems/paid");
        return cartItemMapper.cartItemListToCartItemDtoList(cartItemService.getCartItems(CartItem.Status.PAID_FOR));
    }
}
