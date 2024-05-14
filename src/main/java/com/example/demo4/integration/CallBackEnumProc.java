package com.example.demo4.integration;

import com.example.demo4.dto.CadBoxBlockDto;
import com.example.demo4.dto.CadLayerDto;
import com.example.demo4.dto.CadPointDto;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CallBackEnumProc implements CadLib.CADEnumProc {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallBackEnumProc.class);

    private final CadLib cadLib;
    private final Map<String, Boolean> layersVisible;
    private final Map<String, CadBoxBlockDto> cadBlocks = new HashMap<>();
    private int depth;

    public CallBackEnumProc(CadLib cadLib, List<CadLayerDto> layers) {
        this.cadLib = cadLib;
        this.layersVisible = layers.stream().collect(Collectors.toMap(CadLayerDto::getName, CadLayerDto::isVisible));
    }

    public List<CadBoxBlockDto> getCadBlocks() {
        return this.cadBlocks.values().stream().toList();
    }

    private void printInsert(CadLib.CadData entData) {
        Pointer block = entData.u.insertData.Block;

        String layerName = CadUtil.toString(entData.Layer);
        boolean isVisibleLayers = this.layersVisible.getOrDefault(layerName, false);

        if (isVisibleLayers) {
            CadLib.FRect.ByReference rect = new CadLib.FRect.ByReference();
            cadLib.CADLayoutBox(block, rect);
            String blockName = CadUtil.toString(entData.Text);
            if (!cadBlocks.containsKey(blockName)) {
                CadBoxBlockDto blockDto = new CadBoxBlockDto();
                blockDto.setName(blockName);
                blockDto.getSize().setWidth(rect.Right - rect.Left);
                blockDto.getSize().setHeight(rect.Top - rect.Bottom);
                blockDto.getSize().setDepth(rect.Z2 - rect.Z1);
                cadBlocks.put(blockName, blockDto);
            }
            CadBoxBlockDto blockDto = cadBlocks.get(blockName);
            CadPointDto point = new CadPointDto(entData.Point.X, entData.Point.Y, entData.Rotation);
            blockDto.getPoints().add(point);
        }
    }

    @Override
    public void invoke(CadLib.CadData entData, PointerByReference param) {
        short tag = entData.Tag;

        if (tag == CadLib.CAD_BEGIN_INSERT) {
            this.depth++;
        }
        if (tag == CadLib.CAD_END_INSERT) {
            this.depth--;
        }

        if (tag == CadLib.CAD_INSERT && this.depth == 1) {
            printInsert(entData);
        }
    }

}
