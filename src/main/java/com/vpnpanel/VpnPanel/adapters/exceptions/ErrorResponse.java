package com.vpnpanel.VpnPanel.adapters.exceptions;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path,
    List<FieldErrorResponse> fieldErrors
) {}
