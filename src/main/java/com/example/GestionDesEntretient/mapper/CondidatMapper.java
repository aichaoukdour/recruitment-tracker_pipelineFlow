package com.example.GestionDesEntretient.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.GestionDesEntretient.dto.CondidatDto;
import com.example.GestionDesEntretient.entities.Condidat;

@Mapper(componentModel = "spring")
public interface CondidatMapper {

    @Mapping(target = "entretien", ignore = true)
    Condidat toEntity(CondidatDto dto);

    CondidatDto toDto(Condidat entity);

}
