package com.voyagerss.api.controller;

import com.voyagerss.api.oauth.entity.UserPrincipal;
import com.voyagerss.api.oauth.token.JwtTokenProvider;
import com.voyagerss.persist.component.exception.CommonException;
import com.voyagerss.persist.component.exception.CommonExceptionType;
import com.voyagerss.persist.dto.AccountDTO;
import com.voyagerss.persist.dto.AccountInfoDTO;
import com.voyagerss.persist.dto.AccountRoleDTO;
import com.voyagerss.persist.dto.QueryDTO;
import com.voyagerss.persist.entity.Account;
import com.voyagerss.persist.service.AccountInfoService;
import com.voyagerss.persist.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AccountService accountService;
    private final AccountInfoService accountInfoService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity getUserByAuth(HttpServletRequest request) {
        UserPrincipal userPrincipal;
        try {
            userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException e){
            return new ResponseEntity<String>("token is " + request.getHeader("Authorization"), HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(accountService.getAccountDtoById(userPrincipal.getUserId()));
    }

    // SignUp method
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody AccountDTO accountDTO) {
        try {
            if (accountService.existsByEmail(accountDTO.getEmail())) {
                throw new CommonException(CommonExceptionType.EMAIL_ALREADY_EXISTS);
            }

            accountDTO.setPassword(passwordEncoder.encode(accountDTO.getPassword())); // Encrypt password

            Account account = accountService.save(accountDTO);
            AccountInfoDTO accountInfoDTO = new AccountInfoDTO();
            accountInfoDTO.setAccountId(account.getAccountId());
            accountInfoService.save(accountInfoDTO);

            return ResponseEntity.ok(account);
        } catch (CommonException ce) {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ce);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonException(CommonExceptionType.INTERNAL_SERVER_ERROR, e));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AccountDTO accountDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    accountDTO.getEmail(),
                    accountDTO.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Get user details from AccountService
            accountDTO = accountService.getAccountDtoByEmail(accountDTO.getUsername());
            
            // Create token using JwtTokenProvider
            String token = tokenProvider.createAccessToken(
                accountDTO.getAccountId(),
                accountDTO.getEmail(),
                accountDTO.getAccountRoles().stream().map(AccountRoleDTO::getRoleType).toList()
            );

            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new CommonException(CommonExceptionType.INVALID_CREDENTIALS));
        }
    }
}
