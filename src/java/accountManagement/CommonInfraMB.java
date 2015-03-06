/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accountManagement;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.xml.ws.WebServiceRef;
import wx.accMngmtWS.AdminAccMngmtWS_Service;
import wx.accMngmtWS.AdminUsr;
import wx.custAccMngmtWS.CustAccMngmtWS_Service;
import wx.custAccMngmtWS.Customer;
import wx.custAccMngmtWS.OrderDetail;

/**
 *
 * @author ¿.¿.¿
 */
@ManagedBean (name = "commonInfraMB")
@SessionScoped
public class CommonInfraMB {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/WineXpressWebService-war/CustAccMngmtWS.wsdl")
    private CustAccMngmtWS_Service service_1;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/WineXpressWebService-war/AdminAccMngmtWS.wsdl")
    private AdminAccMngmtWS_Service service;
    
    //Check login Flag variables and ID input for Webservice
    private long logInAdmin;
    private long logInCust;
    
    //Input Varaibles to Web Services
    //Login
    private String emailAdd;
    private String password;
    //Edit Particulars
    private AdminUsr newAdminUsr;
    private Customer newCust;
    //Change PW
    private String oldPW;
    private String newPW;
    //Selected Customer
    private Customer selectedCustomer;
    
    //Output variables from Web Services
    //view particulars
    private Customer outCust;
    private AdminUsr outAdmin;
    //Get all Customer
    private List<wx.accMngmtWS.Customer> custList;
    //Get Purchase History
    private List<OrderDetail> custPurchaseHistory;
    private List<wx.accMngmtWS.OrderDetail> adminViewPurchaseHistory;
    

    /**
     * Creates a new instance of CommonInfraMB
     */
    public CommonInfraMB() {
        this.logInAdmin = -1;
        this.logInCust = -1;
        
    }
    
    //WebService methods
    //Admin methods
    public void blockCust(ActionEvent actionEvent) throws IOException {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        wx.accMngmtWS.AdminAccMngmtWS port = service.getAdminAccMngmtWSPort();
        if (port.blockCust(this.getLogInAdmin(), this.getSelectedCustomer().getId())) {
            infoMsg("Customer Blocked");
            FacesContext.getCurrentInstance().getExternalContext().redirect("../index.xhtml");
        } else {
            errorMsg("Member Activation Fail");
        }
    }
    
    public void cancelAccount(ActionEvent actionEvent) throws IOException {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        wx.accMngmtWS.AdminAccMngmtWS port = service.getAdminAccMngmtWSPort();
        if (port.cancelAccount(this.getLogInAdmin())) {
            infoMsg("Account Cancelled");
            FacesContext.getCurrentInstance().getExternalContext().redirect("../index.xhtml");
        } else {
            errorMsg("Account Cancellaion Failed");
        }
    }

    public void changePwAdmin(ActionEvent actionEvent) throws IOException {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        wx.accMngmtWS.AdminAccMngmtWS port = service.getAdminAccMngmtWSPort();
        if (port.changePwAdmin(this.getLogInAdmin(), this.getOldPW(), this.getNewPW())) {
            infoMsg("Password Change Success");
            FacesContext.getCurrentInstance().getExternalContext().redirect("../index.xhtml");
        } else {
            errorMsg("Password Change Failed");
        }
    }

    public void editParticularsAdmin(ActionEvent actionEvent) throws IOException {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        wx.accMngmtWS.AdminAccMngmtWS port = service.getAdminAccMngmtWSPort();
        if (port.editParticularsAdmin(this.getLogInAdmin(), this.getNewAdminUsr())) {
            infoMsg("Password Change Success");
            FacesContext.getCurrentInstance().getExternalContext().redirect("../index.xhtml");
        } else {
            errorMsg("Password Change Failed");
        }
    }

    public void getAllCust(ActionEvent actionEvent) throws IOException {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        wx.accMngmtWS.AdminAccMngmtWS port = service.getAdminAccMngmtWSPort();
        this.setCustList(port.getAllCust(this.getLogInAdmin()));
    }

