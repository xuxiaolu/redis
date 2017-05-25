package com.xuxl.redis.test.rest;

import com.xuxl.redis.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestService testService;


    @RequestMapping("/set/{key}/{value}")
    public String set(@PathVariable String key, @PathVariable String value) {
        testService.put(key, value);
        return "success";
    }

    @RequestMapping("/get/{key}")
    public String get(@PathVariable String key) {
       return testService.get(key);

    }
}
