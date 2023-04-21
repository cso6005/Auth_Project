package io.csy.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountDTO {
	
    private String id;
    private String userName;
    private String email;
    private RoleEnum role;

}
