/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package productManagement;

import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.xml.ws.WebServiceRef;
import product.Categories;
import product.CategoryWS_Service;
import product.SubCategoryWS_Service;

/**
 *
 * @author wangyan
 */
@Named(value = "SubCategoryManagedBean")
@ViewScoped
public class SubCategoryManagedBean {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/WineXpressWebService-war/categoryWS.wsdl")
    private CategoryWS_Service service_1;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/WineXpressWebService-war/subCategoryWS.wsdl")
    private SubCategoryWS_Service service;
    
    private Long subCategoryId;
    private String subCategoryName;
    private String categoryName;
    private String statusMessage;
    private List<String> categoryList;
    /**
     * Creates a new instance of SubCategoryManagedBean
     */
    public SubCategoryManagedBean() {
    }

    public Long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public List<String> getCategoryList() {
        System.out.println("test getCategoryList in subcategoryManagedBean");
      List<Categories> cc = this.viewAllCategories();
      
        System.out.println("test getCategoryList in subcategoryManagedBean");
         categoryList = new ArrayList<>();
         categoryList.clear();
         for (Object o:cc){
             Categories c = (Categories)o;
             categoryList.add(c.getName());
         }
        
       
        return categoryList;

    
       
    }

    public void setCategoryList(List<String> categoryList) {
        this.categoryList = categoryList;
    }
    
    
    
     //Methods
    //save new subcategory
    public void saveSubCategory(ActionEvent event) {

        try {
            subCategoryId =saveSubCategories(categoryName,subCategoryName);
            if (subCategoryId != -2l) {
                statusMessage = "subcategory saved successfully";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", ""));
            } else {
                statusMessage = "subcategory saved failed. subcategory name already exists in category";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ADD NEW SUBCATEGORY RESULT: " + statusMessage, ""));
            }
        } catch (NumberFormatException e) {
            statusMessage = "subcategory saved failed. invalid number format";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "PLEASE ENTER A VALID NUMBER FORMAT: " + statusMessage, ""));
        }
    }

    private long saveSubCategories(java.lang.String categoryName, java.lang.String subcategoriesName) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        product.SubCategoryWS port = service.getSubCategoryWSPort();
        return port.saveSubCategories(categoryName, subcategoriesName);
    }

    private java.util.List<product.Categories> viewAllCategories() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        product.CategoryWS port = service_1.getCategoryWSPort();
        return port.viewAllCategories();
    }


    
   
    
}
