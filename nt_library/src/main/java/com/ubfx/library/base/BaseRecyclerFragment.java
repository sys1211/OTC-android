package com.ubfx.library.base;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ubfx.library.R;
import com.ubfx.library.view.TitleBarLayout;
import com.ubfx.library.view.recycler.SectioningAdapter;
import com.ubfx.library.view.recycler.StickyHeaderLayoutManager;
import com.ubfx.log.LogUtils;
import com.ubfx.network.UBHttpError;
import com.ubfx.theme.ThemeMgr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

;

/**
 * Created by chuanzheyang on 17/3/13.
 */

public abstract class BaseRecyclerFragment<T> extends UBFXFragmentBase {


    private TitleBarLayout titleRecyclerRefresh;
    private RecyclerView recycler;
    private PtrClassicFrameLayout ptrRecycler;
    private SectioningAdapter mAdapter;

    private int page = 0;

    private int total;

    private boolean isRefreshing = false;

    private List<T> dataList = new ArrayList<>();

    public RecyclerView getRecyclerView() {
        return recycler;
    }

    @Override
    public int getLayoutId() {
        return R.layout.lib_fragment_recycler_base;
    }

    @Override
    public void onInitializeView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        titleRecyclerRefresh = container.findViewById(R.id.title_recycler_refresh);
        recycler = container.findViewById(R.id.recycler);
        ptrRecycler = container.findViewById(R.id.ptr_recycler);
        ptrRecycler.setBackgroundColor(ThemeMgr.get().getThemeProvider().backgroundLight());
        //init title view
        initTitle(titleRecyclerRefresh);
        //init recycler
        recycler.setLayoutManager(getLayoutManager());
        RecyclerView.ItemDecoration itemDecoration = getItemDecoration();
        if (itemDecoration != null) {
            recycler.addItemDecoration(itemDecoration);
        }
        mAdapter = new BaseRecyclerAdapter();
        recycler.setAdapter(mAdapter);
        initRecycler(recycler);

