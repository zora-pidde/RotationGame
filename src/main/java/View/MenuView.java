package View;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.scene.Scene;


public class MenuView extends Application {
    private Stage stage;
    private Scene menuScene;

    private Group root = new Group();
    private int screenWidth = 650;
    private int screenHeight = 650;

    private LevelView level;
    String css =
        this.getClass().getResource("/styles.css").toExternalForm();

    public void menuCallback(){
        this.stage.setWidth(this.screenWidth);
        this.stage.setHeight(this.screenHeight);
        this.stage.setScene(this.menuScene);
    }

    public LinearGradient crinklePattern(double width) {
    Color color1 = Color.hsb(0, 0.2, 0.8);

    Color color2 = Color.hsb(0, 0.2, 0.7);
    Stop[] stops = new Stop[]{new Stop(0, color1), new Stop(1, color2)};
     return new LinearGradient(0, 0.3, 10, 0.3, false, CycleMethod.REPEAT, stops);
    }

    public ImageView applyLevel(String imgSrc, int size){
        Image img = new Image(imgSrc);
        ImageView imgV = new ImageView(img);
        //check if (close to) squared
        double proportion = (float)img.getWidth()/img.getHeight();
        int screenX = this.screenWidth;
        int screenY = this.screenHeight;
        if(Math.abs(proportion - 1) < 0.1) {
            imgV.setFitWidth(size);
            imgV.setFitHeight(size);
        } else if(img.getWidth() < img.getHeight()) {
            imgV.setFitHeight(size);
            imgV.setFitWidth(size * proportion);
            screenX = (int) (this.screenWidth*proportion);
        } else {
            imgV.setFitWidth(size);
            imgV.setFitHeight(size * 1/proportion);//(img.getHeight()/img.getWidth()));
            screenY = (int)(this.screenHeight/proportion);
        }
        int finalScreenX = screenX;
        int finalScreenY = screenY;
        imgV.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                level.setSrcImage(imgSrc);
                level.setScreenSize(finalScreenX, finalScreenY);
                Scene levelScene = level.start();
                stage.setScene(levelScene);
                stage.setTitle("Level: "+imgSrc.substring(0,imgSrc.length()-4));
                stage.show();
            }
        }));
        return imgV;
    }

    public void changeScene(Scene newScene){
        stage.setScene(newScene);
        stage.show();
        stage.sizeToScene();
    }

    public void resetStageSize(double width, double height){
        this.stage.setWidth(width);
        this.stage.setHeight(height);
    }


    public void addLevels(VBox heightRegion){
        int spacing = 15;
        int borderDist = 10;
        int size = 200;
        // CAREFUL: if adding new pictures need to reset this number
        int nPics = 8;
        int nPicsPerRow = (this.screenWidth - 2* borderDist + spacing)/(size + spacing);
        int nRows = Math.ceilDiv(nPics, nPicsPerRow);
        for(int i = 0; i < nRows; i++) {
            System.out.println();
            HBox levels = new HBox(spacing);
//            levels.setPadding(new Insets(this.screenHeight / 10 + i*(size+spacing), borderDist, borderDist, borderDist));
            levels.setPadding(new Insets(borderDist, borderDist, borderDist, borderDist));
            levels.setMaxWidth(screenWidth);
            levels.setMinWidth(screenWidth);
            levels.setPrefHeight(size + spacing);
            levels.setAlignment(Pos.CENTER);
            for (int j = 0; j < nPicsPerRow; j++) {
                if (i * nPicsPerRow + (j + 1) > nPics) {
                    break;
                }
                ImageView imgV = applyLevel("level" + (i * nPicsPerRow + (j + 1)) + ".png", size);
                levels.getChildren().add(imgV);
            }
            heightRegion.getChildren().add(levels);
        }
    }

    public void addHeader(VBox heightRegion){
        HBox headerBox = new HBox();
        headerBox.setPrefWidth(this.screenWidth);
        headerBox.setPadding(new Insets(10, 10, 10, 10));
        Label header = new Label("Rotation Game");
        header.getStyleClass().add("header_text");
        headerBox.setAlignment(Pos.CENTER);
        headerBox.getChildren().add(header);
//        this.root.getChildren().add(headerBox);
        heightRegion.getChildren().add(headerBox);
    }


    @Override
    public void start(Stage stage) throws Exception {
        //create & initialize stage/scene
        this.stage = stage;
        this.level = new LevelView(stage);
        this.level.setScreenSize(this.screenWidth, this.screenHeight);
        this.level.setMenuView(this);
        this.menuScene = new Scene(this.root, this.screenWidth, this.screenHeight);
        this.menuScene.setFill(crinklePattern(0.3));
        this.menuScene.getStylesheets().add(css);
        VBox heightRegion = new VBox();
        heightRegion.setPrefHeight(this.screenHeight);
        addHeader(heightRegion);
        addLevels(heightRegion);
        this.root.getChildren().add(heightRegion);

        stage.setScene(this.menuScene);
        stage.setTitle("Menu");
        stage.show();

    }
}
