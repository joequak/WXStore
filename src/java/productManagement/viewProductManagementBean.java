/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productManagement;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.xml.ws.WebServiceRef;
import org.primefaces.event.RateEvent;
import product.Product;
import product.Customer;
import product.ProductWS_Service;
import wx.custAccMngmtWS.CustAccMngmtWS_Service;

/**
 *
 * @author mac
 */
@Named(value = "viewProductManagementBean")
@ViewScoped
public class viewProductManagementBean {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/WineXpressWebService-war/CustAccMngmtWS.wsdl")
    private CustAccMngmtWS_Service service_1;

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/WineXpressWebService-war/productWS.wsdl")
    private ProductWS_Service service;

    //Attributes
    private Product selectedProduct;
    private Integer rating;
    private String myComment;
    private Customer onCus;
    
    @ManagedProperty(value = "#{commonInfraMB.logInCust}")
    private long logInCust;

    public viewProductManagementBean() {
    }

    @PostConstruct
    public void init() {
        selectedProduct = (Product) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedProduct");
        if (this.getLogInCust() != -1) {
           // onCus = (Customer) this.viewAccInfoMember(this.getLogInCust());
        }
    }

    // geter and setter
    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public Integer getRating() {
        return rating;
    }

    public String getMyComment() {
        return myComment;
    }

    public long getLogInCust() {
        return logInCust;
    }

    public void setLogInCust(long logInCust) {
        this.logInCust = logInCust;
    }

    public void setMyComment(String myComment) {
        this.myComment = myComment;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Customer getOnCus() {
        return onCus;
    }

    public void setOnCus(Customer onCus) {
        this.onCus = onCus;
    }

    public void makeComment() {
        this.makeComment_1(selectedProduct, myComment, onCus);

    }

    public void makeRate() {
        this.rateProduct(onCus, selectedProduct, rating);
    }

    public void oncancel() {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cannot Cancel your rate", "!");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    //call web service
    private void rateProduct(product.Customer cus, product.Product myProduct, int myRate) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        product.ProductWS port = service.getProductWSPort();
        port.rateProduct(cus, myProduct, myRate);
    }

    private void makeComment_1(product.Product myProduct, java.lang.String newComment, product.Customer cus) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        product.ProductWS port = service.getProductWSPort();
        port.makeComment(myProduct, newComment, cus);
    }

    private Customer findCustomerById(long cusId) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        product.ProductWS port = service.getProductWSPort();
        return port.findCustomerById(cusId);
    }



}
