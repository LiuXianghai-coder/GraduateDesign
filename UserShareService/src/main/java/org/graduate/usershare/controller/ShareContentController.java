package org.graduate.usershare.controller;

import lombok.extern.slf4j.Slf4j;
import org.graduate.usershare.data.UserShare;
import org.graduate.usershare.data.UserShareCollections;
import org.graduate.usershare.data.UserShareComment;
import org.graduate.usershare.data.UserShareStar;
import org.graduate.usershare.entity.ShareEntity;
import org.graduate.usershare.entity.UserShareContent;
import org.graduate.usershare.repository.UserCollectReactRepo;
import org.graduate.usershare.repository.UserShareCommReactRepo;
import org.graduate.usershare.repository.UserShareReactRepo;
import org.graduate.usershare.repository.UserShareStarReactRepo;
import org.graduate.usershare.tools.TimeTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : LiuXianghai on 2021/3/9
 * @Created : 2021/03/09 - 10:59
 * @Project : usershare
 */
@Slf4j
@Controller
@RequestMapping(path = "/shareContent")
@CrossOrigin(origins = "*")
public class ShareContentController {
    private final UserShareReactRepo userShareReactRepo;

    private final UserCollectReactRepo userCollectReactRepo;

    private final UserShareStarReactRepo userShareStarReactRepo;

    private final UserShareCommReactRepo userShareCommReactRepo;

    @Autowired
    public ShareContentController(UserShareReactRepo userShareReactRepo,
                                  UserCollectReactRepo userCollectReactRepo,
                                  UserShareStarReactRepo userShareStarReactRepo,
                                  UserShareCommReactRepo userShareCommReactRepo) {
        this.userShareReactRepo     = userShareReactRepo;
        this.userCollectReactRepo   = userCollectReactRepo;
        this.userShareStarReactRepo = userShareStarReactRepo;
        this.userShareCommReactRepo = userShareCommReactRepo;
    }

    @GetMapping(path = "/content/{shareId}")
    public String content(@PathVariable(name = "shareId") Long shareId, Model model) {
        /*
            这里是作为 Android 的 WebView 显示给用户
         */
        Optional<UserShare> share = userShareReactRepo
                .findUserSharesByShareId(shareId);
        share.ifPresent(userShare -> model.addAttribute("content", userShare.getShareContent()));

        return "content";
    }

    /**
     * 保存用户上传的动态共享信息
     *
     * @param shareContent ： 用户上传的共享信息内容
     * @param userId       ： 上传这个动态信息的用户的 ID
     * @return ： 处理上传内容的结果
     */
    @Deprecated
    @PostMapping("/saveShareContent")
    public @ResponseBody
    HttpStatus saveShareContent(@RequestBody String shareContent,
                                @RequestParam(name = "userId") Long userId) {
        log.info("ShareContent: " + shareContent);

        synchronized (ShareContentController.class) {
            long shareCount = userShareReactRepo.count();

            UserShare userShare = new UserShare();
            userShare.setUserId(userId);
            userShare.setShareId(++shareCount);
            userShare.setShareDate(TimeTools.currentTime());
            userShare.setShareContent(shareContent);
            userShare.setShareHeader(getShareImageSrc(shareContent));

            log.debug("UserShare: " + userShare.toString());

            userShareReactRepo.save(userShare);
        }

        return HttpStatus.OK;
    }

    @PostMapping(path = "/saveUserShare")
    public @ResponseBody
    HttpStatus saveUserShare(@RequestBody UserShare userShare) {
        userShare.setShareDate(TimeTools.currentTime());

        userShareReactRepo.save(userShare);

        return HttpStatus.OK;
    }

    /**
     * 保存用户的动态信息
     * @param userShareContent ：带保存的用户动态内容
     * @return ： 处理结果， 成功则返回 Http 状态码 200
     */
    @PostMapping(path = "/saveUserShareContent")
    public @ResponseBody
    HttpStatus saveUserShareContent(@RequestBody UserShareContent userShareContent) {
        synchronized (ShareContentController.class) {
            long shareCount = userShareReactRepo.count();

            UserShare userShare = new UserShare();
            userShare.setUserId(userShareContent.getUserId());
            userShare.setShareId(++shareCount);
            userShare.setShareDate(TimeTools.currentTime());
            userShare.setShareContent(userShareContent.getShareContent());
            userShare.setShareHeader(getShareImageSrc(userShareContent.getShareContent()));

            userShareReactRepo.save(userShare);
        }

        return HttpStatus.OK;
    }

