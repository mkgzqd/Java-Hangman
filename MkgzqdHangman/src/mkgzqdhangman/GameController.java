/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mkgzqdhangman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 */
public class GameController extends CheckVictory implements Initializable, GameBoard {
    
    
    @FXML
    private TextField textField; 
    
    VictoryCount vic = new VictoryCount();
    
    private int numWrong=0;
    //private int player1Wins =vic.getPlayer1Victory();
    
    private final StringBuilder fieldContent;
    
    public String info = "";
    private String result;
    private String hold;
    private String boardLength=null;
    private char c;
    
    private Stage stage;
    public Scene page1Scene;
    public Player1Controller player1Controller;
    @FXML
    private Label gameBoard;
    @FXML
    private Label wrongGuess;
    @FXML
    private ImageView hangmanImage;
    @FXML
    private Label scoreBoard;
    

    public GameController() {
        this.fieldContent = new StringBuilder("");
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void start(Stage stage) {
        this.stage = stage;
        
    }
    
    @FXML
    private void goBackToPage1(ActionEvent event) {
        stage.setScene(page1Scene);
      //  player1Controller.doThisThing("This is the info I sent you! From: " + result);
    }
    
    @FXML
    private void guessLetter (ActionEvent event){
        result = textField.getText();
        
        if(result.length() > 1 || result.length() < 1){
            System.out.println("A guess must be one letter.");
            Alert alert = new Alert(AlertType.WARNING, "");
            alert.getDialogPane().setPrefSize(200, 100);
            alert.setHeaderText("WARNING!!!");// Header
            alert.setContentText("Guess can only be one letter.");
            alert.showAndWait();
            
        }
        
        else{
            System.out.println("Guess is: " + result);
            c = result.toUpperCase().charAt(0);
            searchString(hold);
            checkPlayer2Victory();
            
        }
    }
    
    private void searchString(String dataFromPage1){
        //System.out.println(dataFromPage1);
        int count =0;
        //c = result.charAt(0);
        for(int i=0; i < dataFromPage1.length(); i++){
            if(dataFromPage1.toUpperCase().charAt(i) == c){
                 count++;
                 System.out.println("Occurs at index: " + i);
                 changeGameBoard(i);
            }    
        }
        
        if(count==0){
            countWrong();
        }
        
        System.out.println("No of Occurences of character "+ c + " is "+count);
    }
    
    public void changeSomething(String dataFromPage1){
        //labelForTextField.setText(dataFromPage1); 
        hold = dataFromPage1;
        setGameBoard(hold);
    }
    
    public void setScoreBoard(){
        try{
            FileInputStream file = new FileInputStream("Count.ser");
            ObjectInputStream in = new ObjectInputStream(file);
            
            vic = (VictoryCount)in.readObject();
            
            in.close();
            file.close();
            
            System.out.println("Deserialization is complete.");
            
            scoreBoard.setText("Player 1: " + (Integer.toString(vic.player1Victory)) + " Player 2: " + Integer.toString(vic.player2Victory));
        }
        
        catch(IOException ex){
            System.out.println("IOException is caught.");
        }
        catch(ClassNotFoundException ex){
            System.out.println("ClassNotFoundException is caught");
        }
    }
    
    /**
     *
     * @param hold
     */
    @Override
    public void setGameBoard(String hold){
        //gameBoard.setText(hold);
        
        ArrayList<Character> chars;
        chars = new ArrayList<>(
                hold.chars()
                        .mapToObj(e -> (char) e)
                        .collect(
                                Collectors.toList()
                        )    
        );
        
        //char[] check = hold.toCharArray();
        for(int i=0; i < chars.size(); i++){
           
           if(boardLength == null && chars.get(i)!= ' ')
               boardLength = "_";
           else if(boardLength == null && chars.get(i) == ' ')
               boardLength = " ";
           else if(chars.get(i) != ' ')
            boardLength +="_";
           else if(chars.get(i) == ' ')
                boardLength+= " ";
        }
        
        gameBoard.setText(boardLength); 
        setScoreBoard();
    }
    
    @Override
    public void changeGameBoard(int index){
        char[] newString = boardLength.toCharArray();
        for(int i=0; i < newString.length; i++){
            if(i== index){
                newString[i] = c;
            }
        }
        
        boardLength= String.valueOf(newString);
        gameBoard.setText(boardLength);
    }
    
    private void countWrong(){
        numWrong++;      
            Image test = new Image(getClass().getResource("hang_" + (numWrong +1) + ".gif").toExternalForm());
            hangmanImage.setImage(test);
        
        //wrongGuess.setText(String.valueOf(numWrong));
        StringBuilder append = fieldContent.append(" ").append(c).append("  ");

        wrongGuess.setText("Letters wrong:\n" + fieldContent.toString());
        
        checkPlayer1Victory();
        
    }
    
    @Override
    void checkPlayer1Victory(){
        if(numWrong==6){
             System.out.println("CONGRATULATIONS! PLAYER 1 HAS WON THE GAME!");
             
             vic.player1Victory++;
             
             try{
                File file = new File("Count.ser");
                
                FileOutputStream fileOut = new FileOutputStream(file.getPath());
                
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                
                out.writeObject(vic);
                out.close();
                fileOut.close();
                
                System.out.println("Object is now serialized");
            }
            catch(IOException ex){
                System.out.println(ex.toString());
            }
            
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("WINNER!");
            alert.setHeaderText("CONGRATULATIONS!");
            alert.setContentText("CONGRATULATIONS! PLAYER 1 HAS WON THE GAME!");

            alert.showAndWait()
            .filter(response -> response == ButtonType.OK)
             .ifPresent(response -> stage.setScene(page1Scene));
        }
    }
    
    @Override
    void checkPlayer2Victory(){
        char[] charArray = boardLength.toCharArray();
        boolean contains;
        contains = false;
        
        for(int i=0; i<charArray.length; i++){
            if(charArray[i]=='_'){
                contains = true;
            }
        }
        
        if(contains==false){
            //gameBoard.setText("WINNER! CONGRATULATIONS!");
            System.out.println("CONGRATULATIONS! PLAYER 2 HAS WON THE GAME!");
            
            vic.player2Victory++;
            
            try{
                File file = new File("Count.ser");
                
                FileOutputStream fileOut = new FileOutputStream(file.getPath());
                
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                
                out.writeObject(vic);
                out.close();
                fileOut.close();
                
                System.out.println("Object is now serialized");
            }
            catch(IOException ex){
                System.out.println(ex.toString());
            }
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("WINNER!");
            alert.setHeaderText("CONGRATULATIONS!");
            alert.setContentText("CONGRATULATIONS! PLAYER 2 HAS WON THE GAME!");

            alert.showAndWait()
             .filter(response -> response == ButtonType.OK)
             .ifPresent(response -> stage.setScene(page1Scene));
        }
    }
    
}