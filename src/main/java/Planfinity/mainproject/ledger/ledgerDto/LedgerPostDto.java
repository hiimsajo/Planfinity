package Planfinity.mainproject.ledger.ledgerDto;

import Planfinity.mainproject.exception.BusinessLogicException;
import Planfinity.mainproject.exception.ExceptionCode;
import Planfinity.mainproject.ledger.domain.Category;
import Planfinity.mainproject.ledger.domain.Inoutcome;
import Planfinity.mainproject.ledger.domain.Ledger;
import Planfinity.mainproject.ledger.domain.Payment;
import Planfinity.mainproject.ledgergroup.domain.LedgerGroup;
import Planfinity.mainproject.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LedgerPostDto {
    @JsonProperty(value = "ledger_title")
    private String ledgerTitle;

    @JsonProperty(value = "ledger_content")
    private String ledgerContent;

    @JsonProperty(value = "ledger_amount")
    private Long ledgerAmount;

    @JsonProperty(value = "ledger_schedule_date")
    private String ledgerScheduleDate;

    @JsonProperty(value = "category_id")
    private Long categoryId;

    @JsonProperty(value = "in_outcome_id")
    @NotNull(message = "Inoutcome required")
    private Long inoutcomeId;

    @JsonProperty(value = "payment_id")
    private Long paymentId;

    public Ledger toEntity(Member member, LedgerGroup ledgerGroup, Category category, Inoutcome inoutcome, Payment payment) {
        LocalDate date = LocalDate.parse(ledgerScheduleDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return new Ledger(member, ledgerGroup, ledgerTitle, ledgerContent, ledgerAmount, date, category, inoutcome, payment);
    }

    public Long getCategoryId() {

        return categoryId != null ? categoryId : null;
    }

    public Long getPaymentId() {
        return paymentId != null ? paymentId : null;
    }

    public Long getInoutcomeId() {
        if (inoutcomeId == null) {
            throw new BusinessLogicException(ExceptionCode.INOUTCOME_REQUIRED);
        }
        return inoutcomeId;
    }

}
