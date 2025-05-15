package com.voyagerss.api.controller;

import com.voyagerss.api.oauth.entity.UserPrincipal;
import com.voyagerss.api.oauth.token.JwtTokenProvider;
import com.voyagerss.persist.dto.AccountDTO;
import com.voyagerss.persist.dto.AccountInfoDTO;
import com.voyagerss.persist.dto.QueryDTO;
import com.voyagerss.persist.entity.Account;
import com.voyagerss.persist.service.AccountInfoService;
import com.voyagerss.persist.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;
    private final AccountInfoService accountInfoService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @GetMapping
    public ResponseEntity getUserByAuth(HttpServletRequest request) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            String userEmail;
             if (userPrincipal.getUserId() != null) {
                // Handle JWT token case - extract user ID from token
                String token = request.getHeader("Authorization").replace("Bearer ", "");
                userEmail = tokenProvider.getUserEmail(token);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            return ResponseEntity.ok(accountService.getAccountDtoByEmail(userEmail));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity findAccount(QueryDTO queryDTO,
                                      @PageableDefault(page = 0, size = 10, sort = "created_at", direction = Sort.Direction.DESC) Pageable pageable) {
        queryDTO.setPageable(pageable);
        Page<AccountDTO> accountDTOPage = accountService.getAccountDtoPage(queryDTO);
        return ResponseEntity.ok(accountDTOPage);
    }

    @PostMapping("/signin")
    public Account signin(@Valid @RequestBody AccountDTO vO) {
        return accountService.signin(vO);
    }


    @GetMapping("/{accountId}")
    public ResponseEntity getById(@Valid @NotNull @PathVariable("accountId") Integer accountId) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AccountDTO accountDTO = accountService.getAccountDtoById(accountId);

        return ResponseEntity.ok(accountDTO);
    }

    @PutMapping
    public ResponseEntity update(@Valid @RequestBody AccountDTO dto) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AccountDTO updatedAccount  = accountService.update(userPrincipal.getUserId(), dto);
        return ResponseEntity.ok(updatedAccount);
    }

    @PutMapping(path = "/{accountId}")
    public ResponseEntity update(
            @Valid @NotNull @PathVariable Integer accountId,
            @Valid @RequestBody AccountDTO dto) throws IOException {
        AccountDTO updatedAccount  = accountService.update(accountId, dto);
        return ResponseEntity.ok(updatedAccount);
    }


//    @PostMapping(path = "/{accountId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity updateProfileImage(
//            @Valid @NotNull @PathVariable Integer accountId,
//            @RequestPart(value = "profileImage") MultipartFile profileImage
//    ) {
//        AccountDTO accountDTO  = accountService.updateImage(accountId, profileImage);
//        return ResponseEntity.ok(accountDTO.getProfileImageUrl());
//    }


    @GetMapping("/{accountId}/info")
    public ResponseEntity getAccountInfoById(@Valid @NotNull @PathVariable("accountId") Integer accountId) {
        return ResponseEntity.ok( accountInfoService.getById(accountId));
    }

    @GetMapping("/{accountId}/team")
    public ResponseEntity getTeamByAccountId(@Valid @NotNull @PathVariable("accountId") Integer accountId) {
        return ResponseEntity.ok(accountService.getTeamByAccountId(accountId));
    }

    @PostMapping("/info")
    public ResponseEntity saveAccountInfoById(@RequestBody AccountInfoDTO vO) {
//        throw new CommonException();
        return ResponseEntity.ok(accountInfoService.save(vO));
    }

}
