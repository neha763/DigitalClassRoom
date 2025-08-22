package com.digital.controller;


import com.digital.dto.NotificationDto;
import com.digital.servicei.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")

public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/me")
    public ResponseEntity<List<NotificationDto>> getMyNotifications() {
        return ResponseEntity.ok(notificationService.getMyNotifications());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/send/{teacherId}")
    public ResponseEntity<Void> sendNotification(@PathVariable Long teacherId,
                                                 @RequestBody String message) {
        notificationService.sendNotification(teacherId, message);
        return ResponseEntity.ok().build();
    }
}

