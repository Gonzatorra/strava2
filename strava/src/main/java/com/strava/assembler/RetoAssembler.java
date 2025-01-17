package com.strava.assembler;

import com.strava.DTO.*;
import com.strava.dominio.*;

public class RetoAssembler {
    public static RetoDTO toDTO(Reto reto) {
        RetoDTO dto = new RetoDTO();
        dto.setId(reto.getId());
        dto.setDeporte(reto.getDeporte());
        dto.setUsuarioCreador(reto.getUsuarioCreador());
        dto.setNombre(reto.getNombre());
        dto.setFecIni(reto.getFecIni());
        dto.setFecFin(reto.getFecFin());
        dto.setObjetivoDistancia(reto.getObjetivoDistancia());
        dto.setObjetivoTiempo(reto.getObjetivoTiempo());
        dto.setParticipantes(reto.getParticipantes());
        return dto;
    }

    public static Reto toDomain(RetoDTO retoDTO) {
        Reto reto = new Reto();
        reto.setId(retoDTO.getId());
        reto.setDeporte(retoDTO.getDeporte());
        reto.setUsuarioCreador(retoDTO.getUsuarioCreador());
        reto.setNombre(retoDTO.getNombre());
        reto.setFecIni(retoDTO.getFecIni());
        reto.setFecFin(retoDTO.getFecFin());
        reto.setObjetivoDistancia(retoDTO.getObjetivoDistancia());
        reto.setObjetivoTiempo(retoDTO.getObjetivoTiempo());
        reto.setParticipantes(retoDTO.getParticipantes());
        return reto;
    }

}