    /***
     * 修改用户对于对应的用户动态的收藏信息。
     * @param shareId ： 对应的动态 ID
     * @param userId ： 对应的用户 ID
     * @return ： 处理结果， 成功返回 Http 状态码 200， 否则返回 Http 状态码 202
     */
    @PostMapping(path = "/updateCollections/{shareId}/{userId}")
    public @ResponseBody
    HttpStatus updateCollections(@PathVariable(name = "shareId") Long shareId,
                                 @PathVariable(name = "userId") Long userId) {
        Optional<UserShareCollections> obj = userCollectReactRepo
                .findUserShareCollectionsByShareIdAndUserId(shareId, userId);
        if (obj.isPresent()) {
            UserShareCollections collections = obj.get();
            collections.setCollectionDate(TimeTools.currentTime());
            collections.setFlag(!collections.getFlag());

            userCollectReactRepo.save(collections);
            return HttpStatus.OK;
        }

        return HttpStatus.ACCEPTED;
    }

    /**
     * 更新用户对于指定动态的点赞状态
     * @param shareId ： 点赞的动态 ID
     * @param userId ： 点赞的用户 ID
     * @return ： 处理结果， 成功则返回 Http 状态码 200， 否则返回 Http 状态码 202
     */
    @PostMapping(path = "/updateStar/{shareId}/{userId}")
    public @ResponseBody
    HttpStatus updateStar(@PathVariable(name = "shareId") Long shareId,
                          @PathVariable(name = "userId") Long userId) {
        Optional<UserShareStar> obj = userShareStarReactRepo
                .findUserShareStarByUserIdAndShareId(shareId, userId);

        if (obj.isPresent()) {
            UserShareStar userShareStar = obj.get();
            userShareStar.setStarDate(TimeTools.currentTime());
            userShareStar.setStarFlag(!userShareStar.getStarFlag());

            userShareStarReactRepo.save(userShareStar);

            return HttpStatus.OK;
        }

        return HttpStatus.ACCEPTED;
    }

    /**
     * 增加用户对于指定动态的评论信息
     * @param shareId ： 对应的动态 ID
     * @param userId ： 对应的用户 Id
     * @param commentContent ： 评论内容
     * @return ： 处理结果， 成功则返回 HTTP状态码 200
     */
    @PostMapping(path = "/addComment/{shareId}/{userId}")
    public @ResponseBody synchronized
    HttpStatus addComment(@PathVariable(name = "shareId") Long shareId,
                          @PathVariable(name = "userId") Long userId,
                          @RequestBody String commentContent) {
        Integer count = userShareCommReactRepo
                .countUserShareCommentByUserIdAndShareId(userId, shareId);

        UserShareComment obj = new UserShareComment();
        obj.setUserId(userId);
        obj.setShareId(shareId);
        obj.setCommentId(++count);
        obj.setCommentContent(commentContent);
        obj.setCommentDate(TimeTools.currentTime());

        userShareCommReactRepo.save(obj);

        return HttpStatus.OK;
    }

    /**
     * 通过输入的副文本内容得到第一个图片内容，如果没有则返回 null
     *
     * @param shareContent ： 传入的动态内容
     * @return ： 得到的第一个图片内容的地址链接， 如果没有则返回 null
     */
    private static String getShareImageSrc(@NonNull String shareContent) {
        Pattern imageTagPattern = Pattern.compile("<img[^>]+>");
        Matcher matcher = imageTagPattern.matcher(shareContent);

        String imagTag;
        if (matcher.find()) {
            imagTag = matcher.group();

            Pattern imageSrcPattern = Pattern.compile("src=\"(http[^\"]+)\"");
            Matcher matcher1 = imageSrcPattern.matcher(imagTag);

            if (matcher1.find())
                return matcher1.group(1);
            return null;
        }

        return "";
    }
}
