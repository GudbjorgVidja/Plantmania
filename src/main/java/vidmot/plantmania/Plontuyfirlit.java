/**
 * Sérhæfður klasi fyrir yfirlit yfir mínar plöntur og allar plöntur. Á að vera tómt, en svo hlutum bætt í flowpane.
 */
package vidmot.plantmania;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;

public class Plontuyfirlit extends AnchorPane {
    @FXML
    private FlowPane fxFlowPane; //aðgangur í flowpane sem inniheldur spjöldin

    public Plontuyfirlit() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("plontuyfirlit.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        //fjöldi barna sem flowpane hefur
        //System.out.println(fxFlowPane.getChildren().size());

 
    }

    public FlowPane getFxFlowPane() {
        return fxFlowPane;
    }

}
