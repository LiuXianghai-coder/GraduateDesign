package org.graduate.service.repository;

import org.graduate.service.data_entity.JsContent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/7
 * Time: 下午3:43
 */

@Repository
public interface JsContentRepo extends CrudRepository<JsContent, Short> {
    @Query(value = "SELECT * FROM js_content ORDER BY content_id DESC LIMIT 1",
            nativeQuery = true)
    Optional<JsContent> findLastJsContent();
}
