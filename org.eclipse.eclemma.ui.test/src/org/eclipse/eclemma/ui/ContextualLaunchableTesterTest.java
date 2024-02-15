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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.eclemma.internal.ui.EclEmmaUIPlugin;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.ui.IEditorPart;
import org.junit.After;
import org.junit.Test;

public class ContextualLaunchableTesterTest {

  private static final SWTWorkbenchBot bot = new SWTWorkbenchBot();

  @After
  public void resetWorkbench() {
    bot.resetWorkbench();
  }

  @Test
  public void error_message_should_contain_delegate_shortcut_id()
      throws Exception {
    final LogListener logListener = new LogListener();
    Platform.addLogListener(logListener);

    final String projectName = "ContextualLaunchableTesterTest";
    new JavaProjectKit(projectName);

    final SWTBotView view = bot.viewByTitle("Project Explorer");
    view.show();
    final SWTBotTree tree = view.bot().tree();
    tree.setFocus();
    SWTBotMenu menu = tree.select(projectName).contextMenu("Coverage As");
    menu.click();

    // Since
    // https://github.com/eclipse-platform/eclipse.platform/commit/494483ea135ec22a0327ec4c9500c693555d710f
    // calculation of ContextualLaunchAction::isApplicable happens
    // asynchronously, so need to wait for the appearance of the menu
    menu.menu("Coverage Configurations...");

    Platform.removeLogListener(logListener);

    final IStatus actualStatus = logListener.statuses.get(1);
    assertEquals(EclEmmaUIPlugin.ID, actualStatus.getPlugin());
    assertEquals(
        "Launch shortcut 'org.eclipse.eclemma.ui.ContextualLaunchableTesterTest.fakeShortcut' enablement expression caused exception.",
        actualStatus.getMessage());
    assertEquals(
        "No property tester contributes a property org.eclipse.eclemma.unknownProperty to type class org.eclipse.core.internal.resources.Project",
        actualStatus.getException().getMessage());
  }

  private static class LogListener implements ILogListener {
    final List<IStatus> statuses = new ArrayList<IStatus>();

    public synchronized void logging(IStatus status, String plugin) {
      if (status.getSeverity() == IStatus.ERROR) {
        statuses.add(status);
      }
    }
  }

  public static class FakeLaunchShortcut implements ILaunchShortcut {
    public void launch(ISelection selection, String mode) {
    }

    public void launch(IEditorPart editor, String mode) {
    }
  }

}
