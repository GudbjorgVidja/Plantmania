package vinnsla.plantmania;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//sér klasi til að setja plöntur í skrá
public class AllarPlontur {
    private static final String filename = "target/classes/vinnsla/plantmania/allarPlontur.json";

    /**
     * Les hluti úr skrá og býr til java hluti
     */
    private void lesaUrSkra() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Gögnin sem mætti breyta t.d.
            List<Planta> plontur = objectMapper.readValue(
                    new File(filename),
                    new TypeReference<>() {
                    });
            System.out.println(plontur);
        } catch (IOException e) {
            System.out.println("skrá er ekki til " + e.getMessage());
        }
    }

    /**
     * Skrifar lista af hlutum í nýja skrá. Ef skráin er nú þegar til er bætt við hana
     */
    private void skrifaISkra() {
        System.out.println("skrifa i skra");
        ObjectMapper objectMapper = new ObjectMapper();

        List<Planta> plontur = new ArrayList<>();

        //hér eru viðfangsbreyturnar í réttri röð
        //String latnesktNafn, String almenntNafn, List<String> ollHeiti, String myndaslod, String texti, Uppruni uppruni, Ljosstyrkur ljosstyrkur, Eitrun eitrun, int kjorhitastig, Vatnsthorf vatnsthorf, int almennurTimiMilliVokvana, int ljosStundir, List<String> einkenniPlontu

        //bæta við hér
        plontur.add(new Planta());

        try {
            File file = new File(filename);
            if (file.createNewFile())
                objectMapper.writeValue(file, plontur);
            else {
                System.out.println("skráin er til");
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

}
