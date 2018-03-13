/*
 * Copyright 2017 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.linecorp.centraldogma.server.internal.command;

import static com.linecorp.centraldogma.testing.internal.TestUtil.assertJsonConversion;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.linecorp.centraldogma.common.Author;
import com.linecorp.centraldogma.internal.Jackson;

public class SavePluginCommandTest {
    @Test
    public void testJsonConversion() {
        assertJsonConversion(new SavePluginCommand(1234L, Author.SYSTEM, "foo", "my_plugin", "/plugin.json"),
                             Command.class,
                             '{' +
                             "  \"type\": \"SAVE_PLUGIN\"," +
                             "  \"timestamp\": 1234," +
                             "  \"author\": {" +
                             "    \"name\": \"System\"," +
                             "    \"email\": \"system@localhost.localdomain\"" +
                             "  }," +
                             "  \"projectName\": \"foo\"," +
                             "  \"pluginName\": \"my_plugin\"," +
                             "  \"path\": \"/plugin.json\"" +
                             '}');
    }

    @Test
    public void backwardCompatibility() throws Exception {
        final SavePluginCommand c = (SavePluginCommand) Jackson.readValue(
                '{' +
                "  \"type\": \"SAVE_PLUGIN\"," +
                "  \"projectName\": \"foo\"," +
                "  \"pluginName\": \"my_plugin\"," +
                "  \"path\": \"/plugin.json\"" +
                '}', Command.class);

        assertThat(c.author()).isEqualTo(Author.SYSTEM);
        assertThat(c.timestamp()).isNotZero();
        assertThat(c.projectName()).isEqualTo("foo");
        assertThat(c.pluginName()).isEqualTo("my_plugin");
        assertThat(c.path()).isEqualTo("/plugin.json");
    }
}