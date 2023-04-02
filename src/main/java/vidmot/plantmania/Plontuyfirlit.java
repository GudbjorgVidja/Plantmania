/**
 * Sérhæfður klasi fyrir yfirlit yfir mínar plöntur og allar plöntur. Á að vera tómt, en svo hlutum bætt í flowpane.
 */
package vidmot.plantmania;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;

public class Plontuyfirlit extends AnchorPane {
    @FXML
    private FlowPane fxFlowPane; //aðgangur í flowpane sem inniheldur spjöldin

    @FXML
    private Label notandiLabel;//label í efra hægra horni með notendanafni og icon

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
        fxFlowPane.getChildren().add(new Spjald());
        fxFlowPane.getChildren().add(new Spjald());
        fxFlowPane.getChildren().add(new Spjald());

        //setNotandiLabel("Lengra nafn");

    }


    //      ** getterar og setterar **

    public FlowPane getFxFlowPane() {
        return fxFlowPane;
    }

    //nær í notendanafn af label
    public String getNotandiLabel() {
        return notandiLabel.getText();
    }

    public void setNotandiLabel(String nafn) {
        notandiLabel.setText(nafn);
    }

}
