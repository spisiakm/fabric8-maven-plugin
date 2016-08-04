/*
 * Copyright 2016 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */

package io.fabric8.maven.enricher.standard;

import java.util.HashMap;
import java.util.Map;

import io.fabric8.maven.core.util.KubernetesResourceUtil;
import io.fabric8.maven.enricher.api.BaseEnricher;
import io.fabric8.maven.enricher.api.EnricherContext;
import io.fabric8.maven.enricher.api.Kind;
import org.apache.maven.project.MavenProject;

/**
 * Add project labels to any object.
 * For selectors, the 'version' part is removed.
 *
 * The following labels are added:
 * <ul>
 *     <li>version</li>
 *     <li>project</li>
 *     <li>group</li>
 *     <li>provider (is set to fabric8)</li>
 * </ul>
 * @author roland
 * @since 01/04/16
 */
public class ProjectEnricher extends BaseEnricher {

    public ProjectEnricher(EnricherContext buildContext) {
        super(buildContext, "f8-project");
    }

    // For the moment return labels for all objects
    @Override
    public Map<String, String> getLabels(Kind kind) {

        MavenProject project = getProject();

        Map<String, String> ret = new HashMap<>();

        ret.put("version", project.getVersion());
        ret.put("project", project.getArtifactId());
        ret.put("group", project.getGroupId());
        ret.put("provider", "fabric8");
        return ret;
    }

    @Override
    public Map<String, String> getSelector(Kind kind) {
        Map ret = getLabels(kind);
        if  (kind == Kind.SERVICE || kind == Kind.DEPLOYMENT || kind == Kind.DEPLOYMENT_CONFIG) {
            return KubernetesResourceUtil.removeVersionSelector(ret);
        }
        return ret;
    }
}