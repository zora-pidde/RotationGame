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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.Scene;


public class MenuView extends Application {
    private Stage stage;
    private Scene menuScene;

    private Group root = new Group();
    private int screenWidth = 700;
    private int screenHeight = 700;

    private LevelView level;
    String css = this.getClass().getResource("/styles.css").toExternalForm();

    /*----CONSTRUCTOR----*/

//    public MenuView(Stage stage, int width, int height, GameView level){
//        this.stage = stage;
//        this.stage.setTitle("Menu");
//        this.menuScene = new Scene(new Group(), width, height);
//        this.level = level;
//    }

    /*----GETTER&SETTER---*/
    public Stage getStage(){
        return this.stage;
    }

    public void menuCallback(){
        this.stage.setScene(this.menuScene);
    }

    public LinearGradient crinklePattern(double width) {
    Color color1 = Color.hsb(0, 0.2, 0.8);

    Color color2 = Color.hsb(0, 0.2, 0.7);
    Stop[] stops = new Stop[]{new Stop(0, color1), new Stop(1, color2)};
     return new LinearGradient(0, 0.3, 10, 0.3, false, CycleMethod.REPEAT, stops);
    }

    public ImageView applyLevel(String imgSrc){
        Image img = new Image(imgSrc);
        ImageView imgV = new ImageView(img);
        imgV.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                level.setSrcImage(imgSrc);
                Scene levelScene = level.start();
                stage.setScene(levelScene);
                stage.setTitle("Level 1: "+imgSrc.substring(0,imgSrc.length()-4));
                stage.show();
            }
        }));
        return imgV;
    }

    public void addLevels(){
        int size = 200;
        int distance = 10;
        int posX = distance;
        ImageView imgV1 = applyLevel("happy.png");
        ImageView imgV2 = applyLevel("sleep.png");
        ImageView imgV3 = applyLevel("flower.png");
        ImageView[] imgVs = {imgV1, imgV2, imgV3};
        for(int i = 0; i < imgVs.length; i++){
            imgVs[i].setFitWidth(size);
            imgVs[i].setFitHeight(size);
            imgVs[i].setY(50);
            imgVs[i].setX(posX);
            posX += size + distance;
        }
        this.root.getChildren().addAll(imgVs);
    }

    public void addHeader(){
        HBox headerBox = new HBox();
        headerBox.setPrefWidth(this.screenWidth);
        headerBox.setPadding(new Insets(10, 10, 10, 10));
        Label header = new Label("Rotation Game");
        header.getStyleClass().add("header_text");
        headerBox.setAlignment(Pos.CENTER);
        headerBox.getChildren().add(header);
        this.root.getChildren().add(headerBox);
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

        addLevels();
        addHeader();

        stage.setScene(this.menuScene);
        stage.setTitle("Menu");
        stage.show();

    }
}
