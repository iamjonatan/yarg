/*
 * Copyright 2013 Haulmont
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import com.haulmont.yarg.loaders.impl.SqlDataLoader;
import com.haulmont.yarg.structure.BandData;
import com.haulmont.yarg.structure.BandOrientation;
import com.haulmont.yarg.structure.impl.ReportQueryImpl;
import junit.framework.Assert;
import utils.TestDatabase;
import org.junit.Test;

import java.util.*;

public class SqlLoaderTest {
    @Test
    public void testListParameter() throws Exception {
        TestDatabase testDatabase = new TestDatabase();
        testDatabase.setUpDatabase();

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("login", Arrays.asList("login1", "login2"));
            SqlDataLoader sqlDataLoader = new SqlDataLoader(testDatabase.getDs());
            BandData rootBand = new BandData("band1", null, BandOrientation.HORIZONTAL);
            rootBand.setData(Collections.emptyMap());

            List<Map<String, Object>> result = sqlDataLoader.loadData(
                    new ReportQueryImpl("", "select login, password from user where login in ${login}", "sql", null, null), rootBand, params);
            printResult(result);
            Assert.assertEquals(2, result.size());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        } finally {
            testDatabase.stop();
        }
    }

    @Test
    public void testArrayParameter() throws Exception {
        TestDatabase testDatabase = new TestDatabase();
        testDatabase.setUpDatabase();

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("login", new String[]{"login1", "login2"});
            SqlDataLoader sqlDataLoader = new SqlDataLoader(testDatabase.getDs());
            BandData rootBand = new BandData("band1", null, BandOrientation.HORIZONTAL);
            rootBand.setData(Collections.emptyMap());

            List<Map<String, Object>> result = sqlDataLoader.loadData(
                    new ReportQueryImpl("", "select login, password from user where login in ${login}", "sql", null, null), rootBand, params);
            printResult(result);
            Assert.assertEquals(2, result.size());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        } finally {
            testDatabase.stop();
        }
    }

    @Test
    public void testFunctionParameter() throws Exception {
        TestDatabase testDatabase = new TestDatabase();
        testDatabase.setUpDatabase();

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("login", "login");
            SqlDataLoader sqlDataLoader = new SqlDataLoader(testDatabase.getDs());
            BandData rootBand = new BandData("band1", null, BandOrientation.HORIZONTAL);
            rootBand.setData(Collections.emptyMap());

            List<Map<String, Object>> result = sqlDataLoader.loadData(
                    new ReportQueryImpl("", "select login, password from user where login = CONCAT(${login}, '1')", "sql", null, null), rootBand, params);
            printResult(result);
            Assert.assertEquals(1, result.size());

            params.put("login", null);
            result = sqlDataLoader.loadData(
                    new ReportQueryImpl("", "select login, password from user [[where login = CONCAT(${login}, '1')]]", "sql", null, null), rootBand, params);
            printResult(result);
            Assert.assertEquals(3, result.size());

            params.put("login", "ABCD");
            result = sqlDataLoader.loadData(
                    new ReportQueryImpl("", "select login, password from user [[where login = CONCAT(${login}, '1')]]", "sql", null, null), rootBand, params);
            printResult(result);
            Assert.assertEquals(0, result.size());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        } finally {
            testDatabase.stop();
        }
    }

    private void printResult(List<Map<String, Object>> result) {
        for (Map<String, Object> stringObjectMap : result) {
            for (Map.Entry<String, Object> entry : stringObjectMap.entrySet()) {
                System.out.print("(" + entry.getKey() + ":" + entry.getValue() + ") ");
            }
            System.out.println();
        }
    }
}