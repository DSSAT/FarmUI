/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.agmip.ui.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import org.agmip.acmo.util.AcmoUtil;
import org.agmip.functions.DataCombinationHelper;
import org.agmip.translators.dssat.DssatControllerOutput;
import static org.agmip.ui.utils.AppKeys.CROP_ID;
import static org.agmip.ui.utils.AppKeys.DSSAT_CULTIVAR_ID;
import static org.agmip.ui.utils.AppKeys.FERT_AMOUNT_RATE;
import static org.agmip.ui.utils.AppKeys.FERT_APP_CODE;
import static org.agmip.ui.utils.AppKeys.FERT_DEPTH;
import static org.agmip.ui.utils.AppKeys.FERT_MATERIAL_CODE;
import static org.agmip.ui.utils.AppKeys.IRR_DATE;
import static org.agmip.ui.utils.AppKeys.IRR_DEPTH;
import static org.agmip.ui.utils.AppKeys.IRR_DURATION;
import static org.agmip.ui.utils.AppKeys.IRR_EFF;
import static org.agmip.ui.utils.AppKeys.IRR_INTERVAL;
import static org.agmip.ui.utils.AppKeys.IRR_NUMBER;
import static org.agmip.ui.utils.AppKeys.IRR_OFFSET;
import static org.agmip.ui.utils.AppKeys.IRR_OPER;
import static org.agmip.ui.utils.AppKeys.IRR_SPACING;
import static org.agmip.ui.utils.AppKeys.IRR_START_TIME;
import static org.agmip.ui.utils.AppKeys.IRR_VALUE;
import static org.agmip.ui.utils.AppKeys.PLANTING_DEPTH;
import static org.agmip.ui.utils.AppKeys.PLANTING_METHOD_CODE;
import static org.agmip.ui.utils.AppKeys.PLANTING_ROW_SPACING;
import static org.agmip.ui.utils.AppKeys.PLANT_POPULATION_AT_EMERGENCE;
import static org.agmip.ui.utils.AppKeys.PLANT_POPULATION_AT_SEEDING;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author rohit
 */
public class DssatXFileGenerator {

    private static File jSon;
    
