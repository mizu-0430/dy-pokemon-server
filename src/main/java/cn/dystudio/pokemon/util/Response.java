package cn.dystudio.pokemon.util;

import cn.dystudio.pokemon.controller.SyncController;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Response {

    /*
     * 200 success
     * 400 fail
     * 600 sync
     */

    public static String success() {
        return success(null);
    }

    public static String success(String tips) {
        return success(tips, null);
    }

    public static String success(String tips, JsonNode data) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("code", "200");
        if (tips != null) {
            node.put("tips", tips);
        }
        if (data != null) {
            node.replace("data", data);
        }
        return node.toString();
    }

    public static String fail() {
        return fail(null);
    }

    public static String fail(String tips) {
        return fail(tips, null);
    }

    public static String fail(String tips, JsonNode data) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("code", "400");
        if (tips != null) {
            node.put("tips", tips);
        }
        if (data != null) {
            node.replace("data", data);
        }
        return node.toString();
    }

    public static String sync() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("code", "600");
        node.put("isSync", SyncController.isSync);
        node.put("module", SyncController.syncModule);
        node.put("total", SyncController.totalSize);
        node.put("sync", SyncController.syncSize);
        return node.toString();
    }

}
