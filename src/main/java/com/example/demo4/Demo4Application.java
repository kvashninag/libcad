package com.example.demo4;

import com.sun.jna.Platform;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Demo4Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo4Application.class, args);
        cadConfig();
    }

    private static void cadConfig() {
        System.setProperty("jna.platform.library.path", Platform.isWindows() ? ".\\libs\\Windows\\" : "/opt/portal/libs/linux/");
    }
}
