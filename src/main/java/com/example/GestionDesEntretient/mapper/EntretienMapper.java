package com.example.GestionDesEntretient.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.example.GestionDesEntretient.dto.EntretienRequestDTO;
import com.example.GestionDesEntretient.dto.EntretienResponseDTO;
import com.example.GestionDesEntretient.entities.Entretien;



@Mapper(componentModel = "spring", uses = {CondidatMapper.class, RecruteurMapper.class})
public interface EntretienMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "condidat", ignore = true)
    @Mapping(target = "recruteur", ignore = true)
    @Mapping(target = "statut", ignore = true)
    Entretien toEntity(EntretienRequestDTO dto);
    
    EntretienResponseDTO toDto(Entretien entity);
}