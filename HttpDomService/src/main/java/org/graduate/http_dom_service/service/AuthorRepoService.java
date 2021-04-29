package org.graduate.http_dom_service.service;

import org.graduate.http_dom_service.entity.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/1/27
 * Time: 下午10:41
 */

@Service
public interface AuthorRepoService extends CrudRepository<Author, Long>{
}
