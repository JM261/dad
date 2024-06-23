package com.jm.dad.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "\"BOARD\"")
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "user_id")
	private String username;
	
	@Column(name = "pwd")
	private String pwd;
	
	@Column(name = "views")
	private long views;
	
	@Column(name = "status", insertable = false)
	private String status;
	
	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;
   
	@Column(name = "updated_at", insertable = false, updatable = false)
	private LocalDateTime updatedAt;
}
