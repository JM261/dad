package com.jm.dad.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import javax.persistence.*;
import lombok.AccessLevel;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "\"USERS\"")
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   private Long id;
   
   @Column(name = "user_id")
   private String username;
   
   @Column(name = "pwd")
   private String pwd;
   
   @Column(name = "name")
   private String name;
   
   @Column(name = "nickname")
   private String nickname;
   
   @Column(name = "phone")
   private String phone;
   
   @Column(name = "birthday")
   private String birthday;

   @Column(name = "email")
   private String email;
   
   @Column(name = "role", insertable = false)
   private String role;
   
   @Column(name = "status", insertable = false)
   private String status;
   
   @Column(name = "created_at", insertable = false, updatable = false)
   private LocalDateTime createdAt;
   
   @Column(name = "updated_at", insertable = false, updatable = false)
   private LocalDateTime updatedAt;
   
   @Builder
   public User(String username, String pwd, String name, String nickname, String phone, String birthday, String email) {
       this.username = username;
       this.pwd = pwd;
       this.name = name;
       this.nickname = nickname;
       this.phone = phone;
       this.birthday = birthday;
       this.email = email;
   }
   
   public void setEncryptedPwd(String pwd) {
       this.pwd = pwd;
   }
   
   public void updateUser(String name, String nickname, String phone, String birthday, String email) {
	 this.name = name;
	 this.nickname = nickname;
	 this.phone = phone;
	 this.birthday = birthday;
	 this.email = email;
   }
}