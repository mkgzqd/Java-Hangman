/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkgzqdhangman;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 */
public class Player1Controller implements Initializable {
    
    @FXML
    //private Label label;
    
    private Stage stage;
    private Scene page1Scene;
    private Scene page2Scene;
    private GameController gameController; 
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
   public void start(Stage stage){
       this.stage = stage;
       page1Scene = stage.getScene();
   } 
    
    
    @FXML
    private void goToPage2(ActionEvent event) {
        try {
            System.out.println("Going to Page 2");
            
            String result = getDataToTransfer();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
            Parent page2Root = loader.load();
            
            gameController = loader.getController();
            
            //so we can come back from page2
            gameController.page1Scene = page1Scene;
            gameController.player1Controller = this;
            
            page2Scene = new Scene(page2Root);
            
            stage.setScene(page2Scene);
            
            gameController.info = "Hello World";
            
            gameController.start(stage);
            gameController.changeSomething(result);
            
        } catch (IOException ex) {
            Logger.getLogger(Player1Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
          
    }
    
    @FXML
    private void aboutPage(ActionEvent event) {
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About");
            alert.setHeaderText("About Page");
            alert.setContentText("This Hangman Game was developed by Matt Gambino for Professor Wergeles' CS 3330 Class at"
                    + " the University of Missouri. This game is designed to be played by two people. Player 1 enters "
                    + "a word or phrase and Player 2 attempts to guess Player 1's entry.");

            alert.showAndWait();
    }
    
    private String getDataToTransfer(){
        TextInputDialog dialog = new TextInputDialog("Enter something");
        dialog.setTitle("Player 1 Input");
        dialog.setHeaderText("Player 1");
        dialog.setContentText("Player 1, enter a word for your opponent to guess.");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            return result.get(); 
        }
     
        return null; 
    }   
}