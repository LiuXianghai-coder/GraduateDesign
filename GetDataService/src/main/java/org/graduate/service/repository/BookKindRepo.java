package org.graduate.service.repository;

import org.graduate.service.data.BookKind;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BookKindRepo extends CrudRepository<BookKind, Integer> {
    BookKind findBookKindByBookKindName(@Param("kindName") String kindName);
}
