import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Decifrador {
    public static JSONObject decifrarFrase(Object json) throws NoSuchAlgorithmException {
        char[] list = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        Stream<Character> myStreamOfCharacters = IntStream
                .range(0, list.length)
                .mapToObj(i -> list[i]);

        List<Character> alfabeto = new LinkedList<>();
        alfabeto.addAll(myStreamOfCharacters.collect(Collectors.toList()));

        JSONObject objetoJson;
        Gson gson = new Gson();
        objetoJson = new JSONObject(gson.toJson(json));

        //get from json
        char[] teste = objetoJson.get("cifrado").toString().toCharArray();
        int qtdCasas = (int)objetoJson.get("numero_casas");
        String decifrado = "";
        int cont = 0;

        //lógica para decifrar a mensagem
        for (int i = 0; i < teste.length; i++) {
            cont = 0;
            if (alfabeto.contains(teste[i])) {
                cont = alfabeto.indexOf(teste[i]) - qtdCasas;

                if (cont < 0) {
                    cont = alfabeto.indexOf(((LinkedList<Character>) alfabeto).getLast()) - (Math.abs(cont) - 1);
                }

                decifrado = decifrado + alfabeto.get(cont).toString();
                continue;
            }
            decifrado = decifrado + teste[i];
        }

        //atualiza json com informação decifrada
        objetoJson.put("decifrado", decifrado);

        //sha1 decifrado para resumo_criptografico
        objetoJson.put("resumo_criptografico", criptografarSHA1(decifrado));

        return objetoJson;
    }

    public static String criptografarSHA1(String mensagem) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");

        // digest() method is called
        // to calculate message digest of the input string
        // returned as array of byte
        byte[] messageDigest = md.digest(mensagem.getBytes());

        // Convert byte array into signum representation
        BigInteger no = new BigInteger(1, messageDigest);

        // Convert message digest into hex value
        String hashtext = no.toString(16);

        // Add preceding 0s to make it 32 bit
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }

        return hashtext;
    }
}
