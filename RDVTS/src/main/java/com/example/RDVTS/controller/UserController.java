package com.example.RDVTS.controller;

import com.example.RDVTS.dto.RDVTSResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping("/getAllUserDetails")
    public RDVTSResponse getAllUserDetails() {
        return null;
    }
}
