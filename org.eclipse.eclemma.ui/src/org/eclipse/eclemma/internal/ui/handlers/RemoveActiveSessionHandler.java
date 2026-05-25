/*******************************************************************************
 * Copyright (c) 2006, 2020 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 ******************************************************************************/
package org.eclipse.eclemma.internal.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import org.eclipse.eclemma.core.CoverageTools;
import org.eclipse.eclemma.core.ICoverageSession;

/**
 * Handler to remove the currently active coverage session.
 */
public class RemoveActiveSessionHandler extends AbstractSessionManagerHandler {

  public RemoveActiveSessionHandler() {
    super(CoverageTools.getSessionManager());
  }

  @Override
  public boolean isEnabled() {
    return sessionManager.getActiveSession() != null;
  }

  public Object execute(ExecutionEvent event) throws ExecutionException {
    final ICoverageSession session = sessionManager.getActiveSession();
    sessionManager.removeSession(session);
    return null;
  }

}
