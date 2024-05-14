package com.example.demo4;

import com.example.demo4.service.CadService;
import com.sun.jna.Platform;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class Demo4Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo4Application.class, args);
        cadConfig();
        CadService.testCad();
    }

    private static void cadConfig() {
        System.setProperty("jna.platform.library.path", Platform.isWindows() ? ".\\libs\\Windows\\" : "/opt/portal/libs/linux/");
    }
}
