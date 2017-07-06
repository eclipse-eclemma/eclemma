/*******************************************************************************
 * Copyright (c) 2006, 2017 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 ******************************************************************************/
package org.eclipse.eclemma.ui;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.eclemma.core.CoverageTools;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.JavaRuntime;

/**
 * Utility class to setup Java projects programatically.
 * 
 * TODO get rid of duplication with org.eclipse.eclemma.core.JavaProjectKit from org.eclipse.eclemma.core.test
 */
public class JavaProjectKit {

	private static final String DEFAULT_PROJECT_NAME = "UnitTestProject";

	public final IWorkspace workspace;

	public final IProject project;

	public final IJavaProject javaProject;

	public JavaProjectKit() throws CoreException {
		this(DEFAULT_PROJECT_NAME);
	}

	public JavaProjectKit(String name) throws CoreException {
		workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		project = root.getProject(name);
		project.create(null);
		project.open(null);
		IProjectDescription description = project.getDescription();
		description.setNatureIds(new String[] { JavaCore.NATURE_ID });
		project.setDescription(description, null);
		javaProject = JavaCore.create(project);
		javaProject.setRawClasspath(new IClasspathEntry[0], null);
		addClassPathEntry(JavaRuntime.getDefaultJREContainerEntry());
	}

	public void enableJava5() {
		javaProject.setOption(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_5);
		javaProject.setOption(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_5);
	}

	public IPackageFragmentRoot createSourceFolder() throws CoreException {
		IPackageFragmentRoot packageRoot = javaProject.getPackageFragmentRoot(javaProject.getResource());
		addClassPathEntry(JavaCore.newSourceEntry(packageRoot.getPath()));
		return packageRoot;
	}

	public IPackageFragment createPackage(IPackageFragmentRoot fragmentRoot, String name) throws CoreException {
		return fragmentRoot.createPackageFragment(name, false, null);
	}

	public ICompilationUnit createCompilationUnit(IPackageFragment fragment, String name, String content)
			throws JavaModelException {
		return fragment.createCompilationUnit(name, content, false, null);
	}

	public void addClassPathEntry(IClasspathEntry entry) throws CoreException {
		IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
		IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
		System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
		newEntries[oldEntries.length] = entry;
		javaProject.setRawClasspath(newEntries, null);
	}

	public static void waitForBuild() throws OperationCanceledException, InterruptedException {
		Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, null);
	}


	/**
	 * Creates launch configuration for the type with given name.
	 */
	public ILaunchConfiguration createLaunchConfiguration(String mainTypeName)
			throws Exception {
		ILaunchConfigurationType type = DebugPlugin.getDefault().getLaunchManager()
				.getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
		ILaunchConfigurationWorkingCopy config = type.newInstance(null, mainTypeName);
		config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, javaProject.getElementName());
		config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, mainTypeName);
		Set<String> modes = new HashSet<String>();
		modes.add(CoverageTools.LAUNCH_MODE);
		config.setPreferredLaunchDelegate(modes, IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
		return config.doSave();
	}

}
