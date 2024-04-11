package in.zeta.ecom.services;

import in.zeta.ecom.entity.Product;
import in.zeta.ecom.entity.ProductCategoriesResponse;
import in.zeta.ecom.entity.ProductCategory;
import in.zeta.ecom.entity.ProductResponse;
import in.zeta.ecom.repo.ProductCategoriesRepo;
import in.zeta.ecom.repo.ProductRepo;
import in.zeta.ecom.utils.RandomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductInfoServices {

    private final ProductRepo productRepo;

    private final ProductCategoriesRepo productCategoriesRepo;

    private final RandomIdGenerator rig;


    @Autowired
    public ProductInfoServices(ProductRepo productRepo, ProductCategoriesRepo productCategoriesRepo, RandomIdGenerator rig) {
        this.productRepo = productRepo;
        this.productCategoriesRepo = productCategoriesRepo;
        this.rig = rig;
    }

    public List<ProductCategoriesResponse> getAllProductCategories() {
        List<ProductCategory> pcs = productCategoriesRepo.findAll();
        List<ProductCategoriesResponse> response = new ArrayList<>();
        for (ProductCategory pc : pcs) {
            ProductCategoriesResponse temp = new ProductCategoriesResponse(pc.getId(), pc.getName());
            response.add(temp);
        }
        return response;
    }

    public String saveProductCategory(String name) {
        ProductCategory existingProductCategory = productCategoriesRepo.findByName(name);
        if (existingProductCategory != null) return "Product with same name already exists";
        else {
            ProductCategory newProductCategory = new ProductCategory();
            String id = rig.generateRandomId(12);
            newProductCategory.setId(id);
            newProductCategory.setName(name);
            productCategoriesRepo.save(newProductCategory);
            return "Product Category " + name + " is saved successfully!";
        }
    }

    public String updateProductCategory(Map<String, String> request) {
        String id = request.get("productCategoryId");
        String oldProductCategoryName = request.get("oldProductCategoryName");
        String newProductCategoryName = request.get("newProductCategoryName");
        ProductCategory p1 = productCategoriesRepo.findById(id).orElse(null);
        ProductCategory p2;
        if (p1 == null) {
            if (productCategoriesRepo.findByName(oldProductCategoryName) != null) {
                p2 = productCategoriesRepo.findByName(oldProductCategoryName);
                p2.setName(newProductCategoryName);
                productCategoriesRepo.save(p2);
                return "Product Category is successfully changed to " + newProductCategoryName;
            } else {
                return "Cannot find product category!";
            }
        } else {
            p1.setName(newProductCategoryName);
            productCategoriesRepo.save(p1);
            return "Product Category is successfully changed to " + newProductCategoryName;
        }
    }

    public String deleteProductCategory(Map<String, String> request) {
        String id = request.get("id");
        String name = request.get("name");
        ProductCategory p1 = productCategoriesRepo.findById(id).orElse(null);
        ProductCategory p2;
        if (p1 == null) {
            if (productCategoriesRepo.findByName(name) != null) {
                p2 = productCategoriesRepo.findByName(name);
                productCategoriesRepo.deleteById(p2.getId());
                return "Product Category is successfully deleted!";
            } else {
                return "Cannot find product category!";
            }
        } else {
            productCategoriesRepo.deleteById(p1.getId());
            return "Product Category is successfully deleted!";
        }
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> prods = productRepo.findAll();
        List<ProductResponse> responses = new ArrayList<>();
        for (Product prod : prods) {
            ProductResponse temp = new ProductResponse(prod.getId(), prod.getProductCategoryId(), prod.getDescription(), prod.getName(), prod.getPrice(), prod.getStock(), prod.getCurrency());
            responses.add(temp);
        }
        return responses;
    }

    public ProductResponse getSpecificProduct(String id) {
        Optional<Product> pRes = productRepo.findById(id);
        if (pRes.isPresent()) {
            Product product = pRes.get();
            return new ProductResponse(product.getId(), product.getProductCategoryId(), product.getDescription(), product.getName(), product.getPrice(), product.getStock(), product.getCurrency());
        }
        return new ProductResponse();
    }

    public List<Product> getAllProductsUnderProductCategoryId(String id) {
        Optional<ProductCategory> productCategory = productCategoriesRepo.findById(id);
        if (productCategory.isPresent()) return productCategory.get().getProducts();
        else return new ArrayList<>();
    }

    public List<Product> getLowStockProductsByNumber(int amount) {
        return productRepo.getAllProductsLessThanStock(amount);
    }

    public String saveNewProduct(Map<String, String> request) {
        String id = request.get("id");
        if (id == null || id.isEmpty()) id = rig.generateRandomId(12);
        Product p = new Product();
        p.setId(id);
        p.setName(request.get("name"));
        p.setProductCategoryId(request.get("productCategoryId"));
        p.setDescription(request.get("description"));
        p.setPrice(Double.parseDouble(request.get("price")));
        p.setStock(Integer.parseInt(request.get("stocks")));
        p.setCurrency(request.get("currencyType"));
        productRepo.save(p);
        return "Product is successfully pushed";
    }

    public String deleteProduct(Map<String, String> request) {
        String id = request.get("id");
        productRepo.deleteById(id);
        return "Product is successfully deleted";
    }

}
