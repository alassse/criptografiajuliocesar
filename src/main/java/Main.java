import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String args[]) throws NoSuchAlgorithmException {
        RestTemplate restTemplate = new RestTemplate();

        String token = "67ea7c85779264bb2632d65807cc4a081b3f5e75";

        Object json = restTemplate.getForObject("https://api.codenation.dev/v1/challenge/dev-ps/generate-data?token=" + token,
                Object.class);

        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("answer", Decifrador.decifrarFrase(json));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);

        String serverUrl = "https://api.codenation.dev/v1/challenge/dev-ps/submit-solution?token=" + token;

        restTemplate.postForEntity(serverUrl, requestEntity, String.class);
    }
}