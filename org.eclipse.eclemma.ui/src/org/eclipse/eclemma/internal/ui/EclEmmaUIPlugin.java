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
package org.eclipse.eclemma.internal.ui;

import java.net.URL;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import org.eclipse.eclemma.core.CoverageTools;
import org.eclipse.eclemma.core.ICoverageSession;
import org.eclipse.eclemma.core.ISessionListener;
import org.eclipse.eclemma.internal.ui.annotation.EditorTracker;
import org.eclipse.eclemma.internal.ui.coverageview.CoverageView;

/**
 * Plug-in activator for the UI.
 */
public class EclEmmaUIPlugin extends AbstractUIPlugin {

  public static final String ID = "org.eclipse.eclemma.ui"; //$NON-NLS-1$

  /** Identifier for the 'coverage' launch group. */
  public static final String ID_COVERAGE_LAUNCH_GROUP = ID
      + ".launchGroup.coverage"; //$NON-NLS-1$

  // Icons used by the Plugin

  public static final String ELCL_SESSION = "icons/full/elcl16/session.png"; //$NON-NLS-1$
  public static final String ELCL_DUMP = "icons/full/elcl16/dump.png"; //$NON-NLS-1$

  public static final String EVIEW_COVERAGE = "icons/full/eview16/coverage.png"; //$NON-NLS-1$
  public static final String EVIEW_EXEC = "icons/full/eview16/exec.png"; //$NON-NLS-1$

  public static final String OBJ_SESSION = "icons/full/elcl16/session.png"; //$NON-NLS-1$
  public static final String OBJ_MARKERFULL = "icons/full/obj16/markerfull.png"; //$NON-NLS-1$
  public static final String OBJ_MARKERNO = "icons/full/obj16/markerno.png"; //$NON-NLS-1$
  public static final String OBJ_MARKERPARTIAL = "icons/full/obj16/markerpartial.png"; //$NON-NLS-1$

  private static final String[] OBJ_COVERAGE_OVERLAY = new String[] {
      "icons/full/ovr16/coverage00.png", //$NON-NLS-1$
      "icons/full/ovr16/coverage01.png", //$NON-NLS-1$
      "icons/full/ovr16/coverage02.png", //$NON-NLS-1$
      "icons/full/ovr16/coverage03.png", //$NON-NLS-1$
      "icons/full/ovr16/coverage04.png", //$NON-NLS-1$
      "icons/full/ovr16/coverage05.png", //$NON-NLS-1$
      "icons/full/ovr16/coverage06.png", //$NON-NLS-1$
      "icons/full/ovr16/coverage07.png" //$NON-NLS-1$
  };

  public static final String WIZBAN_EXPORT_SESSION = "icons/full/wizban/export_session.png"; //$NON-NLS-1$
  public static final String WIZBAN_IMPORT_SESSION = "icons/full/wizban/import_session.png"; //$NON-NLS-1$

  public static final String DGM_REDBAR = "icons/full/dgm/redbar.png"; //$NON-NLS-1$
  public static final String DGM_GREENBAR = "icons/full/dgm/greenbar.png"; //$NON-NLS-1$

  private static EclEmmaUIPlugin instance;

  private EditorTracker editorTracker;

  private ISessionListener sessionListener = new ISessionListener() {
    public void sessionAdded(ICoverageSession addedSession) {
      if (getPreferenceStore()
          .getBoolean(UIPreferences.PREF_SHOW_COVERAGE_VIEW)) {
        getWorkbench().getDisplay().asyncExec(new Runnable() {
          public void run() {
            showCoverageView();
          }
        });
      }
    }

    public void sessionRemoved(ICoverageSession removedSession) {
    }

    public void sessionActivated(ICoverageSession session) {
    }
  };

  @Override
  public void start(BundleContext context) throws Exception {
    super.start(context);
    CoverageTools.setPreferences(UIPreferences.CORE_PREFERENCES);
    CoverageTools.getSessionManager().addSessionListener(sessionListener);
    editorTracker = new EditorTracker(getWorkbench());
    instance = this;
  }

  @Override
  public void stop(BundleContext context) throws Exception {
    instance = null;
    editorTracker.dispose();
    CoverageTools.getSessionManager().removeSessionListener(sessionListener);
    super.stop(context);
  }

  public static EclEmmaUIPlugin getInstance() {
    return instance;
  }

  private void showCoverageView() {
    IWorkbenchWindow window = getWorkbench().getActiveWorkbenchWindow();
    if (window == null)
      return;
    IWorkbenchPage page = window.getActivePage();
    if (page != null) {
      try {
        IViewPart view = page.showView(CoverageView.ID, null,
            IWorkbenchPage.VIEW_CREATE);
        page.bringToTop(view);
      } catch (PartInitException e) {
        log(e);
      }
    }
  }

  public Shell getShell() {
    return getWorkbench().getActiveWorkbenchWindow().getShell();
  }

  public static IStatus errorStatus(String message, Throwable t) {
    return new Status(IStatus.ERROR, ID, IStatus.ERROR, message, t);
  }

  public static void log(Throwable t) {
    String message = t.getMessage();
    if (message == null) {
      message = "Internal Error"; //$NON-NLS-1$
    }
    instance.getLog().log(errorStatus(message, t));
  }

  public static ImageDescriptor getImageDescriptor(String key) {
    return loadImage(key).getDescriptor(key);
  }

  public static Image getImage(String key) {
    return loadImage(key).get(key);
  }

  public static ImageDescriptor getCoverageOverlay(double ratio) {
    int idx = (int) Math.round(ratio * OBJ_COVERAGE_OVERLAY.length);
    if (idx < 0)
      idx = 0;
    if (idx >= OBJ_COVERAGE_OVERLAY.length)
      idx = OBJ_COVERAGE_OVERLAY.length - 1;
    return getImageDescriptor(OBJ_COVERAGE_OVERLAY[idx]);
  }

  private static ImageRegistry loadImage(String path) {
    ImageRegistry reg = getInstance().getImageRegistry();
    if (reg.getDescriptor(path) == null) {
      URL url = instance.getBundle().getEntry(path);
      reg.put(path, ImageDescriptor.createFromURL(url));
    }
    return reg;
  }

}
