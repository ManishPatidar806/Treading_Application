package com.manish.service;

import com.manish.domain.ORDER_TYPE;
import com.manish.model.Coin;
import com.manish.model.Order;
import com.manish.model.OrderItem;
import com.manish.model.User;
import org.aspectj.weaver.ast.Or;

import java.util.List;

public interface OrderService  {

    Order createOrder(User user, OrderItem orderItem, ORDER_TYPE orderType);
    Order getOrderById(Long orderId);

    List<Order> getAllOrderOfUser(Long userId, ORDER_TYPE orderType , String assetSymbol);

    Order processOrder(Coin coin , double quantity, ORDER_TYPE orderType, User user);


}
