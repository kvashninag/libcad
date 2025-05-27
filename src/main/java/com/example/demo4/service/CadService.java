package com.example.demo4.service;

import com.example.demo4.integration.CadLib;
import com.sun.jna.Pointer;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CadService.class);

     private static final ExecutorService createPool = Executors.newFixedThreadPool(10);
    private static final ExecutorService jpegPool = Executors.newFixedThreadPool(10);
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
                    exportImageJpeg(cadImage, num);
                }, jpegPool);
                sleep(3_000);
                CompletableFuture.runAsync(() -> {
                    closeImage(cadImage, num);
                }, closePool);
                sleep(1_000);
                run_memery_leak(num - 1);
            }, createPool);
        }
    }

    private static void run_memery_clean(int num) {
        if (num > 0) {
            CompletableFuture.runAsync(() -> {
                Pointer cadImage = createImage(num);
                closeImage(cadImage, num);
                sleep(1_000);
                run_memery_clean(num - 1);
            }, createPool);
        }
    }

    private static void sleep(int mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    private static void exportImageJpeg(Pointer cadImage, int num) {
        Path path = Paths.get(FileUtils.getTempDirectory().getAbsolutePath(), UUID.randomUUID().toString());
        String savePath = path.toFile().getAbsolutePath();
        String extension = ".jpeg";
        String filePath = savePath + extension;
        String params = "<ExportParams FileName=\"" + path.toFile().getAbsolutePath() + "\" Format=\"" + extension + "\" >\n" +
                "         <Width>300</Width>\n" +
                "         <Height>-1</Height>\n" +
                "         <Proportional>True</Proportional>\n" +
                "         <BitPerPixel>24</BitPerPixel>\n" +
                "         <Quality>100</Quality>\n" +
                "         <MeasureInPixels>True</MeasureInPixels>\n" +
                "         <DPUX>96</DPUX>\n" +
                "         <DPUY>96</DPUY>\n" +
                "        <Compression>LZW</Compression>\n" +
                "      </ExportParams>";
        CadLib cadLib = CadLib.INSTANCE;
        cadLib.SaveCADWithXMLParametrs(cadImage, params);
        FileSystemResource resource = new FileSystemResource(Paths.get(filePath));
        resource.getFile().deleteOnExit();
        LOGGER.info("create file {} = {}", num, filePath);
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
