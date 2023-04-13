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


/**
 * Custom deserializer fyrir vinnsluklasann Notendaupplysingar. Held þetta sé rétt. Smá pæling því þetta stendur
 * í javadocs fyrir JsonDeserializer<T>:
 * Custom deserializers should usually not directly extend this class,
 * but instead extend com.fasterxml.jackson.databind.deser.std.StdDeserializer
 */
class NotendaupplysingarDeserializer extends JsonDeserializer<Notendaupplysingar> {//StdDeserializer<Notendaupplysingar> {

    public NotendaupplysingarDeserializer() {

    }

    @Override
    public Notendaupplysingar deserialize(JsonParser parser, DeserializationContext deserializationContext) throws
            IOException {
        Notendaupplysingar notendaupplysingar = new Notendaupplysingar();
        ObjectMapper objectMapper = (ObjectMapper) parser.getCodec();
        JsonNode node = objectMapper.readTree(parser);


        //deserializa minarPlontur


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
