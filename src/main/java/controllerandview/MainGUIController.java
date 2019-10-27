package main.java.controllerandview;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.controllerandview.mainGUIwindow.controller.ControllerPrimaryGUIWindow;
import main.java.controllerandview.positionswindow.controller.ControllerPositionsWindow;
import main.java.controllerandview.positionswindow.view.MikePositionsWindowCreator;
import main.java.controllerandview.pricecontrolwindow.controller.ControllerPriceControlPanel;
import main.java.model.MainModelThread;
import main.java.model.priceserver.PriceServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainGUIController {
//    private ControllerPrimaryGUIWindow primaryGUIWindow;
    private ControllerPriceControlPanel priceControlPanel;
    private ControllerPositionsWindow posWindowController;
    private long count =0;
    private MainModelThread mainModelThread;
    private PriceServer priceServer;

    private List<ControllerPriceControlPanel> priceControlPanelControllerList = new ArrayList<>();
    private List<ControllerPositionsWindow> posWindowControllerList = new ArrayList<>();

    //called by Mainloop. Updates all GUI windows
    public void updateGUI(){
//        if (posWindowController != null) posWindowController.updateGUI();

        for(ControllerPositionsWindow controller :posWindowControllerList){
            if (controller != null) {
                controller.updateGUI();
            }
        }

//        for (int i = 0; i < 20; i++) {
//            posWindowController.setSpecificButtonInMikeGridPane(i, 0, "" + ((Math.sqrt((count * 79 + count))) * 1) % 567);
//            posWindowController.setSpecificButtonInMikeGridPane(i, 1, "" + count);
//            posWindowController.setSpecificButtonInMikeGridPane(i, 2, "" + Math.sqrt((count * 79 + i + count)) % 7);
//            posWindowController.setSpecificButtonInMikeGridPane(i, 3, "" + Math.sqrt((count * 79 + i + count)) % 56);
//            posWindowController.setSpecificButtonInMikeGridPane(i, 4, "" + Math.sqrt((count * 73 + i + count)) % 74);
//            posWindowController.setSpecificButtonInMikeGridPane(i, 5, "" + Math.sqrt((count * 987 + i + count)) % 34);
//            posWindowController.setSpecificButtonInMikeGridPane(i, 6, "" + Math.sqrt((count * 453 + i + count)) % 9);
//        }
        count++;
    }

    public void initializeGUI(Stage initialStage, PriceServer priceServer/*, MainModelThread mainModelThread*/) throws Exception{

        this.priceServer = priceServer;



        //create PrimaryGUIWindow

        FXMLLoader primaryGUIWindowLoader = new FXMLLoader(getClass().getResource("/MainGUIWindow.fxml"));
        Parent primaryGUIRoot = primaryGUIWindowLoader.load();
        ControllerPrimaryGUIWindow primaryGUIWindow = (ControllerPrimaryGUIWindow) primaryGUIWindowLoader.getController();
        initialStage.setScene(new Scene(primaryGUIRoot));
        initialStage.show();



        //create Price Control window:
        createPriceControlWindow();
//        FXMLLoader priceControlPanelLoader = new FXMLLoader(getClass().getResource("/PriceControlPanel.fxml"));
//        Parent pricePanelRoot =  priceControlPanelLoader.load(); //FXMLLoader.load(getClass().getResource("view/SceneBuilder/PriceControlPanel.fxml"));
//
//        //get the controller class:
//        priceControlPanel = (ControllerPriceControlPanel) priceControlPanelLoader.getController();
//        priceControlPanel.setPriceServer(priceServer);
//
//        //create the window:
//        Stage primaryStage = new Stage();
//        primaryStage.setTitle("Price Control");
//        primaryStage.setScene(new Scene(pricePanelRoot));
//        primaryStage.setX(850);
//        primaryStage.setY(0);
//        //display the window:
//        primaryStage.show();


        //create Positions Window:

        createPosWindow();
        createPosWindow();
        createPosWindow();
        createPosWindow();
        createPosWindow();

//        //we need to add custom MikeGridPane not defined in FXML:
//        MikePositionsWindowCreator posWindow = new MikePositionsWindowCreator(priceServer);
//        posWindowController = posWindow.getPositionsWindowController();
//
//        //create the window:
//        Stage secondStage = new Stage();
//        secondStage.setX(0);
//        secondStage.setY(0);
//        secondStage.setScene(new Scene(posWindow.getPositionsWindowRoot()));
//        //display the window:
//        secondStage.show();


        //display the windows:



    }

    private void createPosWindow(){
        //create Positions Window:
        //we need to add custom MikeGridPane not defined in FXML:
        MikePositionsWindowCreator posWindow = null;
        try {
            posWindow = new MikePositionsWindowCreator(priceServer);
        } catch (IOException e) {
            System.out.println("Exception in createPosWindow");
            e.printStackTrace();
        }
        posWindowController = posWindow.getPositionsWindowController();

        //add the controller to the list of controllers:
        posWindowControllerList.add(posWindowController);



        //create the window:
        Stage secondStage = new Stage();
        secondStage.setX(0);
        secondStage.setY(0);
        secondStage.setScene(new Scene(posWindow.getPositionsWindowRoot()));
        //display the window:
        secondStage.show();

        //name the window:
//        String name = "PositionsWindow";
        String name = ("PositionsWindow " + posWindowControllerList.size());
        secondStage.setTitle(name);


    }

    private void createPriceControlWindow(){
        //create Price Control window:
        FXMLLoader priceControlPanelLoader = new FXMLLoader(getClass().getResource("/PriceControlPanel.fxml"));
        Parent pricePanelRoot = null; //FXMLLoader.load(getClass().getResource("view/SceneBuilder/PriceControlPanel.fxml"));
        try {
            pricePanelRoot = priceControlPanelLoader.load();
        } catch (IOException e) {
            System.out.println("Exception in createPriceControlWindow");
            e.printStackTrace();
        }

        //get the controller class:
        priceControlPanel = (ControllerPriceControlPanel) priceControlPanelLoader.getController();
        priceControlPanel.setPriceServer(priceServer);



        //create the window:
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Price Control");
        primaryStage.setScene(new Scene(pricePanelRoot));
        primaryStage.setX(850);
        primaryStage.setY(0);
        //display the window:
        primaryStage.show();
    }
}