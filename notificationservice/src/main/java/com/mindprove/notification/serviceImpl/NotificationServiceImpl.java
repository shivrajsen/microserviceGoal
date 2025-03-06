package com.mindprove.notification.serviceImpl;

import java.util.List;

import com.mindprove.notification.dto.NotificationDTO;

public interface NotificationServiceImpl {
  
	public NotificationDTO sendNotification(NotificationDTO notificationDto);
	public List<NotificationDTO> getNotification();
	public List<NotificationDTO> getNotificationThroughStatus(String status);
}

