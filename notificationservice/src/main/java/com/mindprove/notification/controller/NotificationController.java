package com.mindprove.notification.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mindprove.notification.dto.NotificationDTO;
import com.mindprove.notification.serviceImpl.NotificationServiceImpl;

@RestController
@RequestMapping("/notification")
public class NotificationController {

	
	private final NotificationServiceImpl notificationServiceImpl;
	
	public NotificationController(NotificationServiceImpl notificationServiceImpl) {
		System.out.println("Jehjdkfk");
			
		this.notificationServiceImpl = notificationServiceImpl;
	}
	
	@PostMapping
	public NotificationDTO sendNotification(@RequestBody NotificationDTO notificationDto)
	{
		return notificationServiceImpl.sendNotification(notificationDto);
	}
	
	@GetMapping("/fetch/all")
	public List<NotificationDTO> getNotification() {
	    System.out.println("Fetching all notifications...");
	    List<NotificationDTO> notifications = notificationServiceImpl.getNotification();
	    System.out.println("Fetched notifications: " + notifications.size());
	    return notifications;
	}

	
	@GetMapping("through/status")
	public List<NotificationDTO> getThroughStatus(@RequestParam String status)
	{
		return notificationServiceImpl.getNotificationThroughStatus(status);
	}
	
}
 

 
  