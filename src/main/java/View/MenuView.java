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

    private int nLevels = 8;

    private int currentPage = 0;

    private VBox[] pages;

    private LevelView level;


    String css =
        this.getClass().getResource("/styles.css").toExternalForm();


    public void addNextPageEvent(Group icon){
        icon.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {

                root.getChildren().remove(pages[currentPage]);
                int nextPage = (currentPage + 1) % pages.length;
                root.getChildren().addFirst(pages[nextPage]);
                currentPage = nextPage;
            }
        }));

    }

    public void addLastPageEvent(Group icon){
        icon.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {

                root.getChildren().remove(pages[currentPage]);
                int nextPage = (pages.length + currentPage - 1) % pages.length;
                root.getChildren().addFirst(pages[nextPage]);
                currentPage = nextPage;
            }
        }));

    }

    public void menuCallback(){
        this.stage.setScene(this.menuScene);
        this.stage.sizeToScene();
    }

    public LinearGradient crinklePattern() {
    Color color1 = Color.hsb(21, 0.17, 0.82);

    Color color2 = Color.hsb(20, 0.01, 0.89);
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
            imgV.setFitHeight(size/proportion);
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


    public void addLevels(){
        int spacing = 15;
        int borderDist = 10;
        int size = 200;
        int nPicsPerRow = (this.screenWidth - 2* borderDist + spacing)/(size + spacing);
        int nRows = Math.ceilDiv(nLevels, nPicsPerRow);
        //deduct space reserved for header
        int nRowsPerPage = (this.screenHeight - 40 - 2* borderDist + spacing)/(size + spacing);
        int nPages = Math.ceilDiv(nLevels, nRowsPerPage * nPicsPerRow);
        pages = new VBox[nPages];
        for(int i = 0; i < nPages; i++){
            VBox page = new VBox();
            page.setPrefHeight(this.screenHeight);
            addHeader(page);
            for(int j = 0; j < nRowsPerPage; j++){
                HBox row = new HBox(spacing);
                row.setPadding(new Insets(borderDist, borderDist, borderDist, borderDist));
                row.setMaxWidth(screenWidth);
                row.setMinWidth(screenWidth);
                row.setPrefHeight(size + spacing);
                row.setAlignment(Pos.CENTER);
                int k = 0;
                while(k < nPicsPerRow && ((i*nRowsPerPage * nPicsPerRow) + j * nPicsPerRow + (k + 1) <= nLevels)){
                    ImageView imgV = applyLevel("level" + ((i*nRowsPerPage * nPicsPerRow) +  (j * nPicsPerRow + (k + 1)))+ ".png", size);
                    row.getChildren().add(imgV);
                    k++;
                }
                page.getChildren().add(row);
            }
            pages[i] = page;

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
        this.menuScene.setFill(crinklePattern());
        this.menuScene.getStylesheets().add(css);
        addLevels();
        this.root.getChildren().add(pages[currentPage]);
        Group nextPage = Icons.nextPageIconRound();
//        nextPage.setTranslateY(screenHeight - Icons.iconSize - nextPage.getTranslateY());
        nextPage.setTranslateX(screenWidth - Icons.iconSize - nextPage.getTranslateX());
        addNextPageEvent(nextPage);

        Group lastPage = Icons.lastPageIconRound();
//
//        lastPage.setTranslateY(screenHeight - Icons.iconSize - lastPage.getTranslateY());
        addLastPageEvent(lastPage);

        this.root.getChildren().addAll(nextPage, lastPage);

        stage.setScene(this.menuScene);
        stage.setTitle("Menu");
        stage.show();

    }
}
