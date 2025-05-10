package com.mindprove.notification.entity;

import java.time.LocalDateTime;

import com.mindprove.notification.dto.NotificationDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class NotificationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull(message = "Enter a valid Employee Id")
	private long id;
	
	private long userId;
	
    @Column(columnDefinition = "TEXT")
	private String message;
	private String status;
	private LocalDateTime createdAt;
	private String createBy;
	private LocalDateTime updatedAt;
	private String updatedBy;
}

   

