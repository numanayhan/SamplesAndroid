package com.example.raghu.ecommerce.listener;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by raghu on 16/10/17.
 */

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerViewScrollListener.class.getSimpleName();

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 1; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1;

    private GridLayoutManager gridLayoutManager;

    int scrollDist = 0;
    boolean isVisible = true;
    static final float MINIMUM = 25;

    public EndlessRecyclerViewScrollListener(int current_page) {

        this.current_page = current_page;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = gridLayoutManager.getItemCount();
        firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

        if (isVisible && scrollDist > MINIMUM) {

            scrollDist = 0;
            isVisible = false;
        }
        else if (!isVisible && scrollDist < -MINIMUM) {

            scrollDist = 0;
            isVisible = true;
        }
        if ((isVisible && dy > 0) || (!isVisible && dy < 0)) {
            scrollDist += dy;
        }


        if (loading) {
            if (totalItemCount > previousTotal+1) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached
            // Do something
            current_page++;
            onLoadMore(current_page,totalItemCount);
            loading = true;
        }
    }




    public abstract void onLoadMore(int current_page,int totalItemCount);

    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }
}

