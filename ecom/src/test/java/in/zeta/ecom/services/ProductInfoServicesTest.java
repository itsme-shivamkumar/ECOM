package in.zeta.ecom.services;

import in.zeta.ecom.entity.Product;
import in.zeta.ecom.entity.ProductCategoriesResponse;
import in.zeta.ecom.entity.ProductCategory;
import in.zeta.ecom.entity.ProductResponse;
import in.zeta.ecom.repo.ProductCategoriesRepo;
import in.zeta.ecom.repo.ProductRepo;
import in.zeta.ecom.utils.RandomIdGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.mockito.Mockito.when;


@SpringBootTest
class ProductInfoServicesTest {

    @Mock
    private ProductRepo productRepo;

    @Mock
    private ProductCategoriesRepo productCategoriesRepo;

    @Mock
    private RandomIdGenerator rig;

    @InjectMocks
    private ProductInfoServices productInfoServices;

    @Test
    void getAllProductCategoriesTest() {

        List<ProductCategory> list = new ArrayList<>();
        ProductCategory pc1 = ProductCategory.builder().id("id1").name("pc1").build();
        ProductCategory pc2 = ProductCategory.builder().id("id2").name("pc2").build();
        list.add(pc1);
        list.add(pc2);

        when(productCategoriesRepo.findAll()).thenReturn(list);

        List<ProductCategoriesResponse> response = productInfoServices.getAllProductCategories();

        Assertions.assertThat(response).isNotNull().hasSize(2);
        Assertions.assertThat(response.get(0).getId()).isEqualTo("id1");
        Assertions.assertThat(response.get(0).getName()).isEqualTo("pc1");
        Assertions.assertThat(response.get(1).getId()).isEqualTo("id2");
        Assertions.assertThat(response.get(1).getName()).isEqualTo("pc2");
    }

    @Test
    void saveProductCategoryTest() {
        String validName = "PC 1";
        String invalidName = "non existent";
        ProductCategory productCategorySample = ProductCategory.builder().name("pc1").id("id1").build();

        when(productCategoriesRepo.findByName(invalidName)).thenReturn(productCategorySample);
        when(productCategoriesRepo.findByName(validName)).thenReturn(null);
        when(productCategoriesRepo.save(Mockito.any(ProductCategory.class))).thenReturn(productCategorySample);

        String res = productInfoServices.saveProductCategory(validName);
        String res2 = productInfoServices.saveProductCategory(invalidName);

        Assertions.assertThat(res2).isEqualTo("Product with same name already exists");
        Assertions.assertThat(res).isEqualTo("Product Category PC 1 is saved successfully!");
    }

    @Test
    void updateProductCategoryTest() {
        Map<String, String> request = new HashMap<>();
        request.put("productCategoryId", "id1");
        request.put("oldProductCategoryName", "Old Name");
        request.put("newProductCategoryName", "New Name");

        ProductCategory existingProductCategory = ProductCategory.builder().id("id1").name("Old Name").build();

        when(productCategoriesRepo.findById(Mockito.anyString())).thenReturn(java.util.Optional.of(existingProductCategory));
        when(productCategoriesRepo.findByName(Mockito.anyString())).thenReturn(existingProductCategory);
        when(productCategoriesRepo.save(Mockito.any(ProductCategory.class))).thenReturn(existingProductCategory);

        String result = productInfoServices.updateProductCategory(request);

        Assertions.assertThat(result).isEqualTo("Product Category is successfully changed to New Name");
    }

    @Test
    void deleteProductCategoryTest() {
        Map<String, String> request = new HashMap<>();
        request.put("id", "id1");
        request.put("name", "Category Name");

        ProductCategory existingProductCategory = ProductCategory.builder().id("id1").name("Category Name").build();

        when(productCategoriesRepo.findById(Mockito.anyString())).thenReturn(java.util.Optional.of(existingProductCategory));
        when(productCategoriesRepo.findByName(Mockito.anyString())).thenReturn(existingProductCategory);
        Mockito.doNothing().when(productCategoriesRepo).deleteById(Mockito.anyString());

        String result = productInfoServices.deleteProductCategory(request);

        Assertions.assertThat(result).isEqualTo("Product Category is successfully deleted!");
    }

