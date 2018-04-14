package primitivebank.controller;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import primitivebank.error.AccountNotFoundException;
import primitivebank.error.DepositFailedException;
import primitivebank.error.NoEnoughBalanceException;
import primitivebank.service.AccountService;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    // -- deposit
    @Test
    public void testDepositSuccess() throws Exception {

        doNothing().when(accountService).deposit(anyLong(), anyDouble());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", 100.0);

        this.mockMvc.perform(put("/api/v1/account/1/balance/deposit")
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk());

        verify(accountService, times(1)).deposit(1L, 100.0);
    }

    @Test
    public void testDepositInvalidInput() throws Exception {

        doNothing().when(accountService).deposit(anyLong(), anyDouble());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amountx", 100.0);

        this.mockMvc.perform(put("/api/v1/account/1/balance/deposit")
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.key").value("input.invalid.amountDto.amount.NotNull"));

        verify(accountService, times(0)).deposit(1L, 100.0);
    }

    @Test
    public void testDepositThrowsAccountNotFoundException() throws Exception {

        doThrow(new AccountNotFoundException()).when(accountService).deposit(anyLong(), anyDouble());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", 100.0);

        this.mockMvc.perform(put("/api/v1/account/1/balance/deposit")
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.key").value("account.not.found"));

        verify(accountService, times(1)).deposit(1L, 100.0);
    }

    @Test
    public void testDepositThrowsDepositFailedException() throws Exception {

        doThrow(new DepositFailedException()).when(accountService).deposit(anyLong(), anyDouble());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", 100.0);

        this.mockMvc.perform(put("/api/v1/account/1/balance/deposit")
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.key").value("deposit.failed"));

        verify(accountService, times(1)).deposit(1L, 100.0);
    }

    // -- withdraw

    @Test
    public void testWithdrawSuccess() throws Exception {

        doNothing().when(accountService).withdraw(anyLong(), anyDouble());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", 100.0);

        this.mockMvc.perform(put("/api/v1/account/1/balance/withdraw")
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk());

        verify(accountService, times(1)).withdraw(1L, 100.0);
    }

    @Test
    public void testWithdrawInvalidInput() throws Exception {

        doNothing().when(accountService).deposit(anyLong(), anyDouble());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amountx", 100.0);

        this.mockMvc.perform(put("/api/v1/account/1/balance/withdraw")
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.key").value("input.invalid.amountDto.amount.NotNull"));

        verify(accountService, times(0)).withdraw(1L, 100.0);
    }

    @Test
    public void testWithdrawThrowsAccountNotFoundException() throws Exception {

        doThrow(new AccountNotFoundException()).when(accountService).withdraw(anyLong(), anyDouble());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", 100.0);

        this.mockMvc.perform(put("/api/v1/account/1/balance/withdraw")
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.key").value("account.not.found"));

        verify(accountService, times(1)).withdraw(1L, 100.0);
    }

    @Test
    public void testWithdrawThrowsNoEnoughBalanceException() throws Exception {

        doThrow(new NoEnoughBalanceException()).when(accountService).withdraw(anyLong(), anyDouble());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", 100.0);

        this.mockMvc.perform(put("/api/v1/account/1/balance/withdraw")
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.key").value("no.enough.balance"));

        verify(accountService, times(1)).withdraw(1L, 100.0);
    }

    @Test
    public void checkBalance() throws Exception {
        when(accountService.checkBalance(anyLong())).thenReturn(100.0);

        this.mockMvc.perform(get("/api/v1/account/1/balance"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(100.0));

        verify(accountService, times(1)).checkBalance(1L);
    }

    @Test
    public void checkBalanceThrowsAccountNotFoundException() throws Exception {
        when(accountService.checkBalance(anyLong())).thenThrow(new AccountNotFoundException());

        this.mockMvc.perform(get("/api/v1/account/1/balance"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.key").value("account.not.found"));

        verify(accountService, times(1)).checkBalance(1L);
    }
}
