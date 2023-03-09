package com.springboot.catdemo.utils;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.springboot.catdemo.entity.POIExcelDao;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * POI EXCEL工具类
 * @Author: can
 * @Description:
 * @Date: Create in 22:17 2022/3/8
 */
@Component
public class ExcelPoiUtil {

    /**
     * POI导出excel
     * @param list 导出的数据
     * @param titleMap 导出的标题key：value，数据库字段:标题名称
     * @param response
     * @throws Exception
     */
    public static void excelExport(String sheetName, List list, Map<String,String> titleMap, HttpServletResponse response) throws Exception {
        {
            // 通过工具类创建writer 写出到磁盘路径
            ExcelWriter writer = ExcelUtil.getWriter(true); // 是否为xlsx格式
            writer.renameSheet(sheetName); // 重命名当前sheet
            writer.setOnlyAlias(true); // 是否只保留别名中的字段值
            // 原标题:别名
            for (Map.Entry<String,String> entry : titleMap.entrySet()) {
                writer.addHeaderAlias(entry.getKey(), entry.getValue());
            }
            // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
            writer.write(list, true);
            writer.autoSizeColumnAll(); // 设置所有列为自动宽度，不考虑合并单元格
            // 设置浏览器响应格式
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            String fileName = URLEncoder.encode("用户信息","UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ".xlsx");
            ServletOutputStream out = response.getOutputStream();
            writer.flush(out, true);
            out.close();
            writer.close();
        }
    }

    /**
     * POI导入excel
     * @param inputStream
     * @param titleMap 导入的标题key：value，标题名称:数据库字段
     * @return 返回List<对象>
     */
    public static <T> POIExcelDao<T> importExcel(InputStream inputStream, HashMap<String, String> titleMap, Class<T> t ) {
        boolean isUpdate = false;
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 判断入库数据中是否存在ID标题(如果存在则为修改数据，如果不存在则为新增数据)
        Object result = reader.readRow(0).stream().filter(o -> "ID".equalsIgnoreCase(o.toString())).findAny().orElse(null);
        // 如果存在ID标题则为修改数据
        if (result != null && result.toString().toUpperCase().trim().equals("ID")) {
            titleMap.put("ID","id");
            isUpdate = true;
        }
        reader.setHeaderAlias(titleMap); // 设置标题行的别名Map
        reader.setIgnoreEmptyRow(true); // 是否忽略空行
        List<Map<String, Object>> userMaps = reader.readAll();
        List<T> userList = reader.readAll(t);
        POIExcelDao<T> poiExcelDao = new POIExcelDao(userList,isUpdate);
        return poiExcelDao;
    }
}
