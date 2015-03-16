/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productManagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.xml.ws.WebServiceRef;
import org.primefaces.event.RowEditEvent;
import product.Categories;
import product.CategoryWS_Service;


/**
 *
 * @author wangyan
 */
@ManagedBean(name = "CategoryManagedBean")
@ViewScoped
public class CategoryManagedBean implements Serializable {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/WineXpressWebService-war/categoryWS.wsdl")
    private CategoryWS_Service service;
  

    private Long categoryId;
    private String categoryName;
    private String newCategoryName;

    private String statusMessage;
    private List<Categories> categoryList;
    private List<String> categoryNameList = new ArrayList<>();
    private List<Categories> filteredCategories;

    /**
     * Creates a new instance of categoryManagedBean
     */
    public CategoryManagedBean() {
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

    public List<Categories> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Categories> categoryList) {
        this.categoryList = categoryList;
    }

    public List<String> getCountries() {
         categoryList =this.viewAllCategories();
         List<String> clist = new ArrayList<>();
         clist.clear();
         for (Object o:categoryList){
             Categories c = (Categories)o;
             clist.add(c.getName());
         }
        return clist;

    }
     public List<String> categoryNameList(String query) {
         
       return getCategoriesName();
    }

    public String getNewCategoryName() {
        return newCategoryName;
    }

    public void setNewCategoryName(String newCategoryName) {
        this.newCategoryName = newCategoryName;
    }


   
   

  
 
  

    
    //Methods
    //save new category
    public void saveNewCategory(ActionEvent event) {

        try {
            categoryId =this.saveNewCategories(categoryName);
            if (categoryId != -2l) {
                statusMessage = "category saved successfully";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, statusMessage, ""));
            } else {
                statusMessage = "category saved failed. category name already exists";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ADD NEW CATEGORY RESULT: " + statusMessage, ""));
            }
        } catch (NumberFormatException e) {
            statusMessage = "category saved failed. invalid number format";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "PLEASE ENTER A VALID NUMBER FORMAT: " + statusMessage, ""));
        }
    }

    //search category
    public void searchCategory(ActionEvent event) {
        categoryList = this.searchCategories(categoryName);
        if (categoryList == null) {
            FacesMessage msg;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "No such Prduct", "The category you are searching for does not exist");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            categoryList.clear();

        }
    }

    //view all categorys
    public void viewAllcategories(ActionEvent event) {
        System.out.println("viewAll");
        this.setCategoryList(this.viewAllCategories());
        if (categoryList == null) {
            FacesMessage msg;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "empty", "No result");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            categoryList.clear();
        }
    }

    //delete category
    public void deletecategory(ActionEvent event) {
        System.out.println("test deletecategory"+categoryId);
        categoryId = (Long) event.getComponent().getAttributes().get("categoryId");
        System.out.println("test deletecategory"+categoryId);
        try {
            Categories category =this.deleteCategories(categoryId);
            statusMessage = " Deleted successfully";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, statusMessage, ""));
            categoryList.remove(category);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
  

    //Edit the category
    public void onRowEdit(RowEditEvent event) {
        System.out.println("category: onRowEdit method" );
        FacesMessage msg = new FacesMessage("category Edited");
        Categories edit = (Categories) event.getObject();
        this.editCategories(edit);
        System.out.println("category: " + edit.getName() + " " + edit.getId() + "has been edited");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    //Edit cancelled
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public List<Categories> getFilteredCategories() {
        return filteredCategories;
    }

    public void setFilteredCategories(List<Categories> filteredCategories) {
        this.filteredCategories = filteredCategories;
    }

    private Categories deleteCategories(long categoriesId) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        product.CategoryWS port = service.getCategoryWSPort();
        return port.deleteCategories(categoriesId);
    }

    private void editCategories(product.Categories newCategories) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        product.CategoryWS port = service.getCategoryWSPort();
        port.editCategories(newCategories);
    }

    private java.util.List<java.lang.String> getCategoriesName() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        product.CategoryWS port = service.getCategoryWSPort();
        return port.getCategoriesName();
    }

    private java.util.List<java.lang.String> getSubCategoryNameList(java.lang.String categoryName) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        product.CategoryWS port = service.getCategoryWSPort();
        return port.getSubCategoryNameList(categoryName);
    }

    private long saveNewCategories(java.lang.String categoriesName) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        product.CategoryWS port = service.getCategoryWSPort();
        return port.saveNewCategories(categoriesName);
    }

    private java.util.List<product.Categories> searchCategories(java.lang.String categoriesName) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        product.CategoryWS port = service.getCategoryWSPort();
        return port.searchCategories(categoriesName);
    }

    private java.util.List<product.Categories> viewAllCategories() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        product.CategoryWS port = service.getCategoryWSPort();
        return port.viewAllCategories();
    }



}
