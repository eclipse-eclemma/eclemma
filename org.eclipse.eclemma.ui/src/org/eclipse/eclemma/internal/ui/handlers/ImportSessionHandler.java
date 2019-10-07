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
package org.eclipse.eclemma.internal.ui.handlers;

import java.util.Collections;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.CommandException;
import org.eclipse.eclemma.core.CoverageTools;
import org.eclipse.eclemma.internal.ui.EclEmmaUIPlugin;
import org.eclipse.eclemma.internal.ui.wizards.SessionImportWizard;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;

/**
 * Handler to import a JaCoCo coverage session.
 *
 * Unlike the default handler for the import command, this implementation does
 * not overwrite menu icons and labels.
 */
public class ImportSessionHandler extends AbstractSessionManagerHandler {

  public ImportSessionHandler() {
    super(CoverageTools.getSessionManager());
  }

  public Object execute(ExecutionEvent event) throws ExecutionException {

    final IWorkbenchSite site = HandlerUtil.getActiveSite(event);
    final ICommandService cs = (ICommandService) site
        .getService(ICommandService.class);
    final IHandlerService hs = (IHandlerService) site
        .getService(IHandlerService.class);
    final Command command = cs
        .getCommand(IWorkbenchCommandConstants.FILE_IMPORT);

    try {
      hs.executeCommand(ParameterizedCommand.generateCommand(command,
          Collections.singletonMap(
              IWorkbenchCommandConstants.FILE_IMPORT_PARM_WIZARDID,
              SessionImportWizard.ID)),
          null);
    } catch (CommandException e) {
      EclEmmaUIPlugin.log(e);
    }

    return null;
  }

}
