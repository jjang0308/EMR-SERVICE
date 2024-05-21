package WELCOME.EMRSERVICE.Config;


import WELCOME.EMRSERVICE.Service.Doctor.DoctorService;
import WELCOME.EMRSERVICE.Service.Member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberService memberService;
    private final DoctorService doctorService;

    @Autowired
    public SecurityConfig(MemberService memberService, DoctorService doctorService) {
        this.memberService = memberService;
        this.doctorService = doctorService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 인증을 무시하기 위한 설정
        web.ignoring().antMatchers("/static/css/**","/js/**","/img/**","/lib/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .antMatchers("/home/signup").permitAll()
                .antMatchers("/doctor/signup").permitAll()
                .antMatchers("/member/signup").permitAll()
                .antMatchers("/doctor/**").access("hasRole('ROLE_DOCTOR')")
                .antMatchers("/member/**").access("hasRole('ROLE_MEMBER')")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true) // 세션 초기화
                .and()
                .exceptionHandling()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/home/dashboard",true)
                .permitAll();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 로그인 처리를 하기 위한 AuthenticationManagerBuilder를 설정
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
        auth.userDetailsService(doctorService).passwordEncoder(passwordEncoder());
    }

}