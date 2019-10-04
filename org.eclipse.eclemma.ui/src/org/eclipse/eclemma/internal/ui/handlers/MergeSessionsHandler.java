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
package org.eclipse.eclemma.internal.ui.handlers;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import org.eclipse.eclemma.core.CoverageTools;
import org.eclipse.eclemma.core.EclEmmaStatus;
import org.eclipse.eclemma.core.ICoverageSession;
import org.eclipse.eclemma.core.ISessionManager;
import org.eclipse.eclemma.internal.ui.UIMessages;
import org.eclipse.eclemma.internal.ui.dialogs.MergeSessionsDialog;

/**
 * Handler to merge session coverage session.
 */
public class MergeSessionsHandler extends AbstractSessionManagerHandler {

  public MergeSessionsHandler() {
    super(CoverageTools.getSessionManager());
  }

  @Override
  public boolean isEnabled() {
    return sessionManager.getSessions().size() > 1;
  }

  public Object execute(ExecutionEvent event) throws ExecutionException {
    final Shell parentShell = HandlerUtil.getActiveShell(event);
    final ISessionManager sm = CoverageTools.getSessionManager();
    List<ICoverageSession> sessions = sm.getSessions();
    String descr = UIMessages.MergeSessionsDialogDescriptionDefault_value;
    descr = MessageFormat.format(descr, new Object[] { new Date() });
    final MergeSessionsDialog d = new MergeSessionsDialog(parentShell,
        sessions, descr);
    if (d.open() == IDialogConstants.OK_ID) {
      createJob(sm, d.getSessions(), d.getDescription()).schedule();
    }
    return null;
  }

  private Job createJob(final ISessionManager sm,
      final Collection<ICoverageSession> sessions, final String description) {
    final Job job = new Job(UIMessages.MergingSessions_task) {

      @Override
      protected IStatus run(IProgressMonitor monitor) {
        try {
          sm.mergeSessions(sessions, description, monitor);
        } catch (CoreException e) {
          return EclEmmaStatus.MERGE_SESSIONS_ERROR.getStatus(e);
        }
        return Status.OK_STATUS;
      }
    };
    job.setPriority(Job.SHORT);
    return job;
  }

}
