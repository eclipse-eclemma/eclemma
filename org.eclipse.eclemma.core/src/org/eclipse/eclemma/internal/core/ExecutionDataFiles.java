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
package org.eclipse.eclemma.internal.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.jacoco.core.data.ExecutionDataWriter;

import org.eclipse.eclemma.core.EclEmmaStatus;
import org.eclipse.eclemma.core.IExecutionDataSource;
import org.eclipse.eclemma.core.URLExecutionDataSource;

/**
 * Internal utility to create and cleanup execution data files manage files in
 * the plugin's state location.
 */
public final class ExecutionDataFiles {

  private static final String FOLDER = ".execdata/"; //$NON-NLS-1$

  private final File folder;

  public ExecutionDataFiles(IPath stateLocation) {
    folder = stateLocation.append(FOLDER).toFile();
    folder.mkdirs();
  }

  /**
   * Delete any existing execution data file.
   */
  public void deleteTemporaryFiles() {
    final File[] files = folder.listFiles();
    for (final File file : files) {
      file.delete();
    }
  }

  /**
   * Creates a new execution data file containing the content of the given
   * source.
   *
   * @param source
   *          source to dump into the file
   * @return created file
   */
  public IExecutionDataSource newFile(IExecutionDataSource source)
      throws CoreException {
    try {
      final File file = File.createTempFile("session", ".exec", folder); //$NON-NLS-1$ //$NON-NLS-2$
      final OutputStream out = new BufferedOutputStream(new FileOutputStream(
          file));
      final ExecutionDataWriter writer = new ExecutionDataWriter(out);
      source.accept(writer, writer);
      out.close();
      return new URLExecutionDataSource(file.toURL());
    } catch (IOException e) {
      throw new CoreException(EclEmmaStatus.EXEC_FILE_CREATE_ERROR.getStatus(e));
    }
  }

}
