package test.Mapper;

import org.apache.ibatis.annotations.Mapper;
import test.DTO.UserDTO;

@Mapper
public interface UserMapper {
    // 회원가입 처리
    void registerAction(UserDTO userDTO);

    // 로그인 처리
    UserDTO loginAction(String userId);
}
