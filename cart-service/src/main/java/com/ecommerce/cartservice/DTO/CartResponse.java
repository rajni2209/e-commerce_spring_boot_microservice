package com.ecommerce.cartservice.DTO;

import com.ecommerce.cartservice.model.Cart;
import com.ecommerce.cartservice.model.CartItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {

    private List<CartItems> items;

    public static CartResponse from(Cart cart) {
        return new CartResponse(cart.getItems());
    }

}