        if (needLoadMore()) {
            recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView view, int scrollState) {
                    super.onScrollStateChanged(view, scrollState);
                    if ((scrollState == RecyclerView.SCROLL_STATE_IDLE) &&
                            !view.canScrollVertically(1)
                            && !isRefreshing) {
                        loadMore();
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }

            });
        }
        requestData();
        ptrRecycler.setDurationToCloseHeader(200);
        ptrRecycler.setEnabled(needPullRefresh());
        ptrRecycler.disableWhenHorizontalMove(true);
        ptrRecycler.setPtrHandler(getPtrHandler());
    }

    public TitleBarLayout getTitleView() {
        return this.titleRecyclerRefresh;
    }

    protected PtrHandler getPtrHandler() {
        return new PtrDefaultHandler() {

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if (content instanceof RecyclerView) {
                    RecyclerView.LayoutManager layoutManager = ((RecyclerView) content).getLayoutManager();
                    if (layoutManager instanceof StickyHeaderLayoutManager) {
                        SectioningAdapter.ItemViewHolder holder = ((StickyHeaderLayoutManager) layoutManager).getFirstVisibleItemViewHolder(true);
                        if (holder != null) {
                            return holder.getSection() == 0 && holder.getPositionInSection() == 0;
                        } else {
                            return true;
                        }
                    }
                }
                return super.checkCanDoRefresh(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refresh();
            }
        };
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity());
//        return new StickyHeaderLayoutManager();
    }

    public RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public SectioningAdapter getAdapter() {
        return mAdapter;
    }

    private int getDataCount() {
        return dataList.size();
    }


    /**
     * about ui view
     */

    protected void initTitle(TitleBarLayout titleBar) {

    }

    public void initRecycler(RecyclerView recyclerView) {

    }

    public String getEmptyTips() {
        return "";
    }


    /**
     * about adapter
     */

    protected abstract SectioningAdapter.ItemViewHolder getItemHolder(ViewGroup parent, int itemUserType);

    protected abstract void bindItemAt(SectioningAdapter.ItemViewHolder viewHolder, int sectionIndex, int itemIndex);

    protected SectioningAdapter.HeaderViewHolder getItemHeaderHolder(ViewGroup parent, int headerUserType) {
        LogUtils.e("Header Holder not init !!!");
        return null;
    }

    protected SectioningAdapter.FooterViewHolder getItemFooterHolder(ViewGroup parent, int footerUserType) {
        LogUtils.e("Footer Holder not init !!!");
        return null;
    }

    protected void bindItemHeaderAt(SectioningAdapter.HeaderViewHolder viewHolder, int sectionIndex) {
    }

    protected void bindItemFooterAt(SectioningAdapter.FooterViewHolder viewHolder, int sectionIndex) {
    }

    protected int getSectionItemUserType(int sectionIndex, int itemIndex) {
        return -1;
    }

    protected int getSectionHeaderUserType(int sectionIndex) {
        return -1;
    }

    protected SectioningAdapter.ItemViewHolder getEmptyItemHolder(ViewGroup parent) {
        LogUtils.e("getEmptyItemHolder need be called !!!");
        return null;
    }

    protected void bindEmptyItem(SectioningAdapter.ItemViewHolder viewHolder) {

    }

    protected boolean sectionHasHeader(int section) {
        return true;
    }

    protected boolean sectionHasFooter(int section) {
        return false;
    }


    public int getSectionCount() {
        return 1;
    }

    public int numberOfRowsIn(int section) {
        return dataList.size();
    }

    public void itemDidClick(int section, int row) {

    }


    /**
     * about api request
     */

    protected boolean needLoadMore() {
        return true;
    }

    protected boolean needPullRefresh() {
        return true;
    }

    /**
     * @param requestPage refer getPageSize()
     * @param pageSize
     */
    protected abstract void doRequest(int requestPage, int pageSize);

    /**
     * page size of multi page request
     */

    protected int getPageSize() {
        return 20;
    }

    /**
     * @param total pass zero when total not return
     * @param data
     */
    public void onRequestFinish(int total, Collection<T> data) {
        if (getActivity() == null) {
            return;
        }
        finishLoading();
        ptrRecycler.refreshComplete();
        if (page == 1) {
            this.total = total;
            dataList.clear();
            dataList.addAll(data);
        } else {
            dataList.addAll(data);
        }
        isRefreshing = false;
        getAdapter().notifyAllSectionsDataSetChanged();
    }

    public void onRequestError(UBHttpError error) {
        if (getActivity() == null) {
            return;
        }
        finishLoading();
        isRefreshing = false;
        ptrRecycler.refreshComplete();
        toast(error.getErrorMessage(getContext()));
    }


    public void refresh() {
        page = 0;
        requestData();
    }

    private void requestData() {
        isRefreshing = true;
        page++;
        doRequest(page, getPageSize());
    }

    private void loadMore() {
        if (!needLoadMore()) {
            return;
        }

        if (getDataCount() < total || total == 0) {
            requestData();
        }
    }

    /**
     * base adapter
     */

    final class BaseRecyclerAdapter extends SectioningAdapter {

        int EMPTY_TYPE = 10;

        @Override
        public int getNumberOfSections() {
            int sectionCount = getSectionCount();
            return sectionCount == 0 ? 1 : sectionCount;
        }

        @Override
        public int getNumberOfItemsInSection(int sectionIndex) {
            int sectionCount = getSectionCount();
            if (sectionCount == 0) {
                return 1;
            }
            int rowsNumber = numberOfRowsIn(sectionIndex);
            return (sectionCount == 1 && rowsNumber == 0) ? 1 : rowsNumber;
        }


        @Override
        public int getSectionHeaderUserType(int sectionIndex) {
            int sectionUserType = BaseRecyclerFragment.this.getSectionHeaderUserType(sectionIndex);
            return sectionUserType == -1 ? super.getSectionHeaderUserType(sectionIndex) : sectionUserType;
        }

        @Override
        public int getSectionItemUserType(int sectionIndex, int itemIndex) {
            int sectionCount = getSectionCount();
            if (sectionCount == 0) {
                return EMPTY_TYPE;
            }
            int rowsNumber = numberOfRowsIn(sectionIndex);
            if (sectionCount == 1 && rowsNumber == 0) {
                return EMPTY_TYPE;
            }
            int itemUserType = BaseRecyclerFragment.this.getSectionItemUserType(sectionIndex, itemIndex);
            return itemUserType == -1 ? super.getSectionItemUserType(sectionIndex, itemIndex) : itemUserType;
        }

        @Override
        public boolean doesSectionHaveHeader(int sectionIndex) {
            if (getSectionCount() == 0) {
                return false;
            }
            return sectionHasHeader(sectionIndex);
        }

        @Override
        public boolean doesSectionHaveFooter(int sectionIndex) {
            if (getSectionCount() == 0) {
                return false;
            }
            return sectionHasFooter(sectionIndex);
        }

        @Override
        public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemUserType) {
            if (itemUserType == EMPTY_TYPE) {
                return getEmptyItemHolder(parent);
            }
            final ItemViewHolder itemViewHolder = getItemHolder(parent, itemUserType);
            if (itemViewHolder != null && itemViewHolder.itemView != null) {
                itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int section = itemViewHolder.getSection();
                        int row = itemViewHolder.getPositionInSection();
                        itemDidClick(section, row);
                    }
                });
            }
            return itemViewHolder;
        }

        @Override
        public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerUserType) {
            return getItemHeaderHolder(parent, headerUserType);
        }

        @Override
        public FooterViewHolder onCreateFooterViewHolder(ViewGroup parent, int footerUserType) {
            return getItemFooterHolder(parent, footerUserType);
        }

        @Override
        public void onBindItemViewHolder(ItemViewHolder viewHolder, final int sectionIndex, final int itemIndex, int itemUserType) {
            if (itemUserType == EMPTY_TYPE) {
                bindEmptyItem(viewHolder);
                return;
            }
            bindItemAt(viewHolder, sectionIndex, itemIndex);
        }

        @Override
        public void onBindHeaderViewHolder(HeaderViewHolder viewHolder, int sectionIndex, int headerUserType) {
            int sectionCount = getSectionCount();
            if (sectionCount == 0) {
                return;
            }
            bindItemHeaderAt(viewHolder, sectionIndex);
        }

        @Override
        public void onBindFooterViewHolder(FooterViewHolder viewHolder, int sectionIndex, int footerUserType) {
            int sectionCount = getSectionCount();
            if (sectionCount == 0) {
                return;
            }
            bindItemFooterAt(viewHolder, sectionIndex);
        }
    }


}
