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
package org.eclipse.eclemma.internal.ui.coverageview;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import org.eclipse.eclemma.core.ISessionManager;
import org.eclipse.eclemma.internal.ui.handlers.AbstractSessionManagerHandler;

/**
 * This handler reloads the active coverage session.
 */
class RefreshSessionHandler extends AbstractSessionManagerHandler {

  public RefreshSessionHandler(ISessionManager sessionManager) {
    super(sessionManager);
  }

  @Override
  public boolean isEnabled() {
    return sessionManager.getActiveSession() != null;
  }

  public Object execute(ExecutionEvent event) throws ExecutionException {
    sessionManager.refreshActiveSession();
    return null;
  }

}
