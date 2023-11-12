package Planfinity.mainproject.ledgergroup.invitationDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class InvitationPostDto {

    @NotEmpty
    @JsonProperty(value = "emails")
    private List<InvitationEmailDto> emails;

    public List<String> extractEmails() {
        return emails.stream()
                .map((e) -> e.getEmail())
                .collect(Collectors.toList());
    }

}