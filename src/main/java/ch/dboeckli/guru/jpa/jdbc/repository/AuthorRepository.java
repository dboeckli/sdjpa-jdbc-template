package ch.dboeckli.guru.jpa.jdbc.repository;

import ch.dboeckli.guru.jpa.jdbc.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
