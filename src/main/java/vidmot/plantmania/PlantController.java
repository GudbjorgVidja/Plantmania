/**
 * hafa einhvers staðar lista af öllum plöntum, til að hafa auðveldari (og kannski hagkvæmari) aðgang að þeim á keyrslutíma
 */
package vidmot.plantmania;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import vinnsla.plantmania.LesaPlontur;
import vinnsla.plantmania.MinPlanta;
import vinnsla.plantmania.Notandi;
import vinnsla.plantmania.Planta;

public class PlantController {
    @FXML
    private Plontuyfirlit fxPlonturYfirlit; //mínar plöntur yfirlitið. Er eiginlega meira eins og allar plöntur yfirlit
    @FXML
    private Plontuyfirlit fxAllarPlonturYfirlit; //yfirlit yfir allar plöntur
    private UpphafController upphafController;
    private ObjectProperty<Notandi> skradurNotandi = new SimpleObjectProperty<>();

    private ObservableList<Planta> allarPlontur = FXCollections.observableArrayList();//er í vesi, geymi hér

    public void initialize() {
        upphafController = (UpphafController) ViewSwitcher.lookup(View.UPPHAFSSIDA);
        skradurNotandi.setValue(upphafController.getSkradurNotandi());

        System.out.println(skradurNotandi.get());
        geraBindings();


        allarPlontur.addAll((new LesaPlontur()).getPlontur());
        //System.out.println("Buid ad lesa inn allar plontur. Staerd lista: " + allarPlontur.size()); //virkar rétt
    }

    private void geraBindings() {
        Bindings.bindBidirectional(skradurNotandi, upphafController.skradurNotandiProperty());
    }


    /**
     * þegar smellt er, plönturnar úr plontur.txt sem MinPlantaSpjald hlutir
     */
    @FXML
    protected void fxBaetaVidHandler() {
        for (int i = 0; i < allarPlontur.size(); i++) {
            MinPlanta mp = new MinPlanta(allarPlontur.get(i));
            MinPlantaSpjald mps = new MinPlantaSpjald(mp);
            fxPlonturYfirlit.getFxFlowPane().getChildren().add(mps);
        }

        //allt hér fyrir neðan virkaði, en þetta er bara plöntuyfirlit, ekki MinarPlonturYfirlit
        /*
        Spjald spjald = new Spjald();
        fxPlonturYfirlit.getFxFlowPane().getChildren().add(spjald);


        LesaPlontur l = new LesaPlontur();
        List<Planta> plontur = l.getPlontur();

        PlantaSpjald spj = new PlantaSpjald(plontur.get(0));
        fxPlonturYfirlit.getFxFlowPane().getChildren().add(spj);

        spj = new PlantaSpjald(plontur.get(1));
        fxPlonturYfirlit.getFxFlowPane().getChildren().add(spj);


         */

    }

    @FXML
    private void hladaOllumPlontum() {
        for (Planta p : allarPlontur) {
            fxAllarPlonturYfirlit.baetaVidYfirlit(p);
        }


        /*
        AllarPlonturYfirlit a = new AllarPlonturYfirlit();
        System.out.println("hladaOllumPlontum handler");
        a.getBirtarAPlontur().addListener((ListChangeListener<? super PlantaSpjald>) change -> {
            change.next();
            fxAllarPlonturYfirlit.getFxFlowPane().getChildren().clear();
            fxAllarPlonturYfirlit.getFxFlowPane().getChildren().addAll(a.getBirtarAPlontur());
        });

         */

    }

    public void skraUt(ActionEvent actionEvent) {
        skradurNotandi = null;
        System.out.println("skra ut");
        ViewSwitcher.switchTo(View.UPPHAFSSIDA);
    }
}
