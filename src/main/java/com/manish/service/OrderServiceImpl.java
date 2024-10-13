package com.manish.service;

import com.manish.domain.ORDER_STATUS;
import com.manish.domain.ORDER_TYPE;
import com.manish.model.*;
import com.manish.repository.OrderItemRepository;
import com.manish.repository.OrderRepository;
import lombok.SneakyThrows;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private AssetService assetService;



    @Override
    public Order createOrder(User user, OrderItem orderItem, ORDER_TYPE orderType) {
        double price = orderItem.getCoin().getCurrentPrice()*orderItem.getQuantity();
        Order order = new Order();
        order.setUser(user);
        order.setOrderType(orderType);
        order.setOrderItem(orderItem);
        order.setPrice(BigDecimal.valueOf(price));
        order.setTimestamp(LocalDateTime.now());
        order.setStatus(ORDER_STATUS.PENDING);

        return orderRepository.save(order);
    }

    @SneakyThrows
    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(()->new Exception("Order not found"));
    }

    @Override
    public List<Order> getAllOrderOfUser(Long userId, ORDER_TYPE orderType, String assetSymbol) {
        return orderRepository.findByUserId(userId);
    }


    private OrderItem createOrderItem(Coin coin , double quantity ,double buyPrice ,double sellPrice) {
        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(coin);
        orderItem.setQuantity(quantity);
        orderItem.setBuyPrice(buyPrice);
        orderItem.setSellPrice(sellPrice);
        return orderItemRepository.save(orderItem);
    }
    @SneakyThrows
    @Transactional
    public Order buyAsset(Coin coin, double quantity,User user){

        if (quantity<=0){
            throw new Exception("Quantity should be Greater than Zero......");
        }
        double buyPrice = coin.getCurrentPrice();

        OrderItem orderItem = createOrderItem(coin,quantity,buyPrice,0);

        Order order = createOrder(user,orderItem,ORDER_TYPE.Buy);
        orderItem.setOrder(order);

        walletService.payOrderPayment(order,user);
        order.setStatus(ORDER_STATUS.SUCCESS);
        order.setOrderType(ORDER_TYPE.Buy);
        Order savedOrder = orderRepository.save(order);


//Create Assest

        Asset oldAsset = assetService.findAssetByUserIdAndCoinId(order.getUser().getId(),order.getOrderItem().getCoin().getId());


        if(oldAsset==null){
            assetService.createAsset(user,orderItem.getCoin(),orderItem.getQuantity());

        }else{
            assetService.updateAsset(oldAsset.getId(),quantity);
        }


        return savedOrder;

    }
    @SneakyThrows
    @Transactional
    public Order sellAsset(Coin coin, double quantity,User user){

        if (quantity<=0){
            throw new Exception("Quantity should be Greater than Zero......");
        }
        double sellPrice = coin.getCurrentPrice();

        Asset assetToSell = assetService.findAssetByUserIdAndCoinId(user.getId(),coin.getId());
        if(assetToSell!=null) {

            double buyPrice = assetToSell.getBuyprice();

            OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, sellPrice);

            Order order = createOrder(user, orderItem, ORDER_TYPE.Sell);


            orderItem.setOrder(order);

            if (assetToSell.getQuantity() >= quantity) {
                order.setStatus(ORDER_STATUS.SUCCESS);
                order.setOrderType(ORDER_TYPE.Sell);
                Order savedOrder = orderRepository.save(order);

                walletService.payOrderPayment(order, user);

                Asset updatedAsset = assetService.updateAsset(assetToSell.getId(), -quantity);
                if (updatedAsset.getQuantity() * coin.getCurrentPrice() <= 1) {
                    assetService.deleteAsset(updatedAsset.getId());
                }

                return savedOrder;
            }
            throw new Exception("Insufficient Quantity to Sell");

        }throw new Exception("Asset not Found");


    }
    @SneakyThrows
@Transactional
    @Override
    public Order processOrder(Coin coin, double quantity, ORDER_TYPE orderType, User user) {
        if(orderType.equals(ORDER_TYPE.Buy)){
            return buyAsset(coin,quantity,user);
        }else if(orderType.equals(ORDER_TYPE.Sell)){
            return sellAsset(coin,quantity,user);
        }

        throw  new Exception("Invalid Order Type...........");
    }
}
