package com.codeup.coopcredit.infrastructure.config;

import com.codeup.coopcredit.application.usecase.CreateCreditRequestUseCaseImpl;
import com.codeup.coopcredit.application.usecase.EditAffiliateUseCaseImpl;
import com.codeup.coopcredit.application.usecase.EvaluateCreditRequestUseCaseImpl;
import com.codeup.coopcredit.application.usecase.RegisterAffiliateUseCaseImpl;
import com.codeup.coopcredit.domain.ports.in.CreateCreditRequestUseCase;
import com.codeup.coopcredit.domain.ports.in.EditAffiliateUseCase;
import com.codeup.coopcredit.domain.ports.in.EvaluateCreditRequestUseCase;
import com.codeup.coopcredit.domain.ports.in.RegisterAffiliateUseCase;
import com.codeup.coopcredit.domain.ports.out.AffiliateRepositoryPort;
import com.codeup.coopcredit.domain.ports.out.CreditRequestRepositoryPort;
import com.codeup.coopcredit.domain.ports.out.RiskCentralPort;
import com.codeup.coopcredit.infrastructure.adapter.output.persistence.repository.SpringDataUserRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {

    @Bean
    public RegisterAffiliateUseCase registerAffiliateUseCase(AffiliateRepositoryPort repository) {
        return new RegisterAffiliateUseCaseImpl(repository);
    }

    @Bean
    public EditAffiliateUseCase editAffiliateUseCase(AffiliateRepositoryPort repository) {
        return new EditAffiliateUseCaseImpl(repository);
    }

    @Bean
    public CreateCreditRequestUseCase createCreditRequestUseCase(CreditRequestRepositoryPort creditRepo,
            AffiliateRepositoryPort affiliateRepo) {
        return new CreateCreditRequestUseCaseImpl(creditRepo, affiliateRepo);
    }

    @Bean
    public EvaluateCreditRequestUseCase evaluateCreditRequestUseCase(CreditRequestRepositoryPort creditRepo,
            RiskCentralPort riskPort) {
        return new EvaluateCreditRequestUseCaseImpl(creditRepo, riskPort);
    }

    @Bean
    public UserDetailsService userDetailsService(SpringDataUserRepository userRepository) {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public org.springframework.web.client.RestTemplate restTemplate() {
        return new org.springframework.web.client.RestTemplate();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
