package com.example.quarkus.services;


import com.example.quarkus.consumer.services.TvMazeService;
import com.example.quarkus.model.TvMazeShow;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/v1/library")
public class ShowsLibraryService {

    @RestClient
    TvMazeService tvMazeService;

    @Inject
    private Logger log;

    /**
     *  Re-route the injected TvMazeService to a different baseURL when provided.
     * @param showName
     * @param baseURL
     * @return
     * @throws MalformedURLException
     */
    @GET
    public TvMazeShow getSimplifiedShow(@QueryParam("showName") String showName,
                                        @HeaderParam("baseURL") String baseURL) throws MalformedURLException {

        if(baseURL != null){
            tvMazeService =  RestClientBuilder.newBuilder()
                    .baseUrl(new URL(baseURL))
                    .build(TvMazeService.class);

            log.info("Request directed to custom baseURL  ----> " + baseURL);
        }

        return tvMazeService.searchShows(showName);
    }

}
