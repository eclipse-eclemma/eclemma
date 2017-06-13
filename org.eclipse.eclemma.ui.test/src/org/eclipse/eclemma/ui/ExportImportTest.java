/*******************************************************************************
 * Copyright (c) 2006, 2017 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

    bot.text(" Please select an existing execution data file.");
  }

  @Test
  public void testExport() {
    bot.menu("File").menu("Export...").click();
    bot.shell("Export").activate();

    SWTBotTreeItem treeItem = bot.tree().getTreeItem("Run/Debug").expand();
    treeItem.getNode("Coverage Session").select();
    bot.button("Next >").click();

    bot.text(" No coverage session available for export.");
  }

}
