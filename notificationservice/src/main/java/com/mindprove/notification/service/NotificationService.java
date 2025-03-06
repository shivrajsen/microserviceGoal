package com.mindprove.notification.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.mindprove.notification.dto.NotificationDTO;
import com.mindprove.notification.entity.NotificationEntity;
import com.mindprove.notification.repository.NotificationRepository;
import com.mindprove.notification.serviceImpl.NotificationServiceImpl;

import jakarta.transaction.Transactional;

@Service
public class NotificationService implements NotificationServiceImpl {

	@Autowired
	NotificationRepository notificationRepository;
	@Autowired
	ModelMapper modelMapper;
	@Autowired 
	JavaMailSender javaMailSender;
	
	@Transactional
	public NotificationDTO sendNotification(NotificationDTO notificationDto)
	{
		NotificationEntity notificationEntity = modelMapper.map(notificationDto,NotificationEntity.class);
		notificationRepository.save(notificationEntity);
		try {
			sendNotificationToUser(notificationDto);
			notificationDto.setStatus("SENT");
		}
		catch(Exception e)
		{
			notificationDto.setStatus("PENDING");
		}
		
		NotificationDTO notificationSave = modelMapper.map(notificationEntity, NotificationDTO.class);
		return notificationSave;
	}

	@Override
	public List<NotificationDTO> getNotification() {
		List<NotificationEntity> notificationEntity = notificationRepository.findAll();
	    if(notificationEntity==null)
	    {
			return null;

	    }
	    
	             return notificationEntity
	            		     .stream()
	                         .map(notification -> modelMapper.map(notification, NotificationDTO.class))  
	                         .collect(Collectors.toList());
	}
	
	public String sendNotificationToUser(NotificationDTO notificationDto)
	{
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		try {
		mailMessage.setTo("mriddhi289@gmail.com");
		mailMessage.setSubject("To check working fine or not");
		mailMessage.setText("Sending this message for an understanding between us");
		mailMessage.setFrom("mriddhi289@gmail.com");
		javaMailSender.send(mailMessage);
		System.out.println("Mail successfully");
		return "Send successfully";
		}
		catch(Exception e )
		{
		   return e.getMessage();
		}
	}
	
	public List<NotificationDTO> getNotificationThroughStatus(String status)
	{
		List<NotificationEntity> notificationEntity = notificationRepository.findByStatus(status);
		if(notificationEntity==null)
		{
			return null;
		}
		
		return notificationEntity
				.stream()
				.map(notification -> modelMapper.map(notification, NotificationDTO.class ))
				.collect(Collectors.toList());
	}	
}
 
 
   
 
  
  
  