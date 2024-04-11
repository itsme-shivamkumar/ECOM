package in.zeta.ecom.controller;

import in.zeta.ecom.entity.*;
import in.zeta.ecom.services.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class OrdersController {

    private OrderInfoService orderInfoService;

    @Autowired
    public OrdersController(OrderInfoService orderInfoService) {
        this.orderInfoService = orderInfoService;
    }


    @PostMapping("/orders/new")
    public String placeAnOrder(@RequestBody Map<String, String> request) {
        return orderInfoService.placeOrder(request);
    }

    @PutMapping("/orders/cancel")
    public String cancelAnOrder(@RequestBody Map<String, String> request) {
        return orderInfoService.cancelOrder(request);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> listOrdersOfUserByUid(@RequestParam("uid") String uid) {
        return ResponseEntity.ok(orderInfoService.listOrdersByUid(uid));
    }

    // Transactions
    @GetMapping("/transactions")
    public ResponseEntity<TransactionReport> listTransactionsByUidAndInterval(@RequestParam("userId") String uid, @RequestParam("interval") String interval) {
        return ResponseEntity.ok(orderInfoService.listTransactionReportByUidAndInterval(uid, interval));
    }



}

