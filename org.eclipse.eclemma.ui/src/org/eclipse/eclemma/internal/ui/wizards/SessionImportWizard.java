/*******************************************************************************
 * Copyright (c) 2006, 2019 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 ******************************************************************************/
package org.eclipse.eclemma.internal.ui.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import org.eclipse.eclemma.core.CoverageTools;
import org.eclipse.eclemma.core.ISessionImporter;
import org.eclipse.eclemma.internal.ui.EclEmmaUIPlugin;
import org.eclipse.eclemma.internal.ui.UIMessages;

/**
 * The import wizard for coverage sessions.
 */
public class SessionImportWizard extends Wizard implements IImportWizard {

  public static final String ID = "org.eclipse.eclemma.ui.sessionImportWizard"; //$NON-NLS-1$

  private static final String SETTINGSID = "SessionImportWizard"; //$NON-NLS-1$

  private SessionImportPage1 page1;
  private SessionImportPage2 page2;

  public SessionImportWizard() {
    super();
    IDialogSettings pluginsettings = EclEmmaUIPlugin.getInstance()
        .getDialogSettings();
    IDialogSettings wizardsettings = pluginsettings.getSection(SETTINGSID);
    if (wizardsettings == null) {
      wizardsettings = pluginsettings.addNewSection(SETTINGSID);
    }
    setDialogSettings(wizardsettings);
    setWindowTitle(UIMessages.ImportSession_title);
    setDefaultPageImageDescriptor(EclEmmaUIPlugin
        .getImageDescriptor(EclEmmaUIPlugin.WIZBAN_IMPORT_SESSION));
    setNeedsProgressMonitor(true);
  }

  public void init(IWorkbench workbench, IStructuredSelection selection) {
    // nothing to initialize
  }

  @Override
  public void addPages() {
    page1 = new SessionImportPage1();
    addPage(page1);
    page2 = new SessionImportPage2();
    addPage(page2);
  }

  @Override
  public boolean performFinish() {
    page1.saveWidgetValues();
    page2.saveWidgetValues();
    return importSession();
  }

  private boolean importSession() {
    final ISessionImporter importer = CoverageTools.getImporter();
    importer.setDescription(page2.getSessionDescription());
    importer.setExecutionDataSource(page1.getExecutionDataSource());
    importer.setScope(page2.getScope());
    importer.setCopy(page1.getCreateCopy());
    IRunnableWithProgress op = new IRunnableWithProgress() {
      public void run(IProgressMonitor monitor)
          throws InvocationTargetException, InterruptedException {
        try {
          importer.importSession(monitor);
        } catch (Exception e) {
          throw new InvocationTargetException(e);
        }
      }
    };
    try {
      getContainer().run(true, true, op);
    } catch (InterruptedException e) {
      return false;
    } catch (InvocationTargetException ite) {
      Throwable ex = ite.getTargetException();
      EclEmmaUIPlugin.log(ex);
      final String title = UIMessages.ImportSessionErrorDialog_title;
      final String msg = UIMessages.ImportSessionErrorDialog_message;
      final IStatus status = EclEmmaUIPlugin.errorStatus(
          String.valueOf(ex.getMessage()), ex);
      ErrorDialog.openError(getShell(), title, msg, status);
      return false;
    }
    return true;
  }

}
