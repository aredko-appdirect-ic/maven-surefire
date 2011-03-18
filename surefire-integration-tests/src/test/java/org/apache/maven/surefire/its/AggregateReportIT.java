package org.apache.maven.surefire.its;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;
import org.apache.maven.surefire.its.misc.HelperAssertions;

import java.io.File;
import java.util.List;

/**
 * Test report aggregation
 *
 * @author <a href="mailto:dfabulich@apache.org">Dan Fabulich</a>
 */
public class AggregateReportIT
    extends AbstractSurefireIntegrationTestClass
{
    public void testAggregateReport()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/aggregate-report" );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        List<String> goals = this.getInitialGoals();
        goals.add( "org.apache.maven.plugins:maven-surefire-report-plugin:" + getSurefireVersion() + ":report" );
        executeGoals( verifier, goals );
        //DGF even though the build will succeed, the log will contain errors (from the failure)
        //verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        File surefireReportHtml = new File( testDir, "target/site/surefire-report.html" );
        assertTrue( "surefire report missing: " + surefireReportHtml.getAbsolutePath(), surefireReportHtml.exists() );

        // TODO HtmlUnit tests on the surefire report

        File[] testDirs = new File[2];
        testDirs[0] = new File( testDir, "child1" );
        testDirs[1] = new File( testDir, "child2" );
        IntegrationTestSuiteResults suite = HelperAssertions.parseTestResults( testDirs );
        HelperAssertions.assertTestSuiteResults( 2, 0, 1, 0, suite );
    }
}
