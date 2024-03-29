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
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.After;
import org.junit.Test;

public class ExportImportTest {

  private static final SWTWorkbenchBot bot = new SWTWorkbenchBot();

  @After
  public void resetWorkbench() {
    bot.resetWorkbench();
  }

  @Test
  public void testImport() {
    bot.menu("File").menu("Import...").click();
    bot.shell("Import").activate();

    SWTBotTreeItem treeItem = bot.tree().getTreeItem("Run/Debug").expand();
    treeItem.getNode("Coverage Session").select();
    bot.button("Next >").click();

    if (UiTestUtils.TITLE_AREA_DIALOG_USES_LABEL) {
      bot.label(" Please select an existing execution data file.");
    } else {
      bot.text(" Please select an existing execution data file.");
    }
  }

  @Test
  public void testExport() {
    bot.menu("File").menu("Export...").click();
    bot.shell("Export").activate();

    SWTBotTreeItem treeItem = bot.tree().getTreeItem("Run/Debug").expand();
    treeItem.getNode("Coverage Session").select();
    bot.button("Next >").click();

    if (UiTestUtils.TITLE_AREA_DIALOG_USES_LABEL) {
      bot.label(" No coverage session available for export.");
    } else {
      bot.text(" No coverage session available for export.");
    }
  }

}
