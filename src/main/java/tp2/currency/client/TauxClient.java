package tp2.currency.client;

import java.time.Duration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import tp2.commonException.ServiceIndisponibleException;
import tp2.currency.dto.TauxReponse;

import java.net.http.HttpClient;

@Component
public class TauxClient {


    private final RestClient restClient;

    public TauxClient() {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(httpClient);
        factory.setReadTimeout(Duration.ofSeconds(5));   // l'API répond trop lentement → échec

        this.restClient = RestClient.builder()
                .baseUrl("https://api.frankfurter.dev/v1")
                .requestFactory(factory)
                .build();
    }

    public Double getTaux(String from, String to) {
        System.out.println(">>> Appel à Frankfurter : " + from + " → " + to);
        try {
            TauxReponse reponse = restClient.get()
                    .uri("/latest?base={from}&symbols={to}", from, to)
                    .retrieve()
                    .body(TauxReponse.class);
            System.out.println(">>> Réponse reçue : " + reponse.getRates());
            return reponse.getRates().get(to);
        } catch (RestClientResponseException e) {
            if (e.getStatusCode().is4xxClientError()) {
                return null;                          // devise inconnue : inutile de réessayer
            }
            throw new ServiceIndisponibleException(); // erreur 5xx de l'API
        } catch (ResourceAccessException e) {
            throw new ServiceIndisponibleException(); // panne réseau ou timeout
        }
    }
}