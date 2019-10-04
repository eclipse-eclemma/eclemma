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
package org.eclipse.eclemma.internal.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.jacoco.core.data.IExecutionDataVisitor;
import org.jacoco.core.data.ISessionInfoVisitor;

import org.eclipse.eclemma.core.ICoverageSession;
import org.eclipse.eclemma.core.IExecutionDataSource;

/**
 * A {@link org.eclipse.eclemma.core.ICoverageSession} implementation.
 */
public class CoverageSession extends PlatformObject implements ICoverageSession {

  private final String description;
  private final Set<IPackageFragmentRoot> scope;
  private final IExecutionDataSource executionDataSource;
  private final ILaunchConfiguration launchconfiguration;

  public CoverageSession(String description,
      Collection<IPackageFragmentRoot> scope,
      IExecutionDataSource executionDataSource,
      ILaunchConfiguration launchconfiguration) {
    this.description = description;
    this.scope = Collections.unmodifiableSet(new HashSet<IPackageFragmentRoot>(
        scope));
    this.executionDataSource = executionDataSource;
    this.launchconfiguration = launchconfiguration;
  }

  // ICoverageSession implementation

  public String getDescription() {
    return description;
  }

  public Set<IPackageFragmentRoot> getScope() {
    return scope;
  }

  public ILaunchConfiguration getLaunchConfiguration() {
    return launchconfiguration;
  }

  public void accept(IExecutionDataVisitor executionDataVisitor,
      ISessionInfoVisitor sessionInfoVisitor) throws CoreException {
    executionDataSource.accept(executionDataVisitor,
        sessionInfoVisitor);
  }

}
