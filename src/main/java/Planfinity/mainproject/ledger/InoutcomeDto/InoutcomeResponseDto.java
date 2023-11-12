package Planfinity.mainproject.ledger.InoutcomeDto;

import Planfinity.mainproject.ledger.domain.Inoutcome;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InoutcomeResponseDto {


    @JsonProperty(value = "in_outcome_id")
    private Long inoutcomeId;

    @JsonProperty(value = "in_outcome_name")
    private String inoutcomeName;

    public InoutcomeResponseDto(Inoutcome inoutcome) {
        this.inoutcomeId = inoutcome.getInoutcomeId();
        this.inoutcomeName = inoutcome.getInoutcomeName();
    }


}
