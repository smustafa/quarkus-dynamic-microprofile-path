package com.example.quarkus.consumer.services;

import com.example.quarkus.model.TvMazeShow;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@RegisterRestClient
public interface TvMazeService {

    @GET
    @Path("/singlesearch/shows")
    public TvMazeShow searchShows(@QueryParam("q") String query);
}
