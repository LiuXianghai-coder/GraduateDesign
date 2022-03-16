package org.graduate.service.repository;

import org.graduate.service.data.CreatedBook;
import org.graduate.service.data.CreatedBookPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CreatedBookRepository extends CrudRepository<CreatedBook, CreatedBookPK> {
    Optional<CreatedBook> findCreatedBookPKByAuthorIdAndIsbn(@Param("authorId") Long authorId,
                                                            @Param("isbn") Long isbn);
}
