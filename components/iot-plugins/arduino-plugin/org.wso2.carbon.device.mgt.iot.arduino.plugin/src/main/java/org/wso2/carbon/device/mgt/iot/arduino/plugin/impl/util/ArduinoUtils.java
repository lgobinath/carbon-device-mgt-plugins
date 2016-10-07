/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.device.mgt.iot.arduino.plugin.impl.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.device.mgt.common.Device;
import org.wso2.carbon.device.mgt.iot.arduino.plugin.constants.ArduinoConstants;
import org.wso2.carbon.device.mgt.iot.arduino.plugin.exception.ArduinoDeviceMgtPluginException;
import org.wso2.carbon.device.mgt.iot.arduino.plugin.internal.ArduinoManagementDataHolder;
import org.wso2.carbon.device.mgt.iot.devicetype.config.DeviceManagementConfiguration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Contains utility methods used by Arduino plugin.
 */
public class ArduinoUtils {

    private static Log log = LogFactory.getLog(ArduinoUtils.class);

    public static String getDeviceProperty(List<Device.Property> deviceProperties, String propertyKey) {
        String deviceProperty = "";
        for(Device.Property property :deviceProperties){
            if(propertyKey.equals(property.getName())){
                deviceProperty = property.getValue();
            }
        }
        return deviceProperty;
    }

    public static Device.Property getProperty(String property, String value) {
        if (property != null) {
            Device.Property prop = new Device.Property();
            prop.setName(property);
            prop.setValue(value);
            return prop;
        }
        return null;
    }

    public static void cleanupResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.warn("Error occurred while closing result set", e);
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.warn("Error occurred while closing prepared statement", e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.warn("Error occurred while closing database connection", e);
            }
        }
    }

    public static void cleanupResources(PreparedStatement stmt, ResultSet rs) {
        cleanupResources(null, stmt, rs);
    }

    /**
     * Creates the device management schema.
     */
    public static void setupDeviceManagementSchema() throws ArduinoDeviceMgtPluginException {
        DeviceManagementConfiguration deviceManagementConfiguration = ArduinoManagementDataHolder.getInstance()
                .getDeviceTypeConfigService().getConfiguration(ArduinoConstants.DEVICE_TYPE,
                                                               ArduinoConstants.DEVICE_TYPE_PROVIDER_DOMAIN);
        String datasource = deviceManagementConfiguration.getDeviceManagementConfigRepository().getDataSourceConfig()
                .getJndiLookupDefinition().getJndiName();
        try {
            Context ctx = new InitialContext();
            DataSource dataSource = (DataSource) ctx.lookup(datasource);
            DeviceSchemaInitializer initializer = new DeviceSchemaInitializer(dataSource);
            String checkSql = "select * from ARDUINO_DEVICE";
            if (!initializer.isDatabaseStructureCreated(checkSql)) {
                log.info("Initializing device management repository database schema");
                initializer.createRegistryDatabase();
            } else {
                log.info("Device management repository database already exists. Not creating a new database.");
            }

        } catch (NamingException e) {
            log.error("Error while looking up the data source: " + datasource, e);
        } catch (Exception e) {
            throw new ArduinoDeviceMgtPluginException("Error occurred while initializing Iot Device " +
                                                                   "Management database schema", e);
        }
    }


}
