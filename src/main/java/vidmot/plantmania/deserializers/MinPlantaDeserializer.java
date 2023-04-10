package vidmot.plantmania.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import vinnsla.plantmania.MinPlanta;
import vinnsla.plantmania.enums.Eitrun;
import vinnsla.plantmania.enums.Ljosstyrkur;
import vinnsla.plantmania.enums.Uppruni;
import vinnsla.plantmania.enums.Vatnsthorf;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;


//@JsonDeserialize(using = MinPlantaDeserializer.class)
// notum MinPlantaDeserializer til að lesa í klasann í staðinn fyrir sjálfgefinn?

public class MinPlantaDeserializer extends JsonDeserializer<MinPlanta> {
    public MinPlantaDeserializer() {

    }

    @Override
    public MinPlanta deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper objectMapper = (ObjectMapper) parser.getCodec();
        JsonNode node = objectMapper.readTree(parser);
        MinPlanta minPlanta = new MinPlanta();


        //venjuleg gildi
        minPlanta.setLatnesktNafn(node.get("latnesktNafn").asText());
        minPlanta.setAlmenntNafn(node.get("almenntNafn").asText());
        minPlanta.setMyndaslod(node.get("myndaslod").asText());
        minPlanta.setTexti(node.get("texti").asText());
        minPlanta.setAlmennurTimiMilliVokvana(node.get("almennurTimiMilliVokvana").asInt());
        minPlanta.setLjosStundir(node.get("ljosStundir").asInt());
        minPlanta.setNickName(node.get("nickName").asText());
        minPlanta.setNotesFraNotanda(node.get("notesFraNotanda").asText());
        minPlanta.setMedaltimiMilliVokvana(node.get("medaltimiMilliVokvana").asInt());
        minPlanta.setThinnTimiMilliVokvana(node.get("thinnTimiMilliVokvana").asInt());
        minPlanta.setNaestaVokvun(node.get("naestaVokvun").asInt());

        //og LocalDate (var objectProperty). Er þetta rétt?
        //minPlanta.setSidastaVokvun(objectMapper.readValue(node.toString(), LocalDate.class));
        //Integer[] dagar = objectMapper.treeToValue(node.get("sidastaVokvun"), Integer[].class);
        //minPlanta.setSidastaVokvun(LocalDate.of(dagar[0], dagar[1], dagar[2]));
        minPlanta.setSidastaVokvun(
                objectMapper.readValue(node.get("sidastaVokvun").traverse(), LocalDate.class)
        );

        //og enum. er þetta rétt?
        minPlanta.setUppruni(Uppruni.valueOf(node.get("uppruni").asText()));
        minPlanta.setLjosstyrkur(Ljosstyrkur.valueOf(node.get("ljosstyrkur").asText()));
        minPlanta.setEitrun(Eitrun.valueOf(node.get("eitrun").asText()));
        minPlanta.setVatnsthorf(Vatnsthorf.valueOf(node.get("vatnsthorf").asText()));

        //og venjulegir listar. hvernig er þetta?
        minPlanta.setOllHeiti(Arrays.asList(objectMapper.treeToValue(node.get("ollHeiti"), String[].class)));
        minPlanta.setEinkenniPlontu(Arrays.asList(objectMapper.treeToValue(node.get("einkenniPlontu"), String[].class)));
        minPlanta.setKjorhitastig(Arrays.asList(objectMapper.treeToValue(node.get("kjorhitastig"), Integer[].class)));//


        //observableList fyrir vökvanir
        JsonNode vokvanirNodes = node.get("vokvanir");
        ObservableList<LocalDate> vokvanir = FXCollections.observableArrayList();

        for (JsonNode dateNode : vokvanirNodes) {
            //Integer[] dates = objectMapper.treeToValue(node.get(dateNode.asText()), Integer[].class);
            //vokvanir.add(LocalDate.of(dates[0], dates[1], dates[2]));

            vokvanir.add(objectMapper.readValue(node.get("sidastaVokvun").traverse(), LocalDate.class));
        }
        minPlanta.setVokvanir(vokvanir);

        return minPlanta;
    }
}
