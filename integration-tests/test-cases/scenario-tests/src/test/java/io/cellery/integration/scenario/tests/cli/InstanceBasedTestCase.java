/*
 *  Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package io.cellery.integration.scenario.tests.cli;

import io.cellery.integration.scenario.tests.BaseTestCase;
import io.cellery.integration.scenario.tests.Constants;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * This test case includes combinations of cellery cli commands with instance names.
 */
public class InstanceBasedTestCase extends BaseTestCase {
    private final String orgName = "wso2cellery";
    private final String imageName = "hello-world-cell";
    private String instanceName;

    @BeforeClass
    public void quickRunSample() throws Exception {
        this.instanceName = run(orgName, imageName, Constants.SAMPLE_CELLS_VERSION, null, 120);
        Assert.assertTrue(instanceName != null && !instanceName.isEmpty(), "Instance name is empty!");
    }

    @Test
    public void describeInstance() throws Exception {
        Process process = Runtime.getRuntime().exec("cellery describe " + instanceName);
        String errorString = "Unable to describe cell instance: " + instanceName;
        String expectedOut = "/apis/mesh.cellery.io/v1alpha1/namespaces/default/cells/" + instanceName;
        readOutputResult(process, expectedOut, errorString);
    }

    @Test
    public void describeNonExistingInstance() throws Exception {
        String nonExistingInstance = "foo";
        Process process = Runtime.getRuntime().exec("cellery describe " + nonExistingInstance);
        String errorString = "Unable to describe non existing cell instance: " + instanceName;
        try {
            readOutputResult(process, "", errorString);
        } catch (Exception ex) {
            String output = ex.getMessage();
            if (!output.contains("cells.mesh.cellery.io \"" + nonExistingInstance + "\" not found")) {
                throw ex;
            }
        }
    }

    @AfterClass
    public void cleanup() throws Exception {
        terminateCell(instanceName);
    }
}
