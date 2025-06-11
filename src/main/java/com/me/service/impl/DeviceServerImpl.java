package com.me.service.impl;

import com.me.dao.DeviceDao;
import com.me.entity.Device;
import com.me.service.DeviceServer;
import com.me.utils.Message;
import com.me.utils.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class DeviceServerImpl implements DeviceServer {
    private final DeviceDao deviceDao;
    @Override
    public Result<String> uploadDevice(MultipartFile file) {
        List<Device> deviceList = new ArrayList<>();
        Workbook workbook = null;
        try {
            InputStream inputStream = file.getInputStream();
            workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }
                Device device = new Device();
                Cell elderIdCell = row.getCell(0);
                if (elderIdCell != null) {
                    Integer elderId = getIntegerCellValue(elderIdCell);
                    if (elderId == null) {
                        continue;
                    }
                    device.setElderId(elderId);
                }
                Cell deviceIdCell = row.getCell(1);
                if (deviceIdCell != null) {
                    String deviceId = getStringCellValue(deviceIdCell);
                    if (deviceId == null || deviceId.trim().isEmpty()) {
                        continue;
                    }
                    device.setDeviceId(deviceId);
                }
                Cell deviceNameCell = row.getCell(2);
                if (deviceNameCell != null) {
                    String deviceName = getStringCellValue(deviceNameCell);
                    if (deviceName == null || deviceName.trim().isEmpty()) {
                        continue;
                    }
                    device.setDeviceName(deviceName);
                }
                deviceList.add(device);
            }

            if (!deviceList.isEmpty()) {
                deviceDao.batchInsertDevice(deviceList);
            }
            return Result.success(Message.UPLOAD_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(Message.UPLOAD_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(Message.UPLOAD_ERROR);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Result<List<Device>> findDevice(Integer id) {
        return Result.success(Message.SUCCESS,deviceDao.findByElderId(id));
    }


    /**
     * 获取单元格的整数值
     */
    private Integer getIntegerCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        CellType cellType = cell.getCellType();
        switch (cellType) {
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            case STRING:
                try {
                    return Integer.parseInt(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    return null;
                }
            default:
                return null;
        }
    }

    /**
     * 获取单元格的字符串值
     */
    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        CellType cellType = cell.getCellType();
        switch (cellType) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }
}
