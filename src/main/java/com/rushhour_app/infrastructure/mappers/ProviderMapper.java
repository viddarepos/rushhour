package com.rushhour_app.infrastructure.mappers;

import com.rushhour_app.domain.provider.entity.Provider;
import com.rushhour_app.domain.provider.model.ProviderDTO;
import com.rushhour_app.domain.provider.model.ProviderResponseDTO;
import com.rushhour_app.domain.provider.model.ProviderUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface ProviderMapper {

    ProviderDTO toProviderDTO(Provider provider);

    Provider toProviderFromDTO(ProviderDTO provider);

    ProviderResponseDTO toProviderResponseDTO(Provider provider);

    void updateProviderFromDto(ProviderUpdateDTO providerDTO, @MappingTarget Provider entity);

}