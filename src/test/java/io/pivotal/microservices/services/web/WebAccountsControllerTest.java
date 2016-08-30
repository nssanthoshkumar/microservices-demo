package io.pivotal.microservices.services.web;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebConfiguration.class)
public class WebAccountsControllerTest {
	
	private static final String ACCOUNT_1_NO = "123456789";
	private static final String ACCOUNT_1_NAME = "Keri Lee";
	private static final String ACCOUNTS_BY_NUMBER_URL = "/accounts/{accountNumber}";
	private static final String ACCOUNTS_BY_OWNER_URL = "/accounts/owner/{text}";
	
	@InjectMocks
	private WebAccountsController webAccountsController;
	
    MockMvc mockMvc;

    @Mock
    WebAccountsService accountsServiceMock;
    
    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(webAccountsController).build();
    }
    
    @Test
    public void testAccountsByAccountNumber() throws Exception{
    	
		given(accountsServiceMock.findByNumber(ACCOUNT_1_NO)).willReturn(accountBuilder());
		
		mockMvc.perform(get(ACCOUNTS_BY_NUMBER_URL,ACCOUNT_1_NO).accept(MediaType.APPLICATION_JSON))
    	.andExpect(status().isOk());
		
		verify(accountsServiceMock, times(1)).findByNumber(ACCOUNT_1_NO);
    }
    
    @Test
    public void testAccountsByowner() throws Exception{
    	List<Account> accounts = new ArrayList<Account>();
    	accounts.add(accountBuilder());
		given(accountsServiceMock.byOwnerContains(ACCOUNT_1_NAME)).willReturn(accounts);
    	
		 mockMvc.perform(get(ACCOUNTS_BY_OWNER_URL, ACCOUNT_1_NAME).accept(MediaType.APPLICATION_JSON))
    	.andExpect(status().isOk());
		 
		 verify(accountsServiceMock, times(1)).byOwnerContains(ACCOUNT_1_NAME);
    }
    
    private static Account accountBuilder(){
    	Account account = new Account();
    	account.setNumber(ACCOUNT_1_NO);
    	account.setOwner(ACCOUNT_1_NAME);
		return account;	
    }
}
