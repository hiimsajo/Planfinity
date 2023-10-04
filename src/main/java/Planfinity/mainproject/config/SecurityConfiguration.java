package Planfinity.mainproject.config;

import Planfinity.mainproject.auth.filter.JwtAuthenticationFilter;
import Planfinity.mainproject.auth.filter.JwtVerificationFilter;
import Planfinity.mainproject.auth.handler.MemberAccessDeniedHandler;
import Planfinity.mainproject.auth.handler.MemberAuthenticationEntryPoint;
import Planfinity.mainproject.auth.handler.MemberAuthenticationFailureHandler;
import Planfinity.mainproject.auth.handler.MemberAuthenticationSuccessHandler;
import Planfinity.mainproject.auth.jwt.JwtTokenizer;
import Planfinity.mainproject.auth.utils.CustomAuthorityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {

    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils; // 추가


    public SecurityConfiguration(JwtTokenizer jwtTokenizer, CustomAuthorityUtils authorityUtils) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .headers().frameOptions().sameOrigin()
            .and()
            .csrf().disable()
            .cors(withDefaults())
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // (1) 추가
            .and()
            .formLogin().disable()
            .httpBasic().disable()
            .exceptionHandling()
            .authenticationEntryPoint(new MemberAuthenticationEntryPoint())  // (1) 추가
            .accessDeniedHandler(new MemberAccessDeniedHandler())            // (2) 추가
            .and()
            .apply(new CustomFilterConfigurer())
            .and()
            .authorizeHttpRequests(authorize -> authorize
                    .antMatchers(HttpMethod.POST, "/*/members").permitAll()         // (1) 추가
                    .antMatchers(HttpMethod.PATCH, "/*/members/**").hasRole("USER")  // (2) 추가
                    .antMatchers(HttpMethod.GET, "/*/members").hasRole("ADMIN")     // (3) 추가
                    .antMatchers(HttpMethod.GET, "/*/members/**").hasAnyRole("USER", "ADMIN")  // (4) 추가
                    .antMatchers(HttpMethod.DELETE, "/*/members/**").hasRole("USER")  // (5) 추가
//                .anyRequest().permitAll()
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PATCH","DELETE"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    public class CustomFilterConfigurer extends
        AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {  // (2-2)
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);  // (2-3)

            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtTokenizer);  // (2-4)
            jwtAuthenticationFilter.setFilterProcessesUrl("/auths");          // (2-5)
            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new MemberAuthenticationSuccessHandler());  // (3) 추가
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new MemberAuthenticationFailureHandler());  // (4) 추가

            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, authorityUtils);  // (2) 추가

            builder
                .addFilter(jwtAuthenticationFilter)
                .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class);   // (3)추가
        }

    }

}
