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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;

import org.eclipse.eclemma.core.EclEmmaStatus;
import org.eclipse.eclemma.core.ICoverageSession;
import org.eclipse.eclemma.core.ISessionListener;
import org.eclipse.eclemma.core.ISessionManager;
import org.eclipse.eclemma.core.analysis.IJavaCoverageListener;
import org.eclipse.eclemma.core.analysis.IJavaModelCoverage;
import org.eclipse.eclemma.internal.core.analysis.SessionAnalyzer;

/**
 * Internal utility class that loads the coverage data asynchronously, holds the
 * current {@link IJavaModelCoverage} object and sends out events in case of
 * changed coverage information.
 */
public class JavaCoverageLoader {

  private final ISessionManager sessionManager;

  private IJavaModelCoverage coverage;

  private final List<IJavaCoverageListener> listeners = new ArrayList<IJavaCoverageListener>();

  private ISessionListener sessionListener = new ISessionListener() {

    public void sessionActivated(ICoverageSession session) {
      Job.getJobManager().cancel(LOADJOB);
      if (session == null) {
        coverage = null;
        fireCoverageChanged();
      } else {
        coverage = IJavaModelCoverage.LOADING;
        fireCoverageChanged();
        new LoadSessionJob(session).schedule();
      }
    }

    public void sessionAdded(ICoverageSession addedSession) {
    }

    public void sessionRemoved(ICoverageSession removedSession) {
    }

  };

  private static final Object LOADJOB = new Object();

  private class LoadSessionJob extends Job {

    private final ICoverageSession session;

    public LoadSessionJob(ICoverageSession session) {
      super(NLS.bind(CoreMessages.AnalyzingCoverageSession_task,
          session.getDescription()));
      this.session = session;
    }

    protected IStatus run(IProgressMonitor monitor) {
      final IJavaModelCoverage c;
      try {
        c = new SessionAnalyzer().processSession(session, monitor);
      } catch (CoreException e) {
        return EclEmmaStatus.SESSION_LOAD_ERROR.getStatus(e);
      }
      coverage = monitor.isCanceled() ? null : c;
      fireCoverageChanged();
      return Status.OK_STATUS;
    }

    @Override
    public boolean belongsTo(Object family) {
      return family == LOADJOB;
    }

  }

  public JavaCoverageLoader(ISessionManager sessionManager) {
    this.sessionManager = sessionManager;
    sessionManager.addSessionListener(sessionListener);
  }

  public void addJavaCoverageListener(IJavaCoverageListener l) {
    if (l == null) {
      throw new IllegalArgumentException();
    }
    if (!listeners.contains(l)) {
      listeners.add(l);
    }
  }

  public void removeJavaCoverageListener(IJavaCoverageListener l) {
    listeners.remove(l);
  }

  protected void fireCoverageChanged() {
    // avoid concurrent modification issues
    for (IJavaCoverageListener l : new ArrayList<IJavaCoverageListener>(
        listeners)) {
      l.coverageChanged();
    }
  }

  public IJavaModelCoverage getJavaModelCoverage() {
    return coverage;
  }

  public void dispose() {
    sessionManager.removeSessionListener(sessionListener);
  }

}
