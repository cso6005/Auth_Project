package io.csy.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class SignUpDTO {
		
//	@NotBlank // null, 빈 문자열 (스페이스 포함X) 불가
	@NotEmpty(message = "이메일을 입력하지 않았습니다.") // null, 빈 문자열, 스페이스만 포함한 문자열 불가
    @Email(message = "이메일 형식이 맞지 않습니다.") // 이메일 형식만 가능
	private String accountEmail;
	
	@NotEmpty(message = "비밀번호을 입력하지 않았습니다.") // null, 빈 문자열, 스페이스만 포함한 문자열 불가
    @Pattern(regexp="[a-zA-Z1-9]{6,12}", message = "비밀번호는 영어와 숫자로 포함해서 6~12자리 이내로 입력해주세요.")
	private String accountPassword;
	
    @NotBlank(message = "이름을 입력하지 않았습니다.")
    @Size(min = 2, max = 8, message = "이름을 2 ~ 8 자 사이로 입력해주세요.")
	private String accountName;
	
	@NotNull // null 불가능
	private String accountBirth;
	
	@NotNull
	private String accountAddress;
	
	@NotNull
	@Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "전화번호 형식이 맞지 않습니다.")
	private String accountPhoneNumber;
	
	@NotNull
	private String accountGender;
	
}
