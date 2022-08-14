package fr.pellan.api.openfoodfacts.util;

import fr.pellan.api.openfoodfacts.exception.QueryUtilException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Class handling http queries.
 */
@Slf4j
@Service
public class QueryUtil {

    private static final String USER_AGENT = "User-Agent-wrapper - Linux - 1.0";

    /**
     * Does a http get on the target url and sends back the response as a string.
     * @param url the target url to hit
     * @return the response sting
     * @throws QueryUtilException in case the request fails or does not get a 200 ok response
     */
    public String getDataAsString(String url) throws QueryUtilException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", USER_AGENT)
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new QueryUtilException(e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new QueryUtilException(e.getMessage());
        }

        if(response == null || response.statusCode() != HttpStatus.SC_OK){
            log.error(String.format("QueryUtil : Got empty response or error response for the request on url %s", url));
            throw new QueryUtilException("QueryUtil : getData : error during query");
        }

        return response.body();
    }

    /***
     * Does a http get on the target url and sends back the response body as an Input stream.
     * @param url the target url to hit
     * @return the body of the http response as an input stream
     * @throws QueryUtilException if the request fails in any way
     */
    public InputStream getFileData(String url) throws QueryUtilException{

        //Query and get the file from the API
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", USER_AGENT)
                .build();

        HttpResponse<InputStream> response;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofInputStream());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new QueryUtilException(e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new QueryUtilException(e.getMessage());
        }

        if(response == null || response.statusCode() != HttpStatus.SC_OK){
            log.error(String.format("QueryUtil : Got empty response or error response for the request on url %s", url));
            throw new QueryUtilException("QueryUtil : getFileData : error during query");
        }

        return response.body();
    }
}
