package com.digital.servicei;

import com.digital.dto.NotificationDto;
import com.digital.enums.EventType;

import java.util.List;

public interface NotificationService {
    List<NotificationDto> getMyNotifications();
    void sendNotification(Long teacherId, EventType reservationAvailable, String message);
    void markAsRead(Long id);
    // Parent notifications
    List<NotificationDto> getParentNotifications(Long parentId);
    void sendParentNotification(Long parentId, String message, String type);
}

