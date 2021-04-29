package org.graduate.book_service.repository;

import org.graduate.book_service.data.UserBookBrowse;
import org.graduate.book_service.data.UserBookBrowsePK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBookBrowseRepo
        extends CrudRepository<UserBookBrowse, UserBookBrowsePK> {
}
