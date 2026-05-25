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

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import org.eclipse.eclemma.core.CoverageTools;
import org.eclipse.eclemma.core.ICoverageSession;
import org.eclipse.eclemma.core.ISessionManager;
import org.eclipse.eclemma.internal.ui.EclEmmaUIPlugin;
import org.eclipse.eclemma.internal.ui.UIMessages;

/**
 * Dynamically created menu items for selecting the current coverage session.
 */
public class SelectActiveSessionsItems extends ContributionItem {

  @Override
  public boolean isDynamic() {
    return true;
  }

  @Override
  public void fill(final Menu menu, int index) {
    final ISessionManager sm = CoverageTools.getSessionManager();
    final ICoverageSession activeSession = sm.getActiveSession();
    int position = 1;
    for (ICoverageSession session : sm.getSessions()) {
      createItem(menu, index++, session, session == activeSession, position++,
          sm);
    }
  }

  private void createItem(final Menu parent, final int index,
      final ICoverageSession session, final boolean selected,
      final int position, final ISessionManager sm) {
    final MenuItem item = new MenuItem(parent, SWT.RADIO, index);
    item.setImage(EclEmmaUIPlugin.getImage(EclEmmaUIPlugin.ELCL_SESSION));
    item.setText(getLabel(session, position));
    item.setSelection(selected);
    item.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        sm.activateSession(session);
      }
    });
  }

  private String getLabel(ICoverageSession session, int idx) {
    return NLS.bind(UIMessages.CoverageViewSelectSessionMenu_label,
        Integer.valueOf(idx), session.getDescription());
  }

}
