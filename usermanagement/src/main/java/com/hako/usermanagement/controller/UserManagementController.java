package com.hako.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hako.usermanagement.dto.ReqRes;
import com.hako.usermanagement.entity.Users;
import com.hako.usermanagement.service.UserManagementService;

@RestController
@RequestMapping("/")
public class UserManagementController {
    @Autowired
    private UserManagementService userManagementService;
    @PostMapping("/auth/register")
    public ResponseEntity<ReqRes> register(@RequestBody ReqRes reg) {
         return ResponseEntity.ok(userManagementService.register(reg));
    }
    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes reg) {
        return ResponseEntity.ok(userManagementService.login(reg));
    }
    @PostMapping("/auth/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes reg) {
        return ResponseEntity.ok(userManagementService.refreshToken(reg));
    }
    @GetMapping("/admin/getUsers")
    public ResponseEntity<ReqRes> getAllUsers() {
        return ResponseEntity.ok(userManagementService.getAllUsers());
    }
    @GetMapping("/admin/get-users/{userId}")
    public ResponseEntity<ReqRes> getUserById(@PathVariable Integer userId) {
        return ResponseEntity.ok(userManagementService.getUsersById(userId));
    }
    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<ReqRes> updateUser(@PathVariable Integer userId, @RequestBody Users reqres ) {
        return ResponseEntity.ok(userManagementService.updateUser(userId, reqres));
    }
    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<ReqRes> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ReqRes response = userManagementService.getMyInfo(email);
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<ReqRes> deleteUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(userManagementService.deleteUser(userId));
    }
}
