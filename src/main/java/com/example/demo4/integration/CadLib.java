package com.example.demo4.integration;

import com.sun.jna.Callback;
import com.sun.jna.Function;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.Union;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// this code copy from cad lib source
public interface CadLib extends Library {
    //CHECKSTYLE:OFF

    static final Logger LOGGER = LoggerFactory.getLogger(CadLib.class);

    CadLib INSTANCE = cadLibLoad();

    private static CadLib cadLibLoad() {
        return Platform.isWindows() ? cadLibWindowsLoad() : cadLibLinuxLoad();
    }

    private static CadLib cadLibLinuxLoad() {
        Map<String, ?> options = new HashMap<>() {
            {
                put(Library.OPTION_STRING_ENCODING, "UTF-8");
            }
        };
        return Native.load("libcad.so", CadLib.class, options);
    }

    private static CadLib cadLibWindowsLoad() {
        Map<String, ?> options = new HashMap<>() {
            {
                put(Library.OPTION_STRING_ENCODING, "UTF-16LE");
                put(Library.OPTION_CALLING_CONVENTION, Function.C_CONVENTION);
            }
        };
        return Native.load("cad.dll", CadLib.class, options);
    }

    public static final int CAD_SEC_TABLES = 0;
    public static final int CAD_SEC_BLOCKS = 1;
    public static final int CAD_SEC_ENTITIES = 2;
    public static final int CAD_SEC_LTYPE = 3;
    public static final int CAD_SEC_LAYERS = 4;
    public static final int CAD_UNKNOWN = 0;
    public static final int CAD_TABLE = 1;
    public static final int CAD_BLOCK = 2;
    public static final int CAD_LTYPE = 3;
    public static final int CAD_LAYER = 4;
    public static final int CAD_VERTEX = 5;
    public static final int CAD_LINE = 6;
    public static final int CAD_SOLID = 7;
    public static final int CAD_CIRCLE = 8;
    public static final int CAD_ARC = 9;
    public static final int CAD_POLYLINE = 10;
    public static final int CAD_LWPOLYLINE = 11;
    public static final int CAD_SPLINE = 12;
    public static final int CAD_INSERT = 13;
    public static final int CAD_DIMENSION = 14;
    public static final int CAD_TEXT = 15;
    public static final int CAD_MTEXT = 16;
    public static final int CAD_ATTDEF = 17;
    public static final int CAD_ELLIPSE = 18;
    public static final int CAD_POINT = 19;
    public static final int CAD_3DFACE = 20;
    public static final int CAD_HATCH = 21;
    public static final int CAD_IMAGE_ENT = 22;
    public static final int CAD_ATTRIB = 23;
    public static final int CAD_BEGIN_POLYLINE = 100;
    public static final int CAD_END_POLYLINE = 101;
    public static final int CAD_BEGIN_INSERT = 102;
    public static final int CAD_END_INSERT = 103;
    public static final int CAD_BEGIN_VIEWPORT = 104;
    public static final int CAD_END_VIEWPORT = 105;
    public static final int CADERR_GENERAL = 1000;
    public static final int CADERR_INVALID_HANDLE = (CADERR_GENERAL + 1);
    public static final int CADERR_INVALID_INDEX = (CADERR_GENERAL + 2);
    public static final int CADERR_FILE_NOT_FOUND = (CADERR_GENERAL + 3);
    public static final int CADERR_FILE_READ = (CADERR_GENERAL + 4);

    @FieldOrder({"Left", "Top", "Z1", "Right", "Bottom", "Z2"})
    class FRect extends Structure {
        public double Left, Top, Z1, Right, Bottom, Z2;

        public static class ByReference extends FRect implements Structure.ByReference {
        }
    }

    @FieldOrder({"X", "Y", "Z"})
    class FPoint extends Structure {
        public double X, Y, Z;

        public static class ByReference extends FPoint implements Structure.ByReference {
        }

    }

    @FieldOrder({"X", "Y"})
    class TPoint extends Structure {
        public int X, Y;

        public static class ByReference extends TPoint implements Structure.ByReference {
        }
    }

    public interface ProgressProc extends Callback {

        int invoke(byte PercentDone);
    }

    public class TsgCADExportParams extends Structure {

        public static class ByReference extends TsgCADExportParams implements Structure.ByReference {
        }

        public double XScale;
        public byte Units;
        public ProgressProc ProgressFunc;

        public TsgCADExportParams() {
            super(ALIGN_NONE);
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("XScale", "Units", "ProgressFunc");
        }
    }

    public class TsgImageParam extends Structure {

        public static class ByReference extends TsgImageParam implements Structure.ByReference {
        }

