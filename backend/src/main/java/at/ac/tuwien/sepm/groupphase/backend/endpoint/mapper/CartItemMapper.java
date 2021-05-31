package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartItemDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.CartItem;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(uses = {UserMapper.class, TicketMapper.class})
public interface CartItemMapper {

    CartItem cartItemDtoToCartItem(CartItemDto cartItemDto);

    CartItemDto cartItemToCartItemDto(CartItem cartItem);

    CartItemDto[] cartItemSetToCartItemDtoArray(Set<CartItem> cartItemSet);

    List<CartItemDto> cartItemListToCartItemDtoList(List<CartItem> cartItemList);
}
