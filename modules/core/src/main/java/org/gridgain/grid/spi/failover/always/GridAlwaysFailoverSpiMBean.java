/* 
 Copyright (C) GridGain Systems. All Rights Reserved.
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi.failover.always;

import org.gridgain.grid.spi.*;
import org.gridgain.grid.util.mbean.*;

/**
 * Management bean for {@link GridAlwaysFailoverSpi}.
 */
@GridMBeanDescription("MBean that provides access to always failover SPI configuration.")
public interface GridAlwaysFailoverSpiMBean extends GridSpiManagementMBean {
    /**
     * Gets maximum number of attempts to execute a failed job on another node.
     * If not specified, {@link GridAlwaysFailoverSpi#DFLT_MAX_FAILOVER_ATTEMPTS} value will be used.
     *
     * @return Maximum number of attempts to execute a failed job on another node.
     */
    @GridMBeanDescription("Maximum number of attempts to execute a failed job on another node.")
    public int getMaximumFailoverAttempts();

    /**
     * Get total number of jobs that were failed over.
     *
     * @return Total number of failed over jobs.
     */
    @GridMBeanDescription("Total number of jobs that were failed over.")
    public int getTotalFailoverJobsCount();
}
