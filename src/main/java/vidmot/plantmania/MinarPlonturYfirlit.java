/**
 * sorry
 */
package vidmot.plantmania;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import vinnsla.plantmania.MinPlanta;

public class MinarPlonturYfirlit extends Plontuyfirlit {
    private MenuBar fxMenuBar;

    private Menu siaMenu;

    private ObservableList<CheckMenuItem> menuItemsObsListi = FXCollections.observableArrayList();

    private ObservableList<MinPlanta> allarMinarPlontur = FXCollections.observableArrayList();//allar plöntur notanda

    private ObservableList<MinPlanta> birtarPlontur = FXCollections.observableArrayList();//plöntur sýndar í yfirliti


    public MinarPlonturYfirlit() {
        fxMenuBar = getFxMenuBar();
        ObservableList<Menu> menuListi = FXCollections.observableArrayList(fxMenuBar.getMenus());

        for (int i = 0; i < menuListi.size(); i++) { //nær í sía menu
            if (menuListi.get(i).getText().equals("sía")) siaMenu = menuListi.get(i);
        }

        //setSiaMenuItems();
    }


    /**
     * Mismunandi leiðir til að raða:
     * sjálfgefið er tími í næstu vökvun
     * fræðiheiti A-Ö
     * nickname A-Ö
     */
    private void radaSpjoldum() {
        
    }

    /**
     * setur gefna flokka undir Sia menu.
     * Les inn hvaða flokkar eru skráðir á notandann, setur þá alla á listann
     */
    private void setSiaMenuItems() {
        //ekki víst að allir upprunalegu flokkarnir séu notaðir
        //siaMenu.getItems().clear(); //kannski ekki taka nr 0 og 1? allt og seperator?

        //getSiaItemsList().clear();

        //ná í alla flokka og setja í lista
        ObservableList<String> flokkar = FXCollections.observableArrayList("fl1", "fl2", "fl3", "fl4");

        //gera menuItem fyrir hvern flokk
        for (int i = 0; i < flokkar.size(); i++) {
            CheckMenuItem item = new CheckMenuItem(flokkar.get(i));
            //siaMenu.getItems().add(item);

            //getSiaItemsList().add(item);
        }
    }


    private void test() {
        System.out.println(fxMenuBar.getMenus());
        //[Menu@36a7a485[styleClass=[menu-item, menu]], Menu@2f60d524[styleClass=[menu-item, menu]], Menu[id=menu, styleClass=[menu-item, menu, yfirMenu]]]

        ObservableList<Menu> menuListi = FXCollections.observableArrayList(fxMenuBar.getMenus());
        for (int i = 0; i < menuListi.size(); i++) {
            System.out.println(menuListi.get(i).getText());
        }
        //prentar texta á menus í menubar
    }

}
