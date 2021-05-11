package org.astra_g.session_cookie_reader.utilities;

/*
 * https://github.com/JohnyDaDeveloper/AndroidAsync
 */

import java.util.concurrent.ExecutorService;

public abstract class AsyncTask<INPUT, PROGRESS, OUTPUT> {

    public AsyncTask() {}

    /**
     * Starts is all
     * @param input Data you want to work with in the background
     */
    public void execute() {
        ExecutorService executorService = AsyncWorker.getInstance().getExecutorService();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final OUTPUT output = AsyncTask.this.doInBackground();
                    AsyncWorker.getInstance().getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            AsyncTask.this.onPostExecute(output);
                        }
                    });
                } catch (final Exception e) {
                    e.printStackTrace();
                    AsyncWorker.getInstance().getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            AsyncTask.this.onBackgroundError(e);
                        }
                    });
                }
            }
        });

    }

    /**
     * Work on background
     * @param input Input data
     * @return      Output data
     * @throws Exception    Any uncought exception which occurred while working in background. If
     * any occurs, {@link #onBackgroundError(Exception)} will be executed (on the UI thread)
     */
    protected abstract OUTPUT doInBackground();

    /**
     * Work which you want to be done on UI thread after {@link #doInBackground}
     * @param output    Output data from {@link #doInBackground}
     */
    protected void onPostExecute(OUTPUT output) {}

    /**
     * Triggered on UI thread if any uncought exception occurred while working in background
     * @param e Exception
     * @see #doInBackground
     */
    protected abstract void onBackgroundError(Exception e);
}
