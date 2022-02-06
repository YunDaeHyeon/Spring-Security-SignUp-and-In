package test.DTO;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
public class UserDTO implements UserDetails {
    private int userNumber; // 사용자 식별번호ˆ
    private String userId; // 사용자 ID
    private String userPassword; // 사용자 PASSWORD
    private String userName; // 사용자 이름
    private String userAuth; // 사용자의 권한
    private String createdDate; // 계정이 생성된 날짜

    // 사용자의 권한을 Collection 형태로 반환한다.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.userAuth));
    }

    // 사용자의 Password를 반환한다.
    @Override
    public String getPassword() {
        return this.userPassword;
    }

    // 사용자의 ID를 반환한다. (Unique하기에 해당 프로젝트에서는 Id를 반환한다.)
    // Security에서 인증처리를 하기 위해 userId를 지정해주는 것.
    @Override
    public String getUsername() {
        return this.userId;
    }

    public String getUserName(){
        return this.userName;
    }

    // 계정의 만료 여부를 확인한다.
    @Override
    public boolean isAccountNonExpired() {
        return true; // true:만료되지 않음, false:만료됨
    }

    // 계정의 잠금 여부를 확인한다.
    @Override
    public boolean isAccountNonLocked() {
        return true; // true:잠기지 않음, false:잠김
    }

    // 비밀번호의 만료 여부를 확인한다.
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // true:만료되지 않음, false:만료됨
    }

    // 계정이 사용 가능한지 확인한다.
    @Override
    public boolean isEnabled() {
        return true; // true:사용 가능, false:사용 불가
    }
}
