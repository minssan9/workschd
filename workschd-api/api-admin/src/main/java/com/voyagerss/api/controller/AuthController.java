package com.voyagerss.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.voyagerss.api.oauth.entity.UserPrincipal;
import com.voyagerss.api.oauth.token.JwtTokenProvider;
import com.voyagerss.persist.component.exception.CommonException;
import com.voyagerss.persist.component.exception.CommonExceptionType;
import com.voyagerss.persist.dto.AccountDTO;
import com.voyagerss.persist.dto.AccountInfoDTO;
import com.voyagerss.persist.dto.AccountRoleDTO;
import com.voyagerss.persist.entity.Account;
import com.voyagerss.persist.service.AccountInfoService;
import com.voyagerss.persist.service.AccountService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

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
    public ResponseEntity<AccountDTO> getUserByAuth(HttpServletRequest request) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            AccountDTO account = accountService.getAccountDtoById(userPrincipal.getUserId());
            return ResponseEntity.ok(account);
        } catch (ClassCastException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<Account> registerUser(@RequestBody AccountDTO accountDTO) {
        try {
            if (accountService.existsByEmail(accountDTO.getEmail())) {
                throw new CommonException(CommonExceptionType.EMAIL_ALREADY_EXISTS);
            }

            accountDTO.setPassword(passwordEncoder.encode(accountDTO.getPassword()));

            Account account = accountService.save(accountDTO);
            AccountInfoDTO accountInfoDTO = new AccountInfoDTO();
            accountInfoDTO.setAccountId(account.getAccountId());
            accountInfoService.save(accountInfoDTO);

            return ResponseEntity.ok(account);
        } catch (CommonException ce) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody AccountDTO accountDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    accountDTO.getEmail(),
                    accountDTO.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            accountDTO = accountService.getAccountDtoByEmail(accountDTO.getEmail());
            
            String token = tokenProvider.createAccessToken(
                accountDTO.getAccountId(),
                accountDTO.getEmail(),
                accountDTO.getAccountRoles().stream().map(AccountRoleDTO::getRoleType).toList()
            );

            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
