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
package org.eclipse.eclemma.internal.core.launching;

import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.Launch;
import org.eclipse.jdt.core.IPackageFragmentRoot;

import org.eclipse.eclemma.core.CoverageTools;
import org.eclipse.eclemma.core.launching.ICoverageLaunch;
import org.eclipse.eclemma.internal.core.EclEmmaCorePlugin;

/**
 * Implementation of {@link ICoverageLaunch}.
 */
public class CoverageLaunch extends Launch implements ICoverageLaunch {

  private final Set<IPackageFragmentRoot> scope;
  private final AgentServer agentServer;

  public CoverageLaunch(ILaunchConfiguration launchConfiguration,
      Set<IPackageFragmentRoot> scope) {
    super(launchConfiguration, CoverageTools.LAUNCH_MODE, null);
    this.scope = scope;
    final EclEmmaCorePlugin plugin = EclEmmaCorePlugin.getInstance();
    this.agentServer = new AgentServer(this, plugin.getSessionManager(),
        plugin.getExecutionDataFiles(), plugin.getPreferences());
  }

  public AgentServer getAgentServer() {
    return agentServer;
  }

  // ICoverageLaunch interface

  public Set<IPackageFragmentRoot> getScope() {
    return scope;
  }

  public void requestDump(boolean reset) throws CoreException {
    agentServer.requestDump(reset);
  }

}
