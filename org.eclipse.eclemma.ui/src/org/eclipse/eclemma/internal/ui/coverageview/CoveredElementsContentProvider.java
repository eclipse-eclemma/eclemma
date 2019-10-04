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
package org.eclipse.eclemma.internal.ui.coverageview;

import org.eclipse.ui.model.WorkbenchContentProvider;

import org.eclipse.eclemma.core.analysis.IJavaModelCoverage;

/**
 * Specialized workbench content provider that selects entry elements depending
 * on the view setting (projects, package roots, packages or types).
 */
class CoveredElementsContentProvider extends WorkbenchContentProvider {

  private final ViewSettings settings;

  public CoveredElementsContentProvider(ViewSettings settings) {
    this.settings = settings;
  }

  @Override
  public Object[] getElements(Object element) {
    IJavaModelCoverage coverage = (IJavaModelCoverage) element;
    if (coverage == IJavaModelCoverage.LOADING) {
      return new Object[] { CoverageView.LOADING_ELEMENT };
    }
    if (coverage != null) {
      switch (settings.getRootType()) {
      case GROUP:
        return coverage.getProjects();
      case BUNDLE:
        return coverage.getPackageFragmentRoots();
      case PACKAGE:
        return coverage.getPackageFragments();
      case CLASS:
        return coverage.getTypes();
      }
    }
    return new Object[0];
  }

}
