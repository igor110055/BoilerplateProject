package com.lgcns.bizactor.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SimpleTimeZone;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import dataset.converter.DotNetXmlDataSetConverter;
import running.exception.RunningServerBizException;
import server.agent.ServerAgent;

@WebServlet(name = "bizarest", urlPatterns = { "/bizarest_v2", "/bizarest_v2/*" })
public class BizActorJSONServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String ACT_ID = "actID";

    private static final String IN_DATATABLE_NAMES = "inDTName";

    private static final String OUT_DATATABLE_NAMES = "outDTName";

    private static final String REF_DATASET = "refDS";

    private static final String SUCCESS = "SUCCESS";

    private static final String BIZEXCEPTION = "BIZEXCEPTION";

    private static final String FAILED = "FAILED";

    private static final String PATTERN_ACT_ID_ERR = ".*Service (.*) Not Exist.*";

    private static final String PATTERN_INPUT_ERR = ".*This service (.*) need Input Parameter Value.*";

    private static final String PATTERN_OUTPUT_ERR = ".*Final Return Data Failed.*";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long beforeTime = System.currentTimeMillis(); // ?????? ?????? ?????? ?????? ????????????
        request.setCharacterEncoding("UTF-8");
        StringBuilder sb = new StringBuilder();
        System.out.println("doPost==1");
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                sb.append(line);
        } catch (Exception e) {
            System.out.println("doPost--exception==1");
            e.printStackTrace();
            throw new ServletException(e.getMessage());
        }
        String jsonStr = sb.toString();
        System.out.println(jsonStr);
        System.out.println("doPost==1-1");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(Include.NON_NULL);
            objectMapper.setSerializationInclusion(Include.NON_EMPTY);
            Map<String, Object> map = new HashMap<>();
            map = objectMapper.readValue(jsonStr, new TypeReference<Map<String, Object>>() {
    
            });
            System.out.println("doPost==1-2");
            String actID = null;
            String pathInfo = request.getPathInfo();
            if (pathInfo != null && pathInfo.length() > 1) {
                actID = pathInfo.substring(1, pathInfo.length());
            } else {
                actID = (String) map.get("actID");
            }
            System.out.println("doPost==1-3");
            String inDTName = (String) map.get("inDTName");
            String outDTName = (String) map.get("outDTName");
            String _id = null;
            /*{
                "inDTName":"IN_PSET,LSESSION",
                "refDS":{"IN_PSET":[{}],"LSESSION":[{"USER_NO":"0"}]},
                "actID":"BR_CM_GRP_CD_FIND",
                "_id":"92d3b0a5-cb7d-4cfc-9a3e-2bf2bdd0ad40",
                "outDTName":"OUT_DATA"
            }
            */
            System.out.println("doPost==1-4");
            if (map.get("_id") != null) {
                _id = (String) map.get("_id");
            } else {
                UUID one = UUID.randomUUID();
                _id = one.toString();
            }
            System.out.println("doPost==1-5");

            Map<String, Object> resultMap = new HashMap<>();
            DataSet outDS = null;
            Object outDSObj = null;
            String errString = "";
            DataSet refDS =  new DataSet();
            try {
                refDS =  convertToDataSet((Map<String, List<Map<String, Object>>>) map.get("refDS"));
                System.out.println("doPost==2");                
                ServerAgent sa = new ServerAgent();
                /*
                 * ???????????? outDTName??? ????????? ?????? ????????? ????????? ????????? ?????????..
                 * 
                 * 
                 */
                outDS = sa.executeService(actID, refDS, inDTName, outDTName);
                
    
                // outDTName??? __PAGING_INFO_OUT__??? ?????? ????????? ????????? outDs??? __PAGING_INFO_OUT__??? ?????????
                // ????????? ????????? ?????????.
                if (outDTName != null) {
                    if (!outDTName.equals("")) {
                        outDTName = outDTName + ",__PAGING_INFO_OUT__"; // ????????? ????????? ????????? ?????? ??????
                    }
                }
                System.out.println("doPost==3");
                outDSObj = getListDs_V2(outDS);
                System.out.println("doPost==4");
                resultMap.put("status", "SUCCESS");
                resultMap.put("description", "Service was called successfully.");
                resultMap.put("result", outDSObj);
            } catch (RunningServerBizException e) {
                System.out.println("doPost--exception==2");
                resultMap.put("status", "BIZEXCEPTION");
                resultMap.put("description", "Service was called. But business exception was occurred.");
                /*
                {
                    resultMap.put("CODE", e.data);
                    "CODE":{
                        "LOC":"[BR:BR_CM_USER_createUser:6]"
                        ,"CODE":"CM_ERROR"
                        ,"DATA":"Email??? ????????? ????????? ???????????????. Email ????????????(Y)"
                        ,"PARA":"Email??? ????????? ????????? ???????????????. Email ????????????(Y)"
                        ,"USER":null
                        ,"TYPE":"USER"
                }
                ,"description":"Service was called. But business exception was occurred."
                ,"message":"DYNAMIC_ERROR_KEY|Email??? ????????? ????????? ???????????????. Email ????????????(Y)|%2"
                ,"status":"BIZEXCEPTION"
                }
                */                
                if(e.data.get("CODE").toString().equals("CM_ERROR")){
                    //??????????????? ????????? ??????
                    resultMap.put("message", e.data.get("DATA"));
                } else {
                    resultMap.put("message", e.getMessage());
                }
                resultMap.put("code", e.data.get("CODE"));
                e.printStackTrace();
                errString = e.getMessage();
            } catch (dataset.exception.DataColumnNotFoundException e) {
                //?????????
                //DataSet refDS = convertToDataSet((Map<String, List<Map<String, Object>>>) map.get("refDS"));
                //????????? ????????? ????????? ??????.
                System.out.println("doPost-exception-actID==" + actID);
                System.out.println("doPost-exception-inDTName==" + inDTName);
                System.out.println("doPost-exception-outDTName==" + outDTName);
                e.printStackTrace();
                StringBuilder descBuilder = new StringBuilder();
                if (Pattern.matches(".*Service (.*) Not Exist.*", e.getMessage())) {
                    System.out.println("doPost--exception==4");
                    descBuilder.append("Failed to call service. Check input parameter: ");
                    descBuilder.append("actID");
                    errString = e.getMessage();
                } else if (Pattern.matches(".*This service (.*) need Input Parameter Value.*", e.getMessage())) {
                    System.out.println("doPost--exception==5");
                    descBuilder.append("Failed to call service. Check input parameter: ");
                    descBuilder.append("inDTName");
                    errString = e.getMessage();
                } else if (Pattern.matches(".*Final Return Data Failed.*", e.getMessage())) {
                    System.out.println("doPost--exception==6");
                    descBuilder.append("Failed to call service. Check input parameter: ");
                    descBuilder.append("outDTName");
                    errString = e.getMessage();
                } else {
                    System.out.println("doPost--exception==7");
                    descBuilder.append("Failed to call service.");
    
                    /*
                     * throw new ServletException(e.getMessage()); ?????? ????????????
                     */
    
                }
                resultMap.put("status", "FAILED");
                resultMap.put("description", descBuilder.toString());
                resultMap.put("message", e.getMessage());
                errString = e.getMessage();

            } catch (Exception e) {
                System.out.println("doPost-exception-actID==" + actID);
                String outString = DotNetXmlDataSetConverter.convertFromDataSet(refDS);
                System.out.println("doPost-exception-refDS==" + outString);
                System.out.println("doPost-exception-inDTName==" + inDTName);
                System.out.println("doPost-exception-outDTName==" + outDTName);
                e.printStackTrace();
                StringBuilder descBuilder = new StringBuilder();
                if (Pattern.matches(".*Service (.*) Not Exist.*", e.getMessage())) {
                    System.out.println("doPost--exception==4");
                    descBuilder.append("Failed to call service. Check input parameter: ");
                    descBuilder.append("actID");
                    errString = e.getMessage();
                } else if (Pattern.matches(".*This service (.*) need Input Parameter Value.*", e.getMessage())) {
                    System.out.println("doPost--exception==5");
                    descBuilder.append("Failed to call service. Check input parameter: ");
                    descBuilder.append("inDTName");
                    errString = e.getMessage();
                } else if (Pattern.matches(".*Final Return Data Failed.*", e.getMessage())) {
                    System.out.println("doPost--exception==6");
                    descBuilder.append("Failed to call service. Check input parameter: ");
                    descBuilder.append("outDTName");
                    errString = e.getMessage();
                } else {
                    System.out.println("doPost--exception==7");
                    descBuilder.append("Failed to call service.");
    
                    /*
                     * throw new ServletException(e.getMessage()); ?????? ????????????
                     */
    
                }
                resultMap.put("status", "FAILED");
                resultMap.put("description", descBuilder.toString());
                resultMap.put("message", e.getMessage());
                errString = e.getMessage();
            }
    
            // objectMapper = new ObjectMapper();
            // ?????? ?????? ??????(LocalDateTime, java.util.Date)??? ????????? ??? ??? timestamp ???????????? ?????? ????????? ????????????
            // ??????.
            // objectMapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            // ????????? ????????? ???????????????, BigDecimal ???????
            // junit????????? ??????????????? ????????? ?????????. ????????? E??? ?????? ?????????
            // ?????? ?????? ???????????? ???????????? ??????????????? ??????E??? ??????????????? 0.000005??? ?????????
            // objectMapper.enable(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN);
            // objectMapper.enable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
    
            JsonMapper objectMapper2 = JsonMapper.builder()
                    // ?????? ?????? ??????(LocalDateTime, java.util.Date)??? ????????? ??? ??? timestamp ???????????? ?????? ????????? ????????????
                    // ??????.
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    // ????????? ????????? ???????????????, BigDecimal ???????
                    // junit????????? ??????????????? ????????? ?????????. ????????? E??? ?????? ?????????
                    // ?????? ?????? ???????????? ???????????? ??????????????? ??????E??? ??????????????? 0.000005??? ?????????
                    .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)
                    .enable(JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS).build();
    
            String outJsonStr = objectMapper2.writeValueAsString(resultMap);
    
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(outJsonStr);
            out.close();
    
            long afterTime = System.currentTimeMillis(); // ?????? ?????? ?????? ?????? ????????????
            long secDiffTime = (afterTime - beforeTime); // ??? ????????? ??? ??????
            long gap = secDiffTime;
            String inDsStr = dataset.converter.DotNetXmlDataSetConverter.convertFromDataSet(refDS);
            String outDsStr = "";
            if (outDS != null) {
                outDsStr = dataset.converter.DotNetXmlDataSetConverter.convertFromDataSet(outDS);
            }
            if (!(actID.equals("BR_CM_API_LOG_GetApiLogList") || actID.equals("BR_CM_API_LOG_GetApiLog")
                    || actID.equals("BR_CM_API_LOG_DropLog"))) {
                this.insertBizActorApiLog(_id, actID, inDTName, outDTName, jsonStr, outJsonStr, inDsStr, outDsStr, gap,
                        errString);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


    }

    private DataSet convertToDataSet(Map<String, List<Map<String, Object>>> refDSData) {
        DataSet ds = new DataSet();
        DataTable dt = null;
        Iterator<Map.Entry<String, List<Map<String, Object>>>> iterMap = refDSData.entrySet().iterator();
        while (iterMap.hasNext()) {
            Map.Entry<String, List<Map<String, Object>>> newMap = iterMap.next();
            String key = newMap.getKey();
            List<Map<String, Object>> value = newMap.getValue();
            dt = convertToDataTable(key, (List) value);
            if (dt != null){
                ds.addTable(dt);
            }
                
        }
        return ds;
    }

    private DataTable convertToDataTable(String tableName, List<Map<String, Object>> data) {
        DataTable dt = new DataTable(tableName);
        if (data == null || data.size() == 0) {
            return dt;
        }

        Set<String> keys = ((Map) data.get(0)).keySet();
        for (String column : keys) {
            dt.addColumn(column);
        }

        try{
            for (Map<String, Object> map : data) {
                DataRow dr = dt.addRow();
                Iterator<Map.Entry<String, Object>> iterMap = map.entrySet().iterator();
                while (iterMap.hasNext()) {
                    Map.Entry<String, Object> newMap = iterMap.next();
                    if(newMap.getValue()==null){
                        dr.setObject(newMap.getKey(), null);
                    } else {
                        String tmp =newMap.getValue().toString();
                        if(tmp.trim().equals("")){
                            dr.setObject(newMap.getKey(), null);
                        } else {
                            dr.setObject(newMap.getKey(), tmp);
                        }
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        return dt;
    }

    private Object getListDs_V2(DataSet outDS) throws Exception {
        Map<String, List<Map<String, Object>>> dataTableList = new HashMap<>();
        List<Map<String, Object>> modelList = null;
        for (int i = 0; i < outDS.getTableCount(); i++) {
            DataTable dataTable = outDS.getTable(i);
            String tableName = dataTable.getName();
            int totalRowCount = dataTable.getRowCount();
            modelList = new ArrayList<>();
            for (int k = 0; k < totalRowCount; k++) {
                DataRow dataRow = dataTable.getRow(k);
                Map<String, Object> row = new HashMap<>();
                for (int m = 0; m < dataTable.getColumnCount(); m++) {
                    switch (dataTable.getColumnDataType(m)) {
                        case 107:
                            row.put(dataTable.getColumnName(m), Integer.valueOf(dataRow.getInt(m)));
                            break;
                        case 106:
                            row.put(dataTable.getColumnName(m), Short.valueOf(dataRow.getShort(m)));
                            break;
                        case 108:
                        case 114:
                        case 115:
                            row.put(dataTable.getColumnName(m), Long.valueOf(dataRow.getLong(m)));
                            break;
                        case 110:
                            row.put(dataTable.getColumnName(m), Double.valueOf(dataRow.getDouble(m)));
                            break;
                        case 109:
                            row.put(dataTable.getColumnName(m), Float.valueOf(dataRow.getFloat(m)));
                            break;
                        case 104:
                            row.put(dataTable.getColumnName(m), dataRow.getBigDecimal(m));
                            // row.put(dataTable.getColumnName(m), new
                            // java.math.BigDecimal(dataRow.getBigDecimal(m).toPlainString()));
                            // dataRow.getBigDecimal(m) ??? ??????????????? "5E-8 ???????????? ??????.
                            // ???????????? ?????????????????? ???????????? 0.00000005??? ?????????.
                            // ????????? ???????????? ????????? 5E-8??? ????????? ?????? ??? ?????????
                            // dataset.converter.DotNetXmlDataSetConverter.convertFromDataSet
                            // ??? xml??? ??????????????? 0.00000005??? ????????????.
                            // ????????? outDs??? ?????? ???????????? ????????????.
                            // System.out.println(dataTable.getColumnName(m));
                            // System.out.println(dataRow.getBigDecimal(m));
                            // System.out.println(dataRow.getBigDecimal(m).toPlainString());
                            // ??????????????? ?????? ?????? ??????
                            // objectMapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
                            // objectMapper.enable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
                            // ??? 2?????? ???????????? string?????? ???????????? ?????? E??? ??????????????? 0.0000005??? ?????????
                            // ??????????????? out_data??? ????????? ?????? ??????????????? ??? ??????.
                            // ????????? ????????? ??????????????? ?????? ??????????????? ??? ??????.
                            // ???????????? ???????????? ???
                            //2021-12-03??? ??????
                            //?????? ????????? ???????????? ??????????????? ????????? ????????? ??????.
                            break;
                        case 101:
                            row.put(dataTable.getColumnName(m), Boolean.valueOf(dataRow.getBoolean(m)));
                            break;
                        case 112:
                            row.put(dataTable.getColumnName(m), dataRow.getString(m));
                            break;
                        default:
                            row.put(dataTable.getColumnName(m), dataRow.getString(m));
                            break;
                    }
                }
                modelList.add(row);
            }
            dataTableList.put(tableName, modelList);
        }
        return dataTableList;
    }

    private Object getListDs(DataSet outDS, String outDTName) throws Exception {
        Map<String, List<Map<String, Object>>> dataTableList = new HashMap<>();
        List<Map<String, Object>> modelList = null;
        String[] outDTNameArr = outDTName.split(",");
        for (int i = 0; i < outDS.getTableCount(); i++) {
            DataTable dataTable = outDS.getTable(i);
            boolean hasTable = false;
            String tableName = null;
            for (String element : outDTNameArr) {
                if (dataTable.getName().equals(element)) {
                    hasTable = true;
                    tableName = element;
                    break;
                }
            }
            if (hasTable) {
                int totalRowCount = dataTable.getRowCount();
                modelList = new ArrayList<>();
                for (int k = 0; k < totalRowCount; k++) {
                    DataRow dataRow = dataTable.getRow(k);
                    Map<String, Object> row = new HashMap<>();
                    for (int m = 0; m < dataTable.getColumnCount(); m++) {
                        switch (dataTable.getColumnDataType(m)) {
                            case 107:
                                row.put(dataTable.getColumnName(m), Integer.valueOf(dataRow.getInt(m)));
                                break;
                            case 106:
                                row.put(dataTable.getColumnName(m), Short.valueOf(dataRow.getShort(m)));
                                break;
                            case 108:
                            case 114:
                            case 115:
                                row.put(dataTable.getColumnName(m), Long.valueOf(dataRow.getLong(m)));
                                break;
                            case 110:
                                row.put(dataTable.getColumnName(m), Double.valueOf(dataRow.getDouble(m)));
                                break;
                            case 109:
                                row.put(dataTable.getColumnName(m), Float.valueOf(dataRow.getFloat(m)));
                                break;
                            case 104:
                                row.put(dataTable.getColumnName(m), dataRow.getBigDecimal(m));
                                // row.put(dataTable.getColumnName(m), new
                                // java.math.BigDecimal(dataRow.getBigDecimal(m).toPlainString()));
                                // dataRow.getBigDecimal(m) ??? ??????????????? "5E-8 ???????????? ??????.
                                // ???????????? ?????????????????? ???????????? 0.00000005??? ?????????.
                                // ????????? ???????????? ????????? 5E-8??? ????????? ?????? ??? ?????????
                                // dataset.converter.DotNetXmlDataSetConverter.convertFromDataSet
                                // ??? xml??? ??????????????? 0.00000005??? ????????????.
                                // ????????? outDs??? ?????? ???????????? ????????????.
                                // System.out.println(dataTable.getColumnName(m));
                                // System.out.println(dataRow.getBigDecimal(m));
                                // System.out.println(dataRow.getBigDecimal(m).toPlainString());
                                // ??????????????? ?????? ?????? ??????
                                // objectMapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
                                // objectMapper.enable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
                                // ??? 2?????? ???????????? string?????? ???????????? ?????? E??? ??????????????? 0.0000005??? ?????????
                                // ??????????????? out_data??? ????????? ?????? ??????????????? ??? ??????.
                                // ????????? ????????? ??????????????? ?????? ??????????????? ??? ??????.
                                // ???????????? ???????????? ???
                                //2021-12-03??? ??????
                                //?????? ????????? ???????????? ??????????????? ????????? ????????? ??????.
                                break;
                            case 101:
                                row.put(dataTable.getColumnName(m), Boolean.valueOf(dataRow.getBoolean(m)));
                                break;
                            case 112:
                                row.put(dataTable.getColumnName(m), dataRow.getString(m));
                                break;
                            default:
                                row.put(dataTable.getColumnName(m), dataRow.getString(m));
                                break;
                        }
                    }
                    modelList.add(row);
                }
                dataTableList.put(tableName, modelList);
            }
        }
        return dataTableList;
    }

    public void insertBizActorApiLog(String _id, String actID, String inDTName, String outDTName, String inJsonStr,
            String outJsonStr, String inDsStr, String outDsStr, long gap, String errString) {
                DataSet outDS = null;
        /* ????????? BizActor BR??? ???????????? */
        DataSet inDs = new DataSet();
        DataTable inDt = new DataTable("IN_PSET");
        inDt.addColumn("BIZACTOR_LOG_ID");
        inDt.addColumn("ACT_ID");
        inDt.addColumn("IN_DT_NAME");
        inDt.addColumn("OUT_DT_NAME");
        inDt.addColumn("IN_JSON_STR");
        inDt.addColumn("OUT_JSON_STR");
        inDt.addColumn("IN_DS_STR");
        inDt.addColumn("OUT_DS_STR");
        inDt.addColumn("GAP");
        inDt.addColumn("ERR_STR");
        inDs.addTable(inDt);

        DataRow dr =inDt.addRow();
        dr.setString("BIZACTOR_LOG_ID", _id);
        dr.setString("ACT_ID", actID);
        dr.setString("IN_DT_NAME", inDTName);
        dr.setString("OUT_DT_NAME", outDTName);
        dr.setString("IN_JSON_STR", inJsonStr);
        dr.setString("OUT_JSON_STR", outJsonStr);
        dr.setString("IN_DS_STR", inDsStr);
        dr.setString("OUT_DS_STR", outDsStr);
        dr.setString("GAP", String.valueOf(gap));
        dr.setString("ERR_STR", errString);


        System.out.println("_id=>"+_id);
        ServerAgent sa = new ServerAgent();
        try {
            outDS = sa.executeService("BR_CM_API_LOG_CreateApiLog", inDs, "IN_PSET", null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}