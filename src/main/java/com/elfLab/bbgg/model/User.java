package com.elfLab.bbgg.model;

import java.sql.Timestamp;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {


	private int id;
	
	private String username;
	
	private String password;

	private String email;

	private String oauth;

	private String AUTH;
	
	private int ENABLED;


}