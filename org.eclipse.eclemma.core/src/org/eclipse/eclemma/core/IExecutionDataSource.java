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
package org.eclipse.eclemma.core;

import org.eclipse.core.runtime.CoreException;
import org.jacoco.core.data.IExecutionDataVisitor;
import org.jacoco.core.data.ISessionInfoVisitor;

/**
 * Common interface for all sources of execution data.
 */
public interface IExecutionDataSource {

  /**
   * Emits all stored execution data in the given visitors.
   *
   * @param executionDataVisitor
   *          visitor for execution data
   * @param visitor
   *          for session information
   */
  public abstract void accept(IExecutionDataVisitor executionDataVisitor,
      ISessionInfoVisitor sessionInfoVisitor) throws CoreException;

}
