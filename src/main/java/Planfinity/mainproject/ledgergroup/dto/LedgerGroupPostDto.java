package Planfinity.mainproject.ledgergroup.dto;

import Planfinity.mainproject.ledgergroup.domain.LedgerGroup;
import Planfinity.mainproject.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class LedgerGroupPostDto {

    @JsonProperty(value = "ledger_group_title")
    private String ledgerGroupTitle;

    public LedgerGroup toEntity(Member member) {
        return new LedgerGroup(member, ledgerGroupTitle);
    }
}