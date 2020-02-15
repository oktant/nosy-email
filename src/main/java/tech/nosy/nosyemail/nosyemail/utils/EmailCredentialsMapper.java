package tech.nosy.nosyemail.nosyemail.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import tech.nosy.nosyemail.nosyemail.dto.EmailCredentialDto;
import tech.nosy.nosyemail.nosyemail.dto.InputSystemDto;
import tech.nosy.nosyemail.nosyemail.model.EmailCredential;
import tech.nosy.nosyemail.nosyemail.model.InputSystem;

import java.util.List;
import java.util.Set;

@Mapper
public abstract class EmailCredentialsMapper {
    public static final EmailCredentialsMapper INSTANCE = Mappers.getMapper(EmailCredentialsMapper.class);
    @Mapping(source = "emailCredentialProfileName", target = "profileName")
    @Mapping(source = "emailCredentialUsername", target = "username")
    @Mapping(source = "emailCredentialPassword", target = "password")
    public abstract EmailCredentialDto toEmailCredentialDto(EmailCredential emailCredential);
    @Mapping(source = "username", target = "emailCredentialUsername")
    @Mapping(source = "password", target = "emailCredentialPassword")
    @Mapping(source = "profileName", target = "emailCredentialProfileName")
    public abstract EmailCredential toEmailCredential(EmailCredentialDto emailCredentialDto);

    public abstract Set<EmailCredentialDto> toEmailCredentialsDtoSet(Set<EmailCredential> emailCredentialSet);


}
