package View;

import javafx.animation.FadeTransition;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.geometry.*;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;


public class LevelView {

    private Group root;

    Stage gameStage;
    Scene gameScene;
    private int screenWidth;
    private int screenHeight;

    private int correctTiles = 0;
    private int sectionCount = 0;

    private String srcImage;

    private MenuView menu;

    private Color controlColor = Color.WHITE;

    private int sectionsX = 2;
    private int sectionsY = 2;
    private Image img;

    String css = this.getClass().getResource("/styles.css").toExternalForm();

    /*----CONSTRUCTOR----*/

    public LevelView(Stage stage){
        this.gameStage = stage;
    }

    /*----GETTER & SETTER----*/
    public void setSrcImage(String img){
        this.srcImage = img;
    }

    public void setScreenSize(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
    }

    public void setMenuView(MenuView menu){
        this.menu = menu;
    }

    /*----CONTROL-BUTTON-GRAPHICS----*/
    private Group hintIcon(){
        Group icon = new Group();
        int strokeWidth = 5;

        Circle lensCorpus = new Circle(50, 50, 15);
        lensCorpus.setFill(Color.TRANSPARENT);
        lensCorpus.setStroke(controlColor);
        lensCorpus.setStrokeWidth(strokeWidth);

        Line lensGrip = new Line(30, 75, 40, 62);
        lensGrip.setStroke(controlColor);
        lensGrip.setStrokeWidth(strokeWidth);
        lensGrip.setStrokeLineCap(StrokeLineCap.ROUND);
        icon.getChildren().addAll(lensCorpus, lensGrip);
        return icon;
    }

    //create rotation-Icon: one circle with a down-arrow on circumference (length of arrow-point is 0.5*radius)
    private Group rotateIcon(int centerX, int centerY, int radius){
        int strokeWidth = radius/3;
        Group icon = new Group();

        Circle rotation = new Circle(centerX, centerY, radius);
        rotation.setFill(Color.TRANSPARENT);
        rotation.setStroke(this.controlColor);
        rotation.setStrokeWidth(strokeWidth);

        Line stroke1 = new Line(centerX+radius, centerY,centerX+radius*1.5, centerY-radius*0.5);
        stroke1.setStroke(this.controlColor);
        stroke1.setStrokeWidth(strokeWidth);
        stroke1.setStrokeLineCap(StrokeLineCap.ROUND);
        Rotate rotate = new Rotate();
        rotate.setAngle(348);
        rotate.setPivotX(centerX+radius);
        rotate.setPivotY(centerY);
        stroke1.getTransforms().addAll(rotate);

        Line stroke2 = new Line(centerX+radius, centerY, centerX+radius*0.5, centerY-radius*0.5);
        stroke2.setStroke(this.controlColor);
        stroke2.setStrokeWidth(strokeWidth);
        stroke2.setStrokeLineCap(StrokeLineCap.ROUND);
        stroke2.getTransforms().addAll(rotate);

        icon.getChildren().addAll(rotation, stroke1, stroke2);
        return icon;
    }

    private Group exitIcon(){
        int strokeWidth = 5;
        Group icon = new Group();

        Line stroke1 = new Line(screenWidth-60, 65, screenWidth-30, 35);
        stroke1.setStroke(this.controlColor);
        stroke1.setStrokeWidth(strokeWidth);
        stroke1.setStrokeLineCap(StrokeLineCap.ROUND);

        Line stroke2 = new Line(screenWidth-60, 35, screenWidth-30, 65);
        stroke2.setStroke(this.controlColor);
        stroke2.setStrokeWidth(strokeWidth);
        stroke2.setStrokeLineCap(StrokeLineCap.ROUND);

        //enhance hit-zone by adding invisible square around the cross
        Rectangle hiddenHitZone = new Rectangle(screenWidth-65, 30, 40, 40);
        hiddenHitZone.setFill(Color.TRANSPARENT);

        icon.getChildren().addAll(hiddenHitZone,stroke2, stroke1);
        return icon;
    }

    /*----CONTROL-BUTTON LOGIC----*/

