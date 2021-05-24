package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartItemDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.CartItem;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CartItemMapper implements FileTypeMapper {

    @Autowired
    protected UserService userService;

    @Mapping(target = "user", expression = "java(userService.findUserById(cartItemDto.getUserId()))")
    public abstract CartItem cartItemDtoToCartItem(CartItemDto cartItemDto);

    @Mapping(target = "userId", expression = "java(cartItem.getUser().getId())")
    public abstract CartItemDto cartItemToCartItemDto(CartItem cartItem);

    public abstract List<CartItemDto> cartItemListToCartItemDtoList(List<CartItem> cartItems);
}