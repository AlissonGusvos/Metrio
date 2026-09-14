package com.metrio.Metrio.dto;

public record ResultsRequest(
        int clicks,
        double adSpent,
        int leads,
        int reach,
        int views
) {
}
