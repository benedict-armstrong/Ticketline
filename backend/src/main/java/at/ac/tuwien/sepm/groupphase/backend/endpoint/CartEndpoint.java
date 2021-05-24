package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartItemDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CartItemMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.CartItem;
import at.ac.tuwien.sepm.groupphase.backend.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.lang.invoke.MethodHandles;
import java.util.List;


@RestController
@RequestMapping(value = "/api/v1/cart")
public class CartEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final CartService cartService;
    private final CartItemMapper cartItemMapper;

    @Autowired
    public CartEndpoint(CartService cartService, CartItemMapper cartItemMapper) {
        this.cartItemMapper = cartItemMapper;
        this.cartService = cartService;
    }

    @PostMapping
    @PermitAll
    @Operation(summary = "Add Cart Item")
    public CartItemDto add(@Valid @RequestBody CartItemDto cartItemDto) {
        LOGGER.info("POST /api/v1/cart {}", cartItemDto);
        return cartItemMapper.cartItemToCartItemDto(cartService.addCartItem(cartItemMapper.cartItemDtoToCartItem(cartItemDto)));
    }

    @GetMapping
    @PermitAll
    @Operation(summary = "Get Cart Items")
    public List<CartItemDto> getCart(@Valid @RequestBody Long userId) {
        LOGGER.info("Get /api/v1/cart {}", userId);
        return cartItemMapper.cartItemListToCartItemDtoList(cartService.getCart(userId));
    }

    @GetMapping("/{userId}")
    @PermitAll
    @Operation(summary = "Get Cart Items")
    public List<CartItemDto> getCartPathVariable(@PathVariable Long userId) {
        LOGGER.info("Get /api/v1/cart {}", userId);
        return cartItemMapper.cartItemListToCartItemDtoList(cartService.getCart(userId));
    }

    @PutMapping
    @PermitAll
    @Operation(summary = "Update Cart Item")
    public CartItemDto update(@Valid @RequestBody CartItemDto cartItemDto) {
        LOGGER.info("PUT /api/v1/cart {}", cartItemDto);
        return cartItemMapper.cartItemToCartItemDto(cartService.updateCartItem(cartItemMapper.cartItemDtoToCartItem(cartItemDto)));
    }
}
