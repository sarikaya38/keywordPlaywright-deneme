package com.kdd.actions;

import com.kdd.config.DriverManager;
import com.kdd.config.GlobalVariables;
import com.kdd.config.SessionDataManager;
import com.kdd.exceptions.InvalidLocatorException;
import com.kdd.pages.*;
import com.kdd.utility.ElementOperations;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import java.io.FileInputStream;

import static org.testng.Assert.*;

public class ActionsClass extends ElementOperations implements GlobalVariables {
    public ActionsClass() {
        driver = DriverManager.getInstance().getDriver();
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, implicitWaitTime), this);
    }

    private WebDriver driver;
    String locatorName = null;
    String value = null;
    By locater = By.xpath("");
    FileInputStream file = null;
    String subvalue = "";
    String header = "";
    Actions actions;
    private static final Logger Log = Logger.getLogger(ActionsClass.class.getName());
    private HomePage homePage = new HomePage();
    private SignInPage loginPage = new SignInPage();
    private RegisterationPage registerationPage = new RegisterationPage();
    private ProductsPage productsPage = new ProductsPage();
    private ShoppingCartPage cartPage = new ShoppingCartPage();
    private BillingDetailsPage billingDetailsPage = new BillingDetailsPage();
    private ShippingAddressPage shippingAddressPage = new ShippingAddressPage();
    private OrderPage orderPage = new OrderPage();

    public void navigateToRegistrationScreen(String data) {
        homePage.clickSignupLink();
    }

    public void navigateToLoginScreen(String data) {
        homePage.clickSigninLink();
    }

    public void navigateToShoppingCart(String data) {
        homePage.clickCartLink();
    }

    public void navigateToMyOrdersScreen(String data) {
        homePage.clickMyOrdersLink();
    }

    public void navigateToMyAccountScreen(String data) {
        homePage.clickMyAccountLink();
    }

    public void login(String data) {
        String[] dataArray = data.split("\\|");
        //	loginToStore(dataArray[0], dataArray[1]);

    }

    public void openUrl(String value, String locate) {

        driver.get(value);


    }

    public void click(String value) {


        System.out.println(value);
        if (value.contains("-")) {

            header = value.split("-")[1];

            subvalue = value.split("-")[2];
            System.out.println(header);
            System.out.println(subvalue);

            if (header.equalsIgnoreCase("header")) {
                System.out.println("************");
                locater = By.xpath("//div[@class='ust kapsayici']//child::a[text()='" + subvalue + "']");
            }
            if (header.equalsIgnoreCase("LeftMenu")) {
                locater = By.xpath("//nav//child::a[text()='" + subvalue + "']");
            }
            if (header.equalsIgnoreCase("Footer")) {
                locater = By.xpath("//div[@class='alt kapsayici']//child::a[text()='" + subvalue + "']");
            }
        } else locater = By.xpath("//span[text()='" + value + "']");

        Actions actions = new Actions(driver);
        actions.click(driver.findElement(locater)).perform();


    }

    public void verıfyOpenedPage(String value, String locate) {
        driver.getCurrentUrl().contains(value);
    }

    public void input(String locate, String value) throws InterruptedException {

        System.out.println(value+"***************");
        System.out.println(locate+"++++++++++++++");
        if (value.contains("-")) {

            header = value.split("-")[1];

            subvalue = value.split("-")[2];
            System.out.println(header);
            System.out.println(subvalue);


            locater = By.xpath("(//*[text()='" + header + "']//following::label[text()='" + subvalue + "']//following-sibling::input)[1]");
            driver.findElement(locater).sendKeys(locate);
Thread.sleep(2000);


        }
    }

    public void selectRadioButton1111111111111111(String value) throws InterruptedException {
        System.out.println("radıo button");

        //locater = By.xpath("//span[text()='" + locatorValue + "']");

        header = value.split("-")[0];
        System.out.println(header);
        subvalue = value.split("-")[1];
        System.out.println(subvalue);
        Thread.sleep(2000);

        locater = By.xpath("//*[text()='" + header + "']//parent::div//following::a//*[text()='" + subvalue + "']");
        actions = new Actions(driver);
        actions.click(driver.findElement(locater)).perform();
        //driver.findElement(locater).click();

        Thread.sleep(3000);
    }

    public void enterUserInformation(String data) {
        String[] dataArray = data.split("\\|");
        String userName = dataArray[0] + System.nanoTime();
        SessionDataManager.getInstance().setSessionData("userName", userName);
        SessionDataManager.getInstance().setSessionData("password", dataArray[1]);
        loginPage.enterUserName(userName);
        loginPage.enterPassword(dataArray[1]);
        registerationPage.enterConfirmPassowrd(dataArray[2]);
    }

    public void enterAccountInformation(String data) {
        String[] dataArray = data.split("\\|");
        registerationPage.enterFirstName(dataArray[0]);
        registerationPage.enterLastName(dataArray[1]);
        registerationPage.enterEmail(dataArray[2]);
        registerationPage.enterTelephone(dataArray[3]);
        registerationPage.enterAddress1(dataArray[4]);
        registerationPage.enterAddress2(dataArray[5]);
        registerationPage.enterCity(dataArray[6]);
        registerationPage.enterState(dataArray[7]);
        registerationPage.enterZip(dataArray[8]);
        registerationPage.enterCountry(dataArray[9]);
        registerationPage.selectLanguage(dataArray[10]);
        registerationPage.selectFavouriteCategory(dataArray[11]);
        if (dataArray[12].equalsIgnoreCase("yes")) {
            registerationPage.clickMyList();
        }
        if (dataArray[13].equalsIgnoreCase("yes")) {
            registerationPage.clickMyBanner();
        }
    }

    public void clickSaveButton(String data) {
        registerationPage.clickSaveButton();
    }

    public void validateSuccessMessage(String data) {
        assertEquals(registerationPage.getMessage(), data);
    }

    public void validateMessageContainsText(String data) {
        Log.info("Expected Message: " + data);
        assertTrue(registerationPage.getMessage().contains(data));
    }

    public void logout(String data) {
        homePage.clickLogoutLink();
        assertTrue(homePage.isSignInLinkDisplayed(), "Logout unsuccessful");
    }

    public void searchForPet(String data) {
        homePage.searchPet(data);
        homePage.clickSearchButton();
        assertTrue(homePage.isPageHeadingDisplayed(), "Search Results were not displayed");
    }

    public void selectAPet(String data) {
        productsPage.selectFirstPetFromSearchResult();
        SessionDataManager.getInstance().setSessionData("petDescription", productsPage.getPetDescritpion());
    }

    public void selectPetByID(String data) {
        productsPage.selectFirstPetID();
        assertEquals(SessionDataManager.getInstance().getSessionData("petDescription"), productsPage.getPetPDPDescritpion(),
                "Pet Description did not match on PDP");
    }

    public void addToCart(String data) {
        productsPage.clickAddToCartButton();
        assertEquals(SessionDataManager.getInstance().getSessionData("petDescription"), productsPage.getPetCartDescritpion(),
                "Pet Description did not match on Cart");
    }

    public void updateCart(String data) {
        cartPage.changeQuantity(data);
        cartPage.clickUpdateCartButton();
    }

    public void proceedToCheckout(String data) {
        cartPage.clickProceedToCheckoutButton();
    }

    public void continueCheckout(String data) {
        billingDetailsPage.clickContinueButton();
    }

    public void confirmOrder(String data) throws InterruptedException {
        System.out.println("radıo button");

        //locater = By.xpath("//span[text()='" + locatorValue + "']");
        System.out.println(value + "sadsada" + value.contains("-"));
        header = value.split("-")[0];
        System.out.println(header);
        subvalue = value.split("-")[1];
        System.out.println(subvalue);
        Thread.sleep(2000);

        locater = By.xpath("//*[text()='" + header + "']//parent::div//following::a//*[text()='" + subvalue + "']");
        actions = new Actions(driver);
        actions.click(driver.findElement(locater)).perform();
        driver.findElement(locater).click();

        Thread.sleep(2000);
    }

    public void validateOrderDetails(String data) {
        assertTrue(orderPage.isOrderNoDisplayed(), "Order No did not displayed");
        assertTrue(orderPage.isOrderDateDisplayed(), "Order Date was not displayed");
        orderPage.getOrderNo();
        assertEquals(orderPage.getDescription(), SessionDataManager.getInstance().getSessionData("petDescription"),
                "Pet description did not match on Order Details");
        assertEquals(orderPage.getQuality(), data, "Quantity did not match");
    }

    public void selectAnOrder(String data) {
        SessionDataManager.getInstance().setSessionData("orderId", orderPage.getFirstOrderID());
        orderPage.selectFirstOrder();
    }

    public void deleteOrder(String data) {
        orderPage.clickDeleteOrderButton();
        assertTrue(homePage.isPageHeadingDisplayed(), "My Order page was not displayed");
    }

    public void validateOrderIsDeleted(String data) {
        String orderID = (String) SessionDataManager.getInstance().getSessionData("orderId");
        assertFalse(orderID.equalsIgnoreCase(orderPage.getFirstOrderID()), "Order was not deleted");
    }

    public void removePetFromCart(String data) throws InvalidLocatorException {
        int beforeRemove = cartPage.getPetsInCart();
        cartPage.clickRemoveButton();
        assertEquals(cartPage.getPetsInCart(), beforeRemove - 1, "Pet was not removed");
    }

    public void removeAllPetFromCart(String data) {
        cartPage.clickRemoveAllButton();
        assertEquals(cartPage.getEmptyCartMessage(), data, "Cart was not empty");
    }

    public void selectPetCategory(String data) throws InvalidLocatorException {
        homePage.selectPetByCategory(data);
        assertTrue(homePage.isPageHeadingDisplayed(), data + " category was not displayed");
    }

    public void updatePaymentDetails(String data) {
        String[] dataArray = data.split("\\|");
        billingDetailsPage.selectCardType(dataArray[0]);
        billingDetailsPage.updateCardNumber(dataArray[1]);
        billingDetailsPage.updateExpiryDate(dataArray[2]);

    }

    public void updateBillingAddress(String data) {
        String[] dataArray = data.split("\\|");
        billingDetailsPage.updateFirstName(dataArray[0]);
        billingDetailsPage.updateLastName(dataArray[1]);
        billingDetailsPage.updateAddress1(dataArray[2]);
        billingDetailsPage.updateAddress2(dataArray[3]);
        billingDetailsPage.updateCity(dataArray[4]);
        billingDetailsPage.updateState(dataArray[5]);
        billingDetailsPage.updateZip(dataArray[6]);
        billingDetailsPage.updateCountry(dataArray[7]);
    }

    public void quit(String value) throws InterruptedException {
        driver.navigate().back();
        Thread.sleep(3000);

    }

    public void updateShippingAddress(String data) {
        String[] dataArray = data.split("\\|");
        shippingAddressPage.updateFirstName(dataArray[0]);
        shippingAddressPage.updateLastName(dataArray[1]);
        shippingAddressPage.updateAddress1(dataArray[2]);
        shippingAddressPage.updateAddress2(dataArray[3]);
        shippingAddressPage.updateCity(dataArray[4]);
        shippingAddressPage.updateState(dataArray[5]);
        shippingAddressPage.updateZip(dataArray[6]);
        shippingAddressPage.updateCountry(dataArray[7]);
    }
}
