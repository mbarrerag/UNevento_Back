package com.unevento.api.domain.records;

import com.unevento.api.domain.modelo.Estado;

public record AsistToEvents(Long idusuario, Estado estado, Long eventoid) {
}
