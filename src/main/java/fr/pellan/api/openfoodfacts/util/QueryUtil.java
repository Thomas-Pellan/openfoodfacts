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

@Slf4j
@Service
public class QueryUtil {

    private static final String USER_AGENT = "User-Agent-wrapper - Linux - 1.0";

    public String getDataAsString(String url){

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", USER_AGENT)
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new QueryUtilException(e.getMessage());
        }

        if(response == null || response.statusCode() != HttpStatus.SC_OK){
            log.error(String.format("QueryUtil : Got empty response or error response for the request on url %s", url));
            throw new QueryUtilException("QueryUtil : getData : error during query");
        }

        return response.body();
    }

    public InputStream getFileData(String url){

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
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new QueryUtilException(e.getMessage());
        }

        if(response == null || response.statusCode() != HttpStatus.SC_OK){
            log.error(String.format("QueryUtil : Got empty response or error response for the request on url %s", url));
            throw new QueryUtilException("QueryUtil : getFileData : error during query");
        }

        return response.body();
    }
}
