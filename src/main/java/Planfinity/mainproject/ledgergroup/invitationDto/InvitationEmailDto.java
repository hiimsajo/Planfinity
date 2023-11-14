package Planfinity.mainproject.ledgergroup.invitationDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InvitationEmailDto {

    @NotBlank
    private String email;

}
