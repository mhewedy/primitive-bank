package primitivebank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import primitivebank.controller.dto.AmountDto;
import primitivebank.service.AccountService;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PutMapping("/{accountId}/balance/deposit")
    public ResponseEntity<Void> deposit(@PathVariable("accountId") Long accountId,
                                        @Valid @RequestBody AmountDto amountDto) {
        accountService.deposit(accountId, amountDto.getAmount());
        return ok().build();
    }

    @PutMapping("/{accountId}/balance/withdraw")
    public ResponseEntity<Void> withdraw(@PathVariable("accountId") Long accountId,
                                         @Valid @RequestBody AmountDto amountDto) {
        accountService.withdraw(accountId, amountDto.getAmount());
        return ok().build();
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<AmountDto> checkBalance(@PathVariable("accountId") Long accountId) {
        return ok(AmountDto.from(accountService.checkBalance(accountId)));
    }
}
