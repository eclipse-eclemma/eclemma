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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.jacoco.core.analysis.ICoverageNode;

import org.eclipse.eclemma.core.CoverageTools;

/**
 * Internal cache to calculate and keep the maximum total amount within a group.
 */
class MaxTotalCache {

  private final ViewSettings settings;
  private final ITreeContentProvider contentProvider;

  private Map<IJavaElement, Integer> maxTotals;

  MaxTotalCache(ViewSettings settings) {
    this.settings = settings;
    this.contentProvider = new WorkbenchContentProvider();
    this.maxTotals = new HashMap<IJavaElement, Integer>();
  }

  int getMaxTotal(Object element) {
    final IJavaElement parent = ((IJavaElement) element).getParent();
    Integer max = maxTotals.get(parent);
    if (max == null) {
      max = Integer.valueOf(calculateMaxTotal(parent));
      maxTotals.put(parent, max);
    }
    return max.intValue();
  }

  private int calculateMaxTotal(IJavaElement parent) {
    int max = 0;
    for (Object sibling : contentProvider.getChildren(parent)) {
      final ICoverageNode coverage = CoverageTools.getCoverageInfo(sibling);
      if (coverage != null) {
        max = Math.max(max, coverage.getCounter(settings.getCounters())
            .getTotalCount());
      }
    }
    return max;
  }

  void reset() {
    maxTotals.clear();
  }

}
