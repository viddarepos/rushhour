package com.rushhour_app.infrastructure.mappers;

import com.rushhour_app.domain.client.entity.Client;
import com.rushhour_app.domain.client.model.ClientDTO;
import com.rushhour_app.domain.client.model.ClientResponseDTO;
import com.rushhour_app.domain.client.model.ClientUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class ClientMapper {

    private PasswordEncoder passwordEncoder;

    @Mapping(source = "account", target = "account")
    public abstract ClientDTO toClientDTO(Client client);

    @Mapping(source = "account.password", target = "account.password", qualifiedByName = "encodePassword")
    @Mapping(source = "account", target = "account")
    @Mapping(target = "account.role", ignore = true)
    public abstract Client toClient(ClientDTO clientDTO);

    @Mapping(source = "account", target = "account")
    public abstract ClientResponseDTO toClientResponseDTO(Client client);

    public abstract void updateEmployeeFromDto(ClientUpdateDTO client, @MappingTarget Client entity);

    @Named("encodePassword")
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder encoder) {
        this.passwordEncoder = encoder;
    }

}