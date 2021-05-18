package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartItemDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CartItemMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.CartService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.CartServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.lang.invoke.MethodHandles;
import java.util.List;


@RestController
@RequestMapping(value = "/api/v1/carts")
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
    @Operation(summary = "Add Cart Items")
    public List<CartItemDto> add(@Valid @RequestBody List<CartItemDto> cartDto) {
        LOGGER.info("POST /api/v1/carts body: {}", cartDto);
        return cartItemMapper.cartItemListToCartItemDtoList(cartService.addCart(cartItemMapper.cartItemDtoListToCartItemList(cartDto)));
    }
}