        public int Heigth;
        public int Width;
        public int PixelFormat;

        public TsgImageParam() {
            super(ALIGN_NONE);
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("Heigth", "Width", "PixelFormat");
        }
    }

    public class TCADDraw extends Structure {

        public static class ByReference extends TCADDraw implements Structure.ByReference {
        }

        public int Size;
        public Pointer DC;
        public RECT R;
        public byte DrawMode;

        public TCADDraw() {
            super(ALIGN_NONE);
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("Size", "DC", "R", "DrawMode");
        }
    }

    public class CadData extends Structure {

        public static class ByReference extends CadData implements Structure.ByReference {
        }

        public short Tag;
        public short Count;
        public short TickCount;
        public byte Flags;
        public byte Style;
        public int Dimension;
        public Pointer DashDots;
        public int DashDotsCount;
        public int Color;
        public Pointer Ticks;
        public double Thickness;
        public double Rotation;
        public Pointer Layer;
        public Pointer Text;
        public Pointer FontName;
        public Pointer Handle;
        public int Undefined1;
        public double Undefined2;
        public double Undefined3;
        public Pointer CADExtendedData;
        public FPoint Point;
        public FPoint Point1;
        public FPoint Point2;
        public FPoint Point3;

        public CadData() {
            super(ALIGN_NONE);
        }

        // Define the union case
        public static class ArcData extends Structure {

            public double Radius;
            public double StartAngle;
            public double EndAngle;
            public double Ratio;
            public byte EntityType;

            @Override
            protected List<String> getFieldOrder() {
                return Arrays.asList("Radius", "StartAngle", "EndAngle", "Ratio", "EntityType");
            }
        }

        public static class InsertData extends Structure {

            public Pointer Block;
            public FPoint Scale;

            @Override
            protected List<String> getFieldOrder() {
                return Arrays.asList("Block", "Scale");
            }
        }

        public static class TextData extends Structure {

            public double FHeight;
            public double FScale;
            public double RWidth;
            public double RHeight;
            public byte HAlign;
            public byte VAlign;

            @Override
            protected List<String> getFieldOrder() {
                return Arrays.asList("FHeight", "FScale", "RWidth", "RHeight",
                        "HAlign", "VAlign");
            }
        }

        public static class PolylineData extends Structure {

            public Pointer Points;
            public int CountPointOfSegments;

            @Override
            protected List<String> getFieldOrder() {
                return Arrays.asList("Points", "CountPointOfSegments");
            }
        }

        public static class TraceData extends Structure {

            public Pointer SolidPoints;
            public short SolidPointsCount;
            public short SolidInternalFlags;

            @Override
            protected List<String> getFieldOrder() {
                return Arrays.asList("SolidPoints", "SolidPointsCount", "SolidInternalFlags");
            }
        }

        public static class DefaultData extends Structure {

            public byte Data;

            @Override
            protected List<String> getFieldOrder() {
                return Arrays.asList("Data");
            }
        }

        // Define other cases as needed
        // Define the union field
        public static class sgUnion extends Union {

            public static class ByReference extends sgUnion implements Structure.ByReference {
            }

            public ArcData arcData;
            public InsertData insertData;
            public TextData textData;
            public PolylineData polylineData;
            public TraceData traceData;
            public DefaultData defaultData;
        }

        // Define the union selector field
        //   public int unionSelector;
        // Define the union field using the selector
        public sgUnion u;

        @Override
        public void read() {
            super.read();

            switch (Tag) {
                case CAD_ARC, CAD_CIRCLE, CAD_ELLIPSE:
                    u.setType(ArcData.class);
                    break;
                case CAD_INSERT:
                    u.setType(InsertData.class);
                    break;
                case CAD_TEXT, CAD_ATTDEF, CAD_ATTRIB:
                    u.setType(TextData.class);
                    break;
                case CAD_POLYLINE, CAD_LWPOLYLINE:
                    u.setType(PolylineData.class);
                    break;
                case CAD_3DFACE, CAD_SOLID:
                    u.setType(TraceData.class);
                    break;
                default:
                    u.setType(DefaultData.class);
                    break;
            }

            u.read();
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("Tag", "Count", "TickCount", "Flags", "Style",
                    "Dimension", "DashDots", "DashDotsCount", "Color", "Ticks",
                    "Thickness", "Rotation", "Layer", "Text", "FontName",
                    "Handle", "Undefined1", "Undefined2", "Undefined3",
                    "CADExtendedData", "Point", "Point1", "Point2", "Point3",
                    "u");
        }
    }

    public interface CADEnumProc extends Callback {

        void invoke(CadData entData, PointerByReference param);
    }

