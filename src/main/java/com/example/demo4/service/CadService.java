package com.example.demo4.service;

import com.example.demo4.integration.CadLib;
import com.sun.jna.Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CadService.class);

     private static final ExecutorService createPool = Executors.newFixedThreadPool(10);
     private static final ExecutorService closePool = Executors.newFixedThreadPool(10);

    public static void testCad() {
        run_memery_leak(10);
        // run_memery_clean(3);
    }

    private static void run_memery_leak(int num) {
        if (num > 0) {
            CompletableFuture.runAsync(() -> {
                Pointer cadImage = createImage(num);
                CompletableFuture.runAsync(() -> {
                    closeImage(cadImage, num);
                }, closePool);
                sleep();
                run_memery_leak(num - 1);
            }, createPool);
        }
    }

    private static void run_memery_clean(int num) {
        if (num > 0) {
            CompletableFuture.runAsync(() -> {
                Pointer cadImage = createImage(num);
                closeImage(cadImage, num);
                sleep();
                run_memery_clean(num - 1);
            }, createPool);
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    private static Pointer createImage(int num) {
        File file = new File("/opt/portal/libs/big.dwg");;
        CadLib cadLib = CadLib.INSTANCE;
        Pointer cadImage = cadLib.CreateCAD(null, file.getAbsolutePath());
        LOGGER.info("create image async {}", num);
        return cadImage;
    }

    private static void closeImage(Pointer cadImage, int num) {
        CadLib cadLib = CadLib.INSTANCE;
        cadLib.CloseCAD(cadImage);
        LOGGER.info("closed image async {}", num);
    }
}
