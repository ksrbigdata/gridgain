/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.visor.cmd.tasks;

import org.gridgain.grid.GridException;
import org.gridgain.grid.cache.GridCache;
import org.gridgain.grid.cache.GridCacheEntry;
import org.gridgain.grid.kernal.processors.task.GridInternal;
import org.gridgain.grid.kernal.visor.cmd.*;

import java.util.*;

/**
 * Task for swapping backup cache entries.
 */
@GridInternal
public class VisorSwapBackupsCachesTask extends VisorOneNodeTask<VisorOneNodeCachesArg, VisorCachesTaskResult> {
    @SuppressWarnings("PublicInnerClass")
    public static class VisorSwapBackupsCachesJob
        extends VisorOneNodeJob<VisorOneNodeCachesArg, VisorCachesTaskResult> {
        /** */
        private static final long serialVersionUID = 0L;

        /**
         * Create job with specified argument.
         *
         * @param arg Job argument.
         */
        protected VisorSwapBackupsCachesJob(VisorOneNodeCachesArg arg) {
            super(arg);
        }

        @Override
        protected VisorCachesTaskResult run(VisorOneNodeCachesArg arg) throws GridException {
            VisorCachesTaskResult total = new VisorCachesTaskResult();

            for (GridCache c: g.cachesx(null)) {
                String cacheName = c.name();

                if (arg.cacheNames().contains(cacheName)) {
                    final Set<GridCacheEntry> entries = c.entrySet();

                    long before = entries.size(), after = before;

                    for (GridCacheEntry entry: entries) {
                        if (entry.backup() && entry.evict())
                            after--;
                    }

                    total.put(cacheName, new VisorBeforeAfterResult(before, after));
                }
            }

            return total;
        }
    }

    @Override
    protected VisorJob<VisorOneNodeCachesArg, VisorCachesTaskResult> job(VisorOneNodeCachesArg arg) {
        return new VisorSwapBackupsCachesJob(arg);
    }
}
