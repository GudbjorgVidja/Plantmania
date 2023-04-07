package vidmot.plantmania;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import vinnsla.plantmania.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;


//@JsonDeserialize(using = MinPlantaDeserializer.class)
// notum MinPlantaDeserializer til að lesa í klasann í staðinn fyrir sjálfgefn

public class MinPlantaDeserializer extends JsonDeserializer<MinPlanta> {
    public MinPlantaDeserializer() {

    }

    @Override
    public MinPlanta deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper objectMapper = (ObjectMapper) parser.getCodec();
        JsonNode node = objectMapper.readTree(parser);
        // lesum tréð - node er rótin


        MinPlanta minPlanta = new MinPlanta();


        //venjuleg gildi
        minPlanta.setLatnesktNafn(node.get("latnesktNafn").asText());
        minPlanta.setAlmenntNafn(node.get("almenntNafn").asText());
        minPlanta.setMyndaslod(node.get("myndaslod").asText());
        minPlanta.setTexti(node.get("texti").asText());
        minPlanta.setKjorhitastig(node.get("kjorhitastig").asInt());
        minPlanta.setAlmennurTimiMilliVokvana(node.get("almennurTimiMilliVokvana").asInt());
        minPlanta.setLjosStundir(node.get("ljosStundir").asInt());
        minPlanta.setNickName(node.get("nickName").asText());
        minPlanta.setNotesFraNotanda(node.get("notesFraNotanda").asText());
        minPlanta.setMedaltimiMilliVokvana(node.get("medaltimiMilliVokvana").asInt());
        minPlanta.setThinnTimiMilliVokvana(node.get("thinnTimiMilliVokvana").asInt());
        minPlanta.setNaestaVokvun(node.get("naestaVokvun").asInt());

        //og LocalDate (var objectProperty). Er þetta rétt?
        //minPlanta.setSidastaVokvun(objectMapper.readValue(node.toString(), LocalDate.class));
        Integer[] dagar = objectMapper.treeToValue(node.get("sidastaVokvun"), Integer[].class);
        minPlanta.setSidastaVokvun(LocalDate.of(dagar[0], dagar[1], dagar[2]));

        //og enum. er þetta rétt?
        //minPlanta.setUppruni(objectMapper.readValue(node.get("uppruni")));
        minPlanta.setUppruni(Uppruni.valueOf(node.get("uppruni").asText()));
        minPlanta.setLjosstyrkur(Ljosstyrkur.valueOf(node.get("ljosstyrkur").asText()));
        minPlanta.setEitrun(Eitrun.valueOf(node.get("eitrun").asText()));
        minPlanta.setVatnsthorf(Vatnsthorf.valueOf(node.get("vatnsthorf").asText()));

        //og venjulegir listar. hvernig er þetta?
        minPlanta.setOllHeiti(Arrays.asList(objectMapper.treeToValue(node.get("ollHeiti"), String[].class)));
        minPlanta.setEinkenniPlontu(Arrays.asList(objectMapper.treeToValue(node.get("einkenniPlontu"), String[].class)));


        //observableList fyrir vökvanir
        JsonNode vokvanirNodes = node.get("vokvanir");
        ObservableList<LocalDate> vokvanir = FXCollections.observableArrayList();

        for (JsonNode dateNode : vokvanirNodes) {
            Integer[] dates = objectMapper.treeToValue(node.get(dateNode.asText()), Integer[].class);
            vokvanir.add(LocalDate.of(dates[0], dates[1], dates[2]));
        }
        minPlanta.setVokvanir(vokvanir);


        //observableList fyrir flokka
        JsonNode flokkarNodes = node.get("vokvanir");
        ObservableList<String> flokkar = FXCollections.observableArrayList();

        for (JsonNode flokkurNode : flokkarNodes) {
            String flokkur = flokkurNode.get("flokkar").asText();
            flokkar.add(flokkur);
        }
        minPlanta.setFlokkar(flokkar);


        //Planta planta;//taka út!


        return minPlanta;
    }
}
