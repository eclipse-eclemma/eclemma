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
package org.eclipse.eclemma.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.jacoco.core.analysis.ICoverageNode;

import org.eclipse.eclemma.core.analysis.IJavaCoverageListener;
import org.eclipse.eclemma.core.analysis.IJavaModelCoverage;
import org.eclipse.eclemma.core.launching.ICoverageLaunch;
import org.eclipse.eclemma.internal.core.EclEmmaCorePlugin;
import org.eclipse.eclemma.internal.core.SessionExporter;
import org.eclipse.eclemma.internal.core.SessionImporter;

/**
 * For central access to the tools provided by the coverage core plug-in this
 * class offers several static methods.
 */
public final class CoverageTools {

  /**
   * The launch mode used for coverage sessions.
   */
  public static final String LAUNCH_MODE = "coverage"; //$NON-NLS-1$

  /**
   * Returns the global session manager.
   *
   * @return global session manager
   */
  public static ISessionManager getSessionManager() {
    return EclEmmaCorePlugin.getInstance().getSessionManager();
  }

  /**
   * Convenience method that tries to adapt the given object to ICoverageNode,
   * i.e. find coverage information from the active session.
   *
   * @param object
   *          Object to adapt
   * @return adapter or <code>null</code>
   */
  public static ICoverageNode getCoverageInfo(Object object) {
    if (object instanceof IAdaptable) {
      return (ICoverageNode) ((IAdaptable) object)
          .getAdapter(ICoverageNode.class);
    } else {
      return null;
    }
  }

  public static IJavaModelCoverage getJavaModelCoverage() {
    return EclEmmaCorePlugin.getInstance().getJavaCoverageLoader()
        .getJavaModelCoverage();
  }

  public static void addJavaCoverageListener(IJavaCoverageListener l) {
    EclEmmaCorePlugin.getInstance().getJavaCoverageLoader()
        .addJavaCoverageListener(l);
  }

  public static void removeJavaCoverageListener(IJavaCoverageListener l) {
    EclEmmaCorePlugin.getInstance().getJavaCoverageLoader()
        .removeJavaCoverageListener(l);
  }

  public static ISessionExporter getExporter(ICoverageSession session) {
    return new SessionExporter(session);
  }

  public static ISessionImporter getImporter() {
    return new SessionImporter(getSessionManager(), EclEmmaCorePlugin
        .getInstance().getExecutionDataFiles());
  }

  /**
   * Sets a {@link ICorePreferences} instance which will be used by the EclEmma
   * core to query preference settings if required.
   *
   * @param preferences
   *          callback object for preference settings
   */
  public static void setPreferences(ICorePreferences preferences) {
    EclEmmaCorePlugin.getInstance().setPreferences(preferences);
  }

  /**
   * Determines all current coverage launches which are running.
   *
   * @return list of running coverage launches
   */
  public static List<ICoverageLaunch> getRunningCoverageLaunches() {
    final List<ICoverageLaunch> result = new ArrayList<ICoverageLaunch>();
    for (final ILaunch launch : DebugPlugin.getDefault().getLaunchManager()
        .getLaunches()) {
      if (launch instanceof ICoverageLaunch && !launch.isTerminated()) {
        result.add((ICoverageLaunch) launch);
      }
    }
    return result;
  }

  private CoverageTools() {
    // no instances
  }

}
