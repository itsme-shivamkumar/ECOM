package in.zeta.ecom.services;


import in.zeta.ecom.entity.*;
import in.zeta.ecom.repo.OrderRepo;
import in.zeta.ecom.repo.ProductRepo;
import in.zeta.ecom.repo.TransactionRepo;
import in.zeta.ecom.repo.UserRepo;
import in.zeta.ecom.utils.RandomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderInfoService {


    static final String HEADER_NAME_QUANTITY = "quantity";
    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final TransactionRepo transactionRepo;
    private final RandomIdGenerator rig;

    @Autowired
    public OrderInfoService(OrderRepo orderRepo, UserRepo userRepo, ProductRepo productRepo, TransactionRepo transactionRepo, RandomIdGenerator rig) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.transactionRepo = transactionRepo;
        this.rig = rig;
    }

    public static long getDateDifferenceInDays(Date date1, Date date2) {
        long diffInMilliseconds = date2.getTime() - date1.getTime();
        return diffInMilliseconds / (24 * 60 * 60 * 1000); // milliseconds to days
    }

    public Map<String, Integer> getTrendingProducts() {
        List<Object[]> list = orderRepo.findTrendingProducts();
        Map<String, Integer> trendingList = new HashMap<>();
        for (Object[] result : list) {
            trendingList.put(result[0].toString(), ((Number) result[1]).intValue());
        }
        return trendingList;
    }

    public String placeOrder(Map<String, String> request) {
        try {
            String uid = request.get("userId");
            String pid = request.get("productId");
            int quantity = -1;
            if (request.get(HEADER_NAME_QUANTITY).isEmpty() || request.get(HEADER_NAME_QUANTITY) == null)
                return "Cannot place this order";
            quantity = Integer.parseInt(request.get(HEADER_NAME_QUANTITY));

            if (uid == null || pid == null || quantity == -1) return "Cannot place this order";
            User user = userRepo.getById(uid);
            Product product = productRepo.getById(pid);

            Order newOrder = new Order();
            String oid = rig.generateRandomId(20);
            newOrder.setId(oid);
            newOrder.setUserId(user.getId());
            newOrder.setProductId(pid);
            newOrder.setPrice(quantity * product.getPrice());
            newOrder.setQuantity(quantity);
            newOrder.setCreatedDate(new Date());
            newOrder.setUpdatedDate(new Date());
            newOrder.setStatus("PLACED");


            if (!productRepo.getAllProductsLessThanStock(quantity).contains(product)) {
                Transaction newTransaction = new Transaction();
                newTransaction.setId(rig.generateRandomId(32));
                newTransaction.setOrderId(oid);
                newTransaction.setCreatedDate(new Date());
                newTransaction.setAmount(quantity * product.getPrice());
                newTransaction.setType("ACQUIRED");
                product.setStock(product.getStock() - quantity);
                productRepo.save(product);
                orderRepo.save(newOrder);
                transactionRepo.save(newTransaction);
                return "Order placed successfully!";
            } else return "Can't place order due to unavailability of product!";
        } catch (Exception e) {
            return "Invalid Product or User";
        }
    }

    public String cancelOrder(Map<String, String> request) {
        String oid = request.get("orderId");
        String pid = request.get("productId");
        Product product = productRepo.findById(pid).orElse(new Product());
        Order order = orderRepo.findById(oid).orElse(new Order());
        Transaction currTransaction = transactionRepo.findTransaction(order.getId());
        if (order.getStatus().equals("PLACED") && currTransaction.getType().equals("ACQUIRED")) {
            Transaction updatedTransaction = new Transaction(currTransaction.getId(), currTransaction.getOrderId(), currTransaction.getAmount(), currTransaction.getCreatedDate(), "REVERSED");
            Order updatedOrder = new Order(order.getId(), order.getUserId(), order.getProductId(), order.getQuantity(), order.getPrice(), order.getCreatedDate(), new Date(), "CANCELED", order
                    .getTransactions());
            Product updatedProduct = new Product(product.getId(), product.getProductCategoryId(), product.getDescription(), product.getName(), product.getPrice(), product.getStock() + order.getQuantity(), product.getCurrency());
            transactionRepo.save(updatedTransaction);
            orderRepo.save(updatedOrder);
            productRepo.save(updatedProduct);
            return "Order is cancelled";
        } else {
            return "Order already cancelled";
        }
    }

    public List<Order> listOrdersByUid(String uid) {
        return orderRepo.findOrdersByUid(uid);
    }

    public TransactionReport listTransactionReportByUidAndInterval(String uid, String interval) {
        int daysGap = 1;

        if (interval.charAt(0) == 'D' || interval.charAt(0) == 'd') daysGap = 1;
        else if (interval.charAt(0) == 'M' || interval.charAt(0) == 'm') daysGap = 30;
        else if (interval.charAt(0) == 'W' || interval.charAt(0) == 'w') daysGap = 7;
        else daysGap = 356;

        Map<String, List<Transaction>> transactionsByIntervalMap = new HashMap<>();

        List<Order> list = orderRepo.findOrdersByUid(uid);

        for (Order order : list) {
            for (Transaction transaction : order.getTransactions()) {
                int diff = (int) getDateDifferenceInDays(transaction.getCreatedDate(), new Date());
                String intervalKey = calculateIntervalKey(diff, daysGap);
                transactionsByIntervalMap
                        .computeIfAbsent(intervalKey, k -> new ArrayList<>())
                        .add(transaction);
            }
        }

        return new TransactionReport(transactionsByIntervalMap);
    }

    private String calculateIntervalKey(int differenceInDays, int daysGap) {
        int intervalIndex = differenceInDays / daysGap;
        return "Interval_" + intervalIndex;
    }

}
