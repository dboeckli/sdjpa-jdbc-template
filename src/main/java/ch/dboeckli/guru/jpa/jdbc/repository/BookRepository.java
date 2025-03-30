package ch.dboeckli.guru.jpa.jdbc.repository;

import ch.dboeckli.guru.jpa.jdbc.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
