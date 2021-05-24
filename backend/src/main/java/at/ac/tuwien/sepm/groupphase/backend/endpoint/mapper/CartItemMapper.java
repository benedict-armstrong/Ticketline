package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartItemDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.CartItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface CartItemMapper extends FileTypeMapper {

    CartItem cartItemDtoToCartItem(CartItemDto cartItemDto);

    CartItemDto cartItemToCartItemDto(CartItem cartItem);

    List<CartItemDto> cartItemListToCartItemDtoList(List<CartItem> cartItems);

    List<CartItem> cartItemDtoListToCartItemList(List<CartItemDto> cartItemDtos);

    default Long map(ApplicationUser value) {
        return value.getId();
    }

    default ApplicationUser mapToUser(Long value) {
        return ApplicationUser.builder().id(value).build();
    }

}