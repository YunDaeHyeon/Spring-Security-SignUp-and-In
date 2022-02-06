package test.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import test.DTO.UserDTO;
import test.Mapper.UserMapper;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    @Autowired
    UserMapper userMapper;

    Date time = new Date();
    SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:sss");

    @Transactional
    public void saveUserData(UserDTO userDTO){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userDTO.setUserPassword(passwordEncoder.encode(userDTO.getUserPassword()));
        userDTO.setUserAuth("USER");
        userDTO.setCreatedDate(format.format(time).toString());
        userMapper.registerAction(userDTO);
    }

    // 유저의 계정을 비교하여 로그인을 인증한다.
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserDTO userDTO = userMapper.loginAction(userId);
        if(userDTO==null){
            throw new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다.");
        }
        return userDTO;
    }
}
