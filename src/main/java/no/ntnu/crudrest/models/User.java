package no.ntnu.crudrest.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;


@Entity(name = "users")
@Schema(description = "Represents a user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id of the user")
    private Long id;
    @Schema(description = "The username of the user")
    private String username;
    @Schema(description = "The password of the user")
    private String password;


    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @Schema(description = "Flag indicating if the user is active")
    private boolean active = true;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Schema(description = "The roles assigned to the user")
    private Set<Role> roles = new LinkedHashSet<>();

    /**
     * Empty constructor needed for JPA
     */
    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, Address address) {
        this.username = username;
        this.password = password;
        this.address = address;
    }

    //GETTERS AND SETTERS

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Role> getRoles() {
        return roles;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Add a role to the user
     *
     * @param role Role to add
     */
    public void addRole(Role role) {
        roles.add(role);
    }

    /**
     * Check if this user is an admin
     *
     * @return True if the user has admin role, false otherwise
     */
    public boolean isAdmin() {
        return this.hasRole("ROLE_ADMIN");
    }

    /**
     * Check if the user has a specified role
     *
     * @param roleName Name of the role
     * @return True if hte user has the role, false otherwise.
     */
    public boolean hasRole(String roleName) {
        boolean found = false;
        Iterator<Role> it = roles.iterator();
        while (!found && it.hasNext()) {
            Role role = it.next();
            if (role.getName().equals(roleName)) {
                found = true;
            }
        }
        return found;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
