package Planfinity.mainproject.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

// API 호출 시, Member의 정보가 헤더에 담겨져 올텐데, 어떤 Member가 API를 요청했는지 조회하는 코드
// SecurityUtil.getCurrentMemberEmail() 코드를 사용하면 편리하게 현재 memberEmail를 조회할 수 있다.
public class SecurityUtil {
    public static String getCurrentMemberEmail() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("No authentication information.");
        }
        return authentication.getName();
    }

}
