package com.example.quarkus.model;


import java.util.List;

public record TvMazeShow(String id, String url, String name, String type, String language, List<String> genres,
                         Integer runtime, Integer averageRunTime, String status) {}
