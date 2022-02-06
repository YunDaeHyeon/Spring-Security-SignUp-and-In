package test.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import test.Service.UserService;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers("/loginPage", "/registerPage","/register_action","/login_fail","/resource/**").permitAll()
                // 로그인, 회원가입, 로그인 실패, 리소스 접근은 누구나 가능
                .antMatchers("/loginSuccess").hasRole("USER") // /loginSuccess로 이동은 "USER" 권한만.
                .antMatchers("/loginSuccess").hasRole("ADMIN") // /loginSuccess로 이동은 "ADMIN" 권한만.
                .and()
                .formLogin()
                .loginPage("/loginPage") // 로그인 페이지 설정
                .loginProcessingUrl("/login_action") // 로그인이 진행 될 url 설정 (loginpage.html의 th:action="@{/login_action}"를 말함
                .defaultSuccessUrl("/login_success") // 로그인이 성공 시 리다이렉션
                .failureUrl("/login_fail") //로그인 실패 시 리다이렉션 (해당 form으로 error=true를 전송함.)
                .and()
                .csrf().disable();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
    }
}
