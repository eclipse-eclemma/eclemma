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
package org.eclipse.eclemma.internal.core.launching;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

/**
 * Internal wrapper for {@link ILaunchConfigurationWorkingCopy} instances.
 *
 * @see AdjustedLaunchConfiguration
 */
class AdjustedLaunchConfigurationWorkingCopy extends
    AdjustedLaunchConfiguration implements ILaunchConfigurationWorkingCopy {

  private final ILaunchConfigurationWorkingCopy delegate;
  private final ILaunchConfiguration original;

  AdjustedLaunchConfigurationWorkingCopy(String extraVMArgument,
      ILaunchConfigurationWorkingCopy delegate, ILaunchConfiguration original) {
    super(extraVMArgument, delegate);
    this.delegate = delegate;
    this.original = original;
  }

  public ILaunchConfiguration getOriginal() {
    return original;
  }

  @Override
  public boolean isWorkingCopy() {
    return true;
  }

  public void setAttribute(String attributeName,
      @SuppressWarnings("rawtypes") Set value) {
    // Not supported in Eclipse 3.5
    throw new UnsupportedOperationException();
  }

  // delegate-only methods:

  public boolean isDirty() {
    return delegate.isDirty();
  }

  public ILaunchConfiguration doSave() throws CoreException {
    return delegate.doSave();
  }

  public ILaunchConfiguration doSave(int flag) throws CoreException {
    throw new UnsupportedOperationException();
  }

  public void setAttribute(String attributeName, int value) {
    delegate.setAttribute(attributeName, value);
  }

  public void setAttribute(String attributeName, String value) {
    delegate.setAttribute(attributeName, value);
  }

  public void setAttribute(String attributeName,
      @SuppressWarnings("rawtypes") List value) {
    delegate.setAttribute(attributeName, value);
  }

  public void setAttribute(String attributeName,
      @SuppressWarnings("rawtypes") Map value) {
    delegate.setAttribute(attributeName, value);
  }

  public void setAttribute(String attributeName, boolean value) {
    delegate.setAttribute(attributeName, value);
  }

  public void setAttribute(String attributeName, Object value) {
    throw new UnsupportedOperationException();
  }

  public void rename(String name) {
    delegate.rename(name);
  }

  public void setContainer(IContainer container) {
    delegate.setContainer(container);
  }

  public void setAttributes(@SuppressWarnings("rawtypes") Map attributes) {
    delegate.setAttributes(attributes);
  }

  public void setMappedResources(IResource[] resources) {
    delegate.setMappedResources(resources);
  }

  public void setModes(@SuppressWarnings("rawtypes") Set modes) {
    delegate.setModes(modes);
  }

  public void setPreferredLaunchDelegate(
      @SuppressWarnings("rawtypes") Set modes, String delegateId) {
    delegate.setPreferredLaunchDelegate(modes, delegateId);
  }

  public void addModes(@SuppressWarnings("rawtypes") Set modes) {
    delegate.addModes(modes);
  }

  public void removeModes(@SuppressWarnings("rawtypes") Set modes) {
    delegate.removeModes(modes);
  }

  public Object removeAttribute(String attributeName) {
    return delegate.removeAttribute(attributeName);
  }

  public ILaunchConfigurationWorkingCopy getParent() {
    return delegate.getParent();
  }

  public void copyAttributes(ILaunchConfiguration prototype) throws CoreException {
    throw new UnsupportedOperationException();
  }

  public void setPrototype(ILaunchConfiguration prototype, boolean copy) throws CoreException {
    throw new UnsupportedOperationException();
  }

}
