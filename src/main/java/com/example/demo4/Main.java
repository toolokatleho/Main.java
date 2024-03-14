package com.example.demo4;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    private GridPane gridPane;
    private String[] imageUrls;
    private int currentIndex = 0;
    private boolean slideshowEnabled = false;



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Thumbnail Grid");

        // GridPane to arrange thumbnails
        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
       // gridPane.setStyle("-fx-background-color: GREEN");//moss
        gridPane.getStyleClass().add("gridPane");
        gridPane.setPadding(new Insets(10));

            // Adding images
               imageUrls = new String[]{
                       "/images/first.jpg",
                       "/images/seccond.jpg",
                       "/images/third.jpg",
                       "/images/fourth.jpg",
                       "/images/fifth.jpg",
                       "/images/one.jpg",
                       "/images/two.jpg",
                       "/images/three.jpg",
                       "/images/four.jpg",
                       "/images/five.jpg",
                       "/images/six.jpg",
                       "/images/seven.jpg",
                       "/images/eight.jpg", "/images/beach.jpeg"

               };

            int col = 0;
            int row = 0;

            // Adding thumbnails to the GridPane
            for (String imageUrl : imageUrls) {
                Image image = new Image(getClass().getResourceAsStream(imageUrl));
                ImageView thumbnailImageView = new ImageView(image);
                thumbnailImageView.setFitWidth(300); // Set the width of the thumbnail
                thumbnailImageView.setFitHeight(300); // Set the height of the thumbnail

                thumbnailImageView.setOnMouseEntered(event -> thumbnailImageView.getStyleClass().add("thumbnail"));
                thumbnailImageView.getStyleClass().add("thumbnail-box");

                //  styles from the CSS file
                thumbnailImageView.getStyleClass().addAll("thumbnail", "thumbnail-box");

                // click event handler to show full-size image
                thumbnailImageView.setOnMouseClicked(event -> showFullSizeImage(image));

                gridPane.add(thumbnailImageView, col, row);

                // Adjust column and row indices
                col++;
                // Display 4 thumbnails per row
                if (col == 4) {
                    col = 0;
                    row++;
                }
            }
            //wrap the Gridpane in scrollpane
            ScrollPane scrollPane = new ScrollPane(gridPane);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);
            scrollPane.getStyleClass().add("scrollPane");

            Scene scene = new Scene(scrollPane, 800, 600); // Setting  the size of the scene
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            primaryStage.setScene(scene);

            primaryStage.show();
        }
            /* function to display previous image*/
            public void displayPreviousImage(){
                if(currentIndex > 0){
                    currentIndex--;
                    showFullSizeImage(new Image(imageUrls[currentIndex]));}
            }
            // function to display next image
            public void displayNextImage(){
                if(currentIndex < imageUrls.length - 1){
                    currentIndex++;
                   showFullSizeImage(new Image(imageUrls[currentIndex]));

                }
            }
            // Function to show full-size image in a new window
            private void showFullSizeImage(Image image) {
                Stage primaryStage = (Stage) gridPane.getScene().getWindow();
                primaryStage.hide();  // Hide the current stage

                Stage fullSizeStage = new Stage();
                ImageView fullSizeImageView = new ImageView(image);
                fullSizeImageView.setFitWidth(500); // Set the width of the full-size image
                fullSizeImageView.setFitHeight(500); // Set the height of the full-size image

                // Create navigation buttons
                Button previousButton = new Button("<");
                previousButton.setOnMouseEntered(event -> previousButton.setText("previous"));
                previousButton.setOnMouseReleased(event -> previousButton.setText("<"));
                previousButton.setOnAction(event -> displayPreviousImage());

                Button nextButton = new Button(">");
                nextButton.setOnMouseEntered(event-> nextButton.setText("next"));
                nextButton.setOnMouseReleased(event -> nextButton.setText(">"));
                nextButton.setOnAction(event -> displayNextImage());

                // Making classes to the buttons for styling
                previousButton.getStyleClass().add("nav-button");
                nextButton.getStyleClass().add("nav-button");

                // Positioning the buttons within the HBox
                HBox navigationBox = new HBox(400, previousButton, nextButton);
                navigationBox.setPadding(new Insets(10));
                navigationBox.setPrefHeight(50);

                HBox slideshowCloseBox = createSlideshowCloseBox(fullSizeStage);
                //vertical box to put all the control buttons
                VBox holdControlButtons = new VBox(navigationBox,slideshowCloseBox);
                holdControlButtons.setAlignment(Pos.BOTTOM_CENTER);
                holdControlButtons.setSpacing(280);

                StackPane stackPane = new StackPane(fullSizeImageView,holdControlButtons);
                stackPane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

                Scene fullSizeScene = new Scene(stackPane,900,700);
                fullSizeStage.setScene(fullSizeScene);
                fullSizeStage.setTitle("Full Size Image");
                //  event handler for the close button to return to the grid pane when the full-size stage is hidden
                fullSizeStage.setOnHidden(event -> returnToGridPane(primaryStage));
                fullSizeStage.show();
            }

    private void returnToGridPane(Stage primaryStage) {
    }

    private HBox createSlideshowCloseBox(Stage fullSizeStage) {
        // Create slideshow and close buttons
        Button slideshowButton = new Button("Slideshow");
        slideshowButton.getStyleClass().add("con-button");
        slideshowButton.setOnAction(event -> toggleSlideshow());

        Button closeButton = new Button("Back to GridPane");
        closeButton.getStyleClass().add("con-button");
        closeButton.setOnAction(event -> {
            fullSizeStage.close();
            returnToGridPane();
        });

        // Positioning the buttons within the HBox
        HBox slideshowCloseBox = new HBox(20, slideshowButton, closeButton);
        slideshowCloseBox.getStyleClass().add("slideshow-close-box");
        slideshowCloseBox.setAlignment(Pos.BOTTOM_CENTER);
        slideshowCloseBox.setPadding(new Insets(20));
        return slideshowCloseBox;
    }
    private void returnToGridPane() {
        Stage primaryStage = (Stage) gridPane.getScene().getWindow();
        primaryStage.show();
    }
    private void toggleSlideshow() {
        slideshowEnabled = !slideshowEnabled;

        if (slideshowEnabled) {
            // Start a timer to display the next image every 3 seconds
            Timeline slideshowTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(3), event -> {
                        if(currentIndex < imageUrls.length - 1){
                            currentIndex++;
                            showFullSizeImage(new Image(imageUrls[currentIndex]));

                        }
                    })
            );
            slideshowTimeline.setCycleCount(Animation.INDEFINITE);
            slideshowTimeline.play();
        }
    }
}