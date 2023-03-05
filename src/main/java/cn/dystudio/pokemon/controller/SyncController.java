package cn.dystudio.pokemon.controller;

import cn.dystudio.pokemon.util.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sync")
public class SyncController {

    public static boolean isSync = false;
    public static String syncModule = "";
    public static Integer totalSize = 0;
    public static Integer syncSize = 0;

    @GetMapping("/check")
    public String check() {
        try {
            return Response.sync();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.fail();
        }
    }

}
