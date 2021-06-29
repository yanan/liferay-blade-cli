/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liferay.blade.cli.command.validator;

import com.beust.jcommander.ParameterException;

import com.liferay.blade.cli.WorkspaceConstants;
import com.liferay.blade.cli.util.BladeUtil;
import com.liferay.project.templates.extensions.util.VersionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Jiang
 */
public class LiferayMoreVersionValidator implements ValidatorSupplier {

	@Override
	public List<String> get() {
		return BladeUtil.getWorkspaceProductKeys(false);
	}

	@Override
	public void validate(String name, String value) throws ParameterException {
		List<String> possibleValues = new ArrayList<>(get());

		possibleValues.addAll(WorkspaceConstants.originalLiferayVersions);

		if (!possibleValues.contains(value) && !VersionUtil.isLiferayVersion(value)) {
			throw new ParameterException(value + " is not a valid value.");
		}
	}

}