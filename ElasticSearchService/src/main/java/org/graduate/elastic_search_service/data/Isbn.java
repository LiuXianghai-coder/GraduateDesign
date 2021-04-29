package org.graduate.elastic_search_service.data;

import lombok.Data;

/**
 * 书籍的 ISBN 数据对象， 这里是为了通过作者的 ID 来查找书籍所准备的。
 * 由于一个作者会对应多本书籍，同时一本书籍有可能会对应多个作者，
 * 因此， 在通过作者 ID 查找书籍时，需要首先通过作者的 ID 查出对应的书籍的 ISBN 列表，
 * 然后在通过 ISBN 通过已有的方法找到对应的书籍。
 *
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/2/12
 * Time: 下午1:26
 */
@Data
public class Isbn {
}
