/*******************************************************************************
 * Copyright (c) 2006, 2020 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Evgeny Mandrikov - initial API and implementation
 *
 ******************************************************************************/
package org.eclipse.eclemma.ui;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.eclemma.core.CoverageTools;
import org.eclipse.eclemma.core.ISessionManager;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DumpExecutionDataTest {

  private static final SWTWorkbenchBot bot = new SWTWorkbenchBot();

  private static ILaunch launch1;
  private static String terminate1;
  private static ILaunch launch2;
  private static String terminate2;

  @AfterClass
  public static void resetWorkbench() {
    bot.resetWorkbench();
  }

  @BeforeClass
  public static void setup() throws Exception {
    openCoverageView();

    JavaProjectKit project = new JavaProjectKit();
    project.enableJava();
    final IPackageFragmentRoot root = project.createSourceFolder();
    final IPackageFragment fragment = project.createPackage(root, "example");
    project.createCompilationUnit(fragment, "Example.java", "package example;" //
        + "import java.io.File;" //
        + "class Example {" //
        + "  public static void main(String[] args) throws Exception {" //
        + "    File termFile = new File(args[0]);" //
        + "    while (!termFile.exists()) {" //
        + "      Thread.sleep(100);" //
        + "    }" //
        + "  }" //
        + "}");
    JavaProjectKit.waitForBuild();

    terminate1 = project.project.getLocation().toString() + "/terminate1";
    ILaunchConfiguration launchConfiguration1 = project
        .createLaunchConfiguration("example.Example", terminate1);
    launch1 = launchConfiguration1.launch(CoverageTools.LAUNCH_MODE, null);

    terminate2 = project.project.getLocation().toString() + "/terminate2";
    ILaunchConfiguration launchConfiguration2 = project
        .createLaunchConfiguration("example.Example", terminate2);
    launch2 = launchConfiguration2.launch(CoverageTools.LAUNCH_MODE, null);
  }

  @Test
  public void dumpDialog_should_abort_without_errors_when_nothing_was_selected()
      throws Exception {
    final LogListener logListener = new LogListener();
    Platform.addLogListener(logListener);

    SWTBotView coverageView = bot.viewByTitle("Coverage");
    coverageView.show();

    coverageView.toolbarButton("Dump Execution Data").click();
    bot.shell("Dump Execution Data").activate();
    bot.button("Cancel").click();

    // On Linux one element will be selected by default, however for some reason
    // nothing is selected on Mac OS X and Windows:
    coverageView.toolbarButton("Dump Execution Data").click();
    bot.shell("Dump Execution Data").activate();
    bot.button("OK").click();

    Platform.removeLogListener(logListener);

    assertEquals(0, logListener.errors);
  }

  @AfterClass
  public static void terminate() throws Exception {
    new File(terminate1).createNewFile();
    new File(terminate2).createNewFile();
    while (!launch1.isTerminated() || !launch2.isTerminated()) {
      Thread.sleep(100);
    }

    final ISessionManager sessionManager = CoverageTools.getSessionManager();
    sessionManager.removeSessionsFor(launch1);
    sessionManager.removeSessionsFor(launch2);
  }

  static class LogListener implements ILogListener {
    int errors;

    public synchronized void logging(IStatus status, String plugin) {
      if (status.getSeverity() == IStatus.ERROR) {
        errors++;
      }
    }
  }

  private static void openCoverageView() {
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
  }

}
