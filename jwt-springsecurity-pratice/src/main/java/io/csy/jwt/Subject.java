
package io.csy.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subject {
	
	private String accountEmail;
	
	private String accountRole;

	private String tokenType; 
	
	public static Subject atk(String accountEmail, String accountRole) {
		
		return new Subject(accountEmail, accountRole, "ATK");
	}
	
	public static Subject rtk(String accountEmail, String accountRole) {
		
		return new Subject(accountEmail, accountRole, "RTK");
	}

}
