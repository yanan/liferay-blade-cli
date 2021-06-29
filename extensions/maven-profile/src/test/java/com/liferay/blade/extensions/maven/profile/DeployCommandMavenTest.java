/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liferay.blade.extensions.maven.profile;

import com.liferay.blade.cli.BladeTest;
import com.liferay.blade.cli.TestUtil;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * @author Christopher Bryan Boyd
 */
public class DeployCommandMavenTest {

	@Before
	public void setUp() throws Exception {
		_extensionsDir = temporaryFolder.newFolder(".blade", "extensions");
	}

	@Test
	public void testInstallJar() throws Exception {
		File workspaceDir = temporaryFolder.newFolder();

		String[] args = {"--base", workspaceDir.getPath(), "init", "-P", "maven", "-v", BladeTest.LIFERAY_VERSION_721};

		TestUtil.runBlade(workspaceDir, _extensionsDir, args);

		args = new String[] {"--base", workspaceDir.getPath(), "server", "init"};

		String workspacePath = workspaceDir.getAbsolutePath();

		TestUtil.updateMavenRepositories(workspacePath);

		TestUtil.runBlade(workspaceDir, _extensionsDir, args);

		File bundlesDirectory = new File(workspaceDir.getPath(), "bundles");

		Assert.assertTrue(bundlesDirectory.exists());

		File osgiDirectory = new File(bundlesDirectory, "osgi");

		Assert.assertTrue(osgiDirectory.exists());

		File osgiModulesDirectory = new File(osgiDirectory, "modules");

		Assert.assertTrue(osgiModulesDirectory.exists());

		String[] osgiModulesDirectoryList = osgiModulesDirectory.list();

		int filesCount = osgiModulesDirectoryList.length;

		Assert.assertEquals(0, filesCount);

		File modulesDirectory = new File(workspaceDir, "modules");

		Assert.assertTrue(modulesDirectory.exists());

		args = new String[] {
			"--base", modulesDirectory.getAbsolutePath(), "create", "-t", "mvc-portlet", "foo", "-v",
			BladeTest.LIFERAY_VERSION_721
		};

		TestUtil.runBlade(workspaceDir, _extensionsDir, args);

		File projectDirectory = new File(modulesDirectory, "foo");

		Assert.assertTrue(projectDirectory.exists());

		args = new String[] {"--base", projectDirectory.getAbsolutePath(), "deploy"};

		TestUtil.runBlade(workspaceDir, _extensionsDir, args);

		osgiModulesDirectoryList = osgiModulesDirectory.list();

		filesCount = osgiModulesDirectoryList.length;

		Assert.assertEquals(1, filesCount);
	}

	@Test
	public void testInstallWar() throws Exception {
		File workspaceDir = temporaryFolder.newFolder();

		String[] args = {"--base", workspaceDir.getPath(), "init", "-P", "maven", "-v", BladeTest.LIFERAY_VERSION_721};

		TestUtil.runBlade(workspaceDir, _extensionsDir, args);

		args = new String[] {"--base", workspaceDir.getPath(), "server", "init"};

		String workspacePath = workspaceDir.getAbsolutePath();

		TestUtil.updateMavenRepositories(workspacePath);

		TestUtil.runBlade(workspaceDir, _extensionsDir, args);

		File bundlesDirectory = new File(workspaceDir.getPath(), "bundles");

		Assert.assertTrue(bundlesDirectory.exists());

		File warDirectory = new File(bundlesDirectory, "osgi/war");

		Assert.assertTrue(warDirectory.exists());

		String[] deployDirectoryList = warDirectory.list();

		int filesCount = deployDirectoryList.length;

		Assert.assertEquals(0, filesCount);

		File modulesDirectory = new File(workspaceDir, "modules");

		Assert.assertTrue(modulesDirectory.exists());

		args = new String[] {
			"--base", modulesDirectory.getAbsolutePath(), "create", "-t", "war-mvc-portlet", "foo", "-v",
			BladeTest.LIFERAY_VERSION_721
		};

		TestUtil.runBlade(workspaceDir, _extensionsDir, args);

		File projectDirectory = new File(modulesDirectory, "foo");

		Assert.assertTrue(projectDirectory.exists());

		args = new String[] {"--base", projectDirectory.getAbsolutePath(), "deploy"};

		TestUtil.runBlade(workspaceDir, _extensionsDir, args);

		deployDirectoryList = warDirectory.list();

		filesCount = deployDirectoryList.length;

		Assert.assertEquals(1, filesCount);
	}

	@Rule
	public final TemporaryFolder temporaryFolder = new TemporaryFolder();

	private File _extensionsDir = null;

}