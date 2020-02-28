package cc.devcp.project.console.security;


import cc.devcp.project.console.filter.JwtAuthenticationTokenFilter;
import cc.devcp.project.console.security.users.AppUserDetailsServiceImpl;
import cc.devcp.project.core.auth.AuthConfigs;
import cc.devcp.project.core.auth.AuthSystemTypes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

/**
 * Spring security config
 *
 * @author Nacos
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppAuthConfig extends WebSecurityConfigurerAdapter {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String SECURITY_IGNORE_URLS_SPILT_CHAR = ",";

    public static final String LOGIN_ENTRY_POINT = "/v1/auth/login";

    public static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/v1/auth/**";

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String CONSOLE_RESOURCE_NAME_PREFIX = "console/";

    @Autowired
    private Environment env;

    @Autowired
    private JwtTokenManager tokenProvider;

    @Autowired
    private AuthConfigs authConfigs;

    @Autowired
    private AppUserDetailsServiceImpl userDetailsService;

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) {

        String ignoreURLs = null;
//
        if (AuthSystemTypes.SYSTEM.name().equalsIgnoreCase(authConfigs.getAuthSystemType())) {
            ignoreURLs = "/**";
        }
//
        if (StringUtils.isBlank(authConfigs.getAuthSystemType())) {
            ignoreURLs = env.getProperty("app.security.ignore.urls", "/**");
        }

        if (StringUtils.isNotBlank(ignoreURLs)) {
            for (String ignoreURL : ignoreURLs.trim().split(SECURITY_IGNORE_URLS_SPILT_CHAR)) {
                web.ignoring().antMatchers(ignoreURL.trim());
            }
        }

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        if (StringUtils.isBlank(authConfigs.getAuthSystemType())) {
            http

                .csrf().disable()
                .cors() // We don't need CSRF for JWT based authentication

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(LOGIN_ENTRY_POINT).permitAll()

                .and()
                .authorizeRequests()
                .antMatchers(TOKEN_BASED_AUTH_ENTRY_POINT).authenticated()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint());

            // disable cache
            http.headers().cacheControl();

            http.addFilterBefore(new JwtAuthenticationTokenFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
