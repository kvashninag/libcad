package com.example.demo4.service;

import com.example.demo4.dto.CadLayerDto;
import com.example.demo4.integration.CadLib;
import com.example.demo4.integration.CadUtil;
import com.example.demo4.integration.CallBackEnumProc;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class CadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CadService.class);

    public static void testCad() {
        Pointer cadImage = createImage();

        CadLib cadLib = CadLib.INSTANCE;

        List<CadLayerDto> layers = getLayerList(cadImage);

        for (int i = 0; i < 1000; i++) {
            imageBlocks(cadImage, cadLib, layers);
            sleep();
        }

        closeImage(cadImage);
    }

    private static void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    private static void imageBlocks(Pointer cadImage, CadLib cadLib, List<CadLayerDto> layers) {
        CallBackEnumProc proc = new CallBackEnumProc(cadLib, layers);
        PointerByReference param = new PointerByReference();
        cadLib.CADEnum(cadImage, 1, proc, param);
        LOGGER.info("cadBlocks = {}", proc.getCadBlocks());
    }

    private static Pointer createImage() {
        File file = getFile();

        CadLib cadLib = CadLib.INSTANCE;
        Pointer cadImage = cadLib.CreateCAD(null, file.getAbsolutePath());

        cadLib.SetCADBorderType(cadImage, 0);
        cadLib.SetCADBorderSize(cadImage, 0);

        return cadImage;
    }

    private static File getFile() {
        if (Platform.isWindows()) {
            return new File(".\\libs\\test_block_1.dwg");
        } else {
            return new File("/opt/portal/libs/test_block_1.dwg");
        }
    }

    private static void closeImage(Pointer cadImage) {
        CadLib cadLib = CadLib.INSTANCE;
        cadLib.CADClose(cadImage);
    }

    private static List<CadLayerDto> getLayerList(Pointer cadImage) {
        List<CadLayerDto> result = new ArrayList<>();
        CadLib cadLib = CadLib.INSTANCE;

        int layerCount = cadLib.CADLayerCount(cadImage);
        for (int i = 0; i < layerCount; i++) {
            CadLib.CadData.ByReference dxfdata = new CadLib.CadData.ByReference();
            cadLib.CADLayer(cadImage, i, dxfdata);
            String layerName = CadUtil.toString(dxfdata.Text);
            int isVisible = cadLib.CADVisible(cadImage, layerName);
            result.add(new CadLayerDto(layerName, isVisible == 1));
        }
        return result;
    }

}
