package View;

import javafx.animation.FadeTransition;
import javafx.scene.Group;
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
    private Shape[] hintIcon(){
        Color iconColor = Color.WHITE;
        int strokeWidth = 5;

        Circle lensCorpus = new Circle(50, 50, 15);
        lensCorpus.setFill(Color.TRANSPARENT);
        lensCorpus.setStroke(iconColor);
        lensCorpus.setStrokeWidth(strokeWidth);

        Line lensGrip = new Line(30, 75, 40, 62);
        lensGrip.setStroke(iconColor);
        lensGrip.setStrokeWidth(strokeWidth);
        lensGrip.setStrokeLineCap(StrokeLineCap.ROUND);
        Shape[] icon = {lensCorpus, lensGrip};
        return icon;
    }

    //create rotation-Icon: one circle with a down-arrow on circumference (length of arrow-point is 0.5*radius)
    private Shape[] rotateIcon(int centerX, int centerY, int radius){
        Color iconColor = Color.WHITE;
        int strokeWidth = 2;

        Circle rotation = new Circle(centerX, centerY, radius);
        rotation.setFill(Color.TRANSPARENT);
        rotation.setStroke(iconColor);
        rotation.setStrokeWidth(strokeWidth);

        Line stroke1 = new Line(centerX+radius, centerY,centerX+radius*1.5, centerY-radius*0.5);
        stroke1.setStroke(iconColor);
        stroke1.setStrokeWidth(strokeWidth);
        stroke1.setStrokeLineCap(StrokeLineCap.ROUND);
        Rotate rotate = new Rotate();
        rotate.setAngle(338.2);
        rotate.setPivotX(centerX+radius);
        rotate.setPivotY(centerY);
        stroke1.getTransforms().addAll(rotate);

        Line stroke2 = new Line(centerX+radius, centerY, centerX+radius*0.5, centerY-radius*0.5);
        stroke2.setStroke(iconColor);
        stroke2.setStrokeWidth(strokeWidth);
        stroke2.setStrokeLineCap(StrokeLineCap.ROUND);
        stroke2.getTransforms().addAll(rotate);

        Shape[] icon = {rotation, stroke1, stroke2};
        return icon;
    }

    private Shape[] exitIcon(){
        Color iconColor = Color.WHITE;
        int strokeWidth = 5;

        Line stroke1 = new Line(screenWidth-60, 65, screenWidth-30, 35);
        stroke1.setStroke(iconColor);
        stroke1.setStrokeWidth(strokeWidth);
        stroke1.setStrokeLineCap(StrokeLineCap.ROUND);

        Line stroke2 = new Line(screenWidth-60, 35, screenWidth-30, 65);
        stroke2.setStroke(iconColor);
        stroke2.setStrokeWidth(strokeWidth);
        stroke2.setStrokeLineCap(StrokeLineCap.ROUND);

        //enhance hit-zone by adding invisible square around the cross
        Rectangle hiddenHitZone = new Rectangle(screenWidth-65, 30, 40, 40);
        hiddenHitZone.setFill(Color.TRANSPARENT);

        Shape[] icon = {hiddenHitZone,stroke2, stroke1};
        return icon;
    }

    /*----CONTROL-BUTTON LOGIC----*/

    public void giveHint(Group hintButton){
        hintButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Text text = new Text("Correct Tiles: "+correctTiles);
                text.setX(30);
                text.setY(30);
                text.setFill(Color.WHITE);
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
        Group hintButton = new Group();
        hintButton.getChildren().addAll(hintIcon());
        giveHint(hintButton);
        Group exitButton = new Group();
        exitButton.getChildren().addAll(exitIcon());
        exitToMenu(exitButton);
//        Group rotationButton = new Group();
//        rotationButton.getChildren().addAll(rotateIcon(70, 70, 10));
        root.getChildren().addAll(hintButton, exitButton);//,rotationButton);
    }

    /*----WIN-MESSAGE----*/

    public HBox showWinMessage(){
        HBox winBox = new HBox();
        winBox.setPrefWidth(this.screenWidth);
        winBox.setAlignment(Pos.CENTER);
        Label text = new Label("Congratulations!\nYou Won");
        text.setTextFill(Color.WHITE);
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

    public ImageView[] initializeImg(Image img, int xTileCount, int yTileCount){
        ImageView[] imgViews = new ImageView[xTileCount*yTileCount];
        this.sectionCount = xTileCount * yTileCount;
        this.correctTiles = sectionCount;
        int sectionWidth = (int)this.screenWidth/xTileCount;
        int sectionHeight = (int) this.screenHeight/yTileCount;
        for(int x = 0; x < xTileCount; x++){
            for(int y = 0; y < yTileCount; y++){
                int pos = (x * yTileCount + y);
                int posX = pos % xTileCount;
                int posY = Math.floorDiv(pos, yTileCount);

                ImageView imgV =  section(img, xTileCount, yTileCount,pos);// (x * yTileCount + y));
                initializeTile(imgV);
                EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        rotate(imgV, 1);
                    }
                };

                Group rotateIcon = new Group();
                EventHandler<MouseEvent> mouseOverHandler = new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        rotateIcon.getChildren().addAll(rotateIcon((int) (posX*sectionWidth+0.5*sectionWidth), (int)(posY*sectionHeight+0.5*sectionHeight), sectionWidth/20));
                        FadeTransition fadeOut = new FadeTransition(Duration.millis(1000), rotateIcon);
                        fadeOut.setFromValue(1.0);
                        fadeOut.setToValue(0.0);
                        fadeOut.setOnFinished(e -> root.getChildren().remove(rotateIcon));

                        try{
                            root.getChildren().add(rotateIcon);
                            fadeOut.play();
                        } catch (Exception except){
                            fadeOut.play();
                        }
                    }
                };

                EventHandler<MouseEvent> mouseLeaveHandler = new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        try{
                            root.getChildren().remove(rotateIcon);
                        } catch (Exception except){
                        }
                    }
                };

                imgV.addEventHandler(MouseEvent.MOUSE_CLICKED,eventHandler);
                imgV.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, mouseOverHandler);
                imgV.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, mouseLeaveHandler);
                imgViews[(x * yTileCount + y)] = imgV;
            }
        }

        return imgViews;
    }





    /*----OTHER----*/



    public Scene start() {
        Image img = new Image(this.srcImage);
        this.root = new Group();
        this.root.getChildren().addAll(this.initializeImg(img, 5, 5));
        controlButtons();
        this.gameScene = new Scene(this.root, this.screenWidth, this.screenHeight);
        this.gameScene.getStylesheets().add(css);
        return this.gameScene;
    }

}