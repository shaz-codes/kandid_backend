package me.kandid.user.Service.UserDetails;

import me.kandid.user.Model.Customer.Customer;
import me.kandid.user.Repository.Customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        Customer customer = customerRepository.getCustomerByPhone(Long.parseLong(phone));
        if (customer == null) {
            throw new UsernameNotFoundException(phone);
        }
        me.kandid.user.Service.UserDetails.UserDetails UserDetails = new me.kandid.user.Service.UserDetails.UserDetails();
        UserDetails.setCustomer(customer);
        return UserDetails;
    }
}
