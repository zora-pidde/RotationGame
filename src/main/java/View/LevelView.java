package View;

import javafx.animation.FadeTransition;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

    private double proportion;

    private int correctTiles = 0;
    private int sectionCount = 0;

    private String srcImage;

    private MenuView menu;

    private Color controlColor = Color.WHITE;
    private Color controlColor2 = Color.DARKGREY;

    private Image img;

    private Group exitButton;

    private Group tiles;


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
        this.proportion = (float) width/height;
    }

    public void setMenuView(MenuView menu){
        this.menu = menu;
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

    /*----CONTROL-BUTTON LOGIC----*/

    public void giveHint(Group hintButton){
        hintButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {

                Group message = styleMessage(new Label("Correct Tiles: "+correctTiles));

                FadeTransition fadeOut = new FadeTransition(Duration.millis(2000), message);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.setOnFinished(e -> root.getChildren().remove(message));
                try{
                    root.getChildren().add(message);
                    fadeOut.play();
                    //if hint still displayed -> refresh text
                } catch(Exception e){
                    fadeOut.play();
                }
            }
        }));
    }

    public void showSolution(Group showSolutionButton){
        showSolutionButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {

                FadeTransition fadeOpacity = new FadeTransition(Duration.millis(2000), tiles);
                fadeOpacity.setFromValue(0.2);
                fadeOpacity.setToValue(0.7);
                fadeOpacity.setOnFinished(e -> tiles.setOpacity(1.));
                fadeOpacity.play();
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

    public Group retrieveExitButton(){
        Group exitButton = Icons.exitIcon();
        exitButton.setTranslateX(screenWidth - Icons.iconSize - exitButton.getTranslateX());
        exitToMenu(exitButton);
        this.exitButton = exitButton;
        return exitButton;
    }

    public void controlButtons(){
        root.getChildren().addAll(retrieveExitButton());
    }




    public Group styleMessage(Label text){
        Group message = new Group();
        Rectangle backContrast = new Rectangle(screenWidth/8, screenHeight*0.5-40, screenWidth*3/4, 100);
        backContrast.setFill(Color.BLACK);
        backContrast.setOpacity(0.4);
        HBox messageBox = new HBox();
        VBox alignmentBox = new VBox();
        alignmentBox.setPrefHeight(this.screenHeight);
        alignmentBox.setAlignment(Pos.CENTER);

        messageBox.setPadding(new Insets(25, 10, 10, 10));
        messageBox.setPrefWidth(this.screenWidth);
        messageBox.setAlignment(Pos.CENTER);

        text.setTextFill(this.controlColor);
        text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
        text.setTextAlignment(TextAlignment.CENTER);
        messageBox.getChildren().add(text);
        alignmentBox.getChildren().add(messageBox);
        message.getChildren().addAll(backContrast, alignmentBox);
        return message;
    }

    /*----WIN-MESSAGE----*/
    public Group showWinMessage(){
        Group winMessage = styleMessage(new Label("Congratulations!\nYou Won"));
        this.root.getChildren().addAll(winMessage);
        winMessage.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                root.getChildren().remove(winMessage);
            }
        }));
        return winMessage;
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
            Group winBox = showWinMessage();
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

    public ImageView section(Image img, int xSectionAmount, int ySectionAmount, int pos, double offsetX, double offsetY) {
        double sectionSize;
        double imgSectionSize;
        if(img.getWidth() -img.getHeight() > 0.05 ){
            //case longer Width
            sectionSize = (float) this.screenHeight/ySectionAmount;
            imgSectionSize = (float) img.getHeight()/ySectionAmount;
        } else{
            sectionSize = (float) this.screenWidth/xSectionAmount;
            imgSectionSize = (float) img.getWidth()/xSectionAmount;
        }
        int posX = pos % xSectionAmount;
        int posY = (pos-posX)/xSectionAmount;
        ImageView imgV = new ImageView(img);
        imgV.setFitHeight(sectionSize);
        imgV.setFitWidth(sectionSize);
        imgV.setX(sectionSize * posX);
        imgV.setY(sectionSize * posY);

        Rectangle2D imgSection = new Rectangle2D(imgSectionSize * posX +offsetX,imgSectionSize *posY +offsetY, imgSectionSize, imgSectionSize);
        imgV.setViewport(imgSection);
        return imgV;
    }

    public void initializeImg(Image img, int xTileCount, int yTileCount){
        this.sectionCount = xTileCount * yTileCount;
        this.correctTiles = sectionCount;
        //shorter side determines size, as longer side will be clipped to fit
        double sectionSize;

        //calculate offset in order to take middle part of picture if clipping is necessary
        double offsetX = 0;
        double offsetY = 0;

        if(img.getWidth() -img.getHeight() > 0.1 ){
            //case longer Width
            sectionSize =  this.screenHeight/yTileCount;
            int translateX = (int) (screenWidth - sectionSize * xTileCount);
            exitButton.setTranslateX(-translateX);

            offsetX = (screenWidth - xTileCount*sectionSize)/2;
        } else{
            sectionSize = this.screenWidth/xTileCount;
            offsetY = (screenHeight - yTileCount*sectionSize)/2;
        }
        System.out.println("Width: "+img.getWidth()+" Height: "+img.getHeight());
        System.out.println("Screen: "+this.screenWidth+" Height: "+this.screenHeight);
        System.out.println("Tiles: "+sectionSize);
        System.out.println("Xtiles: "+xTileCount+" Ytiles: "+yTileCount);
        System.out.println("offsetX: "+offsetX+" offsetY: "+offsetY);

        this.screenWidth = (int) sectionSize * xTileCount;
        this.screenHeight = (int) sectionSize * yTileCount;
        menu.resetStageSize(screenWidth, screenHeight);

        ImageView imgSolution = new ImageView(img);
//        imgSolution.setFitHeight(img.getHeight());
        imgSolution.setFitWidth(screenWidth);
        imgSolution.setFitHeight(screenHeight);
//        imgSolution.setX(offsetX);
//        imgSolution.setY(offsetY);

        Rectangle2D imgSection = new Rectangle2D(offsetX,offsetY, img.getWidth(), screenHeight*img.getWidth()/screenWidth);
        imgSolution.setViewport(imgSection);
//        ImageView comparisonHelp = section(img, 1, 1, 0, 0, 0);

        Group tiles = new Group();
        for(int x = 0; x < xTileCount; x++){
            for(int y = 0; y < yTileCount; y++){
                int pos = (x * yTileCount + y);
                int posX = pos % xTileCount;
                int posY = (pos-posX)/xTileCount;

                ImageView imgVInitial =  section(img, xTileCount, yTileCount,pos, offsetX, offsetY);
                initializeTile(imgVInitial);

                Group imgV = new Group();
                imgV.getChildren().add(imgVInitial);

                Group rotateIcon = rotateIcon((int) (posX*sectionSize+0.5*sectionSize), (int)(posY*sectionSize+0.5*sectionSize), (int)sectionSize/4);
                rotateIcon.setOpacity(0.2);
                rotateIcon.setVisible(false);
                imgV.getChildren().add(rotateIcon);

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
                tiles.getChildren().addAll(imgV);
            }
        }
        this.tiles = tiles;
        root.getChildren().addAll(imgSolution, tiles);
    }

    public void sectioningRepresentation(int size, int maxOptions, HBox parent){
        int strokeWidth = size/5;
        for(int optionCount = 2; optionCount <= maxOptions; optionCount++) {
            Group optionIcon = new Group();
            double sizeX = size;
            double sizeY = size;
            int optionsX = optionCount;
            int optionsY = optionCount;
            if(this.screenWidth > this.screenHeight){
                sizeY /= proportion;
//                optionsX = Math.floorDiv((int)sizeX, (int)( sizeY/optionCount)) ;
                optionsX = (int) Math.floor((sizeX/(sizeY/optionCount)));
            } else if(this.screenHeight > this.screenWidth){
                sizeX *= proportion;
//                optionsY = Math.floorDiv((int)sizeY, (int)(sizeX/optionCount));
                optionsY = (int) Math.floor( sizeY/( sizeX/optionCount));
                System.out.println("sizeY: "+sizeY+" sizeX: "+sizeX);
                System.out.println("option no: "+optionCount+" optionsY: "+optionsY);
            }
            Rectangle fullSize = new Rectangle(0, 0, sizeX, sizeY);
            fullSize.setFill(controlColor);
            optionIcon.getChildren().addAll(fullSize);
            for (int x = 0; x <= optionsX; x++) {
                Line verticalLine = new Line(x * sizeX / optionsX, 0, x * sizeX / optionsX, sizeY);
                verticalLine.setStroke(this.controlColor2);
                verticalLine.setStrokeWidth(strokeWidth/Math.max(optionsX, optionsY));
                optionIcon.getChildren().add(verticalLine);
            }
            for (int y = 0; y <= optionsY; y++) {
                Line horizontalLine = new Line(0, y * sizeY / optionsY, sizeX, y * sizeY / optionsY);
                horizontalLine.setStroke(this.controlColor2);
                horizontalLine.setStrokeWidth(strokeWidth/Math.max(optionsX,optionsY));
                optionIcon.getChildren().add(horizontalLine);
            }
            int finalOptionsY = optionsY;
            int finalOptionsX = optionsX;
            EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    initializeImg(img, finalOptionsX, finalOptionsY);
                    exitButton.toFront();
                    Group hintButton = Icons.hintIcon();
                    giveHint(hintButton);
                    Group showSolutionButton = Icons.showSolutionIcon();
                    showSolution(showSolutionButton);
                    root.getChildren().addAll(hintButton, showSolutionButton);
                    menu.changeScene(level());
                }
            };
            optionIcon.addEventHandler(MouseEvent.MOUSE_CLICKED,eventHandler);
            parent.getChildren().add(optionIcon);
        }
    }

    public Group chooseSectioning(Image img){
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

        sectioningRepresentation(borderDist*4,6, options);
        Group selectionRoot = new Group();
        selectionRoot.getChildren().addAll(setSectionMenu, options);
        selectionRoot.getChildren().addAll(retrieveExitButton());
        return selectionRoot;
    }


    /*----OTHER----*/

    public Scene start() {
        Image img = new Image(this.srcImage);
        this.img = img;
        this.root = new Group();
        Group selection = chooseSectioning(img);
        return new Scene(selection, this.screenWidth, this.screenHeight);
    }

    public Scene level(){
        controlButtons();
        this.gameScene = new Scene(this.root, this.screenWidth, this.screenHeight);
        this.gameScene.getStylesheets().add(css);
        return this.gameScene;
    }

}