package com.voyagerss.persist.dto;

import com.voyagerss.persist.enums.TeamRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class TeamMemberDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long teamId;
    private Long accountId;
    private TeamRole role;
    private boolean accepted;
    private boolean rejected;
    private String invitationToken;
    
    // Additional display fields
    private String accountName;
    private String accountEmail;

    public void validate() {
        if (teamId == null) {
            throw new IllegalArgumentException("Team ID cannot be null");
        }
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
    }
} 