package com.frogcrew.frogcrew.web.rest;

import com.frogcrew.frogcrew.model.ApiResponse;
import com.frogcrew.frogcrew.service.UserService;
import com.frogcrew.frogcrew.service.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/crewMembers")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllCrewMembers() {
        return ResponseEntity.ok(ApiResponse.success("Find Success", userService.listAll()));
    }

    @DeleteMapping("/crewMembers/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCrewMember(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("Delete Success", null));
    }

    @PostMapping("/updateGameTimes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateGameTimes() {
        // TODO: Implement game times update logic
        return ResponseEntity.ok("Game times updated successfully");
    }

    @PostMapping("/refreshTestData")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> refreshTestData() {
        // TODO: Implement test data refresh logic
        return ResponseEntity.ok("Test data refreshed successfully");
    }

    @PostMapping("/clearTestData")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> clearTestData() {
        // TODO: Implement test data clearing logic
        return ResponseEntity.ok("Test data cleared successfully");
    }
}