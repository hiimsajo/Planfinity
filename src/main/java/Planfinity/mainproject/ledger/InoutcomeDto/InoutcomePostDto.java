package Planfinity.mainproject.ledger.InoutcomeDto;

import Planfinity.mainproject.ledger.domain.Inoutcome;
import Planfinity.mainproject.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InoutcomePostDto {

    @JsonProperty(value = "member_id")
    private Long memberId;

    @JsonProperty(value = "in_outcome_name")
    private String inoutcomeName;

    public Inoutcome toEntity(Member member) {
        return new Inoutcome(member, inoutcomeName);
    }
}
