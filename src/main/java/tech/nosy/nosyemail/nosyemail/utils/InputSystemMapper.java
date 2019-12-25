package tech.nosy.nosyemail.nosyemail.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import tech.nosy.nosyemail.nosyemail.dto.InputSystemDto;
import tech.nosy.nosyemail.nosyemail.model.InputSystem;

import java.util.Set;

@Mapper
public abstract class InputSystemMapper {

    public static final InputSystemMapper INSTANCE = Mappers.getMapper( InputSystemMapper.class );

            @Mapping(source = "inputSystemId", target = "id")
            @Mapping(source = "inputSystemName", target = "name")

    public abstract InputSystemDto toInputSystemDto(InputSystem inputSystem);
            @Mapping(source = "id", target = "inputSystemId")
            @Mapping(source = "name", target = "inputSystemName")
    public abstract InputSystem toInputSystem(InputSystemDto emailTemplateDto);

    public abstract Set<InputSystemDto> toInputSystemDtoSet(Set<InputSystem> inputSystemSet);

}
