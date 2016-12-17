/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.polygene.gradle.dependencies

import groovy.transform.CompileStatic
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.DependencySubstitution
import org.gradle.api.artifacts.component.ModuleComponentSelector
import org.gradle.api.artifacts.repositories.MavenArtifactRepository

@CompileStatic
class DependenciesPlugin implements Plugin<Project>
{
  @Override
  void apply( final Project project )
  {
    def dependenciesDeclaration = project.rootProject.extensions.getByType( DependenciesDeclarationExtension )
    applyRepositories( project, dependenciesDeclaration )
    applyLibraries( project, dependenciesDeclaration )
    applyDependencyResolutionRules( project, dependenciesDeclaration )
    applyDefaultDependencies( project, dependenciesDeclaration )
  }

  private static void applyRepositories( Project project, DependenciesDeclarationExtension declaration )
  {
    declaration.repositoriesUrls.each { name, url ->
      project.repositories.maven { MavenArtifactRepository repo ->
        repo.name = name
        repo.url = url
      }
    }
  }

  private static void applyLibraries( Project project, DependenciesDeclarationExtension declaration )
  {
    project.extensions.extraProperties.set 'libraries', declaration.libraries
  }

  private static void applyDependencyResolutionRules( Project project, DependenciesDeclarationExtension declaration )
  {
    project.configurations.all(
      { Configuration configuration ->
        configuration.resolutionStrategy.dependencySubstitution.all(
          { DependencySubstitution dep ->
            if( dep.requested instanceof ModuleComponentSelector )
            {
              def selector = dep.requested as ModuleComponentSelector
              declaration.dependencySubstitutionSpec.execute dep, selector
            }
          } as Action<DependencySubstitution> )
      } as Action<Configuration> )
  }

  private static void applyDefaultDependencies( Project project, DependenciesDeclarationExtension declaration )
  {
    declaration.defaultDependencies.each { String configuration, List<Object> dependencies ->
      dependencies.each { dependency ->
        if( dependency instanceof Collection )
        {
          dependency.each { subdep ->
            project.dependencies.add( configuration, subdep )
          }
        }
        else
        {
          project.dependencies.add( configuration, dependency )
        }
      }
    }
  }
}