package com.metrio.Metrio.dto;

public record ClientRequest(
        String clientName,
        String clientLogin,
        String clientPassword,
        String clientStatus
) {
}
