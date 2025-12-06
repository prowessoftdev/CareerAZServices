package com.careeraz.services.Service;




import com.careeraz.services.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsImp implements UserDetailsService {
    private final UserRepository respo;

    public UserDetailsImp(UserRepository respo) {
        this.respo = respo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return (UserDetails) respo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not Found"));
    }
}
