/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ---------------------------------------------------------------------
 *
 * History
 *   16.11.2015 (koetter): created
 */
package org.knime.core.node.port.database;

import java.io.File;
import java.io.IOException;
import java.sql.Driver;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

import org.knime.core.node.port.database.connection.DBDriverFactory;

/**
 * {@link DBDriverFactory} implementation for jar drivers registered by the user or via the old jar extension point.
 * @author Tobias Koetter, KNIME.com
 * @since 3.2
 */
public final class RegisteredDriversConnectionFactory implements DBDriverFactory {

    private static final RegisteredDriversConnectionFactory INSTANCE = new RegisteredDriversConnectionFactory();

    /**
     *
     */
    private RegisteredDriversConnectionFactory() {
        //prevent object creation
    }

    /**
     * @return the instance
     */
    public static RegisteredDriversConnectionFactory getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("deprecation")
    @Override
    public Set<String> getDriverNames() {
        return DatabaseDriverLoader.getLoadedDriver();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Driver getDriver(final DatabaseConnectionSettings settings) throws Exception {
        final Driver d = DatabaseDriverLoader.getDriver(settings.getDriver());
        return d;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<File> getDriverFiles(final DatabaseConnectionSettings settings) throws IOException {
      //locate the jdbc jar files
        try {
            final Collection<File> jars = new LinkedList<>();
            @SuppressWarnings("deprecation")
            final File[] driverFiles = DatabaseDriverLoader.getDriverFilesForDriverClass(settings.getDriver());
            if (driverFiles != null) {
                jars.addAll(Arrays.asList(driverFiles));
            }
            return jars;
        } catch (final Exception e) {
            throw new IOException(e);
        }
    }
}