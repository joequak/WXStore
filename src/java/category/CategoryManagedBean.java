/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package category;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.xml.ws.WebServiceRef;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author wangyan
 */
@ManagedBean(name = "CategoryManagedBean")
@ViewScoped
public class CategoryManagedBean implements Serializable {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/WineXpressWebService-war/categoryWS.wsdl")
    private CategoryWS_Service C_service;

    private Long categoryId;
    private String categoryName;

    private String statusMessage;
    private List<Categories> categoryList;
    private List<String> countries;
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
        countries = new ArrayList<>();
        countries.clear();
        countries.add("CountryA");
        countries.add("CountryB");
        countries.add("CountryC");
        return countries;

    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    //Methods
    //save new category
    public void saveNewCategory(ActionEvent event) {

        try {
            categoryId =saveNewCategory_1(categoryName);
            if (categoryId != -2l) {
                statusMessage = "category saved successfully";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ADD NEW CATEGORY RESULT: " + statusMessage + "(new category id is " + categoryId + ")", ""));
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
        categoryList = searchCategory_1(categoryName);
        if (categoryList == null) {
            FacesMessage msg;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "No such Prduct", "The category you are searching for does not exist");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            categoryList.clear();

        }
    }

    //view all categorys
    public void viewAllcategories(ActionEvent event) {
        categoryList = veiwAllCategories();
        if (categoryList == null) {
            FacesMessage msg;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "empty", "No result");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            categoryList.clear();
        }
    }

    //delete category
    public void deletecategory(ActionEvent event) {
        categoryId = (Long) event.getComponent().getAttributes().get("categoryId");
        try {
            Categories category = deleteCategory(categoryId);
            statusMessage = " Deleted successfully";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, statusMessage, ""));
            categoryList.remove(category);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Edit the category
    public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("category Edited");
        Categories edit = (Categories) event.getObject();
        onRowEdit_1(edit);
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

    private Long saveNewCategory_1(java.lang.String categoryName) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        category.CategoryWS port = C_service.getCategoryWSPort();
        return port.saveNewCategory(categoryName);
    }

    private Categories deleteCategory(java.lang.Long categoryId) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        category.CategoryWS port = C_service.getCategoryWSPort();
        return port.deleteCategory(categoryId);
    }

    private void onRowEdit_1(category.Categories edit) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        category.CategoryWS port = C_service.getCategoryWSPort();
        port.onRowEdit(edit);
    }

    private java.util.List<category.Categories> searchCategory_1(java.lang.String categoryName) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        category.CategoryWS port = C_service.getCategoryWSPort();
        return port.searchCategory(categoryName);
    }

    private java.util.List<category.Categories> veiwAllCategories() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        category.CategoryWS port = C_service.getCategoryWSPort();
        return port.veiwAllCategories();
    }



}