    int CADAddXRef(Pointer AImage, Pointer AXrefImage, String AName,
                   FPoint Position, FPoint Scale, double Rotation,
                   Pointer ResultInsert);

    Pointer CADLayer(Pointer Handle, int Index, CadData.ByReference Data);

    int CADLayerCount(Pointer Handle);

    int CADLayerVisible(Pointer Handle, int Visible);

    Pointer CADLayout(Pointer Handle, int Index);

    int CADLayoutBox(Pointer Handle, FRect.ByReference ARect);

    int CADLayoutName(Pointer Handle, int Index, byte[] Name, int nSize);

    int CADLayoutsCount(Pointer Handle);

    boolean CADLayoutVisible(Pointer Handle, int Index, boolean DoSetVisible,
                             boolean NewValue);

    Pointer CurrentLayoutCAD(Pointer Handle, int Index, boolean DoChange);

    int CADLTScale(Pointer Handle, DoubleByReference AScale);

    int CloseCAD(Pointer Handle);

    int CADSetSHXOptions(String SearchSHXPaths, String DefaultSHXPath,
                         String DefaultSHXFont, boolean UseSHXFonts, boolean UseACADPaths);

    int CADVisible(Pointer Handle, String LayerName);

    Pointer CreateCAD(Pointer Window, String FileName);

    Pointer CreateCADEx(Pointer Window, String FileName, String Param);

    int DefaultLayoutIndex(Pointer Handle);

    int DrawCAD(Pointer Handle, HDC DC, RECT R);

    int DrawCADEx(Pointer Handle, TCADDraw CADDraw);

    Pointer DrawCADtoBitmap(Pointer Handle, TCADDraw CADDraw);

    Pointer DrawCADtoDIB(Pointer Handle, RECT R);

    Pointer DrawCADtoGif(Pointer Handle, TCADDraw CADDraw);

    Pointer DrawCADtoJpeg(Pointer Handle, TCADDraw CADDraw);

    int GetAbsSizeCAD(Pointer Handle, FloatByReference AbsWidth, FloatByReference AbsHeight);

    int GetBoxCAD(Pointer Handle, FRect.ByReference ARect);

    int GetCADCoords(Pointer Handle, FloatByReference AXScaled,
                     FloatByReference AYScaled, Pointer Coord);

    int GetExtentsCAD(Pointer Handle, FRect.ByReference ARect);

    int GetIs3dCAD(Pointer Handle, IntByReference AIs3D);

    int GetLastErrorCAD(String Buf, int nSize);

    int GetPlugInInfo(String Version, String Formats);

    int GetNearestEntity(Pointer Handle, String Buf, int nSize, RECT R, Pointer APoint);

    int GetNearestEntityWCS(Pointer Handle, String Buf, int nSize, RECT R, Pointer APoint2D, Pointer APoint3D);

    int GetSnapPoint(Pointer Handle, int AX, int AY, Pointer APointInUCS, Pointer AData);

    int SetObjectSnap(Pointer Handle, int AFlags);

    int GetPointCAD(Pointer Handle, Pointer APoint);

    Pointer ReadCAD(String FileName, String ErrorText);

    int ResetDrawingBoxCAD(Pointer Handle);

    int SaveCADtoBitmap(Pointer Handle, TCADDraw CADDraw, String FileName);

    int SaveCADtoJpeg(Pointer Handle, TCADDraw CADDraw, String FileName);

    int SaveCADtoCAD(Pointer Handle, TsgCADExportParams.ByReference AParam,
                     String FileName);

    int SaveCADtoGif(Pointer Handle, TCADDraw CADDraw, String FileName);

    int SaveCADtoPNG(Pointer Handle, TCADDraw CADDraw, String FileName);

    int SaveCADtoFile(Pointer Handle, TCADDraw CADDraw, String FileName,
                      TsgImageParam.ByReference ImageParam);

    boolean SetBMSize(int Value);

    int SetCADBorderType(Pointer Handle, int ABorderType);

    int SetCADBorderSize(Pointer Handle, double ABorderSize);

    int SetDefaultColor(Pointer Handle, int ADefaultColor);

    int SetDrawingBoxCAD(Pointer Handle, FRect.ByReference ABox);

    int SetProcessMessagesCAD(Pointer Handle, int AIsProcess);

    int SetRotateCAD(Pointer Handle, float AAngle, int AAxis);

    int SetBlackWhite(Pointer Handle, int CMode);

    int SetProgressProc(ProgressProc AProgressProc);

    int StopLoading();

    int CADStopLoading();

    int GetSupportedExts(String ASupportedExts, IntByReference ALength);

    int StRgW(String AUser, String AEMail, String AKey);

