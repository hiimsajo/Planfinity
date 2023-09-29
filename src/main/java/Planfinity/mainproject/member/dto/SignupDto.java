package Planfinity.mainproject.member.dto;

import Planfinity.mainproject.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class SignupDto {

    @Getter
    public static class Request {

        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "잘못된 이메일 형식입니다.")
        @JsonProperty(value = "email")
        private String email;

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",
            message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        @JsonProperty(value = "password")
        private String password;

        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,10}$",
            message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
        @JsonProperty(value = "nickname")
        private String nickname;

        public Member toEntity() {
            return new Member(email, password, nickname);
        }
    }


}
