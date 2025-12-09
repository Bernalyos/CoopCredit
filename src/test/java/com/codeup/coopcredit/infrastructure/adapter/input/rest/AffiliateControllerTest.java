package com.codeup.coopcredit.infrastructure.adapter.input.rest;

import com.codeup.coopcredit.application.usecase.EditAffiliateUseCaseImpl;
import com.codeup.coopcredit.application.usecase.RegisterAffiliateUseCaseImpl;
import com.codeup.coopcredit.infrastructure.adapter.input.rest.dto.AffiliateRequest;
import com.codeup.coopcredit.infrastructure.config.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AffiliateController.class)
@AutoConfigureMockMvc(addFilters = false) // Disabling security filters for simplicity in this specific test or need to
                                          // mock JWT
class AffiliateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegisterAffiliateUseCaseImpl registerUseCase;

    @MockBean
    private EditAffiliateUseCaseImpl editUseCase;

    @MockBean
    private JwtService jwtService; // Required for security config if filters were active, but here we bypass or
                                   // mock user

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRegisterAffiliateWhenValid() throws Exception {
        AffiliateRequest request = new AffiliateRequest();
        request.setDocument("12345");
        request.setName("John Doe");
        request.setSalary(BigDecimal.valueOf(5000));
        request.setAffiliationDate(LocalDate.now());

        mockMvc.perform(post("/affiliates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnBadRequestWhenInvalid() throws Exception {
        AffiliateRequest request = new AffiliateRequest(); // Empty fields

        mockMvc.perform(post("/affiliates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // Validates Phase 7 work
    }
}
