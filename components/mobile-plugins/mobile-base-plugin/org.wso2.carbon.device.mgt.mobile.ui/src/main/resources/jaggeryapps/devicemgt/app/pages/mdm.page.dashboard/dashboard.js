/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License config.json the
 * specific language governing permissions and limitations
 * under the License.
 */

function onRequest(context) {
    var userModule = require("/app/modules/business-controllers/user.js")["userModule"];
    var generalConfig = context.app.conf["generalConfig"];
    var mdmProps = require("/app/modules/conf-reader/main.js")["conf"];

    var viewModel = {};
    viewModel.permissions = userModule.getUIPermissions();
    new Log().debug("## Permissions : " + stringify(userModule.getUIPermissions()));
    //TODO: Move enrollment URL into app-conf.json
    viewModel.enrollmentURL = mdmProps.generalConfig.host +  mdmProps.enrollmentDir;
    return viewModel;
}