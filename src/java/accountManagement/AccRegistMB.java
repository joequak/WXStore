/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accountManagement;

import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.xml.ws.WebServiceRef;
import wx.custAccMngmtWS.CustAccMngmtWS_Service;
import wx.custAccMngmtWS.Customer;

/**
 *
 * @author ¿.¿.¿
 */
@ManagedBean (name = "accRegistMB")
@ViewScoped
public class AccRegistMB {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/WineXpressWebService-war/CustAccMngmtWS.wsdl")
    private CustAccMngmtWS_Service service;
    
    //Input variable to webservices
    private Customer toRegist;
    private String emailToActivate;

    /**
     * Creates a new instance of AccRegistMB
     */
    public AccRegistMB() {
        toRegist = new Customer();
        emailToActivate = "";
    }

    public void activateAccount(ActionEvent actionEvent) throws IOException {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        wx.custAccMngmtWS.CustAccMngmtWS port = service.getCustAccMngmtWSPort();
        if (port.activateAccount(this.getEmailToActivate())) {
            infoMsg("Member Activated");
            FacesContext.getCurrentInstance().getExternalContext().redirect("../index.xhtml");
        } else {
            errorMsg("Member Activation Fail");
            FacesContext.getCurrentInstance().getExternalContext().redirect("../index.xhtml");
        }
    }

    public void registerAsMember (ActionEvent actionEvent) throws IOException {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        wx.custAccMngmtWS.CustAccMngmtWS port = service.getCustAccMngmtWSPort();
        port.registerAsMember(getToRegist());
        if (port.registerAsMember(this.getToRegist())) {
            infoMsg("Member Registered");
            FacesContext.getCurrentInstance().getExternalContext().redirect("../index.xhtml");
        } else {
            errorMsg("Member Registration Fail");
            FacesContext.getCurrentInstance().getExternalContext().redirect("../index.xhtml");
        }
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
     * @return the toRegist
     */
    public Customer getToRegist() {
        return toRegist;
    }

    /**
     * @param toRegist the toRegist to set
     */
    public void setToRegist(Customer toRegist) {
        this.toRegist = toRegist;
    }

    /**
     * @return the emailToActivate
     */
    public String getEmailToActivate() {
        return emailToActivate;
    }

    /**
     * @param emailToActivate the emailToActivate to set
     */
    public void setEmailToActivate(String emailToActivate) {
        this.emailToActivate = emailToActivate;
    }
    
}
