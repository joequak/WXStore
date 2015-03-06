/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accountManagement;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.xml.ws.WebServiceRef;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import wx.accMngmtWS.AdminAccMngmtWS_Service;
import wx.accMngmtWS.AdminUsr;
import wx.custAccMngmtWS.CustAccMngmtWS_Service;
import wx.custAccMngmtWS.Customer;
import wx.custAccMngmtWS.OrderDetail;

/**
 *
 * @author ¿.¿.¿
 */
@ManagedBean (name = "accMngmtMB")
@ViewScoped
public class AccManagementMB {
    
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/WineXpressWebService-war/CustAccMngmtWS.wsdl")
    private CustAccMngmtWS_Service service_1;
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/WineXpressWebService-war/AdminAccMngmtWS.wsdl")
    private AdminAccMngmtWS_Service service;
    
    //For encryption
    private static final char[] PASSWORD = "enfldsgbnlsngdlksdsgm".toCharArray();
    private static final byte[] SALT = {
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,};
    
    //ManagedProperty Variables
    @ManagedProperty(value="#{commonInfraMB.logInAdmin}")
    private long logInAdmin;
    @ManagedProperty(value="#{commonInfraMB.logInCust}")
    private long logInCust;
    @ManagedProperty(value="#{commonInfraMB.outCust}")
    private Customer outCust;
    @ManagedProperty(value="#{commonInfraMB.outAdmin}")
    private AdminUsr outAdmin;
    
    
    //Input Varaibles to Web Services
    //Edit Particulars
    private AdminUsr newAdminUsr;
    private Customer newCust;
    //Change PW
    private String oldPW;
    private String newPW;
    //Selected Customer
    private Customer selectedCustomer;
    //To reactivate
    private String custToReact;
    private String adminToReact;
    
    //Output variables from Web Services
    //Get all Customer
    private List<wx.accMngmtWS.Customer> custList;
    //Get Purchase History
    private List<OrderDetail> custPurchaseHistory;
    private List<wx.accMngmtWS.OrderDetail> adminViewPurchaseHistory;

    /**
     * Creates a new instance of AccManagementMB
     */
    public AccManagementMB() {
        newAdminUsr = new AdminUsr();
        newCust = new Customer();
        oldPW = "";
        newPW = "";
        custToReact = "";
        adminToReact = "";
    }
    
    //WebService methods
    //Admin manage accounts methods
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
        
    public void reActivateAdmin(ActionEvent actionEvent) throws IOException {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        wx.accMngmtWS.AdminAccMngmtWS port = service.getAdminAccMngmtWSPort();
        if (port.reActivateAdmin(this.getAdminToReact())) {
            infoMsg("Admin Account Reactivated");
            FacesContext.getCurrentInstance().getExternalContext().redirect("../index.xhtml");
        } else {
            errorMsg("Admin Account Reactivation Failed");
        }
    }

    public void activateAccount(ActionEvent actionEvent) throws IOException {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        wx.custAccMngmtWS.CustAccMngmtWS port = service_1.getCustAccMngmtWSPort();
        if (port.activateAccount(this.getCustToReact())) {
            infoMsg("Member Account Reactivated");
            FacesContext.getCurrentInstance().getExternalContext().redirect("../index.xhtml");
        } else {
            errorMsg("Member Account Reactivation Failed");
        }
    }
    
    public void getAllCust(ActionEvent actionEvent) throws IOException {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        wx.accMngmtWS.AdminAccMngmtWS port = service.getAdminAccMngmtWSPort();
        this.setCustList(port.getAllCust(this.getLogInAdmin()));
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

    //Admin Self account methods
    public void changePwAdmin(ActionEvent actionEvent) throws IOException, GeneralSecurityException {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        wx.accMngmtWS.AdminAccMngmtWS port = service.getAdminAccMngmtWSPort();
        if (port.changePwAdmin(this.getLogInAdmin(), encrypt(this.getOldPW()), encrypt(this.getNewPW()))) {
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
    
    //Customer Self account methods
    public void changePwMember(ActionEvent actionEvent) throws IOException, GeneralSecurityException {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        wx.custAccMngmtWS.CustAccMngmtWS port = service_1.getCustAccMngmtWSPort();
        if (port.changePwMember(this.getLogInCust(), encrypt(this.getOldPW()), encrypt(this.getNewPW()))) {
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
    
    private static String encrypt(String property) throws GeneralSecurityException, UnsupportedEncodingException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        return base64Encode(pbeCipher.doFinal(property.getBytes("UTF-8")));
    }

    private static String base64Encode(byte[] bytes) {
        // NB: This class is internal, and you probably should use another impl
        return new BASE64Encoder().encode(bytes);
    }

    private static String decrypt(String property) throws GeneralSecurityException, IOException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
    }

    private static byte[] base64Decode(String property) throws IOException {
        // NB: This class is internal, and you probably should use another impl
        return new BASE64Decoder().decodeBuffer(property);
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

    /**
     * @return the custToReact
     */
    public String getCustToReact() {
        return custToReact;
    }

    /**
     * @param custToReact the custToReact to set
     */
    public void setCustToReact(String custToReact) {
        this.custToReact = custToReact;
    }

    /**
     * @return the adminToReact
     */
    public String getAdminToReact() {
        return adminToReact;
    }

    /**
     * @param adminToReact the adminToReact to set
     */
    public void setAdminToReact(String adminToReact) {
        this.adminToReact = adminToReact;
    }
    
}
