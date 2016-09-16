package io.pivotal.microservices.accounts;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;

import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AccountsMain.class)
public class AccountsRestServicesTest extends IntegrationTestConfiguration{
	
	
	private static final String ACCOUNT_1_NO = "123456789";
	private static final String ACCOUNT_1_NAME = "Keri Lee";
	private static final String ACCOUNTS_BY_NUMBER_URL = "/accounts/{accountNumber}";
	private static final String ACCOUNTS_BY_NAME_URL = "/accounts/owner/{name}";
	
	 ArrayList<String> accountNumberList = new ArrayList<String>();
	 
	 
	 @Test
	 public void verifyAccountsByAccountNumber(){
		 given().pathParam("accountNumber", ACCOUNT_1_NO)
		 .when().get(ACCOUNTS_BY_NUMBER_URL)
		 .then().statusCode(HttpStatus.SC_OK)
		 .body("owner",equalTo(ACCOUNT_1_NAME));
	 }
	 
	 @Test
	 public void verifyAccountsByOwner(){
		 accountNumberList.add(ACCOUNT_1_NO);
		 given().pathParam("name", ACCOUNT_1_NAME)
		 .when().get(ACCOUNTS_BY_NAME_URL)
		 .then().statusCode(HttpStatus.SC_OK)
		 .body("number", equalTo(accountNumberList));
	 }
	 
}
