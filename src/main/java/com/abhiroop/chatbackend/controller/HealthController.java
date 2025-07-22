package com.abhiroop.chatbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping(value = "/health")
    public Map<String, String> health(){
        final var ret = new HashMap<String, String >();
        ret.put("status", "UP");
        return ret;
    }

}
