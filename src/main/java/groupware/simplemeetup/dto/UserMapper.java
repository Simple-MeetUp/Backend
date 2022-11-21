package groupware.simplemeetup.dto;

import groupware.simplemeetup.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {


    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User requestToUser(SignUpRequest signUpRequest);

    UserResponse userToResponse(User user);
}
