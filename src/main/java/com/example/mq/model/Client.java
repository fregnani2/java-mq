package com.example.mq.model;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Class responsible for the structure of the client entity, mapped to the table "client".
 *  * Annotations:
 *  * @AllArgsConstructor: Generates a constructor with 1 parameter for each field in your class. Fields are initialized in the order they are declared.
 *  * @NoArgsConstructor: Generates a no-argument constructor.
 *  * @Getter: Generates getters.
 *  * @Setter: Generates setters.
 *  * @EqualsAndHashCode: Generates hashCode and equals implementations from the id field.
 *  * @Entity: Specifies that the class is an entity and is mapped to a database table.
 *  * @Table: Specifies the name of the database table to be used for mapping.
 *  */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "client")
public class Client implements UserDetails, Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private Integer accountNumber;
    @Column(nullable = false, columnDefinition = "double default 0")
    private Double balance;
    private ClientRole role;
    private String password;
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Method to get the authorities of the client
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == ClientRole.ADMIN){
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_CLIENT"));
        }
        else {
            return List.of(new SimpleGrantedAuthority("ROLE_CLIENT"));
        }
    }

    /**
     * Method to get the username of the client
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Method to check if the account of the client is not expired
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Method to check if the account of the client is not locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Method to check if the credentials of the client are not expired
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Method to check if the client is enabled
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
