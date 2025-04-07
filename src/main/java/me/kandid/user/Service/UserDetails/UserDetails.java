package me.kandid.user.Service.UserDetails;

import lombok.Data;
import me.kandid.user.Model.Customer.Customer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Data
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    Customer customer;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return customer.getPhone() + "";
    }

}
