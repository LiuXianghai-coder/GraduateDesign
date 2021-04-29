package org.graduate.usershare.controller;

import org.graduate.usershare.data.*;
import org.graduate.usershare.entity.ShareEntity;
import org.graduate.usershare.repository.*;
import org.graduate.usershare.single.UserShareCollectSingle;
import org.graduate.usershare.single.UserShareStarSingle;
import org.graduate.usershare.tools.TimeTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 用户动态信息的显示信息控制器，注意这里
 *
 * @author : LiuXianghai on 2021/3/9
 * @Created : 2021/03/09 - 22:17
 * @Project : elastic_search_service
 */
@RestController
@RequestMapping(path = "/userShare")
@CrossOrigin(origins = "*")
public class UserShareController {
    private final static String updateShareSql = "SELECT update_user_share(?)";

    @Resource(name = "defaultPage")
    private Pageable defaultPageable;

    private final UserShareBrowseRepo userShareBrowseRepo;

    private final UserShareCommReactRepo userShareCommReactRepo;

    private final UserShareReactRepo userShareRepo;

    private final UserShareStarReactRepo userShareStarRepo;

    private final UserCollectReactRepo userShareCollectRepo;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserShareController(UserShareReactRepo userShareRepo,
                               UserShareStarReactRepo userShareStarRepo,
                               UserCollectReactRepo userShareCollectRepo,
                               UserShareBrowseRepo userShareBrowseRepo,
                               UserShareCommReactRepo userShareCommReactRepo,
                               JdbcTemplate jdbcTemplate) {
        this.userShareRepo = userShareRepo;
        this.userShareStarRepo = userShareStarRepo;
        this.userShareCollectRepo = userShareCollectRepo;
        this.userShareBrowseRepo = userShareBrowseRepo;
        this.userShareCommReactRepo = userShareCommReactRepo;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 得到默认的一页的用户动态基础信息列表
     * @param userId ： 当前访问的用户 ID， 这个是进行排序和检索的基石，
     *               也是界面显示的一个重要因素
     * @return ：得到的基础用户动态信息列表
     */
    @GetMapping(path = "/defaultUserShares/{userId}")
    public List<ShareEntity> defaultUserShares(@PathVariable(name = "userId") Long userId) {
        List<UserShare> userShares = userShareRepo.findDefault();

        List<ShareEntity> shareEntities = new ArrayList<>();

        for (UserShare userShare: userShares) {
            long shareId = userShare.getShareId();

            // 更新每个动态的最新点赞数和评论数、收藏信息。
            jdbcTemplate.execute(updateShareSql.replaceAll("\\?", String.valueOf(shareId)));

            ShareEntity obj = new ShareEntity();
            obj.setShareId(shareId);
            obj.setShareHead(userShare.getShareHeader());
            obj.setStarNum(userShare.getStarNum());
            obj.setCommentNum(userShare.getCommentNum());
            obj.setShareDate(userShare.getShareDate());
            obj.setCollectionNum(userShare.getCollectionNum());
            obj.setImageUrl(userShare.getShareHeader());
            obj.setStar(userShareStarRepo.countOfUserIdAndShareId(userId, shareId) > 0);
            obj.setCollection(userShareCollectRepo.countOfUserIdAndShareId(userId, shareId) > 0);

            shareEntities.add(obj);
        }

        return shareEntities;
    }

    /**
     * 保存用户对于相关动态的浏览信息
     * @param userId ： 访问动态的用户 ID
     * @param shareId ： 访问动态的动态 ID
     * @return ： 处理成功则返回 Http 状态码 200
     */
    @GetMapping(path = "/shareBrowse/{userId}/{shareId}")
    public HttpStatus shareBrowse(@PathVariable(name = "userId") Long userId,
                                  @PathVariable(name = "shareId") Long shareId) {
        UserShareBrowse obj = new UserShareBrowse();
        obj.setUserId(userId);
        obj.setShareId(shareId);
        obj.setBrowseTime(TimeTools.currentTime());

        userShareBrowseRepo.save(obj);

        return HttpStatus.OK;
    }

    /**
     * 更新用户对于动态的收藏信息
     * @param userId ： 访问的用户 id
     * @param shareId ： 要改变收藏状态的动态 ID
     * @return ： 得到的改变后的 UserShare 对象，如果处理失败，则返回一个新的 UserShare 对象
     */
    @GetMapping(path = "/updateShareCollection/{userId}/{shareId}")
    public UserShareCollections updateShareCollection(@PathVariable(name = "userId") Long userId,
                                                      @PathVariable(name = "shareId") Long shareId) {
        Optional<UserShareCollections> optional = userShareCollectRepo
                .findUserShareCollectionsByShareIdAndUserId(shareId, userId);

        if (optional.isEmpty()) return UserShareCollectSingle.DEFAULT_INSTANCE.getObj();

        UserShareCollections obj = optional.get();
        obj.setFlag(!obj.getFlag());

        userShareCollectRepo.save(obj);

        return obj;
    }

    /**
     * 更新用户对于相关动态的点赞信息
     * @param userId ： 访问当前动态的用户 ID
     * @param shareId ： 要改变点赞状态的动态 ID
     * @return ： 改变后的 UserShareStar 对象，失败则返回默认的初始 UserShareStar 对象
     */
    @GetMapping(path = "/updateShareStar/{userId}/{shareId}")
    public UserShareStar updateShareStar(@PathVariable(name = "userId") Long userId,
                                         @PathVariable(name = "shareId") Long shareId) {
       Optional<UserShareStar> optional = userShareStarRepo
               .findUserShareStarByUserIdAndShareId(userId, shareId);

       if (optional.isEmpty()) return UserShareStarSingle.DEFAULT_INSTANCE.getObj();

       UserShareStar obj = optional.get();
       obj.setStarFlag(!obj.getStarFlag());

       userShareStarRepo.save(obj);

       return obj;
    }

    /**
     * 保存用户对于相关动态的评论信息
     * @param userId ： 添加评论的用户 ID
     * @param shareId ： 添加评论的动态 ID
     * @param content ： 添加的评论内容
     * @return ： 处理结果，成功则返回 Http 状态码 200
     */
    @PostMapping(path = "/addShareComment/{userId}/{shareId}")
    public HttpStatus addShareComment(@PathVariable(name = "userId") Long userId,
                                      @PathVariable(name = "shareId") Long shareId,
                                      @RequestBody String content) {
        Integer commentId = userShareCommReactRepo
                .countUserShareCommentByUserIdAndShareId(userId, shareId);

        UserShareComment obj = new UserShareComment();
        obj.setUserId(userId);
        obj.setShareId(shareId);
        obj.setCommentId(++commentId);
        obj.setCommentContent(content);
        obj.setCommentDate(TimeTools.currentTime());

        userShareCommReactRepo.save(obj);

        return HttpStatus.OK;
    }
}
