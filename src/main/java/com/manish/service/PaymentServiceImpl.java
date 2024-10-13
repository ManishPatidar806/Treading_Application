package com.manish.service;

import com.manish.domain.PAYMENT_METHOD;
import com.manish.domain.PAYMENT_ORDER_STATUS;
import com.manish.model.PaymentOrder;
import com.manish.model.User;
import com.manish.repository.PaymentOrderRepository;
import com.manish.response.PaymentResponse;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    @Value("${stripe.api.key}")
    private String stripeSecretKey;
    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecretKey;



    @Override
    public PaymentOrder createOrder(User user, Long amount, PAYMENT_METHOD paymentMethod) {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setAmount(amount);
        paymentOrder.setPaymentMethod(paymentMethod);
        paymentOrder.setUser(user);
        paymentOrder.setStatus(PAYMENT_ORDER_STATUS.PENDING);
       return paymentOrderRepository.save(paymentOrder);
    }
    @SneakyThrows
    @Override
    public PaymentOrder getPaymentOrderById(Long id) {
        return paymentOrderRepository.findById(id).orElseThrow(()->new Exception("Payment Order Not found....."));
    }

    @Override
    @SneakyThrows
    public Boolean proccedPaymentOrder(PaymentOrder paymentOrder, String paymentId) {
        if(paymentOrder.getStatus()==null){
            paymentOrder.setStatus(PAYMENT_ORDER_STATUS.PENDING);
        }
       if(paymentOrder.getStatus().equals(PAYMENT_ORDER_STATUS.PENDING)){

           if(paymentOrder.getPaymentMethod().equals(PAYMENT_METHOD.RAZORPAY)){
               RazorpayClient razorpayClient = new RazorpayClient(apiKey,apiSecretKey);
               Payment payment = razorpayClient.payments.fetch(paymentId);
               Integer amount = payment.get("amount");
            String status = payment.get("status");
            if(status.equals("captured")){
                paymentOrder.setStatus(PAYMENT_ORDER_STATUS.SUCCESS);
                return true;
            }
            paymentOrder.setStatus(PAYMENT_ORDER_STATUS.FAILED);
            paymentOrderRepository.save(paymentOrder);
            return false;
           }
        paymentOrder.setStatus(PAYMENT_ORDER_STATUS.SUCCESS);
           paymentOrderRepository.save(paymentOrder);
           return true;

       }
        return false;
    }

    @SneakyThrows
    @Override
    public PaymentResponse createRazorpayPaymentLing(User user, Long amount , Long orderId) {
       Long Amount = amount*100;

       try{
           RazorpayClient razorpay = new RazorpayClient(apiKey ,apiSecretKey);

           JSONObject paymentLinkRequest = new JSONObject();
           paymentLinkRequest.put("amount",amount);
           paymentLinkRequest.put("currency","INR");

           JSONObject customer = new JSONObject();
           customer.put("name",user.getFullName());

           customer.put("email",user.getEmail());
           paymentLinkRequest.put("customer",customer);

           JSONObject notify = new JSONObject();
           notify.put("email",true);
           paymentLinkRequest.put("notify",notify);

           paymentLinkRequest.put("remainder_enable",true);

           paymentLinkRequest.put("callback_url","http://localhost:5173/wallet?order_id="+orderId);
           paymentLinkRequest.put("callback_method","get");

           PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);

           String paymentLinkId = payment.get("id");
           String paymentLinkUrl = payment.get("short_url");

           PaymentResponse paymentResponse = new PaymentResponse();
           paymentResponse.setPayment_url(paymentLinkUrl);

           return paymentResponse;

       } catch (RazorpayException e) {
           System.out.println("Error creating payment link : "+e.getMessage());
           throw new RazorpayException(e.getMessage());
       }
    }

    @SneakyThrows
    @Override
    public PaymentResponse createStringPaymentLing(User user, Long amount, Long orderId)  {
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder().addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5173/wallet?order_id="+orderId)
                .setCancelUrl("http://localhost:5173/payment/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L).setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(amount*100)
                                .setProductData(SessionCreateParams.LineItem
                                        .PriceData.ProductData.builder()
                                        .setName("Top up wallet").build())
                                .build())
                        .build()).build();

        Session session = Session.create(params);

        System.out.println("session ______"+session);

        PaymentResponse res = new PaymentResponse();
        res.setPayment_url(session.getUrl());

        return res;


    }
}
