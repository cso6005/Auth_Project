package io.csy.hot.model.dto;

import io.csy.hot.model.entity.AccountEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AccountDTO {
	
	private int accountId;
	
	private String accountEmail;
	
	private String accountPassword;
	
	private String accountName;
	
	private String accountBirth;
	
	private String accountAddress;
	
	private String accountPhoneNumber;
	
	private String accountGender;
	
	private String accountType;
	
    public static AccountDTO toDTO(AccountEntity account) {
    	
        return new AccountDTO(
                account.getAccountId(),
                account.getAccountEmail(),
                account.getAccountPassword(),
                account.getAccountName(),
                account.getAccountBirth(),
                account.getAccountAddress(),
                account.getAccountPhoneNumber(),
                account.getAccountGender(),
                account.getAccountType());
    }

}
