package com.example.AutumnMall.controller;

import com.example.AutumnMall.domain.CartItem;
import com.example.AutumnMall.dto.AddCartItemDto;
import com.example.AutumnMall.security.jwt.util.IfLogin;
import com.example.AutumnMall.security.jwt.util.LoginUserDto;
import com.example.AutumnMall.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/cartItems")
@RestController
@RequiredArgsConstructor
public class CartItemController {
    private final CartItemService cartItemService;

    @PostMapping
    public CartItem addCartItem(@IfLogin LoginUserDto loginUserDto, @RequestBody AddCartItemDto addCartItemDto){
        try {
         //    같은 cart에 같은 product가 있으면 quantity를 더해줘야함
                if(cartItemService.isCartItemExist(loginUserDto.getMemberId(), addCartItemDto.getCartId(), addCartItemDto.getProductId())){
                   CartItem cartItem = cartItemService.getCartItem(loginUserDto.getMemberId(), addCartItemDto.getCartId(), addCartItemDto.getProductId());
                 cartItem.setQuantity(cartItem.getQuantity() + addCartItemDto.getQuantity());
                return cartItemService.updateCartItem(cartItem);
             }

            return cartItemService.addCartItem(addCartItemDto);
        }catch(Exception ex){
            ex.printStackTrace();
            return null;

        }
    }

    @GetMapping
    public List<CartItem> getCartItems(@IfLogin LoginUserDto loginUserDto, @RequestParam(required = false) Long cartId) {
        if(cartId == null)
            return cartItemService.getCartItems(loginUserDto.getMemberId());
        return cartItemService.getCartItems(loginUserDto.getMemberId(), cartId);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity deleteCartItem(@IfLogin LoginUserDto loginUserDto, AddCartItemDto addCartItemDto){
        if(cartItemService.isCartItemExist(loginUserDto.getMemberId(), addCartItemDto.getCartId()) == false)
            return ResponseEntity.badRequest().build();
        cartItemService.deleteCartItem(loginUserDto.getMemberId(), addCartItemDto.getCartId());
        return ResponseEntity.ok().build();
    }
}
