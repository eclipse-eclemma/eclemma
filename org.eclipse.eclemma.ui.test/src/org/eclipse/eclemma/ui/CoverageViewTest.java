/*******************************************************************************
 * Copyright (c) 2006, 2020 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Evgeny Mandrikov - initial API and implementation
 *
 ******************************************************************************/
package org.eclipse.eclemma.ui;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.junit.After;
import org.junit.Test;

public class CoverageViewTest {

  private static final SWTWorkbenchBot bot = new SWTWorkbenchBot();

  @After
  public void resetWorkbench() {
    bot.resetWorkbench();
  }

  @Test
  public void testImportSession() {
    // given
    UIThreadRunnable.syncExec(new VoidResult() {
      public void run() {
        try {
          PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
              .showView("org.eclipse.eclemma.ui.CoverageView");
        } catch (PartInitException e) {
          e.printStackTrace();
        }
      }
    });

    // when
    SWTBotView view = bot.viewByTitle("Coverage");
    view.bot().tree().contextMenu("Import Session...").click();

    // then
    bot.shell("Import").activate();
    if (UiTestUtils.TITLE_AREA_DIALOG_USES_LABEL) {
      bot.label(" Please select an existing execution data file.");
    } else {
      bot.text(" Please select an existing execution data file.");
    }
  }

}
