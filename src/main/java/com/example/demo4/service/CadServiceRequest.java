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
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CadServiceRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CadServiceRequest.class);

    private static final Map<String, Pointer> threadPerImage = new ConcurrentHashMap<>();

    public static String createImageExt() {
        String imageId = UUID.randomUUID().toString();
        File file = new File("/opt/portal/libs/big.dwg");
        Pointer image = CadLib.INSTANCE.CreateCAD(null, file.getAbsolutePath());
        threadPerImage.put(imageId, image);
        return imageId;
    }

    public static FileSystemResource exportImageJpegExt(String imageId) {
        Pointer image = threadPerImage.get(imageId);
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
        CadLib.INSTANCE.SaveCADWithXMLParametrs(image, params);
        FileSystemResource resource = new FileSystemResource(Paths.get(filePath));
        resource.getFile().deleteOnExit();
        return resource;
    }

    public static void closeImageExt(String imageId) {
        Pointer image = threadPerImage.remove(imageId);
        CadLib.INSTANCE.CloseCAD(image);
    }
}
