package co.istad.makara.categoryservice.repository;

import co.istad.makara.categoryservice.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Category Repository
 * Handles all database operations for Category entity
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}