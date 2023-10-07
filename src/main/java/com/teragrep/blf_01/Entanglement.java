/*
 * Teragrep Bloom Filter Library BLF-01
 * Copyright (C) 2019, 2020, 2021, 2022, 2023 Suomen Kanuuna Oy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://github.com/teragrep/teragrep/blob/main/LICENSE>.
 *
 *
 * Additional permission under GNU Affero General Public License version 3
 * section 7
 *
 * If you modify this Program, or any covered work, by linking or combining it
 * with other code, such other code is not for that reason alone subject to any
 * of the requirements of the GNU Affero GPL version 3 as long as this Program
 * is the same Program as licensed from Suomen Kanuuna Oy without any additional
 * modifications.
 *
 * Supplemented terms under GNU Affero General Public License version 3
 * section 7
 *
 * Origin of the software must be attributed to Suomen Kanuuna Oy. Any modified
 * versions must be marked as "Modified version of" The Program.
 *
 * Names of the licensors and authors may not be used for publicity purposes.
 *
 * No rights are granted for use of trade names, trademarks, or service marks
 * which are in The Program if any.
 *
 * Licensee must indemnify licensors and authors for any liability that these
 * contractual assumptions impose on licensors and authors.
 *
 * To the extent this program is licensed as part of the Commercial versions of
 * Teragrep, the applicable Commercial License may apply to this file if you as
 * a licensee so wish it.
 */

package com.teragrep.blf_01;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class Entanglement {

    public final LinkedList<Token> tokens;

    public Entanglement(LinkedList<Token> tokens) {
        this.tokens = tokens;
    }

    public LinkedList<Token> entangle() {
        LinkedList<Token> rv;
        try {
            rv = startWindowScan(tokens);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        //System.out.println("entangling> " + tokens + " results into " + rv);
        return rv;

    }

    private LinkedList<Token> startWindowScan(LinkedList<Token> tokenList) throws ExecutionException, InterruptedException {
        LinkedList<Token> resultTokens = new LinkedList<>();

        LinkedList<RecursiveTask<LinkedList<Token>>> subTasks = new LinkedList<>();
        for (int i = 0; i < tokenList.size(); i++) {
            ListIterator<Token> forwardIterator = tokenList.listIterator(i);
            // +++++ task
            subTasks.add(new StartScanTask(forwardIterator));
            // -----
        }

        LinkedList<ForkJoinTask<LinkedList<Token>>> runningTasks = new LinkedList<>();
        for (RecursiveTask<LinkedList<Token>> task : subTasks) {
            runningTasks.add(task.fork());
        }

        for (ForkJoinTask<LinkedList<Token>> runningTask : runningTasks) {
            resultTokens.addAll(runningTask.get());
        }

        return resultTokens;
    }

    private class StartScanTask extends RecursiveTask<LinkedList<Token>> {

        private final ListIterator<Token> forwardIterator;

        StartScanTask(ListIterator<Token> forwardIterator) {
            this.forwardIterator = forwardIterator;
        }

        @Override
        protected LinkedList<Token> compute() {
            LinkedList<Token> taskResultTokens = new LinkedList<>();
            LinkedList<Token> windowTokens = new LinkedList<>();
            while (forwardIterator.hasNext()) {
                windowTokens.addLast(forwardIterator.next());
            }
            // +++++ subtask endWindowScan
            try {
                taskResultTokens.addAll(endWindowScan(windowTokens));
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            // -----
            Token concatenated = new Token(new ConcatenatedToken(windowTokens).concatenate());
            taskResultTokens.add(concatenated);

            return taskResultTokens;
        }
    }

    private LinkedList<Token> endWindowScan(LinkedList<Token> tokenList) throws ExecutionException, InterruptedException {
        LinkedList<Token> resultTokens = new LinkedList<>();

        LinkedList<RecursiveTask<Token>> subTasks = new LinkedList<>();
        for (int i = tokenList.size() - 1; i > 0; i--) {
            ListIterator<Token> backwardIterator = tokenList.listIterator(i);
            // +++++ task
            subTasks.add(new EndScanTask(backwardIterator));
            // ----- task
        }

        LinkedList<ForkJoinTask<Token>> runningTasks = new LinkedList<>();
        for (RecursiveTask<Token> task : subTasks) {
            runningTasks.add(task.fork());
        }
        for (ForkJoinTask<Token> runningTask : runningTasks) {
            resultTokens.add(runningTask.get());
        }

        return resultTokens;
    }

    private class EndScanTask extends RecursiveTask<Token> {

        private final ListIterator<Token> backwardIterator;

        EndScanTask(ListIterator<Token> backwardIterator) {
            this.backwardIterator = backwardIterator;
        }

        @Override
        protected Token compute() {
            LinkedList<Token> windowTokens = new LinkedList<>();
            while (backwardIterator.hasPrevious()) {
                windowTokens.addFirst(backwardIterator.previous());
            }
            return new Token(new ConcatenatedToken(windowTokens).concatenate());
        }
    }
}