    public void logInAdmin(ActionEvent actionEvent) throws IOException {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        wx.accMngmtWS.AdminAccMngmtWS port = service.getAdminAccMngmtWSPort();
        this.setLogInAdmin(port.logInAdmin(this.getEmailAdd(), this.getPassword()));
        if (this.getLogInAdmin() == -1) {
            errorMsg("Login Failed");
        } else {
            infoMsg("Login Success");
            FacesContext.getCurrentInstance().getExternalContext().redirect("../AdminPortal/AdminHomePage.xhtml");
        }
    }

    public void viewAccInfoAdmin(ActionEvent actionEvent) throws IOException {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        wx.accMngmtWS.AdminAccMngmtWS port = service.getAdminAccMngmtWSPort();
        this.setOutAdmin(port.viewAccInfoAdmin(this.getLogInAdmin()));
    }

    public void viewPurchaseHistoryAdmin(ActionEvent actionEvent) throws IOException {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        wx.accMngmtWS.AdminAccMngmtWS port = service.getAdminAccMngmtWSPort();
        this.setAdminViewPurchaseHistory(port.viewPurchaseHistoryAdmin(this.getLogInAdmin(), this.getSelectedCustomer().getId()));
    }

    //Customer methods
   
    public void changePwMember(ActionEvent actionEvent) throws IOException {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        wx.custAccMngmtWS.CustAccMngmtWS port = service_1.getCustAccMngmtWSPort();
        if (port.changePwMember(this.getLogInCust(), this.getOldPW(), this.getNewPW())) {
            infoMsg("Password Change Success");
            FacesContext.getCurrentInstance().getExternalContext().redirect("../index.xhtml");
        } else {
            errorMsg("Password Change Failed");
        }
    }

    public void editParticularsMember(ActionEvent actionEvent) throws IOException {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        wx.custAccMngmtWS.CustAccMngmtWS port = service_1.getCustAccMngmtWSPort();
        if (port.editParticularsMember(this.getLogInCust(), this.getNewCust())) {
            infoMsg("Password Change Success");
            FacesContext.getCurrentInstance().getExternalContext().redirect("../index.xhtml");
        } else {
            errorMsg("Password Change Failed");
        }
    }
    
    public void logInMember(ActionEvent actionEvent) throws IOException {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        wx.custAccMngmtWS.CustAccMngmtWS port = service_1.getCustAccMngmtWSPort();
        this.setLogInCust(port.logInMember(this.getEmailAdd(), this.getPassword()));
        if (this.getLogInCust() == -1) {
            errorMsg("Login Failed");
        } else {
            infoMsg("Login Success");
            FacesContext.getCurrentInstance().getExternalContext().redirect("../index.xhtml");
        }
    }

    public void viewAccInfoMember(ActionEvent actionEvent) throws IOException {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        wx.custAccMngmtWS.CustAccMngmtWS port = service_1.getCustAccMngmtWSPort();
        this.setOutCust(port.viewAccInfoMember(this.getLogInCust()));
    }

    public void viewPurchaseHistoryMember(ActionEvent actionEvent) throws IOException {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        wx.custAccMngmtWS.CustAccMngmtWS port = service_1.getCustAccMngmtWSPort();
        this.setCustPurchaseHistory(port.viewPurchaseHistoryMember(this.getLogInCust()));
    }

    //Non WS methods exposed and used in web pages
    public void checkAdminLogin() throws IOException {
        if (getLogInAdmin() == -1) {
            errorMsg("Please login before proceeding.");
            FacesContext.getCurrentInstance().getExternalContext().redirect("../error.xhtml");
        }
    }
    
    public void checkCustLogin() throws IOException {
        if (getLogInCust() == -1) {
            errorMsg("Please login before proceeding.");
            FacesContext.getCurrentInstance().getExternalContext().redirect("../error.xhtml");
        }
        
    }
    
    //Logout Method
    public void adminLogout(ActionEvent actionEvent) throws IOException, Exception {
        System.out.println("CommonInfraMB: LOGOUT");
        this.setLogInAdmin(-1);
        this.setLogInCust(-1);
        infoMsg("Logout Successful.");
        FacesContext.getCurrentInstance().getExternalContext().redirect("../index.xhtml");
    }
    
