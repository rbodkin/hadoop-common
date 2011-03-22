/**
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
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.apache.hadoop.yarn.server.nodemanager;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.UnsupportedFileSystemException;
import org.apache.hadoop.fs.permission.FsPermission;

public class TestContainerManagerWithLCE extends TestContainerManager {

  private static final Log LOG = LogFactory
      .getLog(TestContainerManagerWithLCE.class);

  public TestContainerManagerWithLCE() throws UnsupportedFileSystemException {
    super();
  }

  static {
    localDir =
        new File("target",
            TestContainerManagerWithLCE.class.getName() + "-localDir")
            .getAbsoluteFile();
    tmpDir = new File("target",
        TestContainerManagerWithLCE.class.getName() + "-tmpDir");
  }

  @Override
  public void setup() throws IOException {
    // Don't run the test if the binary is not available.
    if (!shouldRunTest()) {
      LOG.info("LCE binary path is not passed. Not running the test");
      return;
    }
    super.setup();
    localFS.setPermission(new Path(localDir.getCanonicalPath()),
        new FsPermission(
            (short) 0777));
    localFS.setPermission(new Path(tmpDir.getCanonicalPath()),
        new FsPermission(
            (short) 0777));
  }

  @Override
  public void tearDown() throws IOException, InterruptedException {
    super.tearDown();
    FileUtil.chmod(localDir.getAbsolutePath(), "777", true);
    localFS.delete(new Path(localDir.getCanonicalPath()), true);
  }

  @Override
  public void testContainerSetup() throws IOException, InterruptedException {
    // No cleanup as of now. Only one test for now. TODO: FIX
  }

  @Override
  public void testContainerManagerInitialization() throws IOException {
    // No cleanup as of now. Only one test for now. TODO: FIX
  }

  @Override
  public void testContainerLaunchAndStop() throws IOException,
      InterruptedException {
    // Don't run the test if the binary is not available.
    if (!shouldRunTest()) {
      LOG.info("LCE binary path is not passed. Not running the test");
      return;
    }
    super.testContainerLaunchAndStop();
  }

  private boolean shouldRunTest() {
    return System
        .getProperty(LinuxContainerExecutor.CONTAINER_EXECUTOR_EXEC_KEY) != null;
  }

  @Override
  protected ContainerExecutor createContainerExecutor() {
    super.conf.set(LinuxContainerExecutor.CONTAINER_EXECUTOR_EXEC_KEY, System
        .getProperty(LinuxContainerExecutor.CONTAINER_EXECUTOR_EXEC_KEY));
    LinuxContainerExecutor linuxContainerExecutor =
        new LinuxContainerExecutor();
    linuxContainerExecutor.setConf(super.conf);
    return linuxContainerExecutor;
  }
}
