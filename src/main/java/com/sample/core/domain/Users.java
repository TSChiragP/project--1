package com.sample.core.domain;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Data
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(nullable = false)
	@NotNull(message = "username should not be empty")
	private String userName;

	@Column(nullable = false, unique = true)
	@NotNull(message = "email should not be empty")
	private String email;

	@Column(nullable = false)
	@NotNull(message = "password should not be empty")
	private String password;

	private String roles;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
	private List<Posts> posts;

}
