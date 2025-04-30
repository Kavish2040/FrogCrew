package com.frogcrew.frogcrew.web.rest;

import com.frogcrew.frogcrew.domain.model.Notification;
import com.frogcrew.frogcrew.domain.model.FrogCrewUser;
import com.frogcrew.frogcrew.model.ApiResponse;
import com.frogcrew.frogcrew.repository.NotificationRepository;
import com.frogcrew.frogcrew.repository.FrogCrewUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

        private final NotificationRepository notificationRepository;
        private final FrogCrewUserRepository userRepository;

        @GetMapping("/{userId}")
        public ResponseEntity<ApiResponse<List<Notification>>> getNotifications(@PathVariable UUID userId) {
                FrogCrewUser user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));
                List<Notification> notifications = notificationRepository.findByUserOrderByCreatedAtDesc(user);
                return ResponseEntity.ok(ApiResponse.success("Find Success", notifications));
        }

        @PutMapping("/{notificationId}")
        public ResponseEntity<ApiResponse<Notification>> markNotificationAsRead(@PathVariable UUID notificationId) {
                Notification notification = notificationRepository.findById(notificationId)
                                .orElseThrow(() -> new RuntimeException("Notification not found"));
                notification.setRead(true);
                notificationRepository.save(notification);
                return ResponseEntity.ok(ApiResponse.success("Mark Success", notification));
        }

        @DeleteMapping("/{notificationId}")
        public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable UUID notificationId) {
                Notification notification = notificationRepository.findById(notificationId)
                                .orElseThrow(() -> new RuntimeException("Notification not found"));
                notificationRepository.delete(notification);
                return ResponseEntity.ok(ApiResponse.success("Delete Success", null));
        }
}