    int StRgA(String AUser, String AEMail, String AKey);

    int StRg(String AUser, String AEMail, String AKey);

    int SaveCADtoFileWithXMLParams(Pointer Handle, String AParam, ProgressProc AProc);

    int SaveCADWithXMLParametrs(Pointer Handle, String AParam);

    int ProcessXML(Pointer AHandle, String AInputXML, Pointer AOutputXML);

    int CADSetViewPort(Pointer Handle, int Left, int Top, int Width, int Height);

    int CADGetAutoRegenMode(Pointer Handle);

    int CADSetAutoRegenMode(Pointer Handle, IntByReference AMode);

    int CADGetNumberOfParts(Pointer Handle, IntByReference ASpline, IntByReference ACircle);

    int CADSetNumberOfParts(Pointer Handle, int ASpline, int ACircle);

    int CADToClient(Pointer Handle, FPoint APoint, TPoint.ByReference AClientPos);

    int CADAddSink(Pointer Handle, Pointer AProc, Pointer AParam,
                   char[] AEventName, IntByReference ACookie);

    int CADRemoveSink(Pointer Handle, char[] AEventName, int ACookie);

    int CADScale(Pointer Handle, double s, double cx, double cy);

    int CADMove(Pointer Handle, double dx, double dy);

    int CADFitTo(Pointer Handle, float x, float y, float width, float height);

    int CADRotate(Pointer Handle, double pitch, double turn, double roll,
                  DoubleByReference pcx, DoubleByReference pcy, DoubleByReference pcz);

    int RotToViewCAD(Pointer Handle, int ViewDirection);

    int CADZoom(Pointer Handle, float x, float y, float width, float height, double l, double t, double r, double b);

    int CADShowObject(Pointer Handle, Pointer EntHandle, float x, float y,
                      float width, float height, FRect.ByReference Rect);

    FPoint CADGetDrawRectCenter(Pointer Handle);

    int SetDrawingMatrix(Pointer Handle, DoubleByReference AMatrix);

    int GetDrawingMatrix(Pointer Handle, DoubleByReference AMatrix);

    int CADRenderTo(Pointer Handle, char[] AType, char[] AEncoding,
                    IntByReference ABufLen, Pointer AData);

    int CADRender3DTo(Pointer Handle, char[] AType, char[] AEncoding,
                      IntByReference ABufLen, Pointer AData);

    int CADSetTransparent(Pointer Handle, int ATransparent);

    int CADSetBackgroundColor(Pointer Handle, int AColor);

    Pointer CADCreate(Pointer Window, String FileName);

    int CADClose(Pointer Handle);

    Pointer CADGetSection(Pointer Handle, int Index, CadData.ByReference Data);

    Pointer CADGetChild(Pointer Handle, int Index, CadData.ByReference Data);

    int CADGetData(Pointer Handle, CadData.ByReference Data);

    int CADLayoutCount(Pointer Handle);

    int CADLayoutCurrent(Pointer Handle, IntByReference Index, boolean DoChange);

    int CADEnum(Pointer Handle, int EnumAll, CADEnumProc Proc, PointerByReference Param);

    int CADUnits(Pointer Handle, IntByReference Units);

    int CADIs3D(Pointer Handle);

    int CADGetBox(Pointer Handle, DoubleByReference ALeft,
                  DoubleByReference ARight, DoubleByReference ATop,
                  DoubleByReference ABottom);

    int CADGetLastError(String Buf);

    int CADProhibitCurvesAsPoly(Pointer Handle, int AllArcsAsCurves);

    int CADSetSplinesAccuracy(int Accuracy);

    int CADSetMeshQuality(Pointer Handle, DoubleByReference ANewValue, DoubleByReference AOldValue);

    int CADGetInsUnits(Pointer Handle, short AInsUnits);

    int CADGetEntityHandle(Pointer Handle, LongByReference EntityHandle);

    boolean CADSetInterpretHPGLCmdNRAsNewPageOnRead(Pointer Handle, boolean AValue);

    boolean CADGetInterpretHPGLCmdNRAsNewPageOnRead(Pointer Handle);

    int CADEnumGetEntLineWeight(Pointer Handle, Pointer Entity, DoubleByReference LineWeight);

    int GetSVGTextImportMode();

    int SetSVGTextImportMode(int AValue);

    Pointer CADWrapInstance(Pointer AHandle, Pointer ATypeInfo);

    Pointer CADGetTypeInfo(Pointer obj);

    int CADGetTTFMode();

    int CADSetTTFMode(int AValue);

    int GetEntityBox(Pointer AHandle, LongByReference EntityHandle, FRect.ByReference ARect);

    //CHECKSTYLE:ON
}

