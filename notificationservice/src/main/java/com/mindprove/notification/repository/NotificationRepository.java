package com.mindprove.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mindprove.notification.entity.NotificationEntity;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long>{

    List<NotificationEntity> findByStatus(String status);

//    @Query("SELECT n FROM Notification n WHERE n.status = :status")
//	List<NotificationEntity> findByStatus(@Param("status") String status);
    
}

 