    private static void createJson() throws IOException {
        String[] date = DssatUtil.getInfo(AppKeys.PLANTING_DATE).split("/");
        int year = Integer.parseInt(date[2]);
        JSONObject object = new JSONObject();
        JSONArray experiments = new JSONArray();
        for (int j = 0; j <= 0; j++) {
            JSONObject experiment = new JSONObject();
            Location countypos = new Location();

            CSVFileHandler.getCountyLocation_GlobalDB(DssatUtil.getInfo(AppKeys.LOCATION), countypos);
            experiment.put("data_source", "DSSAT");
            experiment.put("crop_model_version", "v4.6");

            // Enable the Site Name
            if (DssatUtil.getInfo(AppKeys.FARM) != null) {
                experiment.put("site_name", DssatUtil.getInfo(AppKeys.FARM));
            }
            experiment.put("in", "UF");
            experiment.put("person_notes", "Kelly Morgan, L.C. Jones, J.W.(Should be changed to Farmers Name)");
            experiment.put("institution", "UNIVERSITY OF FLORIDA, GAINESVILLE, FL, USA");

            experiment.put("wst_id", DssatUtil.getInfo(AppKeys.STATION_CODE).substring(0, 3));
            experiment.put("fl_lat", "" + countypos.getLatitude());
            experiment.put("fl_long", "" + countypos.getLongitude());
            experiment.put("sc_year", "" + (year + j));
            experiment.put("id_field", DssatUtil.getInfo(AppKeys.STATION_CODE));
            
            String str = DssatUtil.getInfo(IRR_EFF);
            experiment.put(IRR_EFF, String.valueOf(Double.valueOf(str) / 100));

            // Add bed width and bed height
            experiment.put("bed_h", DssatUtil.getInfo(AppKeys.BED_HEIGHT));
            experiment.put("bed_w", DssatUtil.getInfo(AppKeys.BED_WIDTH));
            experiment.put("pmalb", DssatUtil.getInfo(AppKeys.PLASTIC_MULCH_COLOR));

            experiment.put("exname", DssatUtil.getInfo(AppKeys.STATION_CODE) + "_" + (year + j));
            String soilId = CSVFileHandler.getSoilid(DssatUtil.getInfo(AppKeys.LOCATION), DssatUtil.getInfo(AppKeys.SOIL));
            experiment.put("soil_id", soilId);

            JSONObject management = new JSONObject();
            JSONArray events = new JSONArray();

            JSONObject event1 = new JSONObject();
            event1.put("event", "planting");
            event1.put("dssat_cul_id", DssatUtil.getInfo(DSSAT_CULTIVAR_ID));
            event1.put("crid", DssatUtil.getInfo(CROP_ID));
            event1.put("plma", DssatUtil.getInfo(PLANTING_METHOD_CODE));
            event1.put("plpop", DssatUtil.getInfo(PLANT_POPULATION_AT_SEEDING));
            event1.put("plpoe", DssatUtil.getInfo(PLANT_POPULATION_AT_EMERGENCE));
            event1.put("plrs", DssatUtil.getInfo(PLANTING_ROW_SPACING).trim());
            event1.put("date", (year + j) + "" + date[0] + date[1]);
            event1.put("pldp", DssatUtil.getInfo(PLANTING_DEPTH));
            events.put(event1);

            // Add fertilier events
            int count = Integer.parseInt(DssatUtil.getInfo("fert_row_count"));

            for (int i = 0; i < count; i++) {
                JSONObject event = new JSONObject();
                event.put("event", "fertilizer");
                event.put("feamn", DssatUtil.getInfo(FERT_AMOUNT_RATE + i));
                //event.put("fecd", DssatUtil.getInfo("fecd" + i));
                String fertApplicationCode = DssatUtil.getInfo(FERT_APP_CODE + i);
                event.put("feacd", fertApplicationCode);

                
                
                String fertilizationMaterialCode = DssatUtil.getInfo(FERT_MATERIAL_CODE + i);
                event.put("fecd", fertilizationMaterialCode);
                
                String date_i = (year + j) + DssatUtil.getInfo("ftdate" + i).substring(4);
                event.put("date", date_i);
                
                // Get the fertilization depth
                String fertDepth = DssatUtil.getInfo(FERT_DEPTH);
                event.put(FERT_DEPTH, fertDepth);
                
                events.put(event);
            }

            // Add irrigation events
            count = Integer.parseInt(DssatUtil.getInfo("irr_row_count"));
            for (int i = 0; i < count; i++) {
                JSONObject event = new JSONObject();
                event.put("event", "irrigation");
                String eff = DssatUtil.getInfo(IRR_EFF);
                event.put("ireff", String.valueOf(Double.valueOf(DssatUtil.getInfo(IRR_EFF)) / 100));
                event.put("irop", DssatUtil.getInfo(IRR_OPER + i));
                event.put("irval", DssatUtil.getInfo(IRR_VALUE + i));
                event.put("irstr", DssatUtil.getInfo(IRR_START_TIME + i));
                event.put("irdur", DssatUtil.getInfo(IRR_DURATION + i));
                event.put("irint", DssatUtil.getInfo(IRR_INTERVAL + i));
                event.put("irnum", DssatUtil.getInfo(IRR_NUMBER + i));
                event.put("irspc", DssatUtil.getInfo(IRR_SPACING));
                event.put("irmdp", DssatUtil.getInfo(IRR_DEPTH));
                event.put("irdep", DssatUtil.getInfo(AppKeys.DRIPPER_EMITTER_DEPTH));
                event.put("irofs", DssatUtil.getInfo(IRR_OFFSET));
                String date_i = (year + j) + DssatUtil.getInfo(IRR_DATE + i).substring(4);
                event.put("date", date_i);
                events.put(event);
            }

            management.put("events", events);
            experiment.put("management", management);
            experiments.put(experiment);
        }
        object.put("experiments", experiments);

        jSon = new File(PathGenerator.getDataDir() + PathGenerator.getDirSeperator() + DssatUtil.getInfo("Station Code") + ".json");
        FileWriter fw = new FileWriter(jSon);

        fw.write(object.toString());
        fw.close();
    }

    public static void createXFileOutput() throws IOException {

        createJson();
        
        System.out.println("Calling DSSATControllerOutput Class to Create X File.");

        DssatControllerOutput translator = new DssatControllerOutput();
        ArrayList<String> inputPaths = new ArrayList<>();
        String defaultOutputPath = "";
        String xFileDir = PathGenerator.getCropDirectory();
        
        inputPaths.add(jSon.getPath());
        
        HashMap data = DataCombinationHelper.combine(inputPaths);
        DataCombinationHelper.fixData(data);

        translator.writeFile(xFileDir, data);
        AcmoUtil.writeAcmo(defaultOutputPath, data, "dssat", new HashMap());
        PathGenerator.setTranslatorOutputPath (translator.getOutputFiles());
    }

}
