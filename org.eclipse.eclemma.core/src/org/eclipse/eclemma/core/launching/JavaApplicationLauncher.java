/*******************************************************************************
 * Copyright (c) 2006, 2019 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 ******************************************************************************/
package org.eclipse.eclemma.core.launching;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.launching.JavaRuntime;

import org.eclipse.eclemma.core.ScopeUtils;

/**
 * Launcher for local Java applications.
 */
public class JavaApplicationLauncher extends CoverageLauncher {

  public Set<IPackageFragmentRoot> getOverallScope(
      ILaunchConfiguration configuration) throws CoreException {
    final IJavaProject project = JavaRuntime.getJavaProject(configuration);
    if (project == null) {
      return Collections.emptySet();
    } else {
      return ScopeUtils.filterJREEntries(Arrays.asList(project
          .getAllPackageFragmentRoots()));
    }
  }

}
