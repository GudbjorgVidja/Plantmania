package vidmot.plantmania;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import vinnsla.plantmania.Planta;

import java.io.IOException;

public class PlantaSpjald extends AnchorPane {
    @FXML
    private AnchorPane fxBreytilegtSvaedi;//laga nafn, en er anchorPane sem inniheldur stats, til að geta breytt því og sýnt takka

    @FXML
    private ImageView fxPlontuMynd;

    @FXML
    private Label fxAlmenntNafn, fxFlokkur;

    private Planta planta;//Planta vinnsluhluturinn

    public PlantaSpjald() {
        /*
        //setja controller og rót
        FXMLLoader loader = new FXMLLoader(getClass().getResource("planta-view.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

         */


    }

    public PlantaSpjald(Planta p) {
        planta = p;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("planta-view.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        //planta = p;

        //System.out.println("Slodin: " + planta.getMyndaslod());
        //fxPlontuMynd.setImage(new Image("src/resources/vidmot/plantmania/styling/plants/" + planta.getMyndaslod()));//mynd á spjaldinu rétt?

        //Image image = new Image("styling/plants/" + planta.getMyndaslod(), true);
        //Image image = new Image("styling/plants/" + planta.getMyndaslod());
        //Image image = new Image("styling/plants/monstera.png");
        //Image image = new Image("plants/monstera.png");
        //Image image = new Image("src/resources/vidmot/plantmania/styling/plants/monstera.png");

        //Image image = new Image(getClass().getResourceAsStream("styling/plants/monstera.png"));//virkar!!!!!!!

        //Image image = new Image("styling/plants/" + planta.getMyndaslod());
        //System.out.println("image var gert");
        
        fxPlontuMynd.setImage(new Image(getClass().getResourceAsStream("styling/plants/" + planta.getMyndaslod())));//mynd á spjaldinu rétt?
        //fxPlontuMynd.setImage(image);//mynd á spjaldinu rétt?

        fxAlmenntNafn.setText(planta.getAlmenntNafn());

        fxFlokkur.setText(" " + planta.getUppruni() + " ");
    }

    public Planta getPlanta() {
        return planta;
    }
}
