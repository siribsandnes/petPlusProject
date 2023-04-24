package no.ntnu.crudrest.repositories;

import no.ntnu.crudrest.models.User;
import no.ntnu.crudrest.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUser(User user);
}
