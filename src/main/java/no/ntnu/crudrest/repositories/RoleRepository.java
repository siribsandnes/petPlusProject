package no.ntnu.crudrest.repositories;

import no.ntnu.crudrest.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findOneByName(String name);
}
