package org.graduate.service.repository;

import org.graduate.service.data.BookHoldingKind;
import org.graduate.service.data.BookHoldingKindPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BookHoldingKindRepo extends CrudRepository<BookHoldingKind, BookHoldingKindPK> {
    BookHoldingKind findBookHoldingKindByBookKindIdAndIsbn(@Param("bookKindId") Integer bookKindId,
                                                           @Param("isbn") Long isbn);
}
