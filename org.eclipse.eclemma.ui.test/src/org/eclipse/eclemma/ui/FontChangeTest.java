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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.eclemma.core.ICoverageSession;
import org.eclipse.eclemma.core.launching.ICoverageLaunch;
import org.eclipse.eclemma.internal.ui.dialogs.MergeSessionsDialog;
import org.eclipse.eclemma.internal.ui.handlers.DumpExecutionDataHandler;
import org.eclipse.eclemma.internal.ui.handlers.SelectActiveSessionHandler;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.matchers.WidgetMatcherFactory;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FontChangeTest {

  private static final SWTWorkbenchBot bot = new SWTWorkbenchBot();

  private FontData expected;

  @BeforeClass
  public static void closeWelcomeView() {
    try {
      bot.viewByTitle("Welcome").close();
    } catch (WidgetNotFoundException e) {
      // ignore
    }
  }

  @Before
  public void changeFont() {
    ScopedPreferenceStore store = new ScopedPreferenceStore(InstanceScope.INSTANCE, "org.eclipse.ui.workbench");
    expected = new FontData(store.getString(JFaceResources.DIALOG_FONT));
    expected.setHeight(expected.getHeight() + 1);
    store.setValue(JFaceResources.DIALOG_FONT, expected.toString());
  }

  @After
  public void resetWorkbench() {
    bot.resetWorkbench();
  }

  /**
   * Test for {@link org.eclipse.eclemma.internal.ui.dialogs.CoveragePreferencePage}.
   */
  @Test
  public void preferencePage() {
    // This does not work on Mac
    // bot.menu("Window").menu("Preferences").click();
    // Launch preferences programmatically instead
    UIThreadRunnable.asyncExec(new VoidResult() {
      public void run() {
        ActionFactory.PREFERENCES.create(PlatformUI.getWorkbench().getActiveWorkbenchWindow()).run();
      }
    });

    bot.shell("Preferences").activate();
    bot.tree().getTreeItem("Java").expand().getNode("Code Coverage").select();

    assertFont(expected, bot.widget(WidgetMatcherFactory.withId("org.eclipse.ui.help", "org.eclipse.eclemma.ui.coverage_preferences_context")));
  }

  /**
   * Test for {@link org.eclipse.eclemma.internal.ui.wizards.SessionImportPage1} and {@link org.eclipse.eclemma.internal.ui.wizards.SessionImportPage2}.
   */
  @Test
  public void importWizard() {
    bot.menu("File").menu("Import...").click();
    bot.shell("Import").activate();

    SWTBotTreeItem treeItem = bot.tree().getTreeItem("Run/Debug").expand();
    treeItem.getNode("Coverage Session").select();
    bot.button("Next >").click();

    assertFont(expected, bot.widget(WidgetMatcherFactory.withId("org.eclipse.ui.help", "org.eclipse.eclemma.ui.session_import_context")));

    bot.radio("Agent address:").click();
    bot.button("Next >").click();

    assertFont(expected, bot.widget(WidgetMatcherFactory.withId("org.eclipse.ui.help", "org.eclipse.eclemma.ui.session_import_context")));
  }

  /**
   * Test for {@link org.eclipse.eclemma.internal.ui.wizards.SessionExportPage1}.
   */
  @Test
  public void exportWizard() {
    bot.menu("File").menu("Export...").click();
    bot.shell("Export").activate();

    SWTBotTreeItem treeItem = bot.tree().getTreeItem("Run/Debug").expand();
    treeItem.getNode("Coverage Session").select();
    bot.button("Next >").click();

    assertFont(expected, bot.widget(WidgetMatcherFactory.withId("org.eclipse.ui.help", "org.eclipse.eclemma.ui.session_export_context")));
  }

  /**
   * Test for {@link org.eclipse.eclemma.internal.ui.dialogs.CoveragePropertyPage}.
   */
  @Test
  public void propertyPage() throws CoreException {
    createProject();

    bot.viewByTitle("Project Explorer").bot().tree().select("prj").contextMenu().menu("Properties", false, 0).click();
    bot.shell("Properties for prj").activate();
    bot.tree().select("Coverage");

    assertFont(expected, bot.widget(WidgetMatcherFactory.withId("org.eclipse.ui.help", "org.eclipse.eclemma.ui.coverage_properties_context")));
  }

  /**
   * Test for {@link org.eclipse.eclemma.ui.launching.CoverageTab}.
   */
  @Test
  public void launchingTab() throws CoreException {
    createProject();

    bot.viewByTitle("Project Explorer").bot().tree().select("prj").contextMenu("Coverage As").menu("Coverage Configurations...").click();
    bot.shell("Coverage Configurations").activate();
    bot.tree().getTreeItem("Java Application").contextMenu("New").click();
    bot.cTabItem("Coverage").activate();

    assertFont(expected, bot.widget(WidgetMatcherFactory.withId("org.eclipse.ui.help", "org.eclipse.eclemma.ui.coverage_launch_tab_context")));
  }

  private static void createProject() throws CoreException {
    IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("prj");
    if (project.exists()) {
      project.delete(true, null);
    }
    project.create(null);
    project.open(null);
    IProjectDescription description = project.getDescription();
    description.setNatureIds(new String[] { JavaCore.NATURE_ID });
    project.setDescription(description, null);
  }

  /**
   * Test for {@link org.eclipse.eclemma.internal.ui.dialogs.MergeSessionsDialog}.
   */
  @Test
  public void mergeSessionsDialog() {
    UIThreadRunnable.asyncExec(new VoidResult() {
      public void run() {
        new MergeSessionsDialog(bot.activeShell().widget, new ArrayList<ICoverageSession>(), "").open();
      }
    });

    UIThreadRunnable.syncExec(new VoidResult() {
      public void run() {
        assertFont(expected, bot.shellWithId("org.eclipse.ui.help", "org.eclipse.eclemma.ui.merge_sessions_context").widget.getChildren()[0]);
      }
    });
  }

  /**
   * Test for {@link org.eclipse.eclemma.internal.ui.handlers.SelectActiveSessionHandler}.
   */
  @Test
  public void selectActiveSessionDialog() throws ExecutionException {
    UIThreadRunnable.asyncExec(new VoidResult() {
      public void run() {
        try {
          new SelectActiveSessionHandler().execute(new ExecutionEvent());
        } catch (ExecutionException e) {
          throw new RuntimeException(e);
        }
      }
    });

    assertFont(expected, bot.shellWithId("org.eclipse.ui.help", "org.eclipse.eclemma.ui.select_active_session_context").widget);
  }

  /**
   * Test for {@link org.eclipse.eclemma.internal.ui.handlers.DumpExecutionDataHandler#openDialog(ExecutionEvent, java.util.List)}.
   */
  @Test
  public void selectLaunchDialog() {
    UIThreadRunnable.asyncExec(new VoidResult() {
      public void run() {
        DumpExecutionDataHandler.openDialog(new ExecutionEvent(), new ArrayList<ICoverageLaunch>());
      }
    });

    assertFont(expected, bot.shellWithId("org.eclipse.ui.help", "org.eclipse.eclemma.ui.dump_execution_data").widget);
  }

  private static void assertFont(final FontData expected, final Widget parentWidget) {
    UIThreadRunnable.syncExec(new VoidResult() {
      public void run() {
        for (Control widget : bot.widgets(CoreMatchers.any(Control.class), parentWidget)) {
          FontData actual = widget.getFont().getFontData()[0];
          assertEquals(widget + " font", expected, actual);
        }
      }
    });
  }

}
