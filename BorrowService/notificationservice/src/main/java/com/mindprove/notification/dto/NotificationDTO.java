package com.mindprove.notification.dto;

import java.time.LocalDateTime;

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
public class NotificationDTO {

	private String message;
	private String status ="PENDING";
	private LocalDateTime createdAt;
	private String createBy;
	private LocalDateTime updatedAt;
	private String updatedBy;
}

  