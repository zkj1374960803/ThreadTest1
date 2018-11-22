package com.ccbuluo.business.platform.carparts.service;

import com.ccbuluo.business.constants.BusinessPropertyHolder;
import com.ccbuluo.core.entity.OSSClientProperties;
import com.ccbuluo.core.exception.CommonException;
import com.ccbuluo.core.thrift.annotation.ThriftRPCClient;
import com.ccbuluo.db.Page;
import com.ccbuluo.http.StatusDto;
import com.ccbuluo.http.StatusDtoThriftPage;
import com.ccbuluo.http.StatusDtoThriftUtils;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.BasicCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.dto.QueryCarpartsProductDTO;
import com.ccbuluo.merchandiseintf.carparts.parts.service.CarpartsProductService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ${todo}
 *
 * @author liuduo
 * @version v1.0.0
 * @date 2018-11-21 15:59:06
 */
@Service
public class ExportCarpartsServiceImpl implements ExportCarpartsService{

    @ThriftRPCClient("BasicMerchandiseSer")
    private CarpartsProductService carpartsProductService;
    @Resource(name = "carpartsProductPriceServiceImpl")
    private CarpartsProductPriceService carpartsProductServiceImpl;
    @Autowired
    private OSSClientProperties ossClientProperties;
    private HSSFWorkbook workbook = new HSSFWorkbook();
    private HSSFSheet sheet = workbook.createSheet();

    /**
     * 导出零配件
     * @param resp
     * @author liuduo
     * @date 2018-11-21 16:41:15
     */
    @Override
    public void exportCarparts(HttpServletResponse resp) throws IOException {
        // 查询所有零配件
        StatusDtoThriftPage<BasicCarpartsProductDTO> basicCarpartsProductDTOStatusDtoThriftPage = carpartsProductService.queryCarpartsProductList(null, 0, Integer.MAX_VALUE);
        StatusDto<Page<BasicCarpartsProductDTO>> list = StatusDtoThriftUtils.resolve(basicCarpartsProductDTOStatusDtoThriftPage, BasicCarpartsProductDTO.class);
        if (null == list.getData()) {
            throw new CommonException("0", "没有查询到零配件!");
        }
        // 查询零配件的价格
        QueryCarpartsProductDTO queryCarpartsProductDTO = new QueryCarpartsProductDTO();
        queryCarpartsProductDTO.setOffset(0);
        queryCarpartsProductDTO.setPageSize(Integer.MAX_VALUE);
        StatusDto<Page<BasicCarpartsProductDTO>> pageStatusDto = carpartsProductServiceImpl.queryCarpartsProductPriceList(queryCarpartsProductDTO);
        Page<BasicCarpartsProductDTO> data = pageStatusDto.getData();
        List<BasicCarpartsProductDTO> rows = data.getRows();
        Map<String, BasicCarpartsProductDTO> carpartsProductMap = rows.stream().collect(Collectors.toMap(BasicCarpartsProductDTO::getCarpartsCode, Function.identity()));
        Page<BasicCarpartsProductDTO> data1 = list.getData();
        List<BasicCarpartsProductDTO> exportData = data1.getRows();
        for (BasicCarpartsProductDTO exportDatum : exportData) {
            BasicCarpartsProductDTO basicCarpartsProductDTO = carpartsProductMap.get(exportDatum.getCarpartsCode());
            exportDatum.setServerCarpartsPrice(basicCarpartsProductDTO.getServerCarpartsPrice());
            exportDatum.setCarpartsPrice(basicCarpartsProductDTO.getCarpartsPrice());
            exportDatum.setCustCarpartsPrice(basicCarpartsProductDTO.getCustCarpartsPrice());
        }
        // 设置表头
        darwRow(0, new String[] { "序号", "件号", "名称", "计量单位","单车用量","图片","适用车型","服务中心价格","客户经理价格","用户销售价格" }, null, null);
        // 填充数据
        for (int i = 1; i <= exportData.size(); i++) {
            BasicCarpartsProductDTO carpartsProduct = exportData.get(i - 1);
            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 5, i, (short) 5,i);
            anchor.setAnchorType(0);
            darwRow(i , new String[] { String.valueOf(i), getObject(carpartsProduct.getCarpartsMarkno()), getObject(carpartsProduct.getCarpartsName())
                , getObject(carpartsProduct.getCarpartsUnit()),getObject(carpartsProduct.getUsedAmount()), ""
                ,getObject(carpartsProduct.getCarmodelName()),getObject(carpartsProduct.getServerCarpartsPrice()),getObject(carpartsProduct.getCustCarpartsPrice())
                ,getObject(carpartsProduct.getCarpartsPrice()) }, anchor, carpartsProduct.getCarpartsImage());
        }
        build(resp);
    }

    /**
     * 校验数据是否为空
     * @param object 需要校验是否为空的数据
     * @return 校验过后数据
     * @author liuduo
     * @date 2018-11-21 16:42:37
     */
    private String getObject(Object object) {
        return object == null ? "" : object.toString();
    }

    /**
     * 填充excel数据
     * @param rowIndex 行号
     * @param values 零配件信息
     * @param anchor 写图片的来源
     * @param image 图片路径
     * @author liuduo
     * @date 2018-11-21 16:44:13
     */
    public int darwRow(int rowIndex, String[] values, HSSFClientAnchor anchor, String image) throws MalformedURLException {
        HSSFRow row = sheet.createRow(rowIndex);
        HSSFCellStyle style = workbook.createCellStyle();//设置列样式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
        style.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        row.setHeight((short) 1520);
        if (StringUtils.isBlank(image)) {
            for (int i = 0; i < values.length; i++) {
                sheet.setColumnWidth((short) i, (short) 4500);
                HSSFCell cell = row.createCell(i);
                style.setWrapText(true);
                cell.setCellStyle(style);
                cell.setCellValue(values[i]);
            }
        } else {
            HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
            String s = BusinessPropertyHolder.getPathPrefix(BusinessPropertyHolder.FILE_PATH, ossClientProperties.getEndpoint()) + image;
            URL url = new URL(s);
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            try {
                BufferedImage bufferImg = ImageIO.read(url);
                String file = url.getFile();
                if (file.toUpperCase().endsWith("PNG")) {
                    ImageIO.write(bufferImg, "png", byteArrayOut);
                } else if (file.toUpperCase().endsWith("JPEG")) {
                    ImageIO.write(bufferImg, "jpeg", byteArrayOut);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < values.length; i++) {
                sheet.setColumnWidth((short) i, (short) 4500);
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(style);
                if (i == 5 && anchor != null) {
                    patriarch.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                }
                cell.setCellValue(values[i]);
            }
        }
        return rowIndex++;
    }


    /**
     * 导出excel
     * @author liuduo
     * @date 2018-11-21 16:45:06
     */
    public void build(HttpServletResponse resp) {
        try {
            ServletOutputStream out = null;
            resp.setContentType("application/x-msdownload");
            resp.addHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode("零配件信息.xls", "UTF-8") + "\"");
            out = resp.getOutputStream();
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