    public void giveHint(Group hintButton){
        hintButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Text text = new Text("Correct Tiles: "+correctTiles);
                text.setX(30);
                text.setY(30);
                text.setFill(controlColor);
                text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));

                FadeTransition fadeOut = new FadeTransition(Duration.millis(2000), text);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.setOnFinished(e -> root.getChildren().remove(text));
                try{
                    root.getChildren().add(text);
                    fadeOut.play();
                    //if hint still displayed -> refresh text
                } catch(Exception e){
                    fadeOut.play();
                }
            }
        }));
    }

    public void exitToMenu(Group exitButton){
        exitButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                menu.menuCallback();
            }
        }));
    }

    public void controlButtons(){
        Group hintButton = hintIcon();
        giveHint(hintButton);
        Group exitButton = exitIcon();
        exitToMenu(exitButton);
        root.getChildren().addAll(hintButton, exitButton);
    }

    /*----WIN-MESSAGE----*/

    public HBox showWinMessage(){
        HBox winBox = new HBox();
        winBox.setPrefWidth(this.screenWidth);
        winBox.setAlignment(Pos.CENTER);
        Label text = new Label("Congratulations!\nYou Won");
        text.setTextFill(this.controlColor);
        text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
        text.setTextAlignment(TextAlignment.CENTER);
        winBox.getChildren().add(text);

        this.root.getChildren().add(winBox);
        winBox.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                root.getChildren().remove(winBox);
            }
        }));
        return winBox;
    }

    /*----SINGLE-TILE-LOGIC----*/

    public void rotate(ImageView imgV, int rotationCount){
        int originalRotation = (int) imgV.getRotate();
        int rotation =  (int) (originalRotation+ rotationCount * 90)%360;
        if(rotation == 0 && originalRotation != 0){
            this.correctTiles ++;
        } else if (rotation != 0 && originalRotation == 0) {
            this.correctTiles--;
        }
        imgV.setRotate(rotation);
        if(this.correctTiles == this.sectionCount){
            HBox winBox = showWinMessage();
            FadeTransition fadeOut = new FadeTransition(Duration.millis(2000), winBox);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> root.getChildren().remove(winBox));
            fadeOut.play();
        }
    }

    public void initializeTile(ImageView imgV){
        rotate(imgV, (int) (Math.random() * 4));
    }

    /*----INITIALIZE-GAME----*/

    public ImageView section(Image img, int xSectionAmount, int ySectionAmount, int pos) {
        int sectionWidth = (int)this.screenWidth/xSectionAmount;
        int sectionHeight = (int) this.screenHeight/ySectionAmount;
        int posX = pos % xSectionAmount;
        int posY = Math.floorDiv(pos, ySectionAmount);
        ImageView imgV = new ImageView(img);
        imgV.setFitHeight(sectionWidth);
        imgV.setFitWidth(sectionHeight);
        imgV.setX(sectionWidth * posX);
        imgV.setY(sectionHeight * posY);
        int imgSectionWidth = (int) img.getWidth()/xSectionAmount;
        int imgSectionHeight =  (int) img.getHeight()/ySectionAmount;
        Rectangle2D imgSection = new Rectangle2D(imgSectionWidth * posX,imgSectionHeight *posY, imgSectionWidth, imgSectionHeight);
        imgV.setViewport(imgSection);
        return imgV;
    }


    //provide specific case for quadratic cases
    public ImageView section(Image img, int sectionAmountPerLine, int pos) {
        return section(img, sectionAmountPerLine, sectionAmountPerLine, pos);
    }

    public void initializeImg(Image img, int xTileCount, int yTileCount){
        this.sectionCount = xTileCount * yTileCount;
        this.correctTiles = sectionCount;
        int sectionWidth = (int)this.screenWidth/xTileCount;
        int sectionHeight = (int) this.screenHeight/yTileCount;
        for(int x = 0; x < xTileCount; x++){
            for(int y = 0; y < yTileCount; y++){
                int pos = (x * yTileCount + y);
                int posX = pos % xTileCount;
                int posY = Math.floorDiv(pos, yTileCount);

                ImageView imgVInitial =  section(img, xTileCount, yTileCount,pos);// (x * yTileCount + y));
                initializeTile(imgVInitial);

                Group imgV = new Group();
                imgV.getChildren().add(imgVInitial);

                Group rotateIcon = rotateIcon((int) (posX*sectionWidth+0.5*sectionWidth), (int)(posY*sectionHeight+0.5*sectionHeight), sectionWidth/4);
                rotateIcon.setOpacity(0.2);
                rotateIcon.setVisible(false);
                imgV.getChildren().add(rotateIcon);


                System.out.println("visible: "+rotateIcon.isVisible());
                EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        rotateIcon.setVisible(false);
                        rotate(imgVInitial, 1);
                    }
                };


                EventHandler<MouseEvent> mouseOverHandler = new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        FadeTransition fadeOut = new FadeTransition(Duration.millis(1000), rotateIcon);
                        fadeOut.setFromValue(0.2);
                        fadeOut.setToValue(0.0);
                        fadeOut.setOnFinished(e -> rotateIcon.setVisible(false));
                        if(!rotateIcon.isVisible()) {
                            rotateIcon.setVisible(true);
                            fadeOut.play();
                        }
                    }
                };

                EventHandler<MouseEvent> mouseLeaveHandler = new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        rotateIcon.setVisible(false);
                    }
                };

                imgV.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseOverHandler);
                imgV.addEventHandler(MouseEvent.MOUSE_EXITED, mouseLeaveHandler);
                imgV.addEventHandler(MouseEvent.MOUSE_CLICKED,eventHandler);

                root.getChildren().addAll(imgV);
            }
        }

    }

    public void sectioningRepresentation(int size, int maxOptions, HBox parent){
        int strokeWidth = size/10;
        for(int optionCount = 2; optionCount <= maxOptions; optionCount++) {
            Group optionIcon = new Group();
            Rectangle fullSize = new Rectangle(0, 0, size, size);
            fullSize.setFill(controlColor);
            optionIcon.getChildren().addAll(fullSize);
            for (int x = 0; x <= optionCount; x++) {
                Line verticalLine = new Line(x * size / optionCount, 0, x * size / optionCount, size);
                verticalLine.setStroke(Color.DARKGREY);
                verticalLine.setStrokeWidth(strokeWidth);
                optionIcon.getChildren().add(verticalLine);
            }
            for (int y = 0; y <= optionCount; y++) {
                Line horizontalLine = new Line(0, y * size / optionCount, size, y * size / optionCount);
                horizontalLine.setStroke(Color.DARKGREY);
                horizontalLine.setStrokeWidth(strokeWidth);
                optionIcon.getChildren().add(horizontalLine);
            }
            int finalOptionCount = optionCount;
            EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    sectionsX = finalOptionCount;
                    sectionsY = finalOptionCount;
                    initializeImg(img, finalOptionCount, finalOptionCount);
                    controlButtons();
                }
            };
            optionIcon.addEventHandler(MouseEvent.MOUSE_CLICKED,eventHandler);
            parent.getChildren().add(optionIcon);
        }
    }

    public void chooseSectioning(Image img){
        ImageView setSectionMenu = new ImageView(img);
        setSectionMenu.setOpacity(0.6);
        setSectionMenu.setFitWidth(this.screenWidth);
        setSectionMenu.setFitHeight(this.screenHeight);

        int borderDist = 20;
        HBox options = new HBox(borderDist);

        options.setPadding(new Insets(borderDist, borderDist, borderDist, borderDist));
        options.setMaxWidth(screenWidth);
        options.setMinWidth(screenWidth);
        options.setPrefHeight(screenHeight);
        options.setAlignment(Pos.CENTER);
        sectioningRepresentation(borderDist*4,4, options);

        root.getChildren().addAll(setSectionMenu, options);
    }





    /*----OTHER----*/



    public Scene start() {
        Image img = new Image(this.srcImage);
        this.img = img;
        this.root = new Group();
        chooseSectioning(img);
//        initializeImg(img, this.sectionsX, this.sectionsY);
//        controlButtons();
        this.gameScene = new Scene(this.root, this.screenWidth, this.screenHeight);
        this.gameScene.getStylesheets().add(css);
        return this.gameScene;
    }

}