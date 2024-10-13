package com.manish.controller;


import com.manish.domain.ORDER_TYPE;
import com.manish.model.Coin;
import com.manish.model.Order;
import com.manish.model.User;
import com.manish.model.WalletTransaction;
import com.manish.request.CreateOrderRequest;
import com.manish.service.CoinService;
import com.manish.service.OrderService;
import com.manish.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private CoinService coinService;

//    @Autowired
//    private WalletTransactionService walletTransactionService;
//

   @PostMapping("/pay")
    public ResponseEntity<Order> payOrderPayment(
       @RequestHeader("Authorization") String jwt,
               @RequestBody CreateOrderRequest req){
       User user = userService.findUserProfileByJwt(jwt);
       Coin coin = coinService.findById(req.getCoinId());


       Order order = orderService.processOrder(coin, req.getQuantity(), req.getOrderType(),user);
       return ResponseEntity.ok(order);


    }


    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable Long orderId
    ) throws Exception {

       User user= userService.findUserProfileByJwt(jwtToken);
       Order order = orderService.getOrderById(orderId);
       if(order.getUser().getId().equals(user.getId())){

           return ResponseEntity.ok(order);
       }else{
           throw  new Exception("You don't have access");

       }
    }

    @GetMapping("/getAllOrder")
    public ResponseEntity<List<Order>> getAllOrdersForUser(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(required = false) ORDER_TYPE order_type,
            @RequestParam(required = false) String asset_symbol
    ){
        Long userId= userService.findUserProfileByJwt(jwt).getId();
         List<Order> userOrders = orderService.getAllOrderOfUser(userId,order_type,asset_symbol);
         return ResponseEntity.ok(userOrders);




   }





}
