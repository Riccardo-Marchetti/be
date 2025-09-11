package piastrellista.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import piastrellista.entities.Blog;

public interface BlogDAO extends JpaRepository<Blog, Long> {

}
