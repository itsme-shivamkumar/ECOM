package in.zeta.ecom.services;

import in.zeta.ecom.entity.*;
import in.zeta.ecom.repo.OrderRepo;
import in.zeta.ecom.repo.ProductRepo;
import in.zeta.ecom.repo.TransactionRepo;
import in.zeta.ecom.repo.UserRepo;
import in.zeta.ecom.utils.RandomIdGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@SpringBootTest
class OrderInfoServiceTest {

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private ProductRepo productRepo;

    @Mock
    private TransactionRepo transactionRepo;

    @Mock
    private RandomIdGenerator rig;

    @InjectMocks
    private OrderInfoService orderInfoService;

    @Test
    void getDateDifferenceInDays() {
        Date date1 = new Date();
        Date date2 = new Date(System.currentTimeMillis() + 86400000); // Adding one day
        long difference = OrderInfoService.getDateDifferenceInDays(date1, date2);
        assertEquals(1, difference);
    }

    @Test
    void getTrendingProducts() {
        List<Object[]> mockList = new ArrayList<>();
        mockList.add(new Object[]{"Product1", 10});
        mockList.add(new Object[]{"Product2", 8});

        Mockito.when(orderRepo.findTrendingProducts()).thenReturn(mockList);

        Map<String, Integer> trendingList = orderInfoService.getTrendingProducts();

        assertEquals(2, trendingList.size());
        assertEquals(10, trendingList.get("Product1"));
        assertEquals(8, trendingList.get("Product2"));
    }

    @Test
    void placeOrder() {
        Map<String, String> request = new HashMap<>();
        request.put("userId", "user123");
        request.put("productId", "product123");
        request.put(OrderInfoService.HEADER_NAME_QUANTITY, "5");

        User mockUser = new User();
        mockUser.setId("user123");

        Product mockProduct = new Product();
        mockProduct.setId("product123");
        mockProduct.setPrice(20.0);
        mockProduct.setStock(10);

        Mockito.when(userRepo.getById(any())).thenReturn(mockUser);
        Mockito.when(productRepo.getById(any())).thenReturn(mockProduct);
        Mockito.when(rig.generateRandomId(anyInt())).thenReturn("randomId");

        String result = orderInfoService.placeOrder(request);

        assertEquals("Order placed successfully!", result);
    }

    @Test
    void cancelOrder() {
        Map<String, String> request = new HashMap<>();
        request.put("orderId", "order123");
        request.put("productId", "product123");

        Product mockProduct = new Product();
        mockProduct.setId("product123");
        mockProduct.setStock(5);

        Order mockOrder = new Order();
        mockOrder.setId("order123");
        mockOrder.setStatus("PLACED");

        Transaction mockTransaction = new Transaction();
        mockTransaction.setId("transaction123");
        mockTransaction.setType("ACQUIRED");

        Mockito.when(orderRepo.findById(any())).thenReturn(Optional.of(mockOrder));
        Mockito.when(productRepo.findById(any())).thenReturn(Optional.of(mockProduct));
        Mockito.when(transactionRepo.findTransaction(any())).thenReturn(mockTransaction);

        String result = orderInfoService.cancelOrder(request);

        assertEquals("Order is cancelled", result);
    }

    @Test
    void listOrdersByUid() {
        List<Order> mockOrderList = new ArrayList<>();
        mockOrderList.add(new Order());
        mockOrderList.add(new Order());

        Mockito.when(orderRepo.findOrdersByUid(any())).thenReturn(mockOrderList);

        List<Order> result = orderInfoService.listOrdersByUid("user123");

        assertEquals(2, result.size());
    }

    @Test
    void listTransactionReportByUidAndInterval() {
        List<Order> mockOrderList = new ArrayList<>();
        mockOrderList.add(new Order());
        mockOrderList.add(new Order());

        Transaction mockTransaction1 = new Transaction();
        mockTransaction1.setId("transaction1");
        mockTransaction1.setCreatedDate(new Date());

        Transaction mockTransaction2 = new Transaction();
        mockTransaction2.setId("transaction2");
        mockTransaction2.setCreatedDate(new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000)); // One week ago

        mockOrderList.get(0).setTransactions(Arrays.asList(mockTransaction1));
        mockOrderList.get(1).setTransactions(Arrays.asList(mockTransaction2));

        Mockito.when(orderRepo.findOrdersByUid(any())).thenReturn(mockOrderList);

        TransactionReport result = orderInfoService.listTransactionReportByUidAndInterval("user123", "WEEK");

        assertNotNull(result);
        assertEquals(2, result.getTransactionsByInterval().size());
        assertEquals(1, result.getTransactionsByInterval().get("Interval_0").size());
        assertEquals(1, result.getTransactionsByInterval().get("Interval_1").size());
    }
}