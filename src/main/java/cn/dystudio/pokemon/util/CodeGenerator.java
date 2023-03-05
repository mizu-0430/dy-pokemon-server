package cn.dystudio.pokemon.util;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;
import java.util.Scanner;

public class CodeGenerator {

    /**
     * 读取控制台内容
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入" + tip + "：");
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {

        // 数据源
        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(
                "jdbc:mysql://119.45.235.64:7100/pokemon",
                "root",
                "Admin!@12qwaszx")
                .typeConvert(new MySqlTypeConvert())
                .build();

        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig.Builder()
                .outputDir(System.getProperty("user.dir") + "/src/main/java")
                .author("张永清")
                .dateType(DateType.SQL_PACK)
                .commentDate("yyyy-MM-dd")
                .disableOpenDir()
                .build();

        // 包配置
        PackageConfig packageConfig = new PackageConfig.Builder()
                .parent("cn.dystudio.pokemon")
                .entity("entity")
                .service("service")
                .serviceImpl("service.impl")
                .mapper("mapper")
                .xml("mapper.xml")
                .controller("controller")
                .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "/src/main/resources/mappers"))
                .build();

        // 模版配置
        TemplateConfig templateConfig = new TemplateConfig.Builder()
                .disable(TemplateType.CONTROLLER)
                .entity("/templates/entity.java")
                .service("/templates/service.java")
                .serviceImpl("/templates/serviceImpl.java")
                .mapper("/templates/mapper.java")
                .xml("/templates/mapper.xml")
                .build();

        // 策略配置
        StrategyConfig strategyConfig = new StrategyConfig.Builder()
                .addInclude(scanner("表名"))
                .entityBuilder()
                .enableLombok()
                .disableSerialVersionUID()
                .serviceBuilder()
                .formatServiceFileName("%sService")
                .mapperBuilder()
                .mapperAnnotation(org.apache.ibatis.annotations.Mapper.class)
                .build();

        AutoGenerator autoGenerator = new AutoGenerator(dataSourceConfig);
        autoGenerator.global(globalConfig);
        autoGenerator.packageInfo(packageConfig);
        autoGenerator.template(templateConfig);
        autoGenerator.strategy(strategyConfig);
        autoGenerator.execute(new FreemarkerTemplateEngine());

    }

}
