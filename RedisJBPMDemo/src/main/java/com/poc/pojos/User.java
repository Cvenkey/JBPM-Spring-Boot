package com.poc.pojos;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString
@Getter
@Setter
public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String customerId;
	private String phone;
	private String encryptedOtp;
    private String firstName;
	private String middleName;
	private String lastName;
    private String gender;
    private String address;
    private String response;
	
}
