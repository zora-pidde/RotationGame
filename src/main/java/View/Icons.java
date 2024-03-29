package View;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

class Icons {

    protected static Color controlColor = Color.WHITE;
    protected static Color controlColor2 = Color.DARKGREY;


    protected static int strokeWidth = 5;
    protected static int padding = 8;
    protected static int iconSize = 60 - 2* padding;
    protected static int outerStrokeFactor = 3;

    public static Group hintIcon() {
        Group icon = new Group();
        Rectangle hiddenHitZone = new Rectangle(0, 0, iconSize, iconSize);
        hiddenHitZone.setFill(Color.TRANSPARENT);
        icon.getChildren().add(hiddenHitZone);
        icon.getChildren().addAll(lookingGlass(controlColor2, strokeWidth * outerStrokeFactor));
        icon.getChildren().addAll(lookingGlass(controlColor));

        // adjust for strokewidth and padding
        icon.setTranslateX(outerStrokeFactor * strokeWidth * 0.5 + padding);
        icon.setTranslateY(outerStrokeFactor * strokeWidth * 0.5 + padding);

        return icon;
    }

    private static Group lookingGlass(Color color) {
        return lookingGlass(color, strokeWidth);
    }

    private static Group lookingGlass(Color color, int strokeWidth) {
        double radius = iconSize  * 4f / (5 * 2);
        System.out.println(radius);
        double xCenter = 0 + radius ;
        double yCenter = 0 + radius ;
        double xStart = radius * 1.75 ;
        double yStart = radius * 1.75 ;
        System.out.println((xStart));
        double xEnd = iconSize  ;
        double yEnd = iconSize  ;
        System.out.println(xEnd);

        Group lookingGlass = new Group();

        Circle lensCorpusBack = new Circle(xCenter, yCenter, radius);
        lensCorpusBack.setFill(Color.TRANSPARENT);
        lensCorpusBack.setStroke(color);
        lensCorpusBack.setStrokeWidth(strokeWidth);

        Line lensGripBack = new Line(xStart, yStart, xEnd, yEnd);
        lensGripBack.setStroke(color);
        lensGripBack.setStrokeWidth(strokeWidth);
        lensGripBack.setStrokeLineCap(StrokeLineCap.ROUND);

        lookingGlass.getChildren().addAll(lensCorpusBack, lensGripBack);


        return lookingGlass;
    }

    public static Group showSolutionIcon() {
        Group icon = new Group();
        icon.getChildren().addAll(eye(controlColor2, strokeWidth*outerStrokeFactor));
        icon.getChildren().addAll(eye(controlColor, strokeWidth));

        icon.setTranslateX((outerStrokeFactor * strokeWidth *0.5 + padding) * 3 + iconSize);
        icon.setTranslateY(outerStrokeFactor * strokeWidth * 0.5 + padding);
        return icon;
    }

    public static Group eye(Color color, int strokeWidth){
        Group icon = new Group();
        CubicCurve upperLid = eyeLid(color, strokeWidth, iconSize*0.1);
        CubicCurve lowerLid = eyeLid(color, strokeWidth, iconSize*0.9);
        Circle iris = new Circle(iconSize*0.5, iconSize*0.5, iconSize*0.25);
        iris.setFill(Color.TRANSPARENT);
        iris.setStroke(color);
        iris.setStrokeWidth(strokeWidth);
        Circle pupil = new Circle(iconSize*0.5, iconSize*0.5, iconSize*0.1);
        pupil.setFill(color);
        icon.getChildren().addAll(upperLid, lowerLid, iris, pupil);
        return icon;
    }

    public static CubicCurve eyeLid(Color color, int strokeWidth, double controlY){
        CubicCurve eyeLid = new CubicCurve();
        eyeLid.setStartX(0);
        eyeLid.setStartY(iconSize*0.5);
        eyeLid.setEndX(iconSize);
        eyeLid.setEndY(iconSize*0.5);
        eyeLid.setControlX1(iconSize*0.25);
        eyeLid.setControlY1(controlY);
        eyeLid.setControlX2(iconSize*0.75);
        eyeLid.setControlY2(controlY);
        eyeLid.setFill(Color.TRANSPARENT);
        eyeLid.setStroke(color);
        eyeLid.setStrokeWidth(strokeWidth);
        return eyeLid;
    }


    public static Group exitIcon() {
        Group icon = new Group();
        Rectangle hiddenHitZone = new Rectangle(0, 0, iconSize, iconSize);
        hiddenHitZone.setFill(Color.TRANSPARENT);
        icon.getChildren().add(hiddenHitZone);
        icon.getChildren().addAll(cross(controlColor2, strokeWidth * 3));
        icon.getChildren().addAll(cross(controlColor));

        // adjust for strokewidth and padding
        icon.setTranslateX(outerStrokeFactor * strokeWidth * 0.5 + padding);
        icon.setTranslateY(outerStrokeFactor * strokeWidth * 0.5 + padding);
        return icon;
    }

    public static Group cross(Color color) {
        return cross(color, strokeWidth);
    }

