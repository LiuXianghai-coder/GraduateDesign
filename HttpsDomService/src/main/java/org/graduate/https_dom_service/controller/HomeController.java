package org.graduate.https_dom_service.controller;

import org.graduate.https_dom_service.entity.MyData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;

import static org.graduate.https_dom_service.tools.FileHelper.readFromFile;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/1/23
 * Time: 上午11:53
 */
@Controller
@RequestMapping(path = "")
public class HomeController {
    private final File htmlFile;

    public HomeController(MyData myData) {
        this.htmlFile = new File(myData.getPath() + myData.getHtmlFileName());
    }

    @GetMapping(path = "/data")
    public String data(Model model) throws IOException {
        model.addAttribute("content", readFromFile(htmlFile));

        return "data";
    }
}
