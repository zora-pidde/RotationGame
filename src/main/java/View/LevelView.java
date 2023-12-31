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

        root.getChildren().addAll(hintButton, exitButton);
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
        for(int x = 0; x < xTileCount; x++){
            for(int y = 0; y < yTileCount; y++){
                ImageView imgV =  section(img, xTileCount, yTileCount, (x * yTileCount + y));
                initializeTile(imgV);
                EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        rotate(imgV, 1);
                    }
                };
                imgV.addEventHandler(MouseEvent.MOUSE_CLICKED,eventHandler);
                imgViews[(x * yTileCount + y)] = imgV;
            }
        }

        return imgViews;
    }





    /*----OTHER----*/



    public Scene start() {
        Image img = new Image(this.srcImage);
        this.root = new Group();
        this.root.getChildren().addAll(this.initializeImg(img, 2, 2));
        controlButtons();
        this.gameScene = new Scene(this.root, this.screenWidth, this.screenHeight);
        this.gameScene.getStylesheets().add(css);
        return this.gameScene;
    }

}