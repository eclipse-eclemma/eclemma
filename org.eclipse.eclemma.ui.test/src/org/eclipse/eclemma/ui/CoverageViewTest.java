/*******************************************************************************
 * Copyright (c) 2006, 2016 Mountainminds GmbH & Co. KG and Contributors
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
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
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
    bot.menu("Window").menu("Show View").menu("Other...").click();
    bot.shell("Show View").activate();

    SWTBotTreeItem treeItem = bot.tree().getTreeItem("Java").expand();
    treeItem.getNode("Coverage").select();
    bot.button("OK").click();

    // when
    SWTBotView view = bot.viewByTitle("Coverage");
    view.bot().tree().contextMenu("Import Session...").click();

    // then
    bot.shell("Import").activate();
    bot.text(" Please select an existing execution data file.");
  }

}
