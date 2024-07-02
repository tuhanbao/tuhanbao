package com.td.ca.web.util;

import com.td.ca.base.Constants;
import com.td.ca.base.util.exception.AppException;
import com.td.ca.base.util.io.excel.easy.EasyExcelUtil;
import com.td.ca.base.util.io.excel.easy.ExportType;
import com.td.ca.base.util.objutil.FileUtil;
import com.td.ca.base.util.objutil.StringUtil;
import com.td.ca.web.controller.helper.ResultBean;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

public class EasyImportExportUtil {
    public static <E> void doExport(HttpServletResponse response, String fileName, String sheetName, ExportType exportType, List<E> list) throws IOException {
        response.setContentType(exportType.getContentType());
        response.setCharacterEncoding(Constants.UTF_8);
        // 这里URLEncoder.encode可以防止中文乱码
        String fileNameOnUtf8 = URLEncoder.encode(fileName, Constants.UTF_8);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileNameOnUtf8 + exportType.getSuffix());
        EasyExcelUtil.writeSheet(response.getOutputStream(), sheetName, exportType, list);
    }

    public static void doExport(HttpServletResponse response, String fileName, ExportType exportType, byte[] data) throws IOException {
        response.setContentType(exportType.getContentType());
        response.setCharacterEncoding(Constants.UTF_8);
        String fileNameOnUtf8 = URLEncoder.encode(fileName, Constants.UTF_8);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileNameOnUtf8 + exportType.getSuffix());
        response.getOutputStream().write(data);
    }

    public static <E> List<E> doImport(MultipartFile file, Class<E> clazz) {
        return doImport(file, clazz, 0, 1);
    }

    public static <E> List<E> doImport(MultipartFile file, Class<E> clazz, int headRowNumber) {
        return doImport(file, clazz, 0, headRowNumber);
    }

    public static <E> List<E> doImport(MultipartFile file, Class<E> clazz, int sheetIndex, int headRowNumber) {

        try (InputStream in = file.getInputStream()) {
            return EasyExcelUtil.readSheet(in, sheetIndex, headRowNumber, getExportType(file), clazz);
        } catch (IOException e) {
            throw AppException.getAppException(e);
        }
    }

    public static <E> List<E> doImport(MultipartFile file, Class<E> clazz, String sheetName, int headRowNumber) {
        try (InputStream in = file.getInputStream()) {
            return EasyExcelUtil.readSheet(in, sheetName, headRowNumber, getExportType(file), clazz);
        } catch (IOException e) {
            throw AppException.getAppException(e);
        }
    }

    public static ExportType getExportType(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String suffix = FileUtil.getSuffix(fileName);
        for (ExportType exportType : ExportType.values()) {
            if (StringUtil.isEqualIgnoreCase(exportType.getSuffix(), suffix)) {
                return exportType;
            }
        }
        return ExportType.XLSX;
    }

}
