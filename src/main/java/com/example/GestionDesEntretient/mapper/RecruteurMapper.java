package com.example.GestionDesEntretient.mapper;

import com.example.GestionDesEntretient.dto.RecruteurDto;
import com.example.GestionDesEntretient.entities.Recruteur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecruteurMapper {

    @Mapping(target = "entretien", ignore = true)
    Recruteur toEntity(RecruteurDto dto);

    RecruteurDto toDto(Recruteur entity);
}
