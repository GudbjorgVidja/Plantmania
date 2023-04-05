/**
 * Sérhæfði klasinn spjald, grunnur fyrir plöntuspjöldin
 * Þetta er kannski meira bara test
 * <p>
 * eða, spjald gæti verið grunnurinn með tómt anchorPane neðst.
 */
package vidmot.plantmania;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import vinnsla.plantmania.MinPlanta;
import vinnsla.plantmania.Planta;

import java.io.IOException;

public class Spjald extends VBox {
    @FXML
    private Label fxFlokkur, fxAlmenntNafn;

    @FXML
    private ImageView fxPlontuMynd;

    //private Planta plontuhlutur=null;

    private Planta plant;

    private ObjectProperty<Planta> plontuproperty = new SimpleObjectProperty<>();

    public Spjald() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("spjald-view.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /*
    public Spjald() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("spjald-view.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }


        plontuproperty.addListener(change -> {
            setTilviksbreytur();
        });
        //setjaTilviksbreytur();

    }

     */

    /*
    public Spjald(Planta planta) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("spjald-view.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        plontuhlutur = planta;
        setjaTilviksbreytur();
    }

    public Spjald(MinPlanta planta) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("spjald-view.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        plontuhlutur = planta.getPlanta();
        setjaTilviksbreytur();
    }

    private void setjaTilviksbreytur() {//gera bara bind á þetta allt
        fxFlokkur.setText(plontuhlutur.getUppruni().toString().toLowerCase(Locale.ROOT));
        fxAlmenntNafn.setText(plontuhlutur.getAlmenntNafn());
        fxPlontuMynd.setImage(new Image(getClass().getResourceAsStream("styling/plants/" + plontuhlutur.getMyndaslod())));//mynd á spjaldinu rétt?
    }

     */

    public Spjald(Planta p) {//virkar allavega fyrir stakt spjald (ekki á öðru spjaldi)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("spjald-view.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        //TODO gera spjald með p

        /*
        fxFlokkur.setText("flokkur");
        fxAlmenntNafn.setText("almennt nafn");
        fxPlontuMynd.setImage(new Image(getClass().getResourceAsStream("styling/plants/monstera.png")));
         */

        fxFlokkur.setText(p.getUppruni().toString());
        fxAlmenntNafn.setText(p.getAlmenntNafn());
        fxPlontuMynd.setImage(new Image(getClass().getResourceAsStream("styling/plants/" + p.getMyndaslod())));

    }

    public Spjald(MinPlanta p) { //virkar ekki
        FXMLLoader loader = new FXMLLoader(getClass().getResource("spjald-view.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        plontuproperty.set(p);
        setTilviksbreytur();
    }

    private void setTilviksbreytur() {
        fxFlokkur.setText("flokkurinn");
        System.out.println("buid ad setja texta flokks");
        plontuproperty.get();
        System.out.println("plontuproperty.get();");
        System.out.println("");
        plontuproperty.get().getUppruni();
        System.out.println("plontuproperty.get().getUppruni();");
        plontuproperty.get().getUppruni().getMynd();
        System.out.println("plontuproperty.get().getUppruni().getMynd();");
        //fxFlokkur.setText(plontuproperty.get().getUppruni().toString().toLowerCase(Locale.ROOT));
        if (plontuproperty.get().getUppruni().getMynd() == null) System.out.println("null aftur");
        else System.out.println(plontuproperty.get().getUppruni().getMynd());
        fxFlokkur.setText(plontuproperty.get().getUppruni().toString());

        fxAlmenntNafn.setText(plontuproperty.get().getAlmenntNafn());
        fxPlontuMynd.setImage(new Image(getClass().getResourceAsStream("styling/plants/" + plontuproperty.get().getMyndaslod())));
    }

    public void setPlontuproperty(Planta p) {
        System.out.println("setPlontuProperty");
        plontuproperty.set(p);
    }

    public void setFxFlokkur(String nafn) {
        fxFlokkur.setText(nafn);
    }

    public void setFxAlmenntNafn(String nafn) {
        fxAlmenntNafn.setText(nafn);
    }

    public void setFxPlontuMynd(String hlekkur) {
        fxPlontuMynd.setImage(new Image(getClass().getResourceAsStream("styling/plants/" + hlekkur)));
    }
}
