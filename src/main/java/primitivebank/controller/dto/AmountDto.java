package primitivebank.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class AmountDto {

    @NotNull
    @Positive
    private Double amount;

    public static AmountDto from(Double amount) {
        AmountDto amountDto = new AmountDto();
        amountDto.amount = amount;
        return amountDto;
    }
}
