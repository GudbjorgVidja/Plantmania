package vidmot.plantmania.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import vinnsla.plantmania.MinPlanta;
import vinnsla.plantmania.Notendaupplysingar;

import java.io.IOException;
import java.time.LocalDate;

//@JsonDeserialize(using = NotendaupplysingarDeserializer.class)
public class NotendaupplysingarDeserializer extends JsonDeserializer<Notendaupplysingar> {
    public NotendaupplysingarDeserializer() {

    }

    @Override
    public Notendaupplysingar deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
        Notendaupplysingar notendaupplysingar = new Notendaupplysingar();
        ObjectMapper objectMapper = (ObjectMapper) parser.getCodec();
        //objectMapper.registerModule(new SimpleModule().addDeserializer(MinPlanta.class, new MinPlantaDeserializer()));
        JsonNode node = objectMapper.readTree(parser);


        //deserializa minarPlontur
        JsonNode minarPlonturNodes = node.get("minarPlontur");
        ObservableList<MinPlanta> minarPlontur = FXCollections.observableArrayList();
        for (JsonNode minPlantaNode : minarPlonturNodes) {
            MinPlanta minPlanta = objectMapper.treeToValue(minPlantaNode, MinPlanta.class);
            minarPlontur.add(minPlanta);
        }
        notendaupplysingar.setMinarPlontur(minarPlontur);


        // Deserializa fyrriVokvanir
        ObservableList<Pair<MinPlanta, LocalDate>> fyrriVokvanir = FXCollections.observableArrayList();
        JsonNode fyrriVokvanirNodes = node.get("fyrriVokvanir");
        for (JsonNode pairNode : fyrriVokvanirNodes) {
            MinPlanta minPlanta = objectMapper.treeToValue(pairNode.get("key"), MinPlanta.class);
            Integer[] dagur = objectMapper.treeToValue(pairNode.get("value"), Integer[].class);
            LocalDate date = LocalDate.of(dagur[0], dagur[1], dagur[2]);
            fyrriVokvanir.add(new Pair<>(minPlanta, date));
        }
        notendaupplysingar.setFyrriVokvanir(fyrriVokvanir);

        // Deserialize naestuVokvanir
        ObservableList<Pair<MinPlanta, LocalDate>> naestuVokvanir = FXCollections.observableArrayList();
        JsonNode naestuVokvanirNodes = node.get("naestuVokvanir");
        for (JsonNode pairNode : naestuVokvanirNodes) {
            MinPlanta minPlanta = objectMapper.treeToValue(pairNode.get("key"), MinPlanta.class);
            Integer[] dagur = objectMapper.treeToValue(pairNode.get("value"), Integer[].class);
            LocalDate date = LocalDate.of(dagur[0], dagur[1], dagur[2]);
            naestuVokvanir.add(new Pair<>(minPlanta, date));
        }
        notendaupplysingar.setNaestuVokvanir(naestuVokvanir);

        return notendaupplysingar;
    }
}
