package com.chatop.backend.mapper;


import com.chatop.backend.dto.message.MessageRequestDto;
import com.chatop.backend.entity.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    // Dto to entity
    Message messageRequestDtoToEntity(MessageRequestDto messageRequestDto);

}