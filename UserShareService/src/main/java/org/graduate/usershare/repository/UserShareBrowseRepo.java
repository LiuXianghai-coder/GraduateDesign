package org.graduate.usershare.repository;

import org.graduate.usershare.data.UserShareBrowse;
import org.graduate.usershare.data.UserShareBrowsePK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserShareBrowseRepo
        extends CrudRepository<UserShareBrowse, UserShareBrowsePK> {
}
