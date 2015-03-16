/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accountManagement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.ServletContext;
import javax.xml.ws.WebServiceRef;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import product.Comment;
import product.Product;
import product.ProductWS_Service;
import product.SubCategories;

/**
 *
 * @author mac
 */
@Named(value = "productMB")
@ViewScoped
public class ProductMB {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/WineXpressWebService-war/productWS.wsdl")
    private ProductWS_Service service;

    private List<Product> allProducts = new ArrayList();
    private Product selectedProduct;
    private SubCategories selectedSub;
    private Comment selectedComment;
    private UploadedFile file;
    private String fileUrl;
    private String productName;
    private String productDesc;
    private int availableQ;
    private double price;
    private double cost;
    private int discount;
    private String volume;

    /**
     * Creates a new instance of ProductMB
     */
    public ProductMB() {
        allProducts = this.viewAllProducts();
        selectedProduct = (Product) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedProduct");
    }

    public void onRowSelect(SelectEvent event) throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("selectedProduct", selectedProduct);
        FacesContext.getCurrentInstance().getExternalContext().redirect("../AdminPortal/editProduct.xhtml");
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {

        this.setFile(event.getFile());
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String newFileName = "/Users/mac/Documents/school/WineXpress/WXStore/web/images/productImg/" + getFile().getFileName();
        fileUrl = getFile().getFileName();
        //String newFileName = servletContext.getRealPath("") + File.separator + "images" + File.separator + "productImg" + File.separator + getFile().getFileName();
        System.out.println("************************" + newFileName);
        System.err.println("newFileName: " + newFileName);
        try {
            FileOutputStream fos = new FileOutputStream(new File(newFileName));
            InputStream is = getFile().getInputstream();
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];
            int a;
            while (true) {
                a = is.read(buffer);
                if (a < 0) {
                    break;
                }
                fos.write(buffer, 0, a);
                fos.flush();
            }
            fos.close();
            is.close();
        } catch (IOException e) {
            FacesMessage msg = new FacesMessage("Invalid OpOrder Upload");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        System.out.println("************************Consent Form file upload ok?");
        FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void createNewProduct() {
        long productId;
        productId = this.saveNewProduct(fileUrl, productName, price, cost, productDesc, availableQ, discount, volume);
        FacesMessage message = new FacesMessage("Succesful", "New Product Created");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void deleteProduct() throws IOException {
        this.deleteProduct_1(selectedProduct.getId());
        FacesContext.getCurrentInstance().getExternalContext().redirect("../AdminPortal/viewAllProduct.xhtml");
    }

    public void deleteComment() {
        this.deleteComment(selectedComment);
    }

    //getter and setter
    public List<Product> getAllProducts() {
        return allProducts;
    }

    public void setAllProducts(List<Product> allProducts) {
        this.allProducts = allProducts;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public int getAvailableQ() {
        return availableQ;
    }

    public void setAvailableQ(int availableQ) {
        this.availableQ = availableQ;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public SubCategories getSelectedSub() {
        return selectedSub;
    }

    public void setSelectedSub(SubCategories selectedSub) {
        this.selectedSub = selectedSub;
    }

    public Comment getSelectedComment() {
        return selectedComment;
    }

    public void setSelectedComment(Comment selectedComment) {
        this.selectedComment = selectedComment;
    }

    //call web services
    private java.util.List<product.Product> viewAllProducts() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        product.ProductWS port = service.getProductWSPort();
        return port.viewAllProducts();
    }

    private long saveNewProduct(java.lang.String picture, java.lang.String productName, double productPrice, double productCost, java.lang.String productDescription, int productAQ, int productDiscount, java.lang.String productVolume) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        product.ProductWS port = service.getProductWSPort();
        return port.saveNewProduct(picture, productName, productPrice, productCost, productDescription, productAQ, productDiscount, productVolume);
    }

    private Product deleteProduct_1(long productId) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        product.ProductWS port = service.getProductWSPort();
        return port.deleteProduct(productId);
    }

    private void deleteComment(product.Comment myComment) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        product.ProductWS port = service.getProductWSPort();
        port.deleteComment(myComment);
    }

}
