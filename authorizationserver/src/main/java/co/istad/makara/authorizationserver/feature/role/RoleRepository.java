package co.istad.makara.authorizationserver.feature.role;



import co.istad.makara.authorizationserver.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}