package com.digital.controller;

import com.digital.dto.EmailDto;
import com.digital.dto.ManagerStatusDto;
import com.digital.dto.ResetPasswordDto;
import com.digital.entity.User;
import com.digital.servicei.UserServiceI;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private final UserServiceI userServiceI;

    public UserController(UserServiceI userServiceI) {
        this.userServiceI = userServiceI;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value="/create")
    public ResponseEntity<String> createNewUser(@Valid @RequestBody User user){
        return new ResponseEntity<String>(userServiceI.add(user), HttpStatus.CREATED);
    }

    @PostMapping(value="/otp")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody EmailDto emailDto){
        return new ResponseEntity<String>(userServiceI.sendOtp(emailDto), HttpStatus.OK);
    }

    @PutMapping(value = "/password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto){
        return new ResponseEntity<String>(userServiceI.resetPassword(resetPasswordDto), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/status/{userId}")
    public ResponseEntity<String> manageUserStatus(@PathVariable Long userId, @Valid @RequestBody ManagerStatusDto manageStatusDto){
        return new ResponseEntity<String>(userServiceI.manageUserStatus(userId, manageStatusDto), HttpStatus.OK);
    }

}