    @Test
    void getAllProductsTest() {
        List<Product> list = new ArrayList<>();
        Product p1 = new Product("id1", "pc1", "desc1", "prod1", 10.0, 50, "USD");
        Product p2 = new Product("id2", "pc2", "desc2", "prod2", 20.0, 30, "EUR");
        list.add(p1);
        list.add(p2);

        when(productRepo.findAll()).thenReturn(list);

        List<ProductResponse> response = productInfoServices.getAllProducts();

        Assertions.assertThat(response).isNotNull().hasSize(2);
        Assertions.assertThat(response.get(0).getId()).isEqualTo("id1");
        Assertions.assertThat(response.get(0).getProductCategoryId()).isEqualTo("pc1");
        Assertions.assertThat(response.get(0).getDescription()).isEqualTo("desc1");
        Assertions.assertThat(response.get(0).getName()).isEqualTo("prod1");
        Assertions.assertThat(response.get(0).getPrice()).isEqualTo(10.0);
        Assertions.assertThat(response.get(0).getStock()).isEqualTo(50);
        Assertions.assertThat(response.get(0).getCurrency()).isEqualTo("USD");

        Assertions.assertThat(response.get(1).getId()).isEqualTo("id2");
        Assertions.assertThat(response.get(1).getProductCategoryId()).isEqualTo("pc2");
        Assertions.assertThat(response.get(1).getDescription()).isEqualTo("desc2");
        Assertions.assertThat(response.get(1).getName()).isEqualTo("prod2");
        Assertions.assertThat(response.get(1).getPrice()).isEqualTo(20.0);
        Assertions.assertThat(response.get(1).getStock()).isEqualTo(30);
        Assertions.assertThat(response.get(1).getCurrency()).isEqualTo("EUR");
    }

    @Test
    void getSpecificProductTest() {
        String productId = "id1";
        Product product = new Product(productId, "pc1", "desc1", "prod1", 10.0, 50, "USD");

        when(productRepo.findById(Mockito.anyString())).thenReturn(Optional.of(product));

        ProductResponse response = productInfoServices.getSpecificProduct(productId);

        Assertions.assertThat(response.getId()).isEqualTo(productId);
        Assertions.assertThat(response.getProductCategoryId()).isEqualTo("pc1");
        Assertions.assertThat(response.getDescription()).isEqualTo("desc1");
        Assertions.assertThat(response.getName()).isEqualTo("prod1");
        Assertions.assertThat(response.getPrice()).isEqualTo(10.0);
        Assertions.assertThat(response.getStock()).isEqualTo(50);
        Assertions.assertThat(response.getCurrency()).isEqualTo("USD");
    }

    @Test
    void getAllProductsUnderProductCategoryIdTest() {
        String categoryId = "pc1";
        String nonExistentCategoryId = "non-existant product category";
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("id1", categoryId, "desc1", "prod1", 10.0, 50, "USD"));
        productList.add(new Product("id2", categoryId, "desc2", "prod2", 20.0, 30, "EUR"));
        ProductCategory productCategory = ProductCategory.builder().id(categoryId).name("Category 1").products(productList).build();


        when(productCategoriesRepo.findById(nonExistentCategoryId)).thenReturn(Optional.empty());
        when(productCategoriesRepo.findById("pc1")).thenReturn(Optional.of(productCategory));

        List<Product> response = productInfoServices.getAllProductsUnderProductCategoryId(categoryId);
        List<Product> emptyResponse = productInfoServices.getAllProductsUnderProductCategoryId(nonExistentCategoryId);

        Assertions.assertThat(response).isNotNull().hasSize(2);
        Assertions.assertThat(emptyResponse).isEmpty();
    }

    @Test
    void getLowStockProductsByNumberTest() {
        int lowStockAmount = 10;
        List<Product> lowStockProducts = new ArrayList<>();
        lowStockProducts.add(new Product("id1", "pc1", "desc1", "prod1", 10.0, 5, "USD"));
        lowStockProducts.add(new Product("id2", "pc2", "desc2", "prod2", 20.0, 8, "EUR"));

        when(productRepo.getAllProductsLessThanStock(lowStockAmount)).thenReturn(lowStockProducts);

        List<Product> response = productInfoServices.getLowStockProductsByNumber(lowStockAmount);

        Assertions.assertThat(response).isNotNull().hasSize(2);
        Assertions.assertThat(response.get(0).getId()).isEqualTo("id1");
        Assertions.assertThat(response.get(1).getId()).isEqualTo("id2");
    }

    @Test
    void saveNewProductTest() {
        Map<String, String> request = new HashMap<>();
        request.put("id", "id1");
        request.put("name", "prod1");
        request.put("productCategoryId", "pc1");
        request.put("description", "desc1");
        request.put("price", "10.0");
        request.put("stocks", "50");
        request.put("currencyType", "USD");

        when(rig.generateRandomId(12)).thenReturn("generatedId");

        String result = productInfoServices.saveNewProduct(request);

        Assertions.assertThat(result).isEqualTo("Product is successfully pushed");
    }

    @Test
    void deleteProductTest() {
        Map<String, String> request = new HashMap<>();
        request.put("id", "id1");

        String productId = "id1";

        String result = productInfoServices.deleteProduct(request);

        Mockito.verify(productRepo, Mockito.times(1)).deleteById(productId);
        Assertions.assertThat(result).isEqualTo("Product is successfully deleted");
    }

}


































