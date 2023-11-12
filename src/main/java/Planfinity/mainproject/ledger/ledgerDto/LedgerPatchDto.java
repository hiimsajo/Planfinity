package Planfinity.mainproject.ledger.ledgerDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LedgerPatchDto {
    @JsonProperty(value = "ledger_id")
    private Long ledgerId;

    @JsonProperty(value = "ledger_title")
    private String ledgerTitle;

    @JsonProperty(value = "ledger_content")
    private String ledgerContent;

    @JsonProperty(value = "ledger_amount")
    private Long ledgerAmount;

    @JsonProperty(value = "ledger_schedule_date")
    private String ledgerDate;

    @JsonProperty(value = "category_id")
    private Long categoryId;

    @JsonProperty(value = "in_outcome_id")
    @NotNull(message = "Inoutcome_required")
    private Long inoutcomeId;

    @JsonProperty(value = "payment_id")
    private Long paymentId;

    public void setLedgerId(Long ledgerId) {
        this.ledgerId = ledgerId;
    }
}

