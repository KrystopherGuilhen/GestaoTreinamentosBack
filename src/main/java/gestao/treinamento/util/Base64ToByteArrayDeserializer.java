//package gestao.treinamento.util;
//
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JsonDeserializer;
//import com.fasterxml.jackson.databind.JsonNode;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Base64;
//import java.util.List;
//
//public class Base64ToByteArrayDeserializer extends JsonDeserializer<List<byte[]>> {
//    @Override
//    public List<byte[]> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
//        List<byte[]> result = new ArrayList<>();
//        JsonNode node = p.getCodec().readTree(p);
//
//        for (JsonNode element : node) {
//            String base64 = element.get("base64").asText();
//            // Remove o prefixo "data:application/pdf;base64," se existir
//            if (base64.startsWith("data:")) {
//                base64 = base64.substring(base64.indexOf(",") + 1);
//            }
//            result.add(Base64.getDecoder().decode(base64));
//        }
//
//        return result;
//    }
//}
//usei antes para decodificar o base64, porém agora não a necessidade devido ao ajuste no FRONT-END;