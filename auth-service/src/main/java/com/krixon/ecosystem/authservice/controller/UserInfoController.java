package com.krixon.ecosystem.authservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserInfoController
{
    @GetMapping("/userinfo")
    @ResponseBody
    public Principal get(Principal principal)
    {

        return principal;
    }
}
