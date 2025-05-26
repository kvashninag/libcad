package com.example.demo4.service;

import com.example.demo4.integration.CadLib;
import com.sun.jna.Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CadService.class);

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void testCad() {
        CadLib cadLib = CadLib.INSTANCE;
        for (int i = 0; i < 50; i++) {
            createImageAsync();
            sleep();
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(5_000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    private static Pointer createImage() {
        File file = new File("/opt/portal/libs/big.dwg");;
        CadLib cadLib = CadLib.INSTANCE;
        Pointer cadImage = cadLib.CreateCAD(null, file.getAbsolutePath());
        cadLib.SetCADBorderType(cadImage, 0);
        cadLib.SetCADBorderSize(cadImage, 0);
        LOGGER.info("CAD image create async");
        return cadImage;
    }

    private static void closeImage(Pointer cadImage) {
        CadLib cadLib = CadLib.INSTANCE;
        cadLib.CloseCAD(cadImage);
    }

    private static void createImageAsync() {
        executor.execute(() -> {
            Pointer cadImage = createImage();
            closeImageAsync(cadImage);
        });
    }

    private static void closeImageAsync(Pointer cadImage) {
        executor.execute(() -> {
            CadLib.INSTANCE.CADClose(cadImage);
            LOGGER.info("CAD image closed");
        });
    }
}