    //Logout Method
    public void custLogout(ActionEvent actionEvent) throws IOException, Exception {
        System.out.println("CommonInfraMB: LOGOUT");
        this.setLogInAdmin(-1);
        this.setLogInCust(-1);
        infoMsg("Logout Successful.");
        FacesContext.getCurrentInstance().getExternalContext().redirect("../index.xhtml");
    }
    
    public void errorMsg(String message) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "");
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, msg);
        context.getExternalContext().getFlash().setKeepMessages(true);
    }

    public void infoMsg(String message) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, message, "");
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, msg);
        context.getExternalContext().getFlash().setKeepMessages(true);
    }

    /**
     * @return the logInAdmin
     */
    public long getLogInAdmin() {
        return logInAdmin;
    }

    /**
     * @param logInAdmin the logInAdmin to set
     */
    public void setLogInAdmin(long logInAdmin) {
        this.logInAdmin = logInAdmin;
    }

    /**
     * @return the logInCust
     */
    public long getLogInCust() {
        return logInCust;
    }

    /**
     * @param logInCust the logInCust to set
     */
    public void setLogInCust(long logInCust) {
        this.logInCust = logInCust;
    }

    /**
     * @return the emailAdd
     */
    public String getEmailAdd() {
        return emailAdd;
    }

    /**
     * @param emailAdd the emailAdd to set
     */
    public void setEmailAdd(String emailAdd) {
        this.emailAdd = emailAdd;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the newAdminUsr
     */
    public AdminUsr getNewAdminUsr() {
        return newAdminUsr;
    }

    /**
     * @param newAdminUsr the newAdminUsr to set
     */
    public void setNewAdminUsr(AdminUsr newAdminUsr) {
        this.newAdminUsr = newAdminUsr;
    }

    /**
     * @return the newCust
     */
    public Customer getNewCust() {
        return newCust;
    }

    /**
     * @param newCust the newCust to set
     */
    public void setNewCust(Customer newCust) {
        this.newCust = newCust;
    }

    /**
     * @return the oldPW
     */
    public String getOldPW() {
        return oldPW;
    }

    /**
     * @param oldPW the oldPW to set
     */
    public void setOldPW(String oldPW) {
        this.oldPW = oldPW;
    }

    /**
     * @return the newPW
     */
    public String getNewPW() {
        return newPW;
    }

    /**
     * @param newPW the newPW to set
     */
    public void setNewPW(String newPW) {
        this.newPW = newPW;
    }

    /**
     * @return the selectedCustomer
     */
    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    /**
     * @param selectedCustomer the selectedCustomer to set
     */
    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    /**
     * @return the outCust
     */
    public Customer getOutCust() {
        return outCust;
    }

    /**
     * @param outCust the outCust to set
     */
    public void setOutCust(Customer outCust) {
        this.outCust = outCust;
    }

    /**
     * @return the outAdmin
     */
    public AdminUsr getOutAdmin() {
        return outAdmin;
    }

    /**
     * @param outAdmin the outAdmin to set
     */
    public void setOutAdmin(AdminUsr outAdmin) {
        this.outAdmin = outAdmin;
    }

    /**
     * @return the custList
     */
    public List<wx.accMngmtWS.Customer> getCustList() {
        return custList;
    }

    /**
     * @param custList the custList to set
     */
    public void setCustList(List<wx.accMngmtWS.Customer> custList) {
        this.custList = custList;
    }

    /**
     * @return the custPurchaseHistory
     */
    public List<OrderDetail> getCustPurchaseHistory() {
        return custPurchaseHistory;
    }

    /**
     * @param custPurchaseHistory the custPurchaseHistory to set
     */
    public void setCustPurchaseHistory(List<OrderDetail> custPurchaseHistory) {
        this.custPurchaseHistory = custPurchaseHistory;
    }

    /**
     * @return the adminViewPurchaseHistory
     */
    public List<wx.accMngmtWS.OrderDetail> getAdminViewPurchaseHistory() {
        return adminViewPurchaseHistory;
    }

    /**
     * @param adminViewPurchaseHistory the adminViewPurchaseHistory to set
     */
    public void setAdminViewPurchaseHistory(List<wx.accMngmtWS.OrderDetail> adminViewPurchaseHistory) {
        this.adminViewPurchaseHistory = adminViewPurchaseHistory;
    }

    
    
    
    
    
    
}
