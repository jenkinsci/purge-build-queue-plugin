/*
 * The MIT License
 *
 * Copyright (c) 2011, Jesse Farinacci
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.jenkins.plugins;

import hudson.Extension;
import hudson.model.RootAction;
import hudson.model.Queue;
import jenkins.model.Jenkins;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.interceptor.RequirePOST;

/**
 * @author <a href="mailto:jieryn@gmail.com">Jesse Farinacci</a>
 * @since 1.0
 */
@Extension
public final class PurgeBuildQueueAction implements RootAction {

    private static final Logger LOG = Logger
            .getLogger(PurgeBuildQueueAction.class
                    .getName());

    private static String getItemsVerbage(final int length) {
        if (length == 1) {
            return "1 item";
        }

        return length + " items";
    }

    @RequirePOST
    public void doPurge(final StaplerRequest request, final StaplerResponse response) throws ServletException,
            IOException {
        final Queue queue = Jenkins.get().getQueue();

        if (queue != null) {
            LOG.info("Purge Build Queue requested, "
                    + getItemsVerbage(queue.getItems().length)
                    + " in the queue.");

            if (LOG.isLoggable(Level.FINE)) {
                for (final Queue.Item item : queue.getItems()) {
                    LOG.fine("Purging " + item);
                }
            }

            queue.clear();
        }

        response.forwardToPreviousPage(request);
    }

    @Override
    public String getDisplayName() {
        return Messages.rootActionLabel();
    }

    @Override
    public String getIconFileName() {
        return Jenkins.get().hasPermission(Jenkins.ADMINISTER) ? "gear.svg" : null;
    }

    @Override
    public String getUrlName() {
        return "/purge-build-queue";
    }
}
