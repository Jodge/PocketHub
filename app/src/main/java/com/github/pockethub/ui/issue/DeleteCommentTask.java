/*
 * Copyright (c) 2015 PocketHub
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
package com.github.pockethub.ui.issue;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.alorma.github.sdk.bean.dto.response.GithubComment;
import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.github.sdk.services.issues.DeleteIssueCommentClient;
import com.github.pockethub.R;
import com.github.pockethub.ui.ProgressDialogTask;
import com.github.pockethub.util.InfoUtils;
import com.github.pockethub.util.ToastUtils;

/**
 * Task to delete a comment on an issue in a repository
 */
public class DeleteCommentTask extends ProgressDialogTask<GithubComment> {

    private static final String TAG = "DeleteCommentTask";

    private final Repo repository;

    private final GithubComment comment;

    /**
     * Delete task for deleting a comment on the given issue in the given
     * repository
     *
     * @param context
     * @param repository
     * @param comment
     */
    public DeleteCommentTask(final Context context,
            final Repo repository,
            final GithubComment comment) {
        super(context);

        this.repository = repository;
        this.comment = comment;
    }

    @Override
    protected GithubComment run(Account account) throws Exception {
        new DeleteIssueCommentClient(InfoUtils.createRepoInfo(repository), comment.id).observable().toBlocking().first();
        return comment;
    }

    /**
     * Delete comment
     *
     * @return this task
     */
    public DeleteCommentTask start() {
        showIndeterminate(R.string.deleting_comment);

        execute();
        return this;
    }

    @Override
    protected void onException(Exception e) throws RuntimeException {
        super.onException(e);

        Log.d(TAG, "Exception deleting comment on issue", e);

        ToastUtils.show((Activity) getContext(), e.getMessage());
    }
}