    public static Group cross(Color color, int strokeWidth) {
        int xStart = 0;
        int yStart = 0;
        Group cross = new Group();
        Line strokeBack1 = new Line(xStart, yStart, xStart + iconSize, yStart + iconSize);
        strokeBack1.setStroke(color);
        strokeBack1.setStrokeWidth(strokeWidth);
        strokeBack1.setStrokeLineCap(StrokeLineCap.ROUND);

        Line strokeBack2 = new Line(xStart, yStart + iconSize, xStart + iconSize, yStart);
        strokeBack2.setStroke(color);
        strokeBack2.setStrokeWidth(strokeWidth);
        strokeBack2.setStrokeLineCap(StrokeLineCap.ROUND);

        cross.getChildren().addAll(strokeBack1, strokeBack2);
        return cross;
    }

    public static Group rotateIcon(int centerX, int centerY, int radius) {
        int strokeWidth = radius / 3;
        Group icon = new Group();

        Circle rotation = new Circle(centerX, centerY, radius);
        rotation.setFill(Color.TRANSPARENT);
        rotation.setStroke(controlColor);
        rotation.setStrokeWidth(strokeWidth);

        Line stroke1 = new Line(centerX + radius, centerY, centerX + radius * 1.5, centerY - radius * 0.5);
        stroke1.setStroke(controlColor);
        stroke1.setStrokeWidth(strokeWidth);
        stroke1.setStrokeLineCap(StrokeLineCap.ROUND);
        Rotate rotate = new Rotate();
        rotate.setAngle(348);
        rotate.setPivotX(centerX + radius);
        rotate.setPivotY(centerY);
        stroke1.getTransforms().addAll(rotate);

        Line stroke2 = new Line(centerX + radius, centerY, centerX + radius * 0.5, centerY - radius * 0.5);
        stroke2.setStroke(controlColor);
        stroke2.setStrokeWidth(strokeWidth);
        stroke2.setStrokeLineCap(StrokeLineCap.ROUND);
        stroke2.getTransforms().addAll(rotate);

        icon.getChildren().addAll(rotation, stroke1, stroke2);
        return icon;
    }


    public static Group nextPageIcon(){
//        int strokeWidth = 5;
        Group icon = new Group();

        Rectangle hiddenHitZone = new Rectangle(0, 0, iconSize, iconSize);
        hiddenHitZone.setFill(Color.TRANSPARENT);
        icon.getChildren().add(hiddenHitZone);
        icon.getChildren().addAll(rArrow(controlColor2, strokeWidth * outerStrokeFactor));
        icon.getChildren().addAll(rArrow(controlColor, strokeWidth));

        //adjust for strokeWidth and padding
        icon.setTranslateX(outerStrokeFactor * strokeWidth * 0.5 + padding);
        icon.setTranslateY(outerStrokeFactor * strokeWidth * 0.5 + padding);

        return icon;
    }

    public static Group nextPageIconRound(){
//        int strokeWidth = 5;
        Group icon = new Group();

        Rectangle hiddenHitZone = new Rectangle(0, 0, iconSize, iconSize);
        hiddenHitZone.setFill(Color.TRANSPARENT);
        icon.getChildren().add(hiddenHitZone);
        icon.getChildren().addAll(rArrow(controlColor2, strokeWidth * outerStrokeFactor));
        Circle background = new Circle();
        background.setFill(controlColor2);
        background.setCenterX(iconSize*0.5);
        background.setCenterY(iconSize*0.5);
        background.setRadius((iconSize+strokeWidth*outerStrokeFactor)*0.5);
        icon.getChildren().addAll(background);
        icon.getChildren().addAll(rArrow(controlColor, strokeWidth));

        //adjust for strokeWidth and padding
        icon.setTranslateX(outerStrokeFactor * strokeWidth * 0.5 + padding);
        icon.setTranslateY(outerStrokeFactor * strokeWidth * 0.5 + padding);

        return icon;
    }

    public static Group lastPageIcon(){
        Group icon = nextPageIcon();
        icon.setScaleX(-1.);
        return icon;
    }

    public static Group lastPageIconRound(){
        Group icon = nextPageIconRound();
        icon.setScaleX(-1.);
        return icon;
    }

    private static Group rArrow(Color color, int strokeWidth) {
        Group arrow = new Group();
        int xStart = 0;
        int yStart = 0;
        double midHeight = yStart + 0.5 * iconSize;
        Line strokeBack1 = new Line(xStart, midHeight, xStart + iconSize, midHeight);
        strokeBack1.setStroke(color);
        strokeBack1.setStrokeWidth(strokeWidth);
        strokeBack1.setStrokeLineCap(StrokeLineCap.ROUND);

        Line strokeBack2 = new Line(xStart + iconSize, midHeight, xStart + 0.5 * iconSize, yStart);
        strokeBack2.setStroke(color);
        strokeBack2.setStrokeWidth(strokeWidth);
        strokeBack2.setStrokeLineCap(StrokeLineCap.ROUND);

        Line strokeBack3 = new Line(xStart + iconSize, midHeight, xStart + 0.5 * iconSize,  iconSize);
        strokeBack3.setStroke(color);
        strokeBack3.setStrokeWidth(strokeWidth);
        strokeBack3.setStrokeLineCap(StrokeLineCap.ROUND);

        arrow.getChildren().addAll(strokeBack1, strokeBack2, strokeBack3);
        return arrow;
    }
}



