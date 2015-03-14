/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.xml.ws.WebServiceRef;
import org.primefaces.event.RowEditEvent;



/**
 *
 * @author wangyan
 */
@ManagedBean(name = "productManagedBean")
@ViewScoped
public class productManagedBean implements Serializable {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/WineXpressWebService-war/productWS.wsdl")
    private ProductWS_Service service;

  
  

    private Long productId;
    private String productName;
    private String productPrice;
    private String productCost;
    private String productDescription;
    private String productAQ;
    private String productPicture;
    private String productDiscount;
    private String productVolume;
    private String productCountry;
    private String statusMessage;
    private List<Product> productList;
    private List<Product> nonFilteredProducts;
    private List<Product> filteredProducts;
    private List<String> countries;
    private List<String> categories;

    /**
     * Creates a new instance of productManagedBean
     */
    public productManagedBean() {
    }

    @PostConstruct
    public void init() {
        nonFilteredProducts = viewAllProducts_1();
    }
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductCost() {
        return productCost;
    }

    public void setProductCost(String productCost) {
        this.productCost = productCost;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductAQ() {
        return productAQ;
    }

    public void setProductAQ(String productAQ) {
        this.productAQ = productAQ;
    }

    public String getProductPicture() {
        return productPicture;
    }

    public void setProductPicture(String productPicture) {
        this.productPicture = productPicture;
    }

    public String getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(String productDiscount) {
        this.productDiscount = productDiscount;
    }

    public String getProductVolume() {
        return productVolume;
    }

    public void setProductVolume(String productVolume) {
        this.productVolume = productVolume;
    }

    public String getProductCountry() {
        return productCountry;
    }

    public void setProductCountry(String productCountry) {
        this.productCountry = productCountry;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<String> getCountries() {
       return getCountries_1();
      
       // return getCountries_1(); // this one need a very long time to run and cause the connectionpool overflow
        
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public List<String> getCategories() {
        return getCountries_1();
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<Product> getFilteredProducts() {
        return filteredProducts;
    }

    public void setFilteredProducts(List<Product> filteredProducts) {
        this.filteredProducts = filteredProducts;
    }

    public List<Product> getNonFilteredProducts() {
        return nonFilteredProducts;
                
    }

    public void setNonFilteredProducts(List<Product> nonFilteredProducts) {
        this.nonFilteredProducts = nonFilteredProducts;
    }
    
    
    

    //Methods
    //save new product
    public void saveNewProduct(ActionEvent event) {
        try {
            productId =saveNewProduct_1(productName, Double.valueOf(productPrice), Double.valueOf(productCost), productDescription, Integer.valueOf(productAQ), Integer.valueOf(productDiscount), productVolume, productCountry);
           
            if (productId != -2l) {
                statusMessage = "Product saved successfully";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ADD NEW PRODUCT RESULT: " + statusMessage + "(new product id is " + productId + ")", ""));

            } else {
                statusMessage = "Product saved failed. product name already exists";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ADD NEW PRODUCT RESULT: " + statusMessage, ""));

            }

        } catch (NumberFormatException e) {
            statusMessage = "Product saved failed. invalid number format";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "PLEASE ENTER A VALID NUMBER FORMAT: " + statusMessage, ""));

        }
    }

    //search Product
    public void searchProduct(ActionEvent event) {
        productList = searchProduct_1(productName);
        if (productList == null) {
            FacesMessage msg;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "No such Prduct", "The product you are searching for does not exist");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            productList.clear();

        }
    }

    //view all products
    public void viewAllProducts(ActionEvent event) {       
        productList = viewAllProducts_1();
        if (productList == null) {
            FacesMessage msg;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "empty", "No result");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            productList.clear();
        }
    }

    //delete product
    public void deleteProduct(ActionEvent event) {
        productId = (Long) event.getComponent().getAttributes().get("productId");
        System.out.println(" ProductId:"+productId);
        try {
            Product product = deleteProduct_1(productId);
            statusMessage = " Deleted successfully";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, statusMessage, ""));
            productList.remove(product);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Edit the product

    public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Product Edited");
        Product edit = (Product) event.getObject();
        onRowEdit_1(edit);
        System.out.println("Product: " + edit.getName() + " " + edit.getId() + "has been edited");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    //Edit cancelled

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public boolean filterByPrice(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if(filterText == null||filterText.equals("")) {
            return true;
        }
         
        if(value == null) {
            return false;
        }
         
        return ((Comparable) value).compareTo(Double.valueOf(filterText)) > 0;
    }
    
    

    private Product deleteProduct_1(java.lang.Long productId) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        product.ProductWS port = service.getProductWSPort();
        return port.deleteProduct(productId);
    }

    private void onRowEdit_1(product.Product edit) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        product.ProductWS port = service.getProductWSPort();
        port.onRowEdit(edit);
    }

    private Long saveNewProduct_1(java.lang.String productName, java.lang.Double productPrice, java.lang.Double productCost, java.lang.String productDescription, java.lang.Integer productAQ, java.lang.Integer productDiscount, java.lang.String productVolume, java.lang.String productCountry) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        product.ProductWS port = service.getProductWSPort();
        return port.saveNewProduct(productName, productPrice, productCost, productDescription, productAQ, productDiscount, productVolume, productCountry);
    }

    private java.util.List<product.Product> searchProduct_1(java.lang.String productName) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        product.ProductWS port = service.getProductWSPort();
        return port.searchProduct(productName);
    }

    private java.util.List<product.Product> viewAllProducts_1() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        product.ProductWS port = service.getProductWSPort();
        return port.viewAllProducts();
    }

    private java.util.List<java.lang.String> getCountries_1() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        product.ProductWS port = service.getProductWSPort();
        return port.getCountries();
    }

    